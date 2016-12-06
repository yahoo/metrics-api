/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by @xythian - https://github.com/xythian on 3/13/14.
 */
class DiscreteLong implements DiscreteReader {
    private final AtomicLong value;

    DiscreteLong(AtomicLong value) {
        this.value = value;
    }

    @Override
    public long read() {
        return value.get();
    }
}
