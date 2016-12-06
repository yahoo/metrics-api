/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.concurrent.TimeUnit;

/**
 * Represents an in-progress duration.
 */
public final class Duration implements AutoCloseable {
    private final long startTick;
    private final MetricEmitter dimension;
    private final String name;

    public Duration(long startTick, MetricEmitter dimension, String name) {
        this.startTick = startTick;
        this.dimension = dimension;
        this.name = name;
    }

    public void end() {
        dimension.emitDuration(name, System.nanoTime() - startTick, TimeUnit.NANOSECONDS);
    }

    @Override
    public void close() {
        end();
    }
}
