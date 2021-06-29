package byow.Core;

import byow.lab12.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hallway implements Serializable {
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    private int length;
    private int direction;
    private Position hallwayStart;

    public Hallway(int l, int d, Position tL) {
        length = l;
        direction = d;
        hallwayStart = tL;
    }

    /** Get all positions for a given hallway. */
    public List<Position> getHallwayPositions() {
        List<Position> positions = new ArrayList<>();
        for (int loc = 0; loc < length; loc++) {
            Position p = findHallwayPos(loc);
            positions.add(p);
        }
        return positions;
    }

    /** Finds the position of the hallway shifted  by a given int loc.*/
    private Position findHallwayPos(int loc) {
        Position p;
        if (direction == SOUTH) {
            p = new Position(hallwayStart.getX(), hallwayStart.getY() - loc);
        } else if (direction == EAST) {
            p = new Position(hallwayStart.getX() + loc, hallwayStart.getY());
        } else if (direction == WEST) {
            p = new Position(hallwayStart.getX() - loc, hallwayStart.getY());
        } else {
            p = new Position(hallwayStart.getX(), hallwayStart.getY() + loc);
        }
        return p;
    }

    public int getDirection() {
        return direction;
    }

    /** Returns the position directly at the end of a hallway. */
    public Position getHallwayEdge() {
        return findHallwayPos(length);
    }
}
