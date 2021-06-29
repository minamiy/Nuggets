package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.lab12.Position;
import edu.princeton.cs.introcs.StdDraw;
import byow.lab13.MemoryGame;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine implements Serializable {
    private TERenderer ter;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int TILE_SIZE = 16;
    public static final TETile FLOOR_TILE = Tileset.FLOOR;
    public static final TETile WALL_TILE = Tileset.WALL;
    public static final TETile NUGGET = Tileset.NUGGET;
    public static final TETile BAD_NUGGET = Tileset.BAD_NUGGET;
    public static final TETile AVATAR = Tileset.PAWN;
    public static final TETile PORTAL = Tileset.PORTAL;
    public static final TETile UNLOCKED = Tileset.UNLOCKED_DOOR;
    public static final TETile LOCKED = Tileset.LOCKED_DOOR;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static final Font BIG_FONT = new Font("Monaco", Font.BOLD, 32);
    public static final Font DEFAULT = new Font("Monaco", Font.BOLD, 14);
    private String hud = "Move your bot! Then move your mouse!";

    // Phase 2 Attributes
    private boolean gameOver;
    private long seed;
    private List<Character> validKeys;
    private Tessellation tessellation;
    private TETile[][] finalWorldFrame;
    private UserBot bot;
    private TETile botTile;
    private boolean display = true;

    public Engine() {
        ter = new TERenderer();
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        gameOver = false;
        validKeys = new ArrayList<>(Arrays.asList('w', 'a', 's', 'd', ':', 'm', 'n', 'l'));
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        MainMenu menu = new MainMenu(80, 30);
        menu.startMenu();

        while (!gameOver) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char typedKey = Character.toLowerCase(StdDraw.nextKeyTyped());

            if (!validKeys.contains(typedKey)) {
                System.out.println("Invalid key was entered.");
                continue;
            }

            // Start a new game
            if (typedKey == 'n') {
                botTile = chooseBot();
                seed = getInputSeed();
                tessellation = new Tessellation(seed, botTile);
                bot = tessellation.getUserBot();
                displayLegend();
                initializeTessellation();
                interactivePlay();
            // Load an existing game
            } else if (typedKey == 'l') {
                tessellation = loadTessellation();
                bot = tessellation.getUserBot();
                botTile = bot.getBotTile();
                initializeTessellation();
                interactivePlay();
            // Saves and quit
            } else if (typedKey == ':') {
                checkQuit();
                return;
            }
        }
    }

    /** Displays the bot options.*/
    public void drawBotMenu() {
        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 6, "Choose an Avatar Character!");

        Font fontSmall = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(fontSmall);

        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 2, "0. DEFAULT @");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "1. Crown ♛");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, "2. Snowflake ❄");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4, "3. Heart ♥");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 6, "4. Smiley ☻");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 8, "5. Star ★");
        StdDraw.show();
    }

    /** Allows the user to choose their own bot avatar. */
    public TETile chooseBot() {
        drawBotMenu();
        char botChoice = '8';
        while (botChoice == '8') {
            if (StdDraw.hasNextKeyTyped()) {
                botChoice = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (botChoice == '0') {
                    return Tileset.AVATAR;
                } else if (botChoice == '1') {
                    return Tileset.CROWN;
                } else if (botChoice == '2') {
                    return Tileset.SNOWFLAKE;
                } else if (botChoice == '3') {
                    return Tileset.HEART;
                } else if (botChoice == '4') {
                    return Tileset.SMILEY;
                } else if (botChoice == '5') {
                    return Tileset.STAR;
                } else {
                    System.out.println("Invalid choice.");
                }
                drawBotMenu();
            }
        }
        StdDraw.pause(1000);
        return Tileset.AVATAR;
    }

    /** Checks if the user has hit the quit command. */
    private boolean checkQuit() {
        char newTypedKey = '?';
        boolean foundNext = false;
        while (!foundNext) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            newTypedKey = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (newTypedKey == 'q') {
                gameOver = true;
                saveTessellation();
                foundNext = true;
                System.exit(0);
            }
        }
        return newTypedKey == 'q';
    }

    /** Allows the player to move one step in a certain direction. */
    private Position playerMove(Position botOldPos, Position botNewPos,
                                char typedKey, boolean interactive) {
        // Get a direction (WADS)
        // Check if the move is valid, needs to be a floor tile
        if (typedKey == 'w') {
            botNewPos = moveUserBot(NORTH, interactive);
        } else if (typedKey == 'a') {
            botNewPos = moveUserBot(WEST, interactive);
        } else if (typedKey == 'd') {
            botNewPos = moveUserBot(EAST, interactive);
        } else if (typedKey == 's') {
            botNewPos = moveUserBot(SOUTH, interactive);
        } else if (typedKey == 'm') {
            displayLegend();
        } else if (typedKey == ':') {
            checkQuit();
            return null;
        }

        if (botNewPos == null) {
            botNewPos = botOldPos;
        }
        // Switch old position back to floor tile
        // Change new position to avatar tile
        finalWorldFrame[botOldPos.getX()][botOldPos.getY()] = FLOOR_TILE;
        finalWorldFrame[botNewPos.getX()][botNewPos.getY()] = botTile;
        return botNewPos;
    }

    /** Allows the user to interact with userBot.*/
    private void interactivePlay() {
        boolean mouse = false;
        while (!gameOver) {
            if (!StdDraw.hasNextKeyTyped()) {
                if (mouse) {
                    hud();
                    ter.renderFrame(finalWorldFrame, hud, bot.getBotNuggets());
                }
                continue;
            }
            char typedKey = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (!validKeys.contains(typedKey)) {
                System.out.println("Invalid key was entered.");
                continue;
            }

            Position botOldPos = bot.getCurrPos();
            Position botNewPos = botOldPos; // no movement is default

            // Try to move, get new position
            mouse = true;
            botNewPos = playerMove(botOldPos, botNewPos, typedKey, true);
            if (botNewPos == null) {
                break;
            }

            hud(); // HUD capabilities
            checkGameOver(); // Win/Lose feature of game
            if (display) {
                ter.renderFrame(finalWorldFrame, hud, bot.getBotNuggets());
            }
        }
    }

    /** Plays the game from an input string. */
    private void play(String userInput, int currIndex) {
        gameOver = userInput.length() < 1 || userInput.equals(":q");
        while (!gameOver) {
            char curr = userInput.charAt(currIndex);
            if (!validKeys.contains(curr)) {
                System.out.println("Invalid key was entered.");
                continue;
            }

            if (curr == 'l') {
                tessellation = loadTessellation();
                bot = tessellation.getUserBot();
                botTile = bot.getBotTile();
                initializeTessellation();
            } else {
                Position botOldPos = bot.getCurrPos();
                Position botNewPos = botOldPos; // no movement is default

                // Try to move, get new position
                botNewPos = playerMove(botOldPos, botNewPos, curr, false);
                if (botNewPos == null) {
                    break;
                }
            }
            checkGameOver(); // Win/lose feature of games
            if (display) {
                ter.renderFrame(finalWorldFrame, hud, bot.getBotNuggets());
            }
            currIndex++;
            if (currIndex >= userInput.length() || userInput.substring(currIndex).equals(":q")) {
                gameOver = true;
                saveTessellation();
            }
        }
    }

    /** Redraws HUD. */
    private void hud() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x < WIDTH && y < HEIGHT && x >= 0 && y >= 0) {
            TETile tile = finalWorldFrame[x][y];
            if (tile.equals(WALL_TILE)) {
                hud = "Wall";
            } else if (tile.equals(FLOOR_TILE)) {
                hud = "Floor";
            } else if (tile.equals(AVATAR)) {
                hud = "Avatar";
            } else if (tile.equals(NUGGET)) {
                hud = "Nugget";
            } else if (tile.equals(BAD_NUGGET)) {
                hud = "Bad Nugget";
            } else if (tile.equals(PORTAL)) {
                hud = "Portal";
            } else if (tile.equals(UNLOCKED)) {
                hud = "Unlocked Door";
            } else if (tile.equals(LOCKED)) {
                hud = "Locked Door";
            } else if (tile.equals(botTile)) {
                hud = "User Bot";
            } else {
                hud = "Nothingness";
            }
        } else {
            hud = "Nothingness";
        }
    }

    /** Checks if the game has ended, i.e. player got enough nuggets. */
    private void checkGameOver() {
        List<Position> unlockedDoors = tessellation.getUnlockedDoors();
        if (bot.getBotNuggets() >= tessellation.getNumNuggets() + 9
                && tessellation.getNumDoors() >= 1) {
            gameOver = true;
            gameOverWinDisplay();
        } else if (bot.getBotNuggets() < -1) {
            gameOver = true;
            gameOverLoseDisplay();
        }
    }

    /** Called if the user chooses the legend button.*/
    private void displayLegend() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 10,
                "Welcome to our game: NUGGETS!");

        StdDraw.setFont(DEFAULT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 6,
                "The objective of the game is to collect nuggets. ⊛");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 4,
                "You will be able to customize your userBot if you like (default is @)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 2,
                "If you come across another avatar, ♟, "
                        + "you will be asked to answer a random trivia question.");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "If you get it right, you will be rewarded 3 nuggets!");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2,
                "You can go through the portals ☗ from the left and right ONLY.");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4,
                "You will be teleported to another random portal and end up to its right!");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 6,
                "Please press W to go up, A to go left, S to go down, and D to go right.");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 8,
                "You can also press M at any point to see this menu again and :Q to quit.");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 10,
                "To go through doors, █ you will be asked to play a memory game to unlock it ▢.");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 12,
                "Press E to exit this menu. Have fun and go bears! <3");
        StdDraw.show();

        char input = '8';
        while (input == '8') {
            if (StdDraw.hasNextKeyTyped()) {
                input = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (input == 'e') {
                    break;
                }
            }
        }
        StdDraw.pause(1000);
    }

    /** Called if the user wins the game!.*/
    private void gameOverWinDisplay() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(BIG_FONT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "CONGRATULATIONS! You have won the game!");
        StdDraw.setFont(DEFAULT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4, "Go bears <3");
        StdDraw.show();
        StdDraw.pause(3000);
        System.exit(0);
    }

    /** Called if the user loses the game. */
    private void gameOverLoseDisplay() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(BIG_FONT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "You have lost the game :(");
        StdDraw.setFont(DEFAULT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4, "Come again soon!");
        StdDraw.show();
        StdDraw.pause(3000);
        System.exit(0);
    }

    /** Checks for a valid position and moves the userBot appropriately. */
    private Position moveUserBot(int direction,  boolean interactive) {
        Position possibleBotPos = bot.move(direction, 1);
        List<Position> tessellationTiles = tessellation.getTiles();
        List<Position> nuggets = tessellation.getNuggetTiles();
        List<Position> badNuggets = tessellation.getBadNuggetTiles();
        List<Position> avatarTiles = tessellation.getAvatarTiles();
        List<Position> portals = tessellation.getPortalTiles();
        List<Position> doors = tessellation.getDoorTiles();
        List<Position> unlockedDoors = tessellation.getUnlockedDoors();

        // Is this a valid move?
        if (!tessellationTiles.contains(possibleBotPos)
                && !doors.contains(possibleBotPos)
                && !unlockedDoors.contains(possibleBotPos)) {
            System.out.println("Invalid move, try again.");
            return bot.getCurrPos();
        // If a nugget has been found
        } else if (nuggets.contains(possibleBotPos)) {
            bot.addNugget(1);
            tessellation.removeFromNuggetTiles(possibleBotPos);
        // If an avatar has been encountered
        } else if (avatarTiles.contains(possibleBotPos)) {
            if (interactive) {
                Avatar a = tessellation.getAvatar(possibleBotPos);
                tessellation.removeFromAvatarTiles(possibleBotPos);
                avatarEncouragement(a);
                avatarInteraction(a);
            }
        // If a portal is entered from the left or right
        } else if (portals.contains(possibleBotPos)
                && (direction == 1 || direction == 3)) {
            Position endPortal = tessellation.getRandEndPortal(possibleBotPos);
            bot.setCurrPos(endPortal);
            return endPortal;
        // If a portal is attempted to be entered from up/down
        } else if (portals.contains(possibleBotPos)) {
            return bot.getCurrPos();
        // Bad nuggets :(
        } else if (badNuggets.contains(possibleBotPos)) {
            bot.addNugget(-1);
            tessellation.removeFromBadNuggetTiles(possibleBotPos);
        // Doors!
        } else if (doors.contains(possibleBotPos)) {
            tessellation.unlockDoor(possibleBotPos);
            finalWorldFrame[possibleBotPos.getX()][possibleBotPos.getY()] = UNLOCKED;
            if (direction == 0) {
                possibleBotPos = possibleBotPos.getAbove();
            } else if (direction == 1) {
                possibleBotPos = possibleBotPos.getRight();
            } else if (direction == 2) {
                possibleBotPos = possibleBotPos.getBelow();
            } else if (direction == 3) {
                possibleBotPos = possibleBotPos.getLeft();
            }
            MemoryGame game = new MemoryGame(40, 40, seed);
            game.startGame();
            StdDraw.clear();
            StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.setFont(DEFAULT);
        } else if (unlockedDoors.contains(possibleBotPos)) {
            if (direction == 0) {
                possibleBotPos = possibleBotPos.getAbove();
            } else if (direction == 1) {
                possibleBotPos = possibleBotPos.getRight();
            } else if (direction == 2) {
                possibleBotPos = possibleBotPos.getBelow();
            } else if (direction == 3) {
                possibleBotPos = possibleBotPos.getLeft();
            }
        }

        bot.setCurrPos(possibleBotPos);
        return possibleBotPos;
    }

    /** If the bot has reached an avatar, switches interfaces for interaction! */
    private void avatarInteraction(Avatar a) {
        List<String> riddle = a.getRiddle();
        String question = riddle.get(0);
        String optionA = riddle.get(1);
        String optionB = riddle.get(2);
        String optionC = riddle.get(3);
        String optionD = riddle.get(4);
        char letterAnswer = riddle.get(5).charAt(0);
        String answer = riddle.get(6);
        StdDraw.clear(Color.BLACK);
        Font fontBig = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 8, "It's time for a riddle!");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 + 5, question);

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "A: " + optionA);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 2, "B: " + optionB);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4, "C: " + optionC);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 6, "D: " + optionD);
        StdDraw.show();

        char answerChoice = '8';
        while (answerChoice == '8') {
            if (StdDraw.hasNextKeyTyped()) {
                answerChoice = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (answerChoice == letterAnswer) {
                    correctInterface();
                } else {
                    wrongInterface(riddle.get(5), answer);
                }
            }
        }
        StdDraw.pause(1000);
    }

    /** Called if the user chooses the correct answer.*/
    private void correctInterface() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(BIG_FONT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "You got the correct answer!");
        StdDraw.setFont(DEFAULT);
        bot.addNugget(3);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4,
                "You have received 3 extra nuggets.");
        StdDraw.show();
        StdDraw.pause(1800);
    }

    /** Called if the user chooses the wrong answer.*/
    private void wrongInterface(String letterAnswer, String answer) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2,
                "Sorry, that is not the correct answer. :(");
        StdDraw.setFont(DEFAULT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 4,
                "The correct answer was "
                        + letterAnswer.toUpperCase() + ", " + answer + ".");
        StdDraw.show();
        StdDraw.pause(1800);
    }

    /** If the bot has reached an avatar, switches interfaces for encouragement! */
    private void avatarEncouragement(Avatar a) {
        String encouragement = a.getMessage();
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(BIG_FONT);
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, encouragement);
        StdDraw.show();
        StdDraw.pause(1500);
        StdDraw.setFont(DEFAULT);
    }

    /** Checks for file to load previous tessellation. */
    private Tessellation loadTessellation() {
        File f = new File("./byow/Core/tessellation.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (Tessellation) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("File could not be found.");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("IO Exception " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Class could not be found.");
                System.exit(0);
            }
        }
        return new Tessellation(getInputSeed());
    }

    /** Saves existing tessellation. */
    private void saveTessellation() {
        File f = new File("./byow/Core/tessellation.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(tessellation);
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("IO Exception.");
            System.exit(0);
        }
    }

    /** Allows user to choose a given input seed.*/
    private long getInputSeed() {
        drawFrame("Please input seed:");
        String seedString = "";
        char inputChar = 'a';
        while (inputChar != 's') {
            if (StdDraw.hasNextKeyTyped()) {
                inputChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.isDigit(inputChar)) {
                    seedString += String.valueOf(inputChar);
                    drawFrame(seedString);
                }
            }
        }
        seed = Long.valueOf(seedString);
        StdDraw.pause(1000);
        return seed;
    }

    /** Draws a frame with a given seed.*/
    private void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        // Extracts the seed
        if (input.charAt(0) == 'n') {
            String seedString = "";
            int index = 0;
            for (int i = 1; i < input.length(); i++) {
                char c = input.charAt(i);
                if (!Character.isDigit(c)) {
                    index = i + 1;
                    break;
                }
                seedString += Character.toString(c);
            }
            String cleanInput = input.substring(index);
            botTile = Tileset.AVATAR;
            seed = Long.valueOf(seedString);
            tessellation = new Tessellation(seed, botTile);
            bot = tessellation.getUserBot();
            initializeTessellation();

            if (cleanInput.length() > 0) {
                play(cleanInput, 0);
            }
        // Loads an existing game
        } else if (input.charAt(0) == 'l') {
            play(input, 0);
        }
        return finalWorldFrame;
    }

    /** Initializes and generates a tessellation. */
    private void initializeTessellation() {
        if (display) {
            ter.initialize(WIDTH, HEIGHT);
        }
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        addStructure(tessellation.getTiles(), finalWorldFrame, FLOOR_TILE);
        addStructure(tessellation.getWallTiles(), finalWorldFrame, WALL_TILE);
        addStructure(tessellation.getNuggetTiles(), finalWorldFrame, NUGGET);
        addStructure(tessellation.getBadNuggetTiles(), finalWorldFrame, BAD_NUGGET);
        addStructure(tessellation.getAvatarTiles(), finalWorldFrame, AVATAR);
        addStructure(tessellation.getPortalTiles(), finalWorldFrame, PORTAL);
        addStructure(tessellation.getDoorTiles(), finalWorldFrame, LOCKED);
        addStructure(tessellation.getUnlockedDoors(), finalWorldFrame, UNLOCKED);
        Position botPos = bot.getCurrPos();
        finalWorldFrame[botPos.getX()][botPos.getY()] = botTile;
        if (display) {
            ter.renderFrame(finalWorldFrame, hud, bot.getBotNuggets());
        }
    }

    /**Iterates through positions and adds to the world.*/
    private static void addStructure(List<Position> pos, TETile[][] world, TETile t) {
        for (Position p : pos) {
            int pY = p.getY();
            int pX = p.getX();
            world[pX][pY] = t;
        }
    }
}
