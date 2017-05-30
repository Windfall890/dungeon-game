package com.jsaop.dungeon;

public class Game {
    private PlayerCharacter player;
    private Goal goal;
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
        turn = 0;
        hasWon = false;
        placePlayer();
        placeGoalFarFromPlayer();
    }

    public void movePlayerOnce(String direction) {
        map[player.getX()][player.getY()] = dungeon.getTile(player.getX(), player.getY());
        switch (direction) {
            case "DOWN":
                if (getMap()[getPlayer().getX()][getPlayer().getY() + 1] != '#')
                    player.setY(getPlayer().getY() + 1);
                break;
            case "UP":
                if (getMap()[getPlayer().getX()][getPlayer().getY() - 1] != '#')
                    player.setY(getPlayer().getY() - 1);
                break;
            case "LEFT":
                if (getMap()[getPlayer().getX() - 1][getPlayer().getY()] != '#')
                    player.setX(getPlayer().getX() - 1);
                break;
            case "RIGHT":
                if (getMap()[getPlayer().getX() + 1][getPlayer().getY()] != '#')
                    player.setX(getPlayer().getX() + 1);
                break;
        }

        turn++;
        placePlayerOnMap(player);
        if (playerIsOnTreasure())
            hasWon = true;
    }

    private void placeGoalFarFromPlayer() {

        double maxDistance = 0;
        int maxX = 0, maxY = 0;
        double tempDistance = 0;
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
        map[goal.getX()][goal.getY()] = '*';

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
                    placePlayerOnMap(player);
                    return;
                }
    }

    private void placePlayerOnMap(PlayerCharacter player) {
        map[player.getX()][player.getY()] = player.getGlyph();
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
