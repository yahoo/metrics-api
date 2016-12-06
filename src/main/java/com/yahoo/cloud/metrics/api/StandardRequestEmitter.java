/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class StandardRequestEmitter extends BaseTaskEmitter implements RequestMetricEmitter {
    private final long started;
    private final long startTick;
    private final AtomicLong idSource;
    private final RequestMetricSink sink;
    private final ConcurrentLinkedQueue<RequestMetric> metrics;
    private final ConcurrentLinkedQueue<RequestTask> requestTask;
    private final TaskEmitter root;

    public StandardRequestEmitter(MetricDimension dimension, RequestMetricSink sink) {
        super(dimension);
        this.started = System.currentTimeMillis();
        this.startTick = System.nanoTime();
        this.idSource = new AtomicLong(0L);
        this.root = new TaskEmitter(dimension, -1L, idSource.getAndIncrement());
        this.sink = sink;
        this.metrics = new ConcurrentLinkedQueue<RequestMetric>();
        this.requestTask = new ConcurrentLinkedQueue<RequestTask>();
    }

    class RequestTaskMetricEmitter extends BaseMetricEmitter {
        private long taskId;

        RequestTaskMetricEmitter(MetricDimension dimension, long taskId) {
            super(dimension);
            this.taskId = taskId;
        }

        @Override
        protected MetricEmitter create(MetricDimension dimension) {
            return new RequestTaskMetricEmitter(dimension, taskId);
        }

        @Override
        public void emit(String name, long count) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.COUNT, count, 0L, TimeUnit.NANOSECONDS)));
        }

        @Override
        public void emitDuration(String name, long duration, TimeUnit unit) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.DURATION, duration, 0L, unit)));
        }

        @Override
        public void emitSpan(String name, long start, long end, TimeUnit unit) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.DURATION, start, end, unit)));
        }
    }

    @Override
    public long getTaskId() {
        return root.getTaskId();
    }

    @Override
    public long elapsedTicks() {
        return System.nanoTime() - startTick;
    }

    class TaskEmitter extends BaseTaskEmitter {
        final AtomicBoolean ended = new AtomicBoolean(false);
        final long startTime;
        final long parentId;
        final long taskId;

        TaskEmitter(MetricDimension dimension, long parentId, long taskId) {
            super(dimension);
            this.startTime = elapsedTicks();
            this.parentId = parentId;
            this.taskId = taskId;
        }

        @Override
        public long getTaskId() {
            return taskId;
        }

        @Override
        protected TaskMetricEmitter createTask(MetricDimension dimension) {
            return new TaskEmitter(dimension, taskId, idSource.getAndIncrement());
        }

        @Override
        protected MetricEmitter create(MetricDimension dimension) {
            return new RequestTaskMetricEmitter(dimension, taskId);
        }

        @Override
        public void emit(String name, long count) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.COUNT, count, 0L, TimeUnit.NANOSECONDS)));
        }

        @Override
        public void emitDuration(String name, long duration, TimeUnit unit) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.DURATION, duration, 0L, unit)));
        }

        @Override
        public void emitSpan(String name, long start, long end, TimeUnit unit) {
            metrics.add(new RequestMetric(taskId, new Metric(dimension, name, MetricType.SPAN, start, end, unit)));
        }

        @Override
        public void end() {
            if(ended.compareAndSet(false, true)) {
                long now = elapsedTicks();
                emitSpan("task", startTime, now, TimeUnit.NANOSECONDS);
                emitDuration("duration", now - startTime, TimeUnit.NANOSECONDS);
                requestTask.add(new RequestTask(dimension, parentId, taskId, startTime, now));
            }
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
    protected TaskMetricEmitter createTask(MetricDimension dimensions) {
        return root.createTask(dimensions);
    }

    @Override
    protected MetricEmitter create(MetricDimension dimension) {
        return root.create(dimension);
    }

    static class BaseRequestEvent implements RequestEvent {
        private final long startTime;
        private final MetricDimension dimension;
        private final Queue<RequestTask> tasks;
        private final Queue<RequestMetric> metrics;

        BaseRequestEvent(long startTime, MetricDimension dimension, Queue<RequestTask> tasks, Queue<RequestMetric> metrics) {                
            this.startTime = startTime;
            this.dimension = dimension;
            this.tasks = tasks;
            this.metrics = metrics;
        }

        @Override
        public long getStartTime() {
            return startTime;
        }

        @Override
        public MetricDimension getRequestDimensions() {
            return dimension;
        }

        @Override
        public Queue<RequestTask> getTasks() {
            return tasks;
        }

        @Override
        public Queue<RequestMetric> getMetrics() {
            return metrics;
        }
    }

    @Override
    public RequestEvent complete() {
        end();
        RequestEvent event = new BaseRequestEvent(started, dimension, requestTask, metrics);
        sink.emitRequest(event);
        return event;
    }

    @Override
    public void end() {
        root.end();
    }
}
