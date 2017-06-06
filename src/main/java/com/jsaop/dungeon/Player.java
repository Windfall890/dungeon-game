package com.jsaop.dungeon;

import static com.jsaop.dungeon.Action.WAIT;

public class Player extends Entity {


    private static final int STARTING_PLAYER_HP = 10;

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
            System.out.println("player is waiting");
    }

}

