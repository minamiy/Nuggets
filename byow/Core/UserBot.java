package byow.Core;

import byow.lab12.Position;

import java.io.Serializable;
import byow.TileEngine.TETile;

public class UserBot implements Serializable {
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    private int botNuggets;
    private String name;
    private Position startPos;
    private Position currPos;
    private TETile botTile;

    public UserBot(Position givenStart, TETile givenTile) {
        startPos = givenStart;
        currPos = givenStart;
        name = "Josh";
        botNuggets = 0;
        botTile = givenTile;
    }

    /** Move userBot a given distance in a given direction, if valid. */
    public Position move(int direction, int distance) {
        Position newPos;
        if (direction == NORTH) {
            newPos = new Position(currPos, 0, distance);
        } else if (direction == SOUTH) {
            newPos = new Position(currPos, 0, -distance);
        } else if (direction == EAST) {
            newPos = new Position(currPos, distance, 0);
        } else {
            newPos = new Position(currPos, -distance, 0);
        }
        return newPos;
    }

    /** Found a nugget! */
    public void addNugget(int num) {
        botNuggets += num;
    }

    /** Change the current position of the bot. */
    public void setCurrPos(Position newPos) {
        currPos = newPos;
    }

    public Position getCurrPos() {
        return currPos;
    }

    public int getBotNuggets() {
        return botNuggets;
    }

    public TETile getBotTile() {
        return botTile;
    }
}
