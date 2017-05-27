package com.jsaop.dungeon

import spock.lang.Specification

class GameSpec extends Specification {

    Game game

    void setup() {
        game = new Game()
    }

    def "Game takes a dungeon"() {
        expect:
            game.dungeon != null
    }

    def "Game creates a player "() {
        expect:
            game.player != null
    }


}
