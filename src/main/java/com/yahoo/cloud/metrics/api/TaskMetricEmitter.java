/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public interface TaskMetricEmitter extends MetricEmitter, AutoCloseable {
    long getTaskId();
    TaskMetricEmitter start(String key, String value);
    TaskMetricEmitter start(String k1, String v1, String k2, String v2);
    TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3);
    TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3, String... keyValues);
    TaskMetricEmitter start(MetricDimension addedDimensions);
    void end();
}
