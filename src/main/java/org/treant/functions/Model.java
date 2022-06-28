package org.treant.functions;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Model {
    String name;
    Integer count;
    @BsonProperty("last_date")
    String last;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
