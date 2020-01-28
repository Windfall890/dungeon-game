package com.jsaop.dungeonGame.entity;

import com.jsaop.dungeonGame.Util.Calculation;
import com.jsaop.dungeonGame.dungeon.Action;
import com.jsaop.dungeonGame.dungeon.Console;
import com.jsaop.dungeonGame.dungeon.SystemConsole;

import java.io.PrintStream;
import java.util.Random;

import static com.jsaop.dungeonGame.dungeon.BlockValues.WALL;

public abstract class Entity {

    private static final int DEFAULT_HP = 10;
    public static Console out = new SystemConsole();

    protected static Random random = new Random();

    public int id = -1; // if -1, not in the entity manager
    protected String name;
    protected int x;
    protected int y;
    protected char glyph;
    protected int hp = DEFAULT_HP;
    protected char[][] map;


    protected void say(String phrase) {
        out.write(name + " " + phrase.trim());
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

    public void damage(int d) {
        out.write(name + " has taken " + d + " damage.");
        hp -= d;
    }

    public abstract void takeTurn();


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

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isTouching(Entity b) {
        return Math.abs(x - b.getX()) <= 1 &&
                Math.abs(y - b.getY()) <= 1;
    }

    public boolean canSee(int x, int y, int range) {
        return (Calculation.SquareDistance(x, y, getX(), getY()) < (range * range));

    }

    public void moveRandomly() {
        Action[] values = Action.values();
        int pick = random.nextInt(values.length);
        move(values[pick]);
    }

    public void setMap(char[][] map) {
        this.map = map;
    }


    public int getHp() {
        return hp;
    }
}
