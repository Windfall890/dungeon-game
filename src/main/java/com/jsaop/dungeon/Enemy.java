package com.jsaop.dungeon;

public class Enemy extends Entity{

    public Enemy() {
        this(0,0, '*');
    }

    public Enemy(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }
}
