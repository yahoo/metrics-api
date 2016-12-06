/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.MetricDimension;
import com.yahoo.cloud.metrics.api.RequestMetricEmitter;
import com.yahoo.cloud.metrics.api.ServiceMetrics;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class MetricsFilter implements Filter {
    private ServiceMetrics metrics;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        metrics = (ServiceMetrics) filterConfig.getServletContext().getAttribute(ServiceMetrics.class.getName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = null;
        RequestMetricEmitter requestMetricEmitter = null;
        if(servletRequest instanceof HttpServletRequest) {
            req = (HttpServletRequest) servletRequest;
        }

        if(req != null) {
            MetricDimension requestDimensions = new MetricDimension()
                    .with("method", req.getMethod())
                    .with("url", req.getRequestURI());
            // more per-request globally applicable dimensions here
            requestMetricEmitter = metrics.createRequest(requestDimensions);
            ServletMetrics.set(req, requestMetricEmitter);
        }

        filterChain.doFilter(servletRequest, servletResponse);

        if(req != null) {
            if(req.isAsyncStarted()) {
                AsyncContext ctx = req.getAsyncContext();
                final RequestMetricEmitter finalRequestMetricEmitter = requestMetricEmitter;
                ctx.addListener(new AsyncListener() {
                    @Override
                    public void onComplete(AsyncEvent asyncEvent) throws IOException {
                        finalRequestMetricEmitter.complete();

                    }

                    @Override
                    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                        finalRequestMetricEmitter.complete();

                    }

                    @Override
                    public void onError(AsyncEvent asyncEvent) throws IOException {
                        finalRequestMetricEmitter.complete();

                    }

                    @Override
                    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

                    }
                });
            } else {
                requestMetricEmitter.complete();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
