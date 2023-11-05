package com.ehzyil.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author ehyzil
 * @Description
 * @create 2023-11-2023/11/5-19:16
 */
@Slf4j
@WebFilter(urlPatterns = "/*",filterName = "reqRecordFilter", asyncSupported = true)
public class ReqRecordFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.warn("filter...");
        filterChain.    doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
