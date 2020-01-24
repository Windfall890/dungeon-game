package com.jsaop.dungeonGame.dungeon;

import java.util.Random;

public class Room {
    private int width;
    private int height;
    private int x;
    private int y;

    public Room(int width, int height, int x, int y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    private static int calcCenter(int distance, int start) {
        return start + distance / 2;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCenterX() {
        return calcCenter(width, x);
    }

    public int getCenterY() {
        return calcCenter(height, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void translate(int x, int y) {
        this.x = x;
        this.y = y;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (width != room.width) return false;
        if (height != room.height) return false;
        if (x != room.x) return false;
        return y == room.y;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    public static Room randomRoom(int maxW, int maxH, Random random) {
        int height = random.nextInt(maxH - 5) + 5;
        int width = random.nextInt(maxW - 5) + 5;
        return new Room(width, height, 0, 0);
    }
}
