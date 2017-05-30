package com.jsaop.dungeon;

public class PlayerCharacter extends Entity {

    public PlayerCharacter() {
        this.x=0;
        this.y=0;
        this.glyph ='@';
    }

    public PlayerCharacter(int x, int y, char glyph) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
    }

}
