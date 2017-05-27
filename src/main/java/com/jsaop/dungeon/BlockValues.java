package com.jsaop.dungeon;

public enum BlockValues {
    FLOOR(16,'.'),
    WALL(192,'#'),
    EMPTY(255,' ');

    private final int value;
    private final char glyph;

    BlockValues(int value, char glyph) {
        this.value = value;
        this.glyph = glyph;
    }

    public int getValue() {
        return value;
    }

    public char getGlyph() {
        return glyph;
    }

    public char valueToGlyph(int v){
        return glyph;
    }
}
