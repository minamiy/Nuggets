package byow.Core;

import byow.TileEngine.Tileset;
import byow.lab12.Position;

import java.io.Serializable;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import byow.TileEngine.TETile;

public class Tessellation implements Serializable {
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static final int MAX_HEIGHT = 30;
    public static final int MAX_LENGTH = 80;
    public static final int MIN_ROOM_DIM = 3;
    public static final int MAX_ROOM_DIM = 8;
    public static final int MIN_HALLWAY_LENGTH = 1;
    public static final int MAX_HALLWAY_LENGTH = 8;
    public static final int MIN_HALLWAYS_PER_ROOM = 4;
    public static final int MAX_HALLWAYS_PER_ROOM = 6;

    private int totalRooms;
    private int currNumRooms;

    private Random x;

    private List<Position> tiles = new ArrayList<>();
    private List<Position> wallTiles = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Hallway> hallways = new ArrayList<>();

    // Phase 2 Variables
    private Room startRoom;
    private UserBot bot;
    private List<Position> nuggetTiles = new ArrayList<>();
    private List<Position> badNuggetTiles = new ArrayList<>();
    private int numNuggets = 25;
    private int numBadNuggets = 10;
    private int numAvatars = 10;
    private int numPortals = 8;
    private int numDoors = 4;
    private List<Position> avatarTiles = new ArrayList<>();
    private Map<Position, Avatar> avatars = new HashMap<>();
    private List<Avatar> justAvatars = new ArrayList<>();
    private List<Position> portalTiles = new ArrayList<>();
    private List<Position> sidePortalTiles = new ArrayList<>();
    private List<Position> doorTiles = new ArrayList<>();
    private List<Position> adjacentDoorTiles = new ArrayList<>();
    private List<Position> unlockedDoors = new ArrayList<>();

    public Tessellation(long n) {
        x = new Random(n);
        totalRooms = RandomUtils.uniform(x, 20, 30);
        currNumRooms = 0;
        generateStructure();
        addWalls();
        addNuggets();
        addBadNuggets();
        addAvatars();
        addPortals();
        addHorizDoors();
        addVertDoors();
        addUserBot(Tileset.AVATAR);
    }

    public Tessellation(long n, TETile botTile) {
        x = new Random(n);
        totalRooms = RandomUtils.uniform(x, 20, 30);
        currNumRooms = 0;
        generateStructure();
        addWalls();
        addUserBot(botTile);
        addNuggets();
        addBadNuggets();
        addAvatars();
        addPortals();
        addHorizDoors();
        addVertDoors();
    }

    /** Adds horizontal doors (user passes from left to right or vice versa). */
    private void addHorizDoors() {
        for (int i = 0; i < numDoors / 2; i++) {
            int index = RandomUtils.uniform(x, 0, wallTiles.size());
            Position pos = wallTiles.get(index);
            boolean tileCheck = tiles.contains(pos.getLeft()) && tiles.contains(pos.getRight());
            boolean wallCheck = wallTiles.contains(pos.getAbove())
                    && wallTiles.contains(pos.getBelow());
            while (doorTiles.contains(pos) || adjacentDoorTiles.contains(pos)
                    || (!tileCheck || !wallCheck)) {
                index = RandomUtils.uniform(x, 0, wallTiles.size());
                pos = wallTiles.get(index);
                tileCheck = tiles.contains(pos.getLeft()) && tiles.contains(pos.getRight());
                wallCheck = wallTiles.contains(pos.getAbove())
                        && wallTiles.contains(pos.getBelow());
            }
            doorTiles.add(pos);
            adjacentDoorTiles.add(pos.getAbove());
            adjacentDoorTiles.add(pos.getBelow());
        }
    }

    /** Adds vertical doors (user passes from top to bottom or vice versa). */
    private void addVertDoors() {
        for (int i = 0; i < numDoors / 2; i++) {
            int index = RandomUtils.uniform(x, 0, wallTiles.size());
            Position pos = wallTiles.get(index);
            boolean tileCheck = tiles.contains(pos.getAbove()) && tiles.contains(pos.getBelow());
            boolean wallCheck = wallTiles.contains(pos.getLeft())
                    && wallTiles.contains(pos.getRight());
            while (doorTiles.contains(pos) || adjacentDoorTiles.contains(pos)
                    || (!tileCheck || !wallCheck)) {
                index = RandomUtils.uniform(x, 0, wallTiles.size());
                pos = wallTiles.get(index);
                tileCheck = tiles.contains(pos.getAbove()) && tiles.contains(pos.getBelow());
                wallCheck = wallTiles.contains(pos.getLeft())
                        && wallTiles.contains(pos.getRight());
            }
            doorTiles.add(pos);
            adjacentDoorTiles.add(pos.getLeft());
            adjacentDoorTiles.add(pos.getRight());
        }
    }

