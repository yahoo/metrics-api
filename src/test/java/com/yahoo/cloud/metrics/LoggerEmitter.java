/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.yahoo.cloud.metrics.api.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by @xythian - https://github.com/xythian on 3/14/14.
 */
public class LoggerEmitter implements MetricSink, RequestMetricSink {
    private static final Logger log = Logger.getLogger(LoggerEmitter.class.getName());
    private static final JsonFactory factory = new JsonFactory();
    private static final MetricEventWriter writer = new MetricEventWriter();


    @Override
    public void emit(MetricEvent event) {
        try {
            StringWriter out = new StringWriter();
            JsonGenerator gen = factory.createGenerator(out);
            writer.write(event, gen);
            gen.flush();
            log.info(out.toString());
        } catch(IOException e) {
            log.log(Level.FINE, "metric serialization error", e);

        }
    }

    @Override
    public void emitRequest(RequestEvent event) {
        try {
            StringWriter out = new StringWriter();
            JsonGenerator gen = factory.createGenerator(out);
            writer.writeRequest(event, gen);
            gen.flush();
            log.info(out.toString());
        } catch(IOException e) {
            log.log(Level.FINE, "metric serialization error", e);

        }
    }
}
