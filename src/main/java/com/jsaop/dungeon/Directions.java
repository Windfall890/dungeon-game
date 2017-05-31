package com.jsaop.dungeon;

public enum Directions {
    DOWN("DOWN"),
    UP("UP"),
    LEFT("LEFT"),
    RIGHT("RIGHT");

    private final String value;

    Directions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
