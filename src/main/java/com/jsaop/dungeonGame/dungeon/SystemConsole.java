package com.jsaop.dungeonGame.dungeon;

public class SystemConsole implements Console {
    @Override
    public void write(String s) {
        System.out.println(s);
    }

    @Override
    public String readAndFlush() {
        return "";
    }
}
