package com.jsaop.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private PlayerCharacter player;
    private Goal goal;
    private Enemy enemy;
    private List<Entity> entities;
    private Dungeon dungeon;
    private char[][] map;
    private int turn;
    private boolean hasWon;

    public Game() {
        this(100, 100);
    }

    public Game(int width, int height) {
        dungeon = new Dungeon(width, height);
        map = dungeon.getMapCopy();
        player = new PlayerCharacter();
        goal = new Goal();
        enemy = new Enemy();

        entities = new ArrayList<>();

        entities.add(player);
        entities.add(goal);
        entities.add(enemy);

        turn = 0;
        hasWon = false;
        placePlayer();
        placeGoalFarFromPlayer();
        placeEnemy();
    }

    public void takeTurn(Directions direction) {
        turn++;

        moveEntity(direction, player);

        if (playerIsOnTreasure())
            hasWon = true;

        takeEnemyTurn();



    }

    private void takeEnemyTurn() {



    }


    private void moveEntity(Directions direction, Entity entity) {
        map[entity.getX()][entity.getY()] = dungeon.getTile(entity.getX(), entity.getY());
        switch (direction) {
            case DOWN:
                if (getMap()[entity.getX()][entity.getY() + 1] != '#')
                    entity.setY(entity.getY() + 1);
                break;
            case UP:
                if (getMap()[entity.getX()][entity.getY() - 1] != '#')
                    entity.setY(entity.getY() - 1);
                break;
            case LEFT:
                if (getMap()[entity.getX() - 1][entity.getY()] != '#')
                    entity.setX(entity.getX() - 1);
                break;
            case RIGHT:
                if (getMap()[entity.getX() + 1][entity.getY()] != '#')
                    entity.setX(entity.getX() + 1);
                break;
        }
        placeEntityOnMap(player);
    }

    private void placeGoalFarFromPlayer() {

        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double tempDistance;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (map[i][j] != '#') {
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
        map[goal.getX()][goal.getY()] = goal.getGlyph();

    }

    private void placeEnemy() {
        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double distanceEnemyToGoal;
        double distanceEnemyToPlayer;
        for (int i = 0; i < dungeon.getWidth(); i++) {
            for (int j = 0; j < dungeon.getHeight(); j++) {
                if (map[i][j] != '#') {
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
        map[enemy.getX()][enemy.getY()] = enemy.getGlyph();

    }

    private double calcSquareDistance(int x1, int y1, int x2, int y2) {
        return (double) ((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    private boolean playerIsOnTreasure() {
        return (player.getX() == goal.getX() && player.getY() == goal.getY());
    }

    private void placePlayer() {
        for (int i = 0; i < dungeon.getWidth(); i++)
            for (int j = 0; j < dungeon.getHeight(); j++)
                if (map[i][j] != '#') {
                    player.translate(i, j);
                    placeEntityOnMap(player);
                    return;
                }
    }

    private void placeEntityOnMap(Entity entity) {
        map[entity.getX()][entity.getY()] = entity.getGlyph();
    }

    public PlayerCharacter getPlayer() {
        return player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public char[][] getMap() {
        return map;
    }

    public int getTurn() {
        return turn;
    }

    public boolean hasWon() {
        return hasWon;
    }
}
