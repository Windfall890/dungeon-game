package com.jsaop.dungeon;

import static com.jsaop.dungeon.BlockValues.ENEMY;

public class Enemy extends Entity{

    public Enemy() {
        this(0,0, ENEMY.getValue());
    }

    public Enemy(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }
}
