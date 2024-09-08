package Homapage;

import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Character.Character;
import Story.StoryGui;
import Tool.ImageParser;


public class HomaPageGui {
    public static void main(String[] args) {
        new HomaPageGui(new Character("昊庭", 
        "src\\image\\girl2-removebg-preview.png", 
        Arrays.asList("臭甲", "黑人"), 
        "src\\lines\\people_lines.txt"));
    }
    
    private JFrame frame;
    private CustomPanel panel; // 配置背景, 腳色用
    private JButton storyButton;
    private JButton gameButton;
    private JButton foodButton;
    private JButton shopButton;
    private Character character;
    private JLabel favorabilityLabel;
    private JLabel moneyLabel;
    private JLabel nameLabel;
    private JTextArea attributeText;
    private ExecutorService pool = Executors.newFixedThreadPool(5);

    public HomaPageGui(Character character) {
        // thread pool
        
        this.character = character;
        initFrame();
        initImage();
        initButton();
        initLabel();
        initAttribute();
        frame.setVisible(true);
    }

    public Character getCharacter() {
        return character;
    }

    private void initFrame() {
        // init frame
        frame = new JFrame();
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null); // 使用null佈局來手動設置元件位置
    }

    private void initImage() {
        // init panel
        this.panel = new CustomPanel(this);
        panel.setLayout(null); // 使用null佈局來手動設置元件位置
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.setContentPane(panel);
        panel.setBackgroundImage(ImageParser.loadImage("src\\image\\lobby.png"));
        panel.setCharacterImage(character.getImage());
    }

    private void initButton() {
        // init button
        int buttonHeight = 40;
        int buttonWidth = 150;
        int y = frame.getHeight() - 50 - buttonHeight;
        Font buttonFont = new Font("Arial", Font.BOLD, 40); // 可以改成任何你喜歡的字體

        storyButton = new JButton("Story");
        storyButton.setFont(buttonFont);
        storyButton.setFocusPainted(false);
        storyButton.setBounds(30, y, buttonWidth, buttonHeight);
        panel.add(storyButton);

        gameButton = new JButton("Game");
        gameButton.setFont(buttonFont);
        gameButton.setFocusPainted(false);
        gameButton.setBounds(30 + frame.getWidth() / 4, y, buttonWidth, buttonHeight);
        panel.add(gameButton);

        foodButton = new JButton("Food");
        foodButton.setFont(buttonFont);
        foodButton.setFocusPainted(false);
        foodButton.setBounds(30 + frame.getWidth() / 4 * 2, y, buttonWidth, buttonHeight);
        panel.add(foodButton);

        shopButton = new JButton("Shop");
        shopButton.setFont(buttonFont);
        shopButton.setFocusPainted(false);
        shopButton.setBounds(30 + frame.getWidth() / 4 * 3, y, buttonWidth, buttonHeight);
        panel.add(shopButton);

        // 為每個按鈕設置 ActionListener
        storyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(character.getStoryCompleteNumber());
                pool.submit(()->new StoryGui(1)); // gui預設使用同線程, 另開避免堵塞
                character.recordStoryComplete();                
            }
        });

        gameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Game button clicked");
            }
        });

        foodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Food button clicked");
            }
        });

        shopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Shop button clicked");
            }
        });
        
    }

    private void initLabel() {
        int labelWidth = 300;
        int labelHeight = 50;
        Font font = new Font("Arial", Font.BOLD, 40); // 可以改成任何你喜歡的字體

        favorabilityLabel = new CustomLabel(String.valueOf(character.getFavorability()));
        favorabilityLabel.setFont(font);
        favorabilityLabel.setBounds(680, 50, labelWidth, labelHeight);

        moneyLabel = new CustomLabel(String.valueOf(character.getMoney()));
        moneyLabel.setFont(font);
        moneyLabel.setBounds(680, 50 + labelHeight + 30, labelWidth, labelHeight);

        nameLabel = new CustomLabel(character.getName());
        nameLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 50));
        nameLabel.setBounds(20, 20, 200, 100);

        frame.add(favorabilityLabel);
        frame.add(moneyLabel);
        frame.add(nameLabel);
    }

    private void initAttribute() {
        attributeText = new JTextArea("[屬性]\n");
        for(String attribute : character.getAttribute()){
            attributeText.append(attribute + "\n");
        }
        attributeText.setBounds(20, 100, 200, 400);
        attributeText.setFont(new Font("Microsoft YaHei", Font.BOLD, 25));
        attributeText.setEditable(false); // 設置為不可編輯
        attributeText.setEnabled(false);
        attributeText.setFocusable(false);
        attributeText.setLineWrap(true); // 行自動換行
        attributeText.setWrapStyleWord(true); // 換行時保留整詞
        attributeText.setBackground(new Color(0, 0, 0, 150)); // 背景透明
        attributeText.setForeground(Color.WHITE);
        attributeText.setBorder(BorderFactory.createEmptyBorder()); // 無邊框
        frame.add(attributeText);
    }
    
    private void repaint() {
        favorabilityLabel.setText(String.valueOf(character.getFavorability()));
        moneyLabel.setText(String.valueOf(character.getMoney()));
        nameLabel.setText(character.getName());
        StringBuilder sb = new StringBuilder("[屬性]\n");
        for(String attribute : character.getAttribute()){
            sb.append(attribute + "\n");
        }
        attributeText.setText(sb.toString());

        frame.repaint();
    }
}

