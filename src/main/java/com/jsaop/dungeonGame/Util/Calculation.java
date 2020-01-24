package com.jsaop.dungeonGame.Util;

public class Calculation {
    public static double SquareDistance(int x1, int y1, int x2, int y2) {
        return (double) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
