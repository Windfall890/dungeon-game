package com.jsaop.dungeonGame.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon {

    public static final int NUM_ROOMS = 9;

    private List<Room> rooms;
    private char[][] map;
    private int width;
    private int height;
    private Random random;

    public Dungeon(int width, int height, Random random) {
        this.height = height;
        this.width = width;
        this.random = random;
        this.map = initMap(width, height);
        this.rooms = new ArrayList<>();

        generate();


    }

    private static char[][] initMap(int width, int height) {
        char[][] map = new char[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                map[i][j] = '#';

        return map;
    }

    public String getMapAsString() {
        String s = "";

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                s += (map[j][i] + " ");
            }
            s += "\n";
        }

        return s;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void generate() {
        generateRooms(NUM_ROOMS);
        placeRooms();
        generateTunnels();
        System.out.println(getMapAsString());
    }

    private void generateRooms(int num) {
        for (int i = 0; i < num; i++) {
            int maxH = height / 3;
            int maxW = width / 3;
            rooms.add(Room.randomRoom(maxW, maxH, random));
        }
    }

    private void placeRooms() {

        int quarterH = (height - 1) / 3;
        int quarterW = (width - 1) / 3;

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                Room room = rooms.get(i * 3 + j);

                int x = quarterW * (j + 1) - room.getWidth();
                int y = quarterH * (i + 1) - room.getHeight();

                room.translate(x, y);
            }
        }

        for (Room r : rooms) {
            stampRoom(r);

        }
    }

    private void stampRoom(Room room) {

        System.out.println("width: " + room.getWidth()
                + " height: " + room.getHeight()
                + " centerX: " + room.getCenterX()
                + " centerY: " + room.getCenterY());

        int startX = room.getX();
        int endX = startX + room.getWidth();
        int startY = room.getY();
        int endY = startY + room.getHeight();

        for (int i = startY; i < endY; i++) {
            for (int j = startX; j < endX; j++) {
                map[j][i] = '.';
            }
        }

        map[room.getCenterX()][room.getCenterY()] = '+';
    }

    private void generateTunnels() {

        for (Room roomA : rooms) {
            for (Room roomB : rooms) {
                if (roomA != roomB) {
                    tunnel(roomA.getCenterX(), roomA.getCenterY(),
                            roomB.getCenterX(), roomB.getCenterY());
                }
            }

        }

    }

    private void tunnel(int x1, int y1, int x2, int y2) {

        int posX = x1;
        int posY = y1;
        while (posX != x2) {
            map[posX][posY] = '.';
            posX += (posX < x2) ? 1 : -1;
        }
        while (posY != y2) {
            map[posX][posY] = '.';
            posY += (posY < y2) ? 1 : -1;
        }
    }

    public char[][] getMapCopy() {

        char[][] copy = new char[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                copy[j][i] = map[j][i];
            }
        }

        return copy;
    }

    public char getTile(int x, int y) {
        return map[x][y];
    }

    public char[][] getMap() {
        return map;
    }
}
