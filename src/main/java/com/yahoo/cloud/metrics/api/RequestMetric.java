/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

/**
* A metric created during a request.
*/
public class RequestMetric {
    final long taskId;
    final Metric metric;

    RequestMetric(long taskId, Metric metric) {
        if(taskId < 0) {
            throw new IllegalArgumentException("invalid taskid: " + taskId);
        }

        this.taskId = taskId;
        this.metric = metric;
    }

    public long getTaskId() {
        return taskId;
    }

    public Metric getMetric() {
        return metric;
    }
}
