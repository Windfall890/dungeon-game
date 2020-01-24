package com.jsaop.dungeonGame.entity;

import com.jsaop.dungeonGame.dungeon.Action;
import com.jsaop.dungeonGame.dungeon.BlockValues;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;

import static com.jsaop.dungeonGame.dungeon.Action.WAIT;

public class Player extends Entity {


    private static final int STARTING_PLAYER_HP = 15;

    private int visionRange = 5;

    public Player() {
        this(0, 0, BlockValues.PLAYER.getValue(), STARTING_PLAYER_HP);
    }

    public Player(int x, int y, char glyph, int hp) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
        this.hp = hp;
    }

    public void execute(Action action) {
        if (action.isMoveAction())
            move(action);

        if (action == WAIT)
           out.println("player is waiting");
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    public boolean playerCanSee(int x, int y) {
        return canSee(x, y, visionRange);
    }
}
