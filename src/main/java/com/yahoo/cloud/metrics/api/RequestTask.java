/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

/**
* Represents a single task within a request.
*/
public class RequestTask {
    final MetricDimension dimensions;
    final long parentId;
    final long taskId;
    final long startTime;
    final long endTime;

    RequestTask(MetricDimension dimensions, long parentId, long taskId, long startTime, long endTime) {
        this.dimensions = dimensions;
        this.parentId = parentId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MetricDimension getDimension() {
        return dimensions;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getParentId() {
        return parentId;
    }

    public long getStartTick() {
        return startTime;
    }

    public long getEndTick() {
        return endTime;
    }
}