class CustomPanel extends JPanel {
    private HomaPageGui gui;
    private Image backgroundImage;
    private Image characterImage;
    private Image heartImage;
    private Image moneyImage;

    public CustomPanel(HomaPageGui gui) {
        this.gui = gui;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (characterImage != null) {
                    int x = (getWidth() - characterImage.getWidth(CustomPanel.this)) / 2;
                    int y = (getHeight() - characterImage.getHeight(CustomPanel.this)) / 2;
                    Rectangle characterBounds = new Rectangle(x, y, characterImage.getWidth(CustomPanel.this), characterImage.getHeight(CustomPanel.this));
                    if (characterBounds.contains(e.getPoint())) {
                        showCharacterInfo();
                    }
                }
            }
        });
        heartImage = ImageParser.loadImage("src\\image\\heart-removebg-preview.png");
        moneyImage = ImageParser.loadImage("src\\image\\money-removebg-preview.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Error: Background image is null when painting.");
        }

        if (characterImage != null) {
            int characterWidth = characterImage.getWidth(this);
            int characterHeight = characterImage.getHeight(this);
            int x = (getWidth() - characterWidth) / 2;
            int y = (getHeight() - characterHeight) / 2;
            g.drawImage(characterImage, x, y, this);
        } else {
            System.err.println("Error: Character image is null when painting.");
        }

        if(heartImage != null) {
            g.drawImage(heartImage, 650, 30, this);
        }

        if(moneyImage != null) {
            g.drawImage(moneyImage, 650, 110, this);
        }
    }

    public void setBackgroundImage(Image backgroundImage) {
        if (backgroundImage == null) {
            throw new IllegalArgumentException("不正確的圖片");
        }
        this.backgroundImage = backgroundImage;
        repaint();
    }

    public void setCharacterImage(Image characterImage) {
        if (characterImage == null) {
            throw new IllegalArgumentException("不正確的圖片");
        }
        this.characterImage = characterImage;
        repaint();
    }

    private void showCharacterInfo() {
        JOptionPane.showMessageDialog(this, gui.getCharacter().getLine(), gui.getCharacter().getName() + "說", JOptionPane.OK_CANCEL_OPTION);
    }
}

class CustomLabel extends JLabel {
    public CustomLabel(String text) {
        super(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();
        int x = 1;
        int y = fm.getAscent() + 1;

        // 繪製黑色邊緣
        g2.setColor(Color.BLACK);
        g2.drawString(getText(), x - 1, y - 1);
        g2.drawString(getText(), x - 1, y + 1);
        g2.drawString(getText(), x + 1, y - 1);
        g2.drawString(getText(), x + 1, y + 1);

        // 繪製白色填充
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}