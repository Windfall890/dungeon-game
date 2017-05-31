package com.jsaop.dungeon;

import static com.jsaop.dungeon.BlockValues.WALL;

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

    public void move(Directions direction, char[][] map) {
        switch (direction) {
            case DOWN:
                if (map[x][y + 1] != WALL.getValue())
                    y = (y + 1);
                break;
            case UP:
                if (map[x][y - 1] != WALL.getValue())
                    y = (y - 1);
                break;
            case LEFT:
                if (map[x - 1][y] != WALL.getValue())
                    x = (x - 1);
                break;
            case RIGHT:
                if (map[x + 1][y] != WALL.getValue())
                    x = (x + 1);
                break;
        }
    }

    public boolean isTouching(Entity b) {
        return Math.abs(x - b.getX()) <= 1 &&
                Math.abs(y - b.getY()) <= 1;
    }
}
