package com.jsaop.dungeon;

public class PlayerCharacter {
    private int x,y;
    private char body;

    public PlayerCharacter() {
        this.x=0;
        this.y=0;
        this.body='@';
    }

    public PlayerCharacter(int x, int y, char body) {
        this.x = x;
        this.y = y;
        this.body = body;
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

    public char getBody() {
        return body;
    }

    public void setBody(char body) {
        this.body = body;
    }

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
