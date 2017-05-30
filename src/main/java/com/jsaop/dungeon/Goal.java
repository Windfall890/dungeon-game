package com.jsaop.dungeon;

public class Goal extends Entity{

    public Goal() {
        this(0,0, '*');
    }

    public Goal(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }
}


