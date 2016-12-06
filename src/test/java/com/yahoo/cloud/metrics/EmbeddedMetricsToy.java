/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics;

import com.google.common.collect.Lists;
import com.yahoo.cloud.metrics.api.MetricDimension;
import com.yahoo.cloud.metrics.api.StandardRequestEmitter;
import org.asynchttpclient.AsyncCompletionHandlerBase;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by @xythian - https://github.com/xythian on 3/13/14.
 */
public class EmbeddedMetricsToy {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        ServletContextHandler root = new ServletContextHandler(contexts, "/");
        root.addEventListener(new MetricsContextListener());
        root.addFilter(MetricsFilter.class, "/*", EnumSet.allOf(DispatcherType.class))
                .setAsyncSupported(true);
        root.addServlet(ToyWorkServlet.class, "/*");
        server.setHandler(contexts);
        server.start();
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new RunSomeQueries(8080));
        server.join();
    }

    private static class RunSomeQueries implements Runnable {
        public static final Executor SAME_THREAD = new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        };
        private int port;

        private RunSomeQueries(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            LoggerEmitter clientEmitter = new LoggerEmitter();
            AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().build();
            AsyncHttpClient c = new DefaultAsyncHttpClient(config);
            List<ListenableFuture> reqs = Lists.newArrayList();
            for (int i = 0; i < 2; ++i) {
                final StandardRequestEmitter emitter = new StandardRequestEmitter(new MetricDimension().with("client", String.valueOf(i)), clientEmitter);
                BoundRequestBuilder b = c.prepareGet("http://localhost:" + port);
                Request req = b.build();
                AsyncHandler<Response> handler = new ToyClientMetricsEmitter(emitter, req, new AsyncCompletionHandlerBase());
                ListenableFuture<Response> result = c.executeRequest(req, handler);
                result.addListener(new Runnable() {
                    @Override
                    public void run() {
                        emitter.complete();
                    }
                }, SAME_THREAD);

                reqs.add(result);
            }
            for (ListenableFuture<Response> out : reqs) {
                try {
                    Response r = out.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
