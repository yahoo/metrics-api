/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.RequestMetricEmitter;
import com.yahoo.cloud.metrics.api.TaskMetricEmitter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class ToyWorkServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestMetricEmitter emitter = ServletMetrics.get(req);
        emitter.emit("hats", 1);
        for(int i = 0; i < 3; ++i) {
            TaskMetricEmitter task = emitter.start("task", String.valueOf(i));
            task.emit("count", i);
            task.end();
        }
        resp.addHeader("Content-type", "text/plain");
        resp.getWriter().print("hodor");
    }
}
