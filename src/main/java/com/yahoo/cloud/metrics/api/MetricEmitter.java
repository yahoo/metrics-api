/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.concurrent.TimeUnit;

/**
 * Created by @xythian - https://github.com/xythian on 3/13/14.
 */
public interface MetricEmitter {
    MetricDimension dimensions();

    MetricEmitter create(String key, String value);
    MetricEmitter create(String k1, String v1, String k2, String v2);
    MetricEmitter create(String k1, String v1, String k2, String v2, String k3, String v3);
    MetricEmitter create(String k1, String v1, String k2, String v2, String k3, String v3, String... keyValues);
    MetricEmitter with(MetricDimension addedDimensions);

    Duration start(String name);
    void emit(String name, long count);
    void emitDuration(String name, long duration, TimeUnit unit);
    void emitSpan(String name, long start, long end, TimeUnit unit);
}
