package com.jsaop.dungeonGame.dungeon;

import java.util.Random;

class NotVeryRandom extends Random {
    @Override
    public int next(int bound) {
        return 5;
    }
}
