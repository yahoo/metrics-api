/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

abstract class BaseMetricEmitter implements MetricEmitter {
    protected final MetricDimension dimension;

    BaseMetricEmitter(MetricDimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public MetricDimension dimensions() {
        return dimension;
    }

    protected abstract MetricEmitter create(MetricDimension dimension);

    @Override
    public MetricEmitter create(String key, String value) {
        return create(dimension.with(key, value));
    }

    @Override
    public MetricEmitter create(String k1, String v1, String k2, String v2) {
        return create(dimension.with(k1, v1).with(k2, v2));
    }

    @Override
    public MetricEmitter create(String k1, String v1, String k2, String v2, String k3, String v3) {
        return create(dimension.with(k1, v1).with(k2, v2).with(k3, v3));
    }

    protected MetricDimension merge(MetricDimension dims, String... keyValues) {
        if(keyValues != null) {
            if(keyValues.length % 2 != 0) {
                throw new IllegalArgumentException("invalid keyValues length (not divisable by 2 -- form is (k1, v1), ...");
            }
            for(int i = 0; i < keyValues.length; i += 2) {
                dims = dims.with(keyValues[i], keyValues[i+1]);
            }
        }
        return dims;
    }

    @Override
    public MetricEmitter create(String k1, String v1, String k2, String v2, String k3, String v3, String... keyValues) {
        MetricDimension dims = merge(dimension.with(k1, v1).with(k2, v2).with(k3, v3), keyValues);
        return create(dims);
    }

    @Override
    public MetricEmitter with(MetricDimension addedDimensions) {
        return create(dimension.merge(addedDimensions));
    }

    @Override
    public Duration start(String name) {
        return new Duration(System.nanoTime(), this, name);
    }
}
