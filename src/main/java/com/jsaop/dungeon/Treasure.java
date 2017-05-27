package com.jsaop.dungeon;

public class Treasure {

    private int x;
    private int y;
    private char glyph;

    public Treasure() {
        this(0,0, '*');
    }

    public Treasure(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getGlyph() {
        return glyph;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }
}


