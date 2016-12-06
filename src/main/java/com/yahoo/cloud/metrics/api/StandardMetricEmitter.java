/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.concurrent.TimeUnit;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class StandardMetricEmitter extends BaseMetricEmitter {
    private final MetricSink sink;

    public StandardMetricEmitter(MetricDimension dimension, MetricSink sink) {
        super(dimension);
        this.sink = sink;
    }

    @Override
    protected MetricEmitter create(MetricDimension dimension) {
        return new StandardMetricEmitter(dimension, sink);
    }

    @Override
    public void emit(String name, long count) {
        sink.emit(new MetricEvent(System.currentTimeMillis(), new Metric(dimension, name, MetricType.COUNT, count, 0L, TimeUnit.NANOSECONDS)));
    }

    @Override
    public void emitDuration(String name, long duration, TimeUnit unit) {
        sink.emit(new MetricEvent(System.currentTimeMillis(), new Metric(dimension, name, MetricType.DURATION, duration, 0L, unit)));
    }

    @Override
    public void emitSpan(String name, long start, long end, TimeUnit unit) {
        sink.emit(new MetricEvent(System.currentTimeMillis(), new Metric(dimension, name, MetricType.SPAN, start, end, unit)));
    }
}
