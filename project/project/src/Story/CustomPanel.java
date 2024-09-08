package Story;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class CustomPanel extends JPanel {
    private Image backgroundImage;
    private Image characterImage;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 確保基本繪製行為

        // 繪製背景圖片
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Error: Background image is null when painting.");
        }

        // 繪製角色圖片
        if (characterImage != null) {
            int characterWidth = characterImage.getWidth(this);
            int characterHeight = characterImage.getHeight(this);
            int x = (getWidth() - characterWidth) / 2;
            int y = (getHeight() - characterHeight) / 2;
            g.drawImage(characterImage, x, y, this);
        } else {
            System.err.println("Error: Character image is null when painting.");
        }
    }

    // 設置背景圖片並更新面板
    public void setBackgroundImage(Image backgroundImage) {
        if(backgroundImage == null) {
            throw new IllegalArgumentException("不正確的圖片");
        }
        this.backgroundImage = backgroundImage;
        repaint(); // 更新面板以重新繪製
    }

    public void setCharacterImage(Image characterImage) {
        if(characterImage == null) {
            throw new IllegalArgumentException("不正確的圖片");
        }
        this.characterImage = characterImage;
        repaint(); // 更新面板以重新繪製
    }
}