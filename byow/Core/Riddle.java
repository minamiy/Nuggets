package byow.Core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Riddle implements Serializable {
    private List<List<String>> riddleRepository = new ArrayList<>();
    private Random x;
    public Riddle(Random givenX) {
        x = givenX;
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the largest country in the world?",
                "Russia", "Canada", "China", "United States", "a", "Russia")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the oldest building on Cal's campus?",
                "Dwinelle Hall", "South Hall", "Hearst Mining", "Campanile", "b", "South Hall")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "How tall is the Campanile?",
                "405 Feet", "502 Feet", "204 Feet", "307 Feet", "d", "307 Feet")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the name of Cal student's favorite hiking destination?",
                "Big C", "Big Hike", "Bear Hike", "Sather Hike", "a", "Big C")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the name is the official Cal Mascot?",
                "Carol", "Oski", "Harold", "George", "b", "Oski")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "Which of the following is not one of the dorms at UC Berkeley?",
                "Unit 1", "Unit 4", "Clark Kerr", "Foothill", "b", "Unit 4")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the object that is passed between winners of the Big Game?",
                "The Axe", "The Trophy", "The Sword", "The Tree Stump", "a", "The Axe")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "Who does the Cal Football team play against in the big game?",
                "UCLA", "UC Irvine", "Stanford", "Harvard", "c", "Stanford")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "How many official colleges are there at UC Berkeley?",
                "7", "4", "6", "5", "d", "5")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "How many undergraduate students are there currently at UC Berkeley?",
                "31,780", "33,610", "37,220", "31,500", "a", "31,780")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What were the names of the robots who delivered food around UC Berkeley campus?",
                "FoodBots", "CalBots", "BerkBots", "KiwiBots", "d", "Kiwibots")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What famous coffee chain started in Berkeley?",
                "Starbucks", "Peet's Coffee", "Tully's",
                "Dunkin' Donuts", "b", "Peet's Coffee")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "Which movie features Berkeley's Sather Gate?",
                "Ant Man and the Wasp", "Monster's Inc.", "Avengers",
                "Legally Blonde", "a", "Ant Man and the Wasp")));
        riddleRepository.add(new ArrayList<>(Arrays.asList("Who is the best CS professor at Cal?",
                "Josh Hug", "John DeNero", "Dan Garcia", "Satish Rao", "a", "Josh Hug")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "To accommodate huge numbers of students, CS lecture are hosted at...",
                "Soda", "Haas Pavilion", "Memorial Stadium",
                "Zellerbach Hall", "d", "Zellerbach Hall")));
        riddleRepository.add(new ArrayList<>(Arrays.asList("Berkeley time is",
                "5 minutes late", "10 minutes late", "5 minutes early",
                "an hour late", "b", "10 minutes late")));
        riddleRepository.add(new ArrayList<>(Arrays.asList("Sather gate was supposedly built to",
                "guide angels onto campus", "prevent vampires from entering campus",
                "provide an insta-grammable spot on campus",
                "attract tourists", "b", "prevent vampires from entering campus")));
        riddleRepository.add(new ArrayList<>(Arrays.asList(
                "What is the name of the San Francisco fog?",
                "Josh", "John", "Arjun",
                "Karl", "d", "Karl")));
        riddleRepository.add(new ArrayList<>(Arrays.asList("Where is Soda Hall?",
                "Next to Wheeler", "Northside", "Inside Haas",
                "At the Coca Cola Headquarters", "b", "Northside")));

    }

    public List<String> getRandomRiddle() {
        int index = RandomUtils.uniform(x, 0, riddleRepository.size());
        List<String> r = riddleRepository.get(index);
        riddleRepository.remove(r);
        return r;
    }
}
