package Story;

import java.awt.*;

import Tool.ImageParser;

public class BackgroundImageChange implements GameEvent {
    private String newImage;

    public BackgroundImageChange(String newImage) {
        this.newImage = newImage;
    }

    @Override
    public void startEvent(StoryGui gui) {
        Image backgroundImage;
        backgroundImage = ImageParser.loadImage(newImage);
        if (backgroundImage != null) {
            gui.getPanel().setBackgroundImage(backgroundImage);
        }
    }
    @Override
    public void endEvent(StoryGui gui) {}
    @Override
    public int getResult() {
        return 0;
    }
}
