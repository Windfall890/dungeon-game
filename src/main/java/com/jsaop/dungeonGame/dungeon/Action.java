package com.jsaop.dungeonGame.dungeon;

public enum Action {
    DOWN("DOWN"),
    UP("UP"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    WAIT("WAIT"),
    PING("PING");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isMoveAction() {
        return (this == DOWN) || (this == UP) || (this == LEFT) || (this == RIGHT);
    }
}
