package com.study.core.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *  정적 리소스(css, js 등) 요청을 처리하기 위한 필터입니다.
 */
@Slf4j
@WebFilter("/*")
public class ResourceFilter implements Filter {
    private static final List<String> resourcePrefixes = new ArrayList<>();
    static {
        resourcePrefixes.add("/resources/css");
        resourcePrefixes.add("/resources/js");
        resourcePrefixes.add("/resources/fonts");
        resourcePrefixes.add("/resources/images");
        resourcePrefixes.add("/favicon.ico");
    }

    private RequestDispatcher defaultRequestDispatcher;

    /**
     * 필터를 초기화합니다.
     *
     * @param filterConfig 필터 구성 객체
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.defaultRequestDispatcher = filterConfig.getServletContext().getNamedDispatcher("default");
    }

    /**
     * 요청과 응답을 처리하여 리소스 요청인 경우 다른 서블릿이나 리소스로 전달하고,
     * 그렇지 않은 경우 필터 체인을 따라 다음 필터로 전달합니다.
     *
     * @param request  서블릿 요청 객체
     * @param response 서블릿 응답 객체
     * @param chain    필터 체인 객체
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (isResourceUrl(path)) {
            log.debug("path : {}", path);
            defaultRequestDispatcher.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 주어진 URL이 리소스 URL인지 여부를 확인합니다.
     *
     * @param url 확인할 URL
     * @return URL이 리소스 URL인 경우 true, 그렇지 않은 경우 false
     */
    private boolean isResourceUrl(String url) {
        for (String prefix : resourcePrefixes) {
            if (url.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }

}
