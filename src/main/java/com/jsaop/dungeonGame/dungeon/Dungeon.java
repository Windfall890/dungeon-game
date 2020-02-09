package com.jsaop.dungeonGame.dungeon;

import com.jsaop.dungeonGame.Util.Calculation;

import java.util.*;

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


        char[][] c = getMapCopy();
        rooms.forEach(room -> {
            //draw label of room ID on map
            String roomId = "" + room.id;
            int xOffset = 0;
            for (char c1 : roomId.toCharArray()) {
                c[room.getCenterX() + xOffset][room.getCenterY()] = c1;
            }
        });

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                s += (c[j][i] + " ");
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

    private void generate() {
        generateRooms(NUM_ROOMS);
        placeRooms();
        generateTunnels();
        System.out.println(getMapAsString());
    }

    private void generateRooms(int num) {
        for (int i = 0; i < num; i++) {
            int maxH = height / 3;
            int maxW = width / 3;
            addRoom(Room.randomRoom(maxW, maxH, random));
        }
    }

    private void addRoom(Room room) {
        room.id = rooms.size();
        rooms.add(room);
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

        //sometimes spawn 8 rooms
        if(random.nextDouble() < 0.3){
            rooms.remove(random.nextInt(rooms.size()));
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

//        map[room.getCenterX()][room.getCenterY()] = '+';
    }

    private void generateTunnels() {

        List<Tunnel> tunnels = new ArrayList<>();
        for (Room roomA : rooms) {
            Set<Room> proposedRooms = new HashSet<>();
            while (proposedRooms.size() < 2) {
                Room proposed = rooms.get(random.nextInt(rooms.size()));
                if (roomA != proposed) {
                    proposedRooms.add(proposed);
                }
            }
            for (Room roomB : proposedRooms) {
                Tunnel link = new Tunnel(roomA, roomB);
                if (tunnels.stream().noneMatch(t -> t.equals(link))) {
                    tunnel(roomA.getCenterX() + random.nextInt(roomA.getWidth()) - roomA.getWidth() / 2,
                            roomA.getCenterY() + random.nextInt(roomA.getHeight()) - roomA.getHeight() / 2,
                            roomB.getCenterX() + random.nextInt(roomB.getWidth()) - roomB.getWidth() / 2,
                            roomB.getCenterY() + random.nextInt(roomB.getHeight()) - roomB.getHeight() / 2);

                    tunnels.add(link);
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

    public char[][] getMap() {
        return map;
    }

    public Room getRoom(int id) {
        return rooms.stream()
                .filter(room -> room.id == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public Room GetRoomNearest(int x, int y) {
        return rooms.stream()
                .min(Comparator.comparingDouble(room -> Calculation.SquareDistance(room.getX(), room.getY(), x, y)))
                .orElseThrow(RuntimeException::new);
    }


    public List<Room> GetRooms() {
        return rooms;
    }
}
