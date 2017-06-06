package com.jsaop.dungeon;

import java.util.ArrayList;
import java.util.List;

import static com.jsaop.dungeon.Action.*;
import static com.jsaop.dungeon.BlockValues.ENEMY;

public class Enemy extends Entity {

    public static final int CHASE = 1;
    public static final int WANDER = 0;
    public static final int ATTACK = 2;
    public static final int SEEK = 3;

    private static final int DEFAULT_ENEMY_POWER = 10;
    private List<Entity> targets;
    private int power;
    private int state;

    public Enemy() {
        this(0, 0, ENEMY.getValue(), DEFAULT_ENEMY_POWER);
    }

    public Enemy(int x, int y, char glyph, int power) {
        this.x = x;
        this.y = y;
        this.glyph = glyph;
        this.targets = new ArrayList<>();
        this.power = power;
    }

    public void takeTurn() {

        if (isTouching(targets.get(0))) {
            attack();
        } else if (random.nextDouble() > 0.3)
            chase();
        else {
            moveRandomly();
            moveRandomly();
        }
    }

    private void attack() {
        targets.get(0).damage(power);
        if (targets.get(0).isDead()) {
            targets.remove(0);
        }
    }

    public void chase() {

        Action dx = (targets.get(0).getX() > getX()) ? RIGHT : LEFT;
        Action dy = (targets.get(0).getY() > getY()) ? DOWN : UP;

        move(dx);
        move(dy);

    }

    public Entity getCurrentTarget() {
        return targets.get(0);
    }

    public void addTarget(Entity target) {
        targets.add(0, target);
    }

    public void setMap(char[][] map) {
        this.map = map;
    }
}
