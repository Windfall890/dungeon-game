package com.jsaop.dungeonGame.entity;

import com.jsaop.dungeonGame.dungeon.BlockValues;

public class Goal extends Entity {

    public Goal() {
        this(0, 0, BlockValues.GOAL.getValue());
    }

    public Goal(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }
}


