package com.jsaop.dungeonGame.dungeon;

public enum BlockValues {
    FLOOR('.'),
    WALL('#'),
    EMPTY(' '),
    PLAYER('@'),
    GOAL('<'),
    ENEMY('O');

    private final char value;

    BlockValues(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
