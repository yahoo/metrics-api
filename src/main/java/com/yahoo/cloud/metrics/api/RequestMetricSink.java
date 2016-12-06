/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public interface RequestMetricSink {
    void emitRequest(RequestEvent event);
}
