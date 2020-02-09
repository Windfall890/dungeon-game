package com.jsaop.dungeonGame.dungeon;

import com.jsaop.dungeonGame.entity.Entity;
import com.jsaop.dungeonGame.entity.Player;
import com.jsaop.dungeonGame.gui.ISoundManager;

import java.util.List;

import static com.jsaop.dungeonGame.dungeon.Action.WAIT;

public class Game {
    private static final int WIN_LEVEL = 15;
    public static final int RADAR_PING_DURATION = 5;

    private int radarPings;
    private int radarUses = 1;
    private int width;
    private int height;
    private int currentLevel;
    private Console console;
    private ISoundManager soundManager;
    private Level level;


    public Game(int width, int height, int startingLevel, Console console, ISoundManager soundManager) {
        this.width = width;
        this.height = height;
        currentLevel = startingLevel;
        this.console = console;
        this.soundManager = soundManager;
        level = new Level(width, height, startingLevel, console, soundManager);
    }

    public int getCurrentDepth() {
        return currentLevel;
    }

    public void changeLevel(int newLevel) {
        this.currentLevel = newLevel;
        radarUses = 1;
        radarPings = 0;
        console.write(" --- Depth: " + currentLevel + " ---");

        level = new Level(width, height, currentLevel, console, soundManager);

        soundManager.levelTransition();

        console.write("You feel refreshed and have " + level.getPlayer().getHp() + " health");

        int numberEnemies = level.getNumberEnemies();
        if (numberEnemies == 1)
            console.write("There is " + numberEnemies + " enemy");
        else
            console.write("There are " + numberEnemies + " enemies");
    }

    public void takeTurn(Action action) {

        if (radarPings > 0) {
            radarPings--;
            if (radarPings <= 0) {
                console.write("Your radar hums and shuts off");
            }
        }
        switch (action) {

            case DOWN:
            case UP:
            case LEFT:
            case RIGHT:
            case WAIT:
                level.takeTurn(action);
                break;
            case PING:
                if (radarUses > 0) {
                    radarUses--;
                    radarPings = RADAR_PING_DURATION;
                    soundManager.radarPing();
                    console.write("You ping your surroundings. " + radarPings + " turns remaining.");
                }
                if (radarUses <= 0) {
                    console.write("Your Radar goes dim as its power fades.");
                }
                level.takeTurn(WAIT);
                break;
        }

        if (level.hasWon()) {
            soundManager.levelTransition();
            changeLevel(++currentLevel);
        }
    }

    public int getTurn() {
        return level.getTurn();
    }

    public Player getPlayer() {
        return level.getPlayer();
    }

    public int getPings() {
        return radarUses;
    }

    public int getPingTicks() {
        return radarPings;
    }

    public boolean hasWon() {
        return currentLevel > WIN_LEVEL;
    }

    public Level getLevel() {
        return level;
    }

    public List<Entity> getEntities() {
        return level.getEntities();
    }

    public void nextLevel() {
        changeLevel(getCurrentDepth() + 1);
    }

    public void previousLevel() {
        if (currentLevel >= 1)
            changeLevel(--currentLevel);
    }

    public void reset() {
        currentLevel = 0;
        level = new Level(width, height, 0, console, soundManager);

    }
}
