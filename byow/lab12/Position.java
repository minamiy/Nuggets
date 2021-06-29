package byow.lab12;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position ref, int xOffset, int yOffset) {
        this.x = ref.getX() + xOffset;
        this.y = ref.getY() + yOffset;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position getAbove() {
        return new Position(this, 0, 1);
    }

    public Position getBelow() {
        return new Position (this, 0, -1);
    }

    public Position getLeft() {
        return new Position(this, -1, 0);
    }

    public Position getRight() {
        return new Position(this, 1, 0);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Position other = (Position) o;
        return this.x == other.x && this.y == other.y;
    }
}
