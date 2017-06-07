package com.jsaop.dungeonGame

import com.jsaop.dungeonGame.dungeon.Dungeon
import spock.lang.Specification


class SimpleDungeonSpec extends Specification {

    Dungeon dungeon
    static WIDTH = 50
    static HEIGHT = 40

    void setup() {
        dungeon = new Dungeon(WIDTH, HEIGHT)
    }

    def "map can be changed"() {
        dungeon.map[0][0] = '.'
        expect:
            dungeon.map[0][0] == '.'
    }

    def "dungeon can be retrieved as string"() {
        expect:
            dungeon.getMapAsString() instanceof String
    }

    def "dungeon can generate ... a dungeon"() {
        Dungeon dungeon = new Dungeon(50, 50)

        expect:
            dungeon.mapAsString.findAll('\\.').size() > 0

    }
}