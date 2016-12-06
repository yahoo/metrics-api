/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public interface ServiceMetrics extends MetricSink, RequestMetricSink {
    Closeable registerGauge(MetricDimension metricDimension, String name, long interval, TimeUnit units, DiscreteReader reader);
    Closeable registerGauge(MetricDimension metricDimension, String name, long interval, TimeUnit units, AtomicLong reader);

    RequestMetricEmitter createRequest(MetricDimension requestDimensions);
}
