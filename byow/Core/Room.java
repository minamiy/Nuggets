package byow.Core;
import byow.lab12.Position;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room implements Serializable {
    private Position upperLeft;
    private int width;
    private int height;
    private int area;
    private List<Position> northPositions;
    private List<Position> southPositions;
    private List<Position> eastPositions;
    private List<Position> westPositions;
    private List<Integer> hallwayDirections;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public Room(Position uL, int w, int h) {
        upperLeft = uL;
        width = w;
        height = h;
        area = width * height;
        generateNorthPositions();
        generateSouthPositions();
        generateEastPositions();
        generateWestPositions();
        hallwayDirections = new ArrayList<>(
                Arrays.asList(NORTH, EAST, SOUTH, WEST));
    }

    /** Get all positions for a given room. */
    public List<Position> getPositions() {
        List<Position> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = upperLeft.getX() + x;
                int newY = upperLeft.getY() - y;
                positions.add(new Position(newX, newY));
            }
        }
        return positions;
    }

    public List<Integer> getHallwayDirections() {
        return hallwayDirections;
    }

    public Position getBottomLeft() {
        return new Position(upperLeft.getX(), upperLeft.getY() - (height - 1));
    }

    public Position getUpperRight() {
        return new Position(upperLeft.getX() + (width - 1), upperLeft.getY());
    }

    public Position getBottomRight() {
        return new Position(upperLeft.getX() + (width - 1),
                upperLeft.getY() - (height - 1));
    }

    /** For a given wall position, remove it and its adjacent ones if present. */
    public void removeWallPosition(int direction, Position wallPos) {
        List<Position> directionPositions;
        if (direction == NORTH) {
            directionPositions = northPositions;
        } else if (direction == EAST) {
            directionPositions = eastPositions;
        } else if (direction == WEST) {
            directionPositions = westPositions;
        } else {
            directionPositions = southPositions;
        }
        directionPositions.remove(wallPos);

        if (direction == NORTH || direction == SOUTH) {
            if (wallPos.getX() > upperLeft.getX()) {
                directionPositions.remove(wallPos.getLeft());
            }
            if (wallPos.getX() < getUpperRight().getX()) {
                directionPositions.remove(wallPos.getRight());
            }
        } else {
            if (wallPos.getY() > getBottomRight().getY()) {
                directionPositions.remove(wallPos.getBelow());
            }
            if (wallPos.getY() < getUpperRight().getY()) {
                directionPositions.remove(wallPos.getAbove());
            }
        }
    }

    public List<Position> getNorthPositions() {
        return northPositions;
    }

    public List<Position> getSouthPositions() {
        return southPositions;
    }

    public List<Position> getEastPositions() {
        return eastPositions;
    }

    public List<Position> getWestPositions() {
        return westPositions;
    }

    /** For a given position, get all valid wall positions.*/
    private void generateNorthPositions() {
        List<Position> north = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            north.add(new Position(upperLeft.getX() + i, upperLeft.getY() + 1));
        }
        northPositions = north;
    }

    private void generateSouthPositions() {
        List<Position> south = new ArrayList<>();
        Position bottomLeft = getBottomLeft();
        for (int i = 0; i < width; i++) {
            south.add(new Position(bottomLeft.getX() + i,
                    bottomLeft.getY() - 1));
        }
        southPositions = south;
    }

    private void generateEastPositions() {
        List<Position> east = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            east.add(new Position(getBottomRight().getX() + 1,
                    getBottomRight().getY() + i));
        }
        eastPositions = east;
    }

    private void generateWestPositions() {
        List<Position> west = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            west.add(new Position(getBottomLeft().getX() - 1,
                    getBottomRight().getY() + i));
        }
        westPositions = west;
    }

    public Position getUpperLeft() {
        return upperLeft;
    }

}
