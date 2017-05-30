package com.jsaop.dungeon;

/**
 * Created by jsaop on 5/30/17.
 */
public class Entity {
    protected int x;
    protected int y;
    protected char glyph;

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

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
