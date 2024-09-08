package Character;
import java.awt.*;
import Tool.ImageParser;
import Tool.LineParser;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Character {
    private String name;
    private Image image;
    private int favorability;
    private int money;
    private int storyCompleteNumber;
    private List<String> attributes;
    private List<String> lines;

    public Character(String name, String image, List<String> attributes, String linesFile) {
        this.name = name;
        this.image = ImageParser.loadImage(image);
        this.favorability = 0;
        this.money = 0;
        this.storyCompleteNumber = 0;
        this.attributes = attributes;
        this.lines = LineParser.readFile(linesFile);
    }

    public int getStoryCompleteNumber() {
        return storyCompleteNumber;
    }

    public void recordStoryComplete() {
        storyCompleteNumber ++;
    }

    public List<String> getAttribute() {
        return attributes;
    }

    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    public String getLine() {
        Random random = new Random();
        int randomNumber = random.nextInt(lines.size()-1);
        return lines.get(randomNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void changeImage(String image) {
        this.image = ImageParser.loadImage(image);
    }

    public int getFavorability() {
        return favorability;
    }

    public void setFavorability(int favorability) {
        this.favorability = favorability;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
