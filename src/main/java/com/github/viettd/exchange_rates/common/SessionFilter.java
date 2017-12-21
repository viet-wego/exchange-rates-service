package com.github.viettd.exchange_rates.common;


import com.github.viettd.exchange_rates.bean.Response;
import com.google.gson.GsonBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.github.viettd.exchange_rates.common.Constant.*;
import static org.apache.commons.codec.CharEncoding.UTF_8;

public class SessionFilter implements Filter {
    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String apiKey = req.getHeader(HEADER_API_KEY);
        if (Config.getApiKeys().contains(apiKey)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            Response response = new Response();
            response.setCode(CODE_UNAUTHORIZED);
            response.setMessage(MSG_UNAUTHORIZED);

            resp.setCharacterEncoding(UTF_8);
            resp.setContentType(JSON_CONTENT_TYPE_HEADER);
            PrintWriter out = resp.getWriter();
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat(Constant.DATE_TIME_FORMAT);
            out.print(builder.create().toJson(response));
        }
    }

    public void destroy() {

    }
}
