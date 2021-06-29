package byow.Core;

import java.io.Serializable;
import byow.lab12.Position;
import java.util.Random;
import java.util.List;

public class Avatar implements Serializable {
    Position pos;
    String message;
    Riddle riddleObject;
    List<String> riddle;
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
        "You got this!", "You're a star!", "Go Bears!",
        "Too easy for you!", "Wow, so impressive!"};

    public Avatar(Position givenPos, Random x) {
        pos = givenPos;
        int rand = RandomUtils.uniform(x, 0, ENCOURAGEMENT.length);
        message = ENCOURAGEMENT[rand];
        riddleObject = new Riddle(x);
        riddle = riddleObject.getRandomRiddle();
    }

    public String getMessage() {
        return message;
    }

    public Position getPosition() {
        return pos;
    }

    public List<String> getRiddle() {
        return riddle;
    }
}
