package com.jsaop.dungeonGame.dungeon;

import java.util.Objects;

public class Tunnel {

    public Room A;
    public Room B;

    public Tunnel(Room roomA, Room roomB) {
        A = roomA;
        B = roomB;
    }

    public boolean equals(Tunnel o) {
        if (this == o) return true;
        if (o == null) return false;
        return Objects.equals(A, o.A) &&
                Objects.equals(B, o.B) ||
                Objects.equals(A, o.B) &&
                        Objects.equals(B, o.A);
    }

}
