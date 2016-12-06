/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class MetricsContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final MetricDimension serviceDimensions = new MetricDimension()
                .with("host", "host.name.com")
                .with("colo", "corp");
        final LoggerEmitter logger = new LoggerEmitter();
        final MetricEmitter serviceEmitter = new StandardMetricEmitter(serviceDimensions, logger);
        ServiceMetrics metrics = new ServiceMetrics() {
            @Override
            public Closeable registerGauge(MetricDimension metricDimension, String name, long interval, TimeUnit units, DiscreteReader reader) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Closeable registerGauge(MetricDimension metricDimension, String name, long interval, TimeUnit units, AtomicLong reader) {
                throw new UnsupportedOperationException();
            }

            @Override
            public RequestMetricEmitter createRequest(MetricDimension requestDimensions) {
                return new StandardRequestEmitter(serviceDimensions.merge(requestDimensions), this);
            }

            @Override
            public void emit(MetricEvent event) {
                logger.emit(event);
            }

            @Override
            public void emitRequest(RequestEvent event) {
                logger.emitRequest(event);
            }
        };
        servletContextEvent.getServletContext().setAttribute(ServiceMetrics.class.getName(), metrics);
        servletContextEvent.getServletContext().setAttribute(MetricEmitter.class.getName(), serviceEmitter);
        serviceEmitter.emit("start", 1);
        serviceEmitter.create("hodor", "awesome")
            .emitDuration("hodor", 2L, TimeUnit.SECONDS);


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
