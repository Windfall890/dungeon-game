package com.jsaop.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
    private Player player;
    private Goal goal;
    private Enemy enemy;
    private List<Entity> entities;
    private Dungeon dungeon;
    private char[][] masterMap;
    private boolean[][] explored;
    private int turn;
    private boolean hasWon;

    private Random random;

    public Game() {
        this(100, 100);
    }

    public Game(int width, int height) {
        dungeon = new Dungeon(width, height);
        masterMap = dungeon.getMap();
        explored = new boolean[width][height];

        random = new Random();

        player = initPlayer();
        goal = new Goal();
        enemy = initEnemy();

        entities = new ArrayList<>();

        entities.add(player);
        entities.add(goal);
        entities.add(enemy);

        turn = 0;
        hasWon = false;
        pickPlayerStartLocation();
        pickGoalLocationFarFromPlayer();
        pickEnemyStartLocation();

        updateExplored(); // starting view
    }

    private Player initPlayer() {
        Player player = new Player();
        player.setMap(masterMap);
        return player;
    }

    private Enemy initEnemy() {
        Enemy enemy = new Enemy();
        enemy.setMap(masterMap);
        enemy.addTarget(player);
        return enemy;
    }

    public void takeTurn(Action action) {
        turn++;

        player.execute(action);

        updateExplored();

        if (playerIsOnTreasure())
            hasWon = true;

        enemy.takeTurn();

    }

    private void updateExplored() {
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                if(player.canSee(x,y, 11)){
                    explored[x][y] = true;
                }
            }
        }

    }

    private boolean playerCanSee(int x, int y) {
        return (calcSquareDistance(x, y, player.getX(), player.getY()) < 11);
    }

    private void pickGoalLocationFarFromPlayer() {

        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double tempDistance;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (masterMap[i][j] != '#') {
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

    private void pickEnemyStartLocation() {
        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double distanceEnemyToGoal;
        double distanceEnemyToPlayer;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (masterMap[i][j] != '#') {
                    distanceEnemyToGoal = calcSquareDistance(player.getX(), player.getY(), i, j);
                    distanceEnemyToPlayer = calcSquareDistance(goal.getX(), goal.getY(), i, j);
                    if (maxDistance < distanceEnemyToGoal && maxDistance < distanceEnemyToPlayer) {
                        maxDistance = (distanceEnemyToGoal < distanceEnemyToPlayer)? distanceEnemyToGoal: distanceEnemyToPlayer;
                        maxX = i;
                        maxY = j;
                    }
                }
            }
        }

        enemy.setX(maxX);
        enemy.setY(maxY);
    }

    private static double calcSquareDistance(int x1, int y1, int x2, int y2) {
        return (double) ((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    private boolean playerIsOnTreasure() {
        return (player.getX() == goal.getX() && player.getY() == goal.getY());
    }

    private void pickPlayerStartLocation() {
        for (int i = 0; i < dungeon.getWidth(); i++)
            for (int j = 0; j < dungeon.getHeight(); j++)
                if (masterMap[i][j] != '#') {
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

        for (Entity e : entities) {
            placeEntityOnMap(e,copy);
        }

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
}
