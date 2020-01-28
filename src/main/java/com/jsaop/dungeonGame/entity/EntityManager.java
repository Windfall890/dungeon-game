package com.jsaop.dungeonGame.entity;

import com.jsaop.dungeonGame.dungeon.Console;
import com.jsaop.dungeonGame.gui.DialogConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntityManager {

    private final Goal goal;
    private char[][] masterMap;
    private Player player;
    private int IdCounter = 0;
    private List<Entity> entities;

    public EntityManager(Random random, Console printStream, char[][] masterMap, int level) {
        Entity.out = printStream;
        Entity.random = random;
        this.masterMap = masterMap;

        this.entities = new ArrayList<>();

        this.player = new Player();

        player.setVisionRange(calcVisionRange(level));
        player.setMap(masterMap);
        add(player);

        this.goal = new Goal();
        add(this.goal);

        spawnEnemies(level);


    }

    private int calcVisionRange(int level) {

        if (level > 6) {

            int range = Player.BASE_VISION_RANGE + 8 - level;

            if (range < Player.MIN_VISION_RANGE)
                return Player.MIN_VISION_RANGE;

            return range;
        }
        return Player.BASE_VISION_RANGE;
    }

    private void spawnEnemies(int level) {

        if (level > 1) addEnemy();
        if (level > 2) addEnemy();
        if (level > 4) addEnemy();
        if (level > 6) addEnemy();
        if (level > 8) addEnemy();
    }

    private void add(Entity e) {
        e.id = IdCounter;
        entities.add(e);
        IdCounter++;
    }

    public Player Player() {

        return player;
    }

    public Goal Goal() {
        return goal;
    }

    public Enemy addEnemy() {
        Enemy enemy = new Enemy();
        enemy.setMap(masterMap);
        enemy.addTarget(player);
        add(enemy);
        return enemy;
    }

    public List<Entity> GetEntities() {
        return entities;
    }

    public List<Entity> GetNpcs() {
        return entities.stream().filter(entity -> entity.getClass() == Enemy.class).collect(Collectors.toList());
    }
}
