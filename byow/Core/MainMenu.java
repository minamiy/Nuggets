package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

/* @source Code derived from CS61B Fall 2020 lab 13 code.*/
public class MainMenu {
    private int width;
    private int height;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        MainMenu menu = new MainMenu(80, 30);
        menu.startMenu();
    }

    public MainMenu(int width, int height) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 10, this.height * 10);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    /** Displays the start menu to the user. */
    public void startMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);

        StdDraw.text((double) this.width / 2, (double) this.height / 2 + 4, " ⊛ NUGGETS! ⊛");

        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);

        StdDraw.text((double) this.width / 2, (double) this.height / 2 - 2, "New Game (N)");
        StdDraw.text((double) this.width / 2, (double) this.height / 2 - 4, "Load Game (L)");
        StdDraw.text((double) this.width / 2, (double) this.height / 2 - 6, "Quit (:Q)");

        StdDraw.show();
    }
}
