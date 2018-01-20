package com.tollwood;

public class Recommendation {
    private final long id;
    private final String name;

    public Recommendation(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
