package com.github.viettd.exchange_rates.common;

import com.github.viettd.exchange_rates.controller.ExchangeRatesServlet;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RestServer implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(RestServer.class);
    private static final Lock CREATE_LOCK = new ReentrantLock();

    private static RestServer instance = null;

    private static final Server server = new Server();

    public static RestServer getInstance() {
        if (instance == null) {
            CREATE_LOCK.lock();
            try {
                instance = new RestServer();
            } finally {
                CREATE_LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public void run() {
        ServerConnector connector = null;
        try {
            connector = new ServerConnector(server);
            connector.setPort(Config.getRestPort());
            connector.setIdleTimeout(30000);
            server.setConnectors(new Connector[]{connector});
            ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            contextHandler.setContextPath("/");

            GzipHandler gzipHandler = new GzipHandler();
            gzipHandler.setHandler(contextHandler);
            server.setHandler(gzipHandler);

            String versionPath = StringUtils.isBlank(Config.getVersion()) ? "" : "/" + Config.getVersion();
            if (StringUtils.isNotBlank(Config.getServicePath())) {
                versionPath = Config.getServicePath() + versionPath;
            }
            contextHandler.addServlet(ExchangeRatesServlet.class, versionPath + "/*");
            contextHandler.addFilter(SessionFilter.class, versionPath + "/*",
                    EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

            server.start();
            server.join();
        } catch (Exception e) {
            LOGGER.error("Cannot start server", e);
            if (connector != null) {
                connector.close();
            }
            System.exit(1);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

}
