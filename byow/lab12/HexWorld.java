package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    public static void main(String[] args) {
        Tessellation tessellation = new Tessellation(3);
        int height = tessellation.getHeight() + 2;
        int width = tessellation.getWidth() + 2;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] world = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        List<Hexagon> hexagons = tessellation.getHexagons();
        for (Hexagon hexagon : hexagons) {
            addHexagon(hexagon, world);
        }
        ter.renderFrame(world, "", 0);
    }
    private static void addHexagon(Hexagon hexagon, TETile[][] world) {
        List<Position> positions = hexagon.getHexPositions();
        for (Position p : positions) {
            int pY = p.getY();
            int pX = p.getX();
            world[pX][pY] = hexagon.getTile();
        }
    }
}
