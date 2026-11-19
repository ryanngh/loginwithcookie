package com.ryan.loginwithcookie2.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;

import java.io.IOException;

public class SiteMeshFilter implements Filter {

    private org.sitemesh.webapp.SiteMeshFilter delegate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        SiteMeshFilterBuilder builder = new SiteMeshFilterBuilder()
                .addDecoratorPath("/*", "main.jsp")
                .addExcludedPath("/uploads/*")
                .addExcludedPath("/api/*");

        this.delegate = new org.sitemesh.webapp.SiteMeshFilter(
                builder.getSelector(),
                builder.getContentProcessor(),
                builder.getDecoratorSelector(),
                builder.isIncludeErrorPages()
        ) {
            @Override
            protected WebAppContext createContext(String contentType, HttpServletRequest request,
                                                  HttpServletResponse response, ResponseMetaData metaData) {
                return new WebAppContext(contentType, request, response, servletContext,
                        getContentProcessor(), metaData, builder.isIncludeErrorPages()) {
                    @Override
                    protected void dispatch(HttpServletRequest req, HttpServletResponse resp, String path)
                            throws ServletException, IOException {
                        RequestDispatcher rd = servletContext.getRequestDispatcher(path);
                        if (rd == null) {
                            throw new ServletException("Decorator not found: " + path);
                        }
                        rd.include(req, resp);
                    }
                };
            }
        };
        this.delegate.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        delegate.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        if (delegate != null) {
            delegate.destroy();
        }
    }
}