    /** Add all of the portal and populate the portalTiles array. */
    private void addPortals() {
        for (int i = 0; i < numPortals; i++) {
            Position portalPos = addNewTile();
            Position nextToPortalPos = portalPos.getRight();
            while (!tiles.contains(nextToPortalPos)) {
                portalPos = addNewTile();
                nextToPortalPos = portalPos.getRight();
            }
            portalTiles.add(portalPos);
            sidePortalTiles.add(portalPos.getLeft());
            sidePortalTiles.add(portalPos.getRight());
        }
    }

    /** Randomly picks another hallway to move the bot to.*/
    public Position getRandEndPortal(Position startPortal) {
        int randIndex = RandomUtils.uniform(x, 0, portalTiles.size());
        Position endPortal = portalTiles.get(randIndex);
        while (endPortal.equals(startPortal)) {
            randIndex = RandomUtils.uniform(x, 0, portalTiles.size());
            endPortal = portalTiles.get(randIndex);
        }
        return endPortal.getRight();
    }

    /** Generates a new userBot (based on theme) and adds to start Room.*/
    private void addUserBot(TETile botTile) {
        Position startPos = startRoom.getUpperLeft();
        bot = new UserBot(startPos, botTile);
    }

    /** Add all of the nuggets and populate the nuggetTiles array. */
    private void addNuggets() {
        for (int i = 0; i < numNuggets; i++) {
            Position nugPos = addNewTile();
            nuggetTiles.add(nugPos);
        }
    }

    private void addBadNuggets() {
        for (int i = 0; i < numBadNuggets; i++) {
            Position nugPos = addNewTile();
            badNuggetTiles.add(nugPos);
        }
    }

    /** Adds a new tile of type Nugget, Avatar, or (Portal).*/
    public Position addNewTile() {
        int index = RandomUtils.uniform(x, 0, tiles.size());
        Position pos = tiles.get(index);
        while (pos.equals(bot.getCurrPos())
                || nuggetTiles.contains(pos)
                || avatarTiles.contains(pos)
                || portalTiles.contains(pos)
                || sidePortalTiles.contains(pos)
                || badNuggetTiles.contains(pos)) {
            index = RandomUtils.uniform(x, 0, tiles.size());
            pos = tiles.get(index);
        }
        return pos;
    }

    /** Add all of the avatars and populate the avatarTiles array. */
    private void addAvatars() {
        for (int i = 0; i < numAvatars; i++) {
            Position avatarPos = addNewTile();
            Avatar a = new Avatar(avatarPos, x);
            avatarTiles.add(avatarPos);
            avatars.put(avatarPos, a);
            justAvatars.add(a);
        }
    }

    /** Returns the avatar object at a specific position.*/
    public Avatar getAvatar(Position avatarPos) {
        for (int i = 0; i < numAvatars; i++) {
            Avatar a = justAvatars.get(i);
            Position pos = a.getPosition();
            if (pos.equals(avatarPos)) {
                return a;
            }
        }
        return null;
    }

