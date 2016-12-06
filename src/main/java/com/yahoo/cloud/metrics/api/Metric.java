/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.concurrent.TimeUnit;

/**
 * A single metric event capturing a set of dimensions and a value.
 */
public final class Metric {
    private final MetricDimension dimension;
    private final String name;
    private final MetricType type;
    private final long value;
    private final long end;
    private final TimeUnit units;


    Metric(MetricDimension dimension, String name, MetricType type, long value, long end, TimeUnit units) {
        this.dimension = dimension;
        this.name = name;
        this.type = type;
        this.value = value;
        this.end = end;
        this.units = units;
    }

    public MetricDimension getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    public MetricType getType() {
        return type;
    }

    public long getValue() {
        return value;
    }

    /**
     * Only applicable to MetricType.SPAN; in this case value is the start time
     */
    public long getEnd() {
        return end;
    }

    /**
     * Only applicable for MetricType.DURATION and MetricType.SPAN
     */
    public TimeUnit getUnits() {
        return units;
    }
}
