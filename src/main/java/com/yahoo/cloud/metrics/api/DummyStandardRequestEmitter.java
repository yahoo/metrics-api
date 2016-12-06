/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * A reference StandardRequestEmitter implementation which throws away all data.
 */
public class DummyStandardRequestEmitter extends StandardRequestEmitter {
    private static final MetricDimension EMPTY = new MetricDimension();

    private final DummyTaskEmitter root;

    public DummyStandardRequestEmitter(MetricDimension dimension, RequestMetricSink sink) {
        super(dimension, sink);
        this.root = new DummyTaskEmitter();
    }
    
    @Override
    public TaskMetricEmitter start(MetricDimension addedDimensions) {
        return root;
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3, String... keyValues) {
        return root;
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2, String k3, String v3) {
        return root;
    }

    @Override
    public TaskMetricEmitter start(String k1, String v1, String k2, String v2) {
        return root;
    }

    @Override
    public TaskMetricEmitter start(String key, String value) {
        return root;
    }

    class DummyTaskEmitter implements TaskMetricEmitter {  
        private final Duration duration = new Duration(0, this, "dummyDuration");
        DummyTaskEmitter() {
        }

        @Override
        public long getTaskId() {
            return 0L;
        }

        @Override
        public void emit(String name, long count) {
        }

        @Override
        public void emitDuration(String name, long duration, TimeUnit unit) {
        }

        @Override
        public void emitSpan(String name, long start, long end, TimeUnit unit) {
        }

        @Override
        public void end() {
        }

        @Override
        public MetricDimension dimensions() {
          return null;
        }

        @Override
        public DummyTaskEmitter create(String key, String value) {
            return this;
        }

        @Override
        public DummyTaskEmitter create(String k1, String v1, String k2, String v2) {
            return this;
        }

        @Override
        public DummyTaskEmitter create(String k1, String v1, String k2, String v2,
                String k3, String v3) {
            return this;
        }

        @Override
        public DummyTaskEmitter create(String k1, String v1, String k2, String v2,
                String k3, String v3, String... keyValues) {
            return this;
        }

        @Override
        public DummyTaskEmitter with(MetricDimension addedDimensions) {
            return this;
        }

        @Override
        public Duration start(String name) {
            return duration;
        }

        @Override
        public void close() throws Exception {          
        }

        @Override
        public DummyTaskEmitter start(String key, String value) {
            return this;
        }

        @Override
        public DummyTaskEmitter start(String k1, String v1, String k2,
                String v2) {
            return this;
        }

        @Override
        public DummyTaskEmitter start(String k1, String v1, String k2,
                String v2, String k3, String v3) {
            return this;
        }

        @Override
        public DummyTaskEmitter start(String k1, String v1, String k2,
                String v2, String k3, String v3, String... keyValues) {
            return this;
        }

        @Override
        public DummyTaskEmitter start(MetricDimension addedDimensions) {
            return this;
        }
    }

    @Override
    public void emit(String name, long count) {
        root.emit(name, count);
    }

    @Override
    public void emitDuration(String name, long duration, TimeUnit unit) {
        root.emitDuration(name, duration, unit);
    }

    @Override
    public void emitSpan(String name, long start, long end, TimeUnit unit) {
        root.emitSpan(name, start, end, unit);
    }

    @Override
    protected DummyTaskEmitter createTask(MetricDimension dimensions) {
        return root;
    }

    @Override
    protected DummyTaskEmitter create(MetricDimension dimension) {
        return root;
    }

    static class BaseRequestEvent implements RequestEvent {

        @Override
        public long getStartTime() {
            return 0;
        }

        @Override
        public MetricDimension getRequestDimensions() {
            return EMPTY;
        }

        @Override
        public Queue<RequestTask> getTasks() {
            return new LinkedList<>();
        }

        @Override
        public Queue<RequestMetric> getMetrics() {
            return new LinkedList<>();
        }
    }

    @Override
    public RequestEvent complete() {
        return new BaseRequestEvent();
    }

    @Override
    public void end() {
        root.end();
    }
}
