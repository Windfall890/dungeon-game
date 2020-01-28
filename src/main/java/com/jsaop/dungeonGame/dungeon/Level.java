package com.jsaop.dungeonGame.dungeon;

import com.jsaop.dungeonGame.Util.Calculation;
import com.jsaop.dungeonGame.entity.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.*;


public class Level {
    private Player player;
    private Goal goal;
    private final List<Entity> npcs;
    private Dungeon dungeon;
    private char[][] masterMap;
    private boolean[][] explored;
    private int turn;
    private boolean hasWon;
    private EntityManager ems;
    private int level;

    public Level() {
        this(100, 100, 1, System.out);
    }

    public Level(int width, int height, int level, PrintStream out) {
        this.level = level;

        Random random = new Random();
        dungeon = new Dungeon(width, height, random);
        masterMap = dungeon.getMap();
        explored = new boolean[width][height];

        ems = new EntityManager(random, out, masterMap, level);
        player = ems.Player();
        goal = ems.Goal();
        npcs = ems.GetNpcs();

        turn = 0;
        hasWon = false;

        //IN ORDER!
        pickPlayerStartLocation();
        pickGoalLocationFarFromPlayer();
        npcs.forEach(this::pickNpcStartLocationBetter);

        updateExplored(); // starting view
    }



    public void takeTurn(Action action) {
        turn++;

        player.execute(action);

        updateExplored();

        if (playerIsOnTreasure())
            hasWon = true;

        npcs.forEach(Entity::takeTurn);
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
                    tempDistance = Calculation.SquareDistance(player.getX(), player.getY(), i, j);
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

    private void pickNpcStartLocationBetter(Entity e) {
        Room playerRoom = dungeon.GetRoomNearest(player.getX(), player.getY());
        Room goalRoom = dungeon.GetRoomNearest(goal.getX(), goal.getY());


        List<Integer> enemyRoomIds = ems.GetNpcs().stream()
                .map(en -> dungeon.GetRoomNearest(en.getX(), en.getY()).id)
                .collect(toList());

        Room emptyRoom = dungeon.GetRooms().stream()
                .filter(room -> room.id != playerRoom.id && room.id != goalRoom.id)
                .filter(room -> !enemyRoomIds.contains(room.id)).findFirst().get();


        e.setX(emptyRoom.getCenterX());
        e.setY(emptyRoom.getCenterY());
        System.out.println("enemy - X:" + e.getX() + " Y:" + e.getY());

    }

    private boolean playerIsOnTreasure() {
        return (player.getX() == goal.getX() && player.getY() == goal.getY());
    }

    private void pickPlayerStartLocation() {
        for (int i = 0; i < dungeon.getWidth(); i++)
            for (int j = 0; j < dungeon.getHeight(); j++)
                if (masterMap[i][j] != BlockValues.WALL.getValue()) {
                    player.translate(i, j);
                    System.out.println("Player - X:" + player.getX() + " Y:" + player.getY());
                    return;
                }
    }

    public Player getPlayer() {
        return player;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public char[][] getMap() {

        return dungeon.getMapCopy();
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
        return ems.GetEntities();
    }

    public int getNumberEnemies() {
        return ems.GetNpcs().size();
    }
}
