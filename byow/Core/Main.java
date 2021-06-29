package byow.Core;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[0]);
            System.out.println(engine.toString());
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
            //engine.interactWithInputString("lwsd");
            //engine.interactWithInputString("n8772076153521736045ddddssss:q");
            //engine.interactWithInputString("n7313251667695476404s");
            /*engine.interactWithInputString("n980000935s");
            engine.interactWithInputString("n5s");
            engine.interactWithInputString("n2s");
            engine.interactWithInputString("n8004217737854698935s");
            engine.interactWithInputString("n7341909481878015308s");
            engine.interactWithInputString("n373s");
            engine.interactWithInputString("n526s");
            engine.interactWithInputString("n62s");
            engine.interactWithInputString("n18s"); //disconnected
            System.out.println(engine.toString());
        }*/
        /*} else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();*/
        }
    }
}
