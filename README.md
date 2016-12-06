# Thread-Safe api for logging metrics

## What is special of this metrics api?

This api provides an efficient and thread-safe way of loggin metrics in a multi-thread environment. It is easy to use and very flexible. By plugging in different Sink classes metrics can be logged to various back-end targets.

## Usage

Create an ``StandardRequestEmitter`` object with root ``MetricDimension`` and a ``Sink`` object. You are ready to log your metrics. ``MetricDimension`` is used as identifier of metrics and also can be used to log event messages. The various types of metrics may be logged using one of the following methods: ``emit``, ``emitDuration`` and ``emitSpan``. By calling ``complete`` all the metrics you logged to the ``StandardRequestEmitter`` object will be pushed to the back-end of ``Sink`` class.

## Maven

```xml
<!-- Metrics Api -->
<dependency>
    <groupId>com.yahoo.monitor</groupId>
    <artifactId>metrics_api</artifactId>
</dependency>
```

## License

Copyright 2016 Yahoo Inc.

Licensed under the terms of the Apache version 2.0 license. See LICENSE file for terms.
