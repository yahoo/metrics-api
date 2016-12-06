/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MetricDimensionTest {

    @Test
    public void testSize() throws Exception {
        MetricDimension dimension = new MetricDimension()
                .with("key1", "value1")
                .with("key2", "value2")
                .with("key3", "value3")
                .with("key4", "value4")
                .with("key5", "value5")
                .with("key6", "value6");
        assertEquals(6, dimension.size());
    }
    
    @Test
    public void testGetValue() throws Exception {
        MetricDimension dimension = new MetricDimension();
        assertNull("value", dimension.getValue("key"));
        
        dimension = dimension.with("key1", "value1")
                .with("key2", "value2")
                .with("key3", "value3")
                .with("key4", "value4")
                .with("key5", "value5")
                .with("key6", "value6");
        assertEquals("value1", dimension.getValue("key1"));
        assertEquals("value4", dimension.getValue("key4"));
        assertNull("value11", dimension.getValue("key11"));
        
    }
    
}
