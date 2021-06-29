package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(194, 165, 220), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(194, 165, 220), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    public static final TETile NUGGET = new TETile('⊛', Color.yellow, Color.black, "nugget");
    public static final TETile BAD_NUGGET = new TETile('⊛', Color.red, Color.black, "nugget");
    public static final TETile SNOWFLAKE = new TETile('❄', Color.white, Color.black, "snowflake");
    public static final TETile CROWN = new TETile('♛', Color.white, Color.black, "crown");
    public static final TETile STAR = new TETile('★', Color.white, Color.black, "star");
    public static final TETile HEART = new TETile('♥', Color.pink, Color.black, "heart");
    public static final TETile MOON = new TETile('☽', Color.white, Color.black, "moon");
    public static final TETile CLOUD = new TETile('☁', Color.gray, Color.black, "cloud");
    public static final TETile PAWN = new TETile('♟', Color.gray, Color.black, "pawn");
    public static final TETile SMILEY = new TETile('☻', Color.white, Color.black, "smiley");
    public static final TETile APPLE = new TETile('', Color.white, Color.black, "apple");
    public static final TETile DIAMOND = new TETile('◈', Color.white, Color.black, "diamond");
    public static final TETile PORTAL = new TETile(
            '☗', new Color(147, 243, 230), Color.black, "portal");
    public static final TETile DOOR = new TETile('#', Color.darkGray, Color.darkGray, "diamond");
}


