/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.Queue;

/**
 * A service request.
 */
public interface RequestEvent {
    long getStartTime();
    MetricDimension getRequestDimensions();
    Queue<RequestTask> getTasks();
    Queue<RequestMetric> getMetrics();
}
