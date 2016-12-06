/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.RequestMetricEmitter;

import javax.servlet.ServletRequest;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public final class ServletMetrics {
    private ServletMetrics() {}



    public static RequestMetricEmitter get(ServletRequest req) {
        return (RequestMetricEmitter) req.getAttribute("com.yahoo.cloud.metrics.RequestMetricEmitter");
    }

    public static void set(ServletRequest req, RequestMetricEmitter emitter) {
        req.setAttribute("com.yahoo.cloud.metrics.RequestMetricEmitter", emitter);
    }
}
