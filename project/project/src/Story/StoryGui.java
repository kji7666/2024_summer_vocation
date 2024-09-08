package Story;

import java.awt.*;
import javax.swing.*;
import java.util.*;


public class StoryGui {
    public static void main(String[] args) {
        new StoryGui(1);
    }

    private JFrame frame;
    private CustomPanel panel; // 配置背景, 腳色用
    private final Process process;
    private static final Map<Integer, String> storyMap;
    private boolean isFinish = false;
    

    static {
        storyMap = new HashMap<>();
        storyMap.put(1, "src/lines/mainStory1.txt");
    }

    // 劇情, 路徑, 路徑
    public StoryGui(int storyNumber) {
        this.process = new Process(storyMap.get(storyNumber));
        initFrame();
        frame.setVisible(true);
        storyStart();
    }

    public boolean isFinish() {
        return isFinish;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public CustomPanel getPanel() {
        return this.panel;
    }

    private void initFrame() {
        frame = new JFrame();
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        /**
         * 不須初始化Image
         * panel.setBackgroundImage
         * panel.setCharacterImage
         * 可隨時調用更新
         */
        this.panel = new CustomPanel();
        panel.setLayout(new BorderLayout());
        frame.setContentPane(panel);
    }

    private void storyStart() {
        for(GameEvent gameEvent : process.getEventList()) {
            gameEvent.startEvent(this);
            int result = gameEvent.getResult();
        }
        finishGui();
    }

    private void finishGui() {
        isFinish = true;
        frame.dispose();
        //System.exit(0);
    }
}

