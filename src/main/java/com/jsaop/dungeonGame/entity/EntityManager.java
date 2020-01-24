package com.jsaop.dungeonGame.entity;

import java.io.PrintStream;
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

    public EntityManager(Random random, PrintStream printStream, char[][] masterMap) {
        Entity.out = printStream;
        Entity.random = random;
        this.masterMap = masterMap;

        this.entities = new ArrayList<>();

        this.player = new Player();
        player.setMap(masterMap);
        add(player);

        this.goal = new Goal();
        add(this.goal);
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

    public Enemy AddEnemy() {
        Enemy enemy = new Enemy();
        enemy.setMap(masterMap);
        enemy.addTarget(player);
        add(enemy);
        return enemy;
    }

    public List<Entity> GetEntities() {
        return entities;
    }

    public List<Entity> GetEnemies() {
        return entities.stream().filter(entity -> entity.getClass() == Enemy.class).collect(Collectors.toList());
    }
}
