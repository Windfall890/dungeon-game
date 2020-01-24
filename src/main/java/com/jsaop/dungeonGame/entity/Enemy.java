package com.jsaop.dungeonGame.entity;

import com.jsaop.dungeonGame.dungeon.Action;

import java.util.ArrayList;
import java.util.List;

import static com.jsaop.dungeonGame.dungeon.Action.*;
import static com.jsaop.dungeonGame.dungeon.BlockValues.ENEMY;

public class Enemy extends Entity {

    public static final int CHASE = 1;
    public static final int WANDER = 0;
    public static final int ATTACK = 2;
    public static final int SEEK = 3;

    private static final int DEFAULT_ENEMY_POWER = 5;
    private List<Entity> targets;
    private int power;
    private int state;
    private int visionRange = 22;

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

        switch (state) {
            case WANDER:
                wander();
                break;
            case CHASE:
                chase();
                break;
            case ATTACK:
                attack();
                break;
            case SEEK:
                seekCenter();
                break;
        }
    }

    private void seekCenter() {
        Action dx = (map[0].length / 2 > getX()) ? RIGHT : LEFT;
        Action dy = (map.length / 2 > getY()) ? DOWN : UP;
        move(dx);
        move(dy);

        if (canSeeTarget()) {
            out.println("You have been spotted!");
            state = CHASE;
        } else if (random.nextDouble() < .5) {
            state = WANDER;

        }
    }

    private void wander() {
        moveRandomly();
        moveRandomly();

        if (canSeeTarget()) {
            out.println("You have been spotted!");
            state = CHASE;
        } else if (random.nextDouble() < .5) {
            state = SEEK;
        }


    }

    private void attack() {
        targets.get(0).damage(power);

        if (targets.get(0).isDead()) {
            targets.remove(0);
            state = WANDER;
        } else {
            if (!isTouching(targets.get(0)))
                state = CHASE;
        }

    }

    public void chase() {

        if (random.nextDouble() > .3) {
            moveTowardsPlayer();
        } else {
            moveRandomly();
            moveRandomly();
        }

        if (isTouching(targets.get(0))) {
            state = ATTACK;
        } else if (!canSeeTarget()) {
            out.println("The enemy has lost your trail.");
            state = SEEK;
        }
    }

    private void moveTowardsPlayer() {
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

    private boolean canSeeTarget() {
        return (canSee(targets.get(0).getX(), targets.get(0).getY(), visionRange));
    }

    public void setState(int state) {
        this.state = state;
    }
}
