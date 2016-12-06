/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.yahoo.cloud.metrics.api.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by @xythian - https://github.com/xythian on 3/13/14.
 */
public final class MetricEventWriter {
    private long timeToTime(long timeMilliseconds) {
        return timeMilliseconds / 1000L;
    }

    private long tickToDuration(long ticks) {
        return TimeUnit.NANOSECONDS.toMicros(ticks);
    }

    private void emitDimensions(MetricDimension dimension, JsonGenerator output) throws IOException {
        emitDimensions(dimension, output, null);
    }

    private void emitDimensions(MetricDimension dimension, JsonGenerator output, MetricDimension stop) throws IOException {
        if(dimension == null || dimension == stop || dimension.getKey() == null) {
            return;
        }
        emitDimensions(dimension.getParent(), output, stop);
        if(dimension.getParent() != null) {
            output.writeStringField(dimension.getKey(), dimension.getValue());
        }
    }

    public void write(MetricEvent event, JsonGenerator output) throws IOException {
        final Metric metric = event.getMetric();
        output.writeStartObject();
        output.writeStringField("type", "metric");
        output.writeNumberField("time", timeToTime(event.getTime()));
        output.writeObjectFieldStart("dims");
        emitDimensions(metric.getDimension(), output);
        output.writeEndObject();
        emitMetric(event.getMetric(), output);
        output.writeEndObject();
    }

    private void emitMetric(Metric metric, JsonGenerator output) throws IOException {
        output.writeStringField("name", metric.getName());
        switch(metric.getType()) {
            case COUNT:
                output.writeNumberField("count", metric.getValue());
                break;
            case DURATION:
                output.writeNumberField("duration", metric.getUnits().toMicros(metric.getValue()));
                break;
            case SPAN:
                output.writeNumberField("start", metric.getUnits().toMicros(metric.getValue()));
                output.writeNumberField("end", metric.getUnits().toMicros(metric.getEnd()));
                break;
        }
    }

    public void writeRequest(RequestEvent event, JsonGenerator output) throws IOException {
        output.writeStartObject();
        output.writeStringField("type", "request");
        output.writeNumberField("time", timeToTime(event.getStartTime()));
        MetricDimension requestDimensions = event.getRequestDimensions();
        output.writeObjectFieldStart("dims");
        emitDimensions(event.getRequestDimensions(), output);
        output.writeEndObject();
        output.writeArrayFieldStart("tasks");
        for(RequestTask task : event.getTasks()) {
            output.writeStartObject();
            if(task.getDimension() != requestDimensions) {
                output.writeObjectFieldStart("dims");
                emitDimensions(task.getDimension(), output, requestDimensions);
                output.writeEndObject();
            }
            if(task.getParentId() >= 0L) {
                output.writeNumberField("parent", task.getParentId());
            }
            output.writeNumberField("id", task.getTaskId());
            writeDuration("start", task.getStartTick(), output);
            writeDuration("end", task.getStartTick(), output);
            output.writeEndObject();
        }
        output.writeEndArray();

        output.writeArrayFieldStart("metrics");
        for(RequestMetric task : event.getMetrics()) {
            output.writeStartObject();
            output.writeNumberField("task", task.getTaskId());
            Metric metric = task.getMetric();
            if(metric.getDimension() != requestDimensions) {
                output.writeObjectFieldStart("dims");
                emitDimensions(metric.getDimension(), output, requestDimensions);
                output.writeEndObject();
            }
            emitMetric(metric, output);
            output.writeEndObject();
        }
        output.writeEndArray();
        output.writeEndObject();
    }

    private void writeDuration(String name, long duration, JsonGenerator output) throws IOException {
        output.writeNumberField(name, tickToDuration(duration));
    }

}
