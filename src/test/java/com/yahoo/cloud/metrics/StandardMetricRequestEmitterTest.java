/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
@Test
public class StandardMetricRequestEmitterTest {
    @Test
    public void testEmit() throws Exception {
        final MetricDimension base = new MetricDimension().with("host", "example.com");
        final MetricDimension taskDimension = base.with("task", "0");
        final AtomicInteger count = new AtomicInteger(0);
        RequestMetricSink sink = new RequestMetricSink() {
            @Override
            public void emitRequest(RequestEvent event) {
                Queue<RequestTask> tasks =  event.getTasks();
                RequestTask task1 = tasks.poll();
                Assert.assertEquals(task1.getTaskId(), 1);
                Assert.assertEquals(task1.getDimension(), taskDimension);
                Assert.assertSame(task1.getDimension().getParent(), base);
                count.incrementAndGet();
            }
        };
        StandardRequestEmitter emitter = new StandardRequestEmitter(base, sink);
        emitter.start("task", "0").end();
        emitter.start("task", "1").end();
        emitter.complete();
        Assert.assertEquals(count.get(), 1);
    }
}
