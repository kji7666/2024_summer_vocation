package Story;

import java.awt.*;

import Tool.ImageParser;


public class CharacterImageChange implements GameEvent {
    private String newImage;

    public CharacterImageChange(String newImage) {
        this.newImage = newImage;
    }

    @Override
    public void startEvent(StoryGui gui) {
        Image characterImage = ImageParser.loadImage(newImage);
        if (characterImage != null) {
            gui.getPanel().setCharacterImage(characterImage);
        }
    }
    @Override
    public void endEvent(StoryGui gui) {}
    @Override
    public int getResult() {
        return 0;
    }
}