package byow.lab12;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Tesselation {
    private static Map<Integer, Integer> COLUMN_SIZES = Map.of(
            0, 3,
            1, 4,
            2, 5,
            3, 4,
            4, 3
    );

    private int side; //hexagon side

    private int width; //tesselation dimensions
    private int height;

    private List<Hexagon> hexagons;

    private static Map<Integer, TETile> tiles = Map.of(
            0, Tileset.FLOWER,
            1, Tileset.WALL,
            2, Tileset.AVATAR,
            3, Tileset.FLOOR,
            4, Tileset.GRASS,
            5, Tileset.LOCKED_DOOR,
            6, Tileset.MOUNTAIN,
            7, Tileset.SAND,
            8, Tileset.TREE,
            9, Tileset.UNLOCKED_DOOR
    );

    public Tesselation(int side) {
        this.side = side;
        Hexagon dummy = new Hexagon(new Position(0, 0), this.side, null);
        this.width = dummy.getHexWidth() * 3 + this.side * 2;
        this.height = dummy.getHexHeight() * 5;

        Map<Integer, Position> startingPositions = new HashMap<>();

        int middle = this.width / 2;

        Hexagon dummy2 = new Hexagon(new Position(middle, this.height), this.side, null);
        startingPositions.put(2, dummy2.getUpperLeft());

        Position position1 = new Position(dummy2.getLowerLeft(), -this.side, -1);
        Hexagon dummy1 = new Hexagon(position1, this.side, null);
        startingPositions.put(1, dummy1.getUpperLeft());

        Position position3 = new Position(dummy2.getLowerRight(), 1, -1);
        Hexagon dummy3 = new Hexagon(position3, this.side, null);
        startingPositions.put(3, dummy3.getUpperLeft());

        Position position0 = new Position(dummy1.getLowerLeft(), -this.side, -1);
        Hexagon dummy0 = new Hexagon(position0, this.side, null);
        startingPositions.put(0, dummy0.getUpperLeft());

        Position position4 = new Position(dummy3.getLowerRight(), 1, -1);
        Hexagon dummy4 = new Hexagon(position4, this.side, null);
        startingPositions.put(4, dummy4.getUpperLeft());

        this.hexagons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.addColumn(startingPositions.get(i), COLUMN_SIZES.get(i), dummy.getHexHeight());
        }
    }

    private void addColumn(Position startingPosition, int num, int h) {
        for (int i = 0; i < num; i++) {
            int y = startingPosition.getY() - i * h;
            Position position = new Position(startingPosition.getX(), y);
            TETile tile = tiles.get(RandomUtils.uniform(new Random(), 10));
            this.hexagons.add(new Hexagon(position, this.side, tile));
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return height;
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }
}
