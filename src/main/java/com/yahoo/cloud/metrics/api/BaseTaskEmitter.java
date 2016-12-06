/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

abstract class BaseTaskEmitter extends BaseMetricEmitter implements TaskMetricEmitter {

    protected BaseTaskEmitter(MetricDimension dimension) {
        super(dimension);
    }

    @Override
    public TaskMetricEmitter start(MetricDimension addedDimensions) {
        return createTask(dimension.merge(addedDimensions));
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3, String... keyValues) {
        return createTask(merge(dimension.with(k1, v1).with(k2, v2).with(k3, v3), keyValues));
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3) {
        return createTask(dimension.with(k1, v1).with(k2, v2).with(k3, v3));
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2) {
        return createTask(dimension.with(k1, v1).with(k2, v2));
    }

    @Override
    public TaskMetricEmitter start(String key, String value) {
        return createTask(dimension.with(key, value));
    }

    protected abstract TaskMetricEmitter createTask(MetricDimension dimensions);

    @Override
    public final void close()  {
        end();
    }
}
