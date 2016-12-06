/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.yahoo.cloud.metrics.api.MetricEmitter;
import com.yahoo.cloud.metrics.api.TaskMetricEmitter;

import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Request;

public class ToyClientMetricsEmitter<T> implements AsyncHandler<T> {
    private final TaskMetricEmitter emitter;
    private final AsyncCompletionHandler<T> asyncHandler;
    private final AtomicInteger httpResponseBodyPartLength = new AtomicInteger(0);
    private int statusCode;

    public ToyClientMetricsEmitter(TaskMetricEmitter parent, Request request, AsyncCompletionHandler<T> asyncHandler) {
        this.emitter = parent.start(
                "client.method", request.getMethod(),
                "client.url", request.getUrl(),
                "client.host", request.getUri().getHost());
        this.asyncHandler = asyncHandler;
    }

    @Override
    public void onThrowable(Throwable t) {
        try {
            emitter.emit("exception", 1);
            emitter.end();
        } finally {
            this.asyncHandler.onThrowable(t);
        }
    }

    @Override
    public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
            throws Exception {
        httpResponseBodyPartLength.addAndGet(bodyPart.getBodyPartBytes().length);
        return asyncHandler.onBodyPartReceived(bodyPart);
    }

    @Override
    public State onStatusReceived(HttpResponseStatus responseStatus)
            throws Exception {
        statusCode = responseStatus.getStatusCode();
        return asyncHandler.onStatusReceived(responseStatus);
    }

    @Override
    public State onHeadersReceived(HttpResponseHeaders headers)
            throws Exception {
        return this.asyncHandler.onHeadersReceived(headers);
    }

    @Override
    public T onCompleted() throws Exception {
        MetricEmitter resultEmitter = emitter.create("code", String.valueOf(statusCode));
        resultEmitter.emit("count", 1);
        if (httpResponseBodyPartLength.get() > 0) {
            resultEmitter.emit("length", httpResponseBodyPartLength.get());
        }
        emitter.end();
        return asyncHandler.onCompleted();
    }
}
