package Tool;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import javax.imageio.ImageIO;

public class ImageParser {
    public static Image loadImage(String ImagePath) {
        try {
            if(ImagePath == null) return null;
            Image image = ImageIO.read(new File(ImagePath));
            if (image == null) {
                System.err.println("Error: Character image is null.");
            }
            return image;
        } catch (IOException e) {
            System.err.println("Error loading character image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
