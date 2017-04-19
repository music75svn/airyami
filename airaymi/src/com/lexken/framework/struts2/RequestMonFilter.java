package com.lexken.framework.struts2;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RequestMonFilter implements Filter {
    protected FilterConfig filterConfig = null;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        org.jdf.requestmon.ServiceTrace trace = org.jdf.requestmon.ServiceTrace.getInstance();
        trace.startTrace((HttpServletRequest) request);
        try {
            chain.doFilter(request, response);
        } finally {
            trace.endTrace((HttpServletRequest) request);
        }
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
