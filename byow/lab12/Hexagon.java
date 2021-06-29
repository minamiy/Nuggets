package byow.lab12;
import java.util.List;
import java.util.ArrayList;
import byow.TileEngine.TETile;

public class Hexagon {
    private Position upperLeft;
    private int side;
    private TETile tile;
    private Position lowerLeft;
    private Position lowerRight;

    public Hexagon(Position uL, int s, TETile t) {
        upperLeft = uL;
        side = s;
        tile = t;
        lowerLeft = new Position(this.upperLeft, -(this.side - 1), -(this.side - 1));
        lowerRight = new Position(this.lowerLeft, this.getRowWidth(this.side) - 1, 0);
    }
    public List<Position> getHexPositions() {
        int height = this.getHexHeight();
        List<Position> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            int startRow = this.getRowStart(y);
            int width = this.getRowWidth(y);
            for (int x = 0; x < width; x++) {
                positions.add(new Position(startRow + x, this.upperLeft.getY() - y));
            }
        }
        return positions;
    }

    public int getHexHeight() {
        return this.side * 2;
    }

    public int getHexWidth() {
        return this.getRowWidth(this.side);
    }

    public int getRowWidth(int row) {
        if (row < this.side) {
            return this.side + row * 2;
        } else {
            return this.side + (this.side - 1) * 2 - (row % this.side) * 2;
        }
    }

    public int getRowStart(int row) {
        if (row < this.side) {
            return this.upperLeft.getX() - row;
        } else {
            return this.upperLeft.getX() - (this.side - 1) + (row % this.side);
        }
    }

    public TETile getTile() {
        return tile;
    }

    public Position getUpperLeft() {
        return upperLeft;
    }

    public Position getLowerLeft() {
        return lowerLeft;
    }

    public Position getLowerRight() {
        return lowerRight;
    }
}
