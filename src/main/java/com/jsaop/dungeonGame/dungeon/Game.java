package com.jsaop.dungeonGame.dungeon;

import com.jsaop.dungeonGame.entity.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;


public class Game {
    private Player player;
    private Goal goal;
    private Enemy enemy;
    private Enemy enemy2;
    private List<Entity> entities;
    private Dungeon dungeon;
    private char[][] masterMap;
    private boolean[][] explored;
    private int turn;
    private boolean hasWon;

    public Game() {
        this(100, 100, System.out);
    }

    public Game(int width, int height, PrintStream out) {
        Random random = new Random();
        dungeon = new Dungeon(width, height, random);
        masterMap = dungeon.getMap();
        explored = new boolean[width][height];
        EntityManager entityManager = new EntityManager(random, out, masterMap);

        player = entityManager.Player();
        goal = entityManager.Goal();
        enemy = entityManager.AddEnemy();
        enemy2 = entityManager.AddEnemy();

        entities = entityManager.GetEntities();

        turn = 0;
        hasWon = false;
        pickPlayerStartLocation();
        pickGoalLocationFarFromPlayer();

        //yooo wtffff jon
        try {
            pickEnemyStartLocation(enemy);
            pickEnemy2StartLocation(enemy2);
        } catch (StackOverflowError ex) {
            out.println("Exploded on pick enemy start. trying one more time");
            pickEnemyStartLocation(enemy);
            pickEnemy2StartLocation(enemy2);
        }

        updateExplored(); // starting view
    }

    public void takeTurn(Action action) {
        turn++;

        player.execute(action);

        updateExplored();

        if (playerIsOnTreasure())
            hasWon = true;

        enemy.takeTurn();
        enemy2.takeTurn();

    }

    private void updateExplored() {
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                if (player.playerCanSee(x, y)) {
                    explored[x][y] = true;
                }
            }
        }

    }

    private void pickGoalLocationFarFromPlayer() {

        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double tempDistance;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (masterMap[i][j] != BlockValues.WALL.getValue()) {
                    tempDistance = calcSquareDistance(player.getX(), player.getY(), i, j);
                    if (maxDistance < tempDistance) {
                        maxDistance = tempDistance;
                        maxX = i;
                        maxY = j;
                    }
                }
            }
        }

        goal.setX(maxX);
        goal.setY(maxY);
    }

    private void pickEnemyStartLocation(Enemy enemy) {
        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double distanceEnemyToGoal;
        double distanceEnemyToPlayer;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (masterMap[i][j] != BlockValues.WALL.getValue()) {
                    distanceEnemyToGoal = calcSquareDistance(player.getX(), player.getY(), i, j);
                    distanceEnemyToPlayer = calcSquareDistance(goal.getX(), goal.getY(), i, j);
                    if (maxDistance < distanceEnemyToGoal && maxDistance < distanceEnemyToPlayer) {
                        maxDistance = (distanceEnemyToGoal < distanceEnemyToPlayer) ? distanceEnemyToGoal : distanceEnemyToPlayer;
                        maxX = i;
                        maxY = j;
                    }
                }
            }
        }

        enemy.setX(maxX);
        enemy.setY(maxY);
    }

    private void pickEnemy2StartLocation(Enemy e) {
        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double distanceEnemy2ToPlayer;
        double distanceEnemy2ToEnemy;
        for (int i = dungeon.getWidth() - 1; i >= 0; i--) {
            for (int j = dungeon.getHeight() - 1; j > 0; j--) {
                if (masterMap[i][j] != BlockValues.WALL.getValue()) {
                    distanceEnemy2ToPlayer = calcSquareDistance(goal.getX(), goal.getY(), i, j);
                    distanceEnemy2ToEnemy = calcSquareDistance(enemy.getX(), enemy.getY(), i, j);
                    if (maxDistance < distanceEnemy2ToPlayer && maxDistance < distanceEnemy2ToEnemy) {
                        maxDistance = Math.max(distanceEnemy2ToEnemy, distanceEnemy2ToPlayer);
                        maxX = i;
                        maxY = j;
                    }
                }
            }
        }

        e.setX(maxX);
        e.setY(maxY);

        if (e.canSee(player.getX(), player.getY(), player.getVisionRange())) {
            pickEnemy2StartLocation(e);
        }
    }


    private static double calcSquareDistance(int x1, int y1, int x2, int y2) {
        return (double) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private boolean playerIsOnTreasure() {
        return (player.getX() == goal.getX() && player.getY() == goal.getY());
    }

    private void pickPlayerStartLocation() {
        for (int i = 0; i < dungeon.getWidth(); i++)
            for (int j = 0; j < dungeon.getHeight(); j++)
                if (masterMap[i][j] != BlockValues.WALL.getValue()) {
                    player.translate(i, j);
                    return;
                }
    }

    private static void placeEntityOnMap(Entity entity, char[][] map) {
        map[entity.getX()][entity.getY()] = entity.getGlyph();
    }

    public Player getPlayer() {
        return player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public char[][] getMap() {

        char[][] copy = dungeon.getMapCopy();

//        for (Entity e : entities) {
//            placeEntityOnMap(e, copy);
//        }

        return copy;
    }

    public int getTurn() {
        return turn;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public boolean isExplored(int x, int y) {
        return explored[x][y];
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