    /** Generates the world from a random starting room. */
    private void generateStructure() {
        int startX = RandomUtils.uniform(x, 3, 10);
        int startY = RandomUtils.uniform(x, 20, 28);
        int startWidth = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);
        int startHeight = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);

        Position startPos = checkPosition(new Position(startX, startY));
        startRoom = new Room(startPos, startWidth, startHeight);
        currNumRooms++;
        rooms.add(startRoom);
        addPositions(startRoom.getPositions());
        int numHallwaysFromRoom = RandomUtils.uniform(
                x, MIN_HALLWAYS_PER_ROOM, MAX_HALLWAYS_PER_ROOM);
        for (int i = 0; i < numHallwaysFromRoom; i++) {
            createHallwayFromRoom(startRoom);
        }
    }

    /** Adds all given positions to tiles list. */
    private void addPositions(List<Position> positions) {
        for (Position p : positions) {
            tiles.add(p);
        }
    }

    /** Given a specific hallway edges, finds a shifted room start position. */
    private Position findRoomStart(List<Integer> points,
                                   int direction, Position hallwayEdge, int w, int h) {
        // If the hallway is growing NORTH (bottom to top)
        // we want to shift the x axis of the position to the LEFT (negative)
        // and then we want the UPPER LEFT position to have y offset of height
        int index = RandomUtils.uniform(x, 0, points.size());
        int shift = points.get(index);
        Position roomStart;
        if (direction == NORTH) {
            roomStart = new Position(hallwayEdge, -shift, h - 1);
            // OR if the hallway is growing SOUTH (top to bottom)
            // we want to shift the x axis of the position to the LEFT (negative)
        } else if (direction == SOUTH) {
            roomStart = new Position(hallwayEdge, -shift, 0);
            // If the hallway is growing WEST (right to left)
            // we want to shift the y axis of the position UP (positive)
            // and then we want the UPPER LEFT position to have x offset of neg width
        } else if (direction == WEST) {
            roomStart = new Position(hallwayEdge, -(w - 1), shift);
            // If the hallway is growing EAST (left to right)
            // we want to shift the y axis of the position UP (positive)
        } else {
            roomStart = new Position(hallwayEdge, 0, shift);
        }
        roomStart = checkPosition(roomStart);
        if (roomStart == null) {
            roomStart = hallwayEdge;
        }
        points.remove(index);
        return roomStart;
    }

    /** Checks if a room start position goes off edges of the world. */
    private boolean checkValidRoomStart(Position possibleStart, int w, int h) {
        return ((possibleStart.getX() + w + 1 >= MAX_LENGTH)
                || (possibleStart.getY() - h - 1 <= 0)
                || (possibleStart.getX() - w - 1 <= 0)
                || (possibleStart.getY() + h + 1 >= MAX_HEIGHT));
    }

    /** Creates a valid room of random width/height from a given hallway. */
    private void createRoomFromHallway(Hallway tempHallway) {
        if (currNumRooms >= totalRooms) {
            return;
        }
        // Pseudo-randomly calculated room width and height
        int roomWidth = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);
        int roomHeight = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);
        Position hallwayEdge = tempHallway.getHallwayEdge();

        Room temp = new Room(hallwayEdge, roomHeight, roomWidth);
        int count = 0;
        while (checkValidRoomStart(hallwayEdge, roomWidth, roomHeight)) {
            roomWidth = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);
            roomHeight = RandomUtils.uniform(x, MIN_ROOM_DIM, MAX_ROOM_DIM);
            temp = new Room(hallwayEdge, roomHeight, roomWidth);
            count++;
            if (count == 10) {
                roomWidth = MIN_ROOM_DIM;
                roomHeight = MIN_ROOM_DIM;
                temp = new Room(hallwayEdge, MIN_ROOM_DIM, MAX_ROOM_DIM);
                break;
            }
        }

        List<Integer> startingPoints = new ArrayList<>();
        if (tempHallway.getDirection() == EAST || tempHallway.getDirection() == WEST) {
            for (int i = 0; i < roomHeight; i++) {
                startingPoints.add(i);
            }
        } else if (tempHallway.getDirection() == NORTH
                || tempHallway.getDirection() == SOUTH) {
            for (int i = 0; i < roomWidth; i++) {
                startingPoints.add(i);
            }
        }

        Position pos = findRoomStart(startingPoints, tempHallway.getDirection(),
                hallwayEdge, roomWidth, roomHeight);


        while (checkValidRoomStart(pos, roomWidth, roomHeight)
                || isOverlapping(temp.getPositions())) {
            if (startingPoints.size() == 0) {
                createHallwayFromHallway(tempHallway);
                return;
            }
            pos = findRoomStart(startingPoints, tempHallway.getDirection(),
                    hallwayEdge, roomWidth, roomHeight);
            temp = new Room(pos, roomHeight, roomWidth);
        }

        Room tempRoom = new Room(pos, roomWidth, roomHeight);
        List<Position> roomPositions = tempRoom.getPositions();
        addPositions(roomPositions);
        rooms.add(tempRoom);
        currNumRooms++;

        // Generate and create a random number of hallways from this room
        int numHallwaysFromRoom = RandomUtils.uniform(
                x, MIN_HALLWAYS_PER_ROOM, MAX_HALLWAYS_PER_ROOM);
        for (int i = 0; i < numHallwaysFromRoom; i++) {
            createHallwayFromRoom(tempRoom);
        }
    }

    /** Create a turning hallway from another hallway.*/
    public void createHallwayFromHallway(Hallway original) {
        int d = original.getDirection();
        List<Integer> newDirections = new ArrayList<>();
        if (d == NORTH || d == SOUTH) {
            newDirections.add(EAST);
            newDirections.add(WEST);
        } else {
            newDirections.add(NORTH);
            newDirections.add(SOUTH);
        }
        int randIndex = RandomUtils.uniform(x, 0, 2); //either 0 or 1
        // FIRST POSSIBLE DIRECTION
        int possibleDir = newDirections.get(randIndex);
        newDirections.remove(randIndex);
        int length = RandomUtils.uniform(x, 2, 4);

        Position startTurning = original.getHallwayEdge();
        // First direction min overlap
        if (!checkValidHallwayStart(startTurning, 2, possibleDir)) {
            possibleDir = newDirections.get(0); //switch direction
            // Both overlap, so give up
            if (!checkValidHallwayStart(startTurning, 2, possibleDir)) {
                return;
            } else { // Second direction has a chance
                int firstCount = 0;
                while (!checkValidHallwayStart(startTurning, length, possibleDir)) {
                    length = RandomUtils.uniform(x, 2, 4);
                    firstCount++;
                    if (firstCount == 5) {
                        length = 2;
                        // If you tried hard enough, go with min and break
                        break;
                    }
                }
                makeHallway(length, possibleDir, startTurning);
            }
        } else {
            int elseCount = 0;
            while (!checkValidHallwayStart(startTurning, length, possibleDir)) {
                length = RandomUtils.uniform(x, 2, 4);
                elseCount++;
                if (elseCount == 6) {
                    length = 2;
                    break;
                }
            }
            makeHallway(length, possibleDir, startTurning);
        }
    }

    /** Creates this new hallway. */
    private void makeHallway(int len, int dir, Position start) {
        Hallway newHallway = new Hallway(len, dir, start);
        List<Position> hallwayPositions = newHallway.getHallwayPositions();
        hallways.add(newHallway);
        addPositions(hallwayPositions);
    }

    /** Check if position goes out of bounds. */
    private Position checkPosition(Position oldPos) {
        int pX = oldPos.getX();
        int pY = oldPos.getY();
        if ((pX >= 0 && pX < MAX_LENGTH && pY >= 0 && pY < MAX_HEIGHT)) {
            return new Position(pX, pY);
        }
        return null;
    }

    /** Given a direction, find wall positions of a given room. */
    private List<Position> getWallPositionsFromDirection(
            int direction, Room tempRoom) {
        List<Position> roomWallPositions;
        if (direction == NORTH) {
            roomWallPositions = tempRoom.getNorthPositions();
        } else if (direction == EAST) {
            roomWallPositions = tempRoom.getEastPositions();
        } else if (direction == SOUTH) {
            roomWallPositions = tempRoom.getSouthPositions();
        } else {
            roomWallPositions = tempRoom.getWestPositions();
        }
        return roomWallPositions;
    }

    /** Checks if a hallway start position goes off edges of the world. */
    private boolean checkValidHallwayStart(Position wallPos, int len, int d) {
        int wpX = wallPos.getX();
        int wpY = wallPos.getY();
        return (!(d == EAST && wpX + len + 1 >= MAX_LENGTH)
                && !(d == SOUTH && wpY - len - 1 <= 0)
                && !(d == NORTH && wpY + len + 1 >= MAX_HEIGHT)
                && !(d == WEST && wpX - len - 1 <= 0));
    }

    /** Given a hallway length and a given room, find a valid hallway start position. */
    private List getValidHallwayStart(int len, Room tempRoom) {
        // Get the list of remaining directions for hallways from this room
        List<Integer> directions = tempRoom.getHallwayDirections();

        Position pos = null;
        boolean foundDirection = false;

        int finalDirection = -1;
        while (directions.size() > 0 && !foundDirection) {
            // Randomly select an index and get that direction
            int randDirectionIndex = RandomUtils.uniform(x, 0, directions.size());
            int d = directions.get(randDirectionIndex);

            // Get the remaining positions in that direction
            List<Position> roomWallPositions =
                    getWallPositionsFromDirection(d, tempRoom);
            // Iterating through room wall positions
            for (int i = 0; i < roomWallPositions.size(); i++) {
                Position wallPos = roomWallPositions.get(i);
                Hallway temp = new Hallway(len, d, wallPos);
                // If it is a valid hallway start, keep and break
                if (checkValidHallwayStart(wallPos, len, d)
                        && !isOverlapping(temp.getHallwayPositions())) {
                    pos = checkPosition(wallPos);
                    foundDirection = true;
                    finalDirection = d;
                    directions.remove(randDirectionIndex);
                    break;
                }
            }
            if (!foundDirection) {
                directions.remove(randDirectionIndex);
            }
        }
        if (pos == null) {
            return null;
        }
        tempRoom.removeWallPosition(finalDirection, pos);
        List toReturn = List.of(finalDirection, pos);
        return toReturn;
    }

    /** Creates a valid hallway of random length from a given room. */
    private void createHallwayFromRoom(Room tempRoom) {
        int hallwayLength = RandomUtils.uniform(x,
                MIN_HALLWAY_LENGTH, MAX_HALLWAY_LENGTH);
        List returned = getValidHallwayStart(hallwayLength, tempRoom);
        if (returned == null) {
            return;
        }
        Position startPos = (Position) returned.get(1);
        int direction = (Integer) returned.get(0);
        Hallway tempHallway = new Hallway(hallwayLength, direction, startPos);

        List<Position> hallwayPositions = tempHallway.getHallwayPositions();
        hallways.add(tempHallway);
        addPositions(hallwayPositions);
        createRoomFromHallway(tempHallway);
    }

    /** Iterates through all tiles and add walls to sides + corners. */
    private void addWalls() {
        for (Position tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            List<Position> possibleWalls = new ArrayList<>();
            if (tileX > 0) {
                possibleWalls.add(tile.getLeft());
            }
            if (tileX < MAX_LENGTH) {
                possibleWalls.add(tile.getRight());
            }
            if (tileY > 0) {
                possibleWalls.add(tile.getBelow());
            }
            if (tileY < MAX_HEIGHT) {
                possibleWalls.add(tile.getAbove());
            }
            if (tileX > 0 && tileY > 0) {
                possibleWalls.add(new Position(tile, -1, -1));
            }
            if (tileX > 0 && tileY < MAX_HEIGHT) {
                possibleWalls.add(new Position(tile, -1, 1));
            }
            if (tileX < MAX_LENGTH && tileY > 0) {
                possibleWalls.add(new Position(tile, 1, -1));
            }
            if (tileX < MAX_LENGTH && tileY < MAX_HEIGHT) {
                possibleWalls.add(new Position(tile, 1, 1));
            }
            for (Position possibleWall : possibleWalls) {
                if (validWallPosition(possibleWall)) {
                    wallTiles.add(possibleWall);
                }
            }
        }
    }

    /** Checks if a possible wall position is already a tile or wall tile.*/
    private boolean validWallPosition(Position wallTile) {
        return !wallTiles.contains(wallTile) && !tiles.contains(wallTile);
    }

    /** Checks if any positions list of positions is already a tile.*/
    private boolean isOverlapping(List<Position> positions) {
        for (Position p : positions) {
            if (tiles.contains(p)) {
                return true;
            }
        }
        return false;
    }

    /** Unlocks a given door position and removes it from doorTiles. */
    public void unlockDoor(Position pos) {
        unlockedDoors.add(pos);
        doorTiles.remove(pos);
    }

    public void removeFromNuggetTiles(Position nugPos) {
        nuggetTiles.remove(nugPos);
    }

    public void removeFromBadNuggetTiles(Position nugPos) {
        badNuggetTiles.remove(nugPos);
    }

    public void removeFromAvatarTiles(Position nugPos) {
        avatarTiles.remove(nugPos);
        avatars.remove(nugPos);
        justAvatars.remove(getAvatar(nugPos));
    }

    public List<Position> getTiles() {
        return tiles;
    }

    public List<Position> getWallTiles() {
        return wallTiles;
    }

    public List<Position> getNuggetTiles() {
        return nuggetTiles;
    }

    public List<Position> getBadNuggetTiles() {
        return badNuggetTiles;
    }

    public List<Position> getAvatarTiles() {
        return avatarTiles;
    }

    public List<Position> getPortalTiles() {
        return portalTiles;
    }

    public List<Position> getDoorTiles() {
        return doorTiles;
    }

    public List<Position> getUnlockedDoors() {
        return unlockedDoors;
    }

    public int getNumNuggets() {
        return numNuggets;
    }

    public int getNumDoors() {
        return numDoors;
    }

    public UserBot getUserBot() {
        return bot;
    }
}
