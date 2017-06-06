package com.jsaop.dungeon;

import java.util.Random;

import static com.jsaop.dungeon.BlockValues.WALL;

public class Entity {

    private static final int DEFAULT_HP = 10;

    protected Random random = new Random();

    protected int x;
    protected int y;
    protected char glyph;
    protected int hp = DEFAULT_HP;
    protected char[][] map;

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

    public boolean isDead() {
        return (hp <= 0);
    }

    public void kill() {
        hp = 0;
    }

    public void damage(int d){
        hp -=d;
    }

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Action direction) {
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
            default:
        }
    }

    public boolean isTouching(Entity b) {
        return Math.abs(x - b.getX()) <= 1 &&
                Math.abs(y - b.getY()) <= 1;
    }

    public boolean canSee(int x, int y, int range) {
        return (calcSquareDistance(x, y, getX(), getY()) < range);

    }

    public void moveRandomly() {
        Action[] values = Action.values();
        int pick = random.nextInt(values.length);
        move(values[pick]);
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    private static double calcSquareDistance(int x1, int y1, int x2, int y2) {
        return (double) ((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }
}
