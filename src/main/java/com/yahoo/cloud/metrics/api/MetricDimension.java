/**
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.cloud.metrics.api;

/**
 * Represents a dimensional point to emit metrics.
 *
 * Immutable.
 *
 * This implementation is an experiment.
 */
public final class MetricDimension {
    private final MetricDimension parent;
    private final String key;
    private final String value;

    public MetricDimension() {
        this.parent = null;
        this.key = null;
        this.value = null;
    }

    private MetricDimension(MetricDimension parent, String key, String value) {
        this.parent = parent;
        this.key = key;
        this.value = value;
    }

    public MetricDimension with(String key, String value) {
        verifyNotSet(key);
        return new MetricDimension(this, key, value);
    }

    public MetricDimension getParent() {
        return parent;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private void verifyNotSet(String key) {
        if(this.key == null || key == null) {
            return;
        }
        //which way is faster?
        if (this.key.equals(key)) { 
       // if (this.key.hashCode() == key.hashCode() && this.key.equals(key)) {
            throw new IllegalStateException("Invalid metrics state: duplicate dimension '" + key + "'");
        }
        if (parent != null) {
            parent.verifyNotSet(key);
        }
    }

    public MetricDimension merge(MetricDimension addedDimensions) {
        if(addedDimensions.parent == null || addedDimensions == this) {
            return this;
        } else {
            MetricDimension parent = merge(addedDimensions.getParent());
            return parent.with(addedDimensions.getKey(), addedDimensions.getValue());
        }
    }
    
    public int size() {
        if (key == null)
            return 0;
        if (parent != null) {
            return 1 + parent.size();
        } else {
            return 1;
        }
    }
    
    public String getValue(String searchKey) {
        if (this.key == null)
            return null;
        if (this.key.equals(searchKey))
            return this.value;
        else if (this.parent != null)
            return parent.getValue(searchKey);
        return null;
    }

    @Override
    public String toString() {
        return "MetricDimension{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricDimension that = (MetricDimension) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
