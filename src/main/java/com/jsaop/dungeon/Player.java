package com.jsaop.dungeon;

public class Player extends Entity {

    private boolean isDead = false;

    public Player() {
        this.x=0;
        this.y=0;
        this.glyph ='@';
    }

    public Player(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }

    public boolean isDead() {
        return isDead;
    }

    public void kill() {
        isDead = true;
    }
}
