package Story;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class PlayerDialogue implements GameEvent, ActionListener {
    String[] chooseDialogue; // 給玩家的選擇
    List<JButton> chooseButtons;
    private int choose;

    public PlayerDialogue(String[] chooseDialogue) {
        this.chooseDialogue = chooseDialogue;
        this.chooseButtons = new ArrayList<>();
        choose = -1; // 設置初始值為 -1
        for (String dialogue : chooseDialogue) {
            JButton button = new JButton(dialogue);
            button.setFont(new Font("Microsoft YaHei", Font.BOLD, 35));
            button.setFocusable(false);
            button.addActionListener(this);
            chooseButtons.add(button);
        }
    }

    public String[] getChooseDialogue() {
        return chooseDialogue;
    }

    @Override
    public void startEvent(StoryGui gui) {
        JFrame frame = gui.getFrame();
        int height = frame.getHeight() - 400;
        int buttonWidth = 500;
        int buttonHeight = 50;

        frame.setLayout(null); // 使用絕對佈局

        for (int i = 0; i < chooseButtons.size(); i++) {
            JButton button = chooseButtons.get(i);
            int x = (frame.getWidth() - buttonWidth) / 2;
            button.setBounds(x, 200 + i * height / chooseButtons.size(), buttonWidth, buttonHeight);
            frame.add(button);
        }

        frame.revalidate(); // 重新驗證 frame
        frame.repaint(); // 重新繪製 frame
        waitForEvent();
        endEvent(gui);
    }

    private void waitForEvent() {
        // 等待按鈕選擇的替代方案
        synchronized (this) {
            while (choose == -1) {
                try {
                    wait(); // 等待按鈕選擇
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void endEvent(StoryGui gui) {
        JFrame frame = gui.getFrame();
        for (JButton button : chooseButtons) {
            frame.remove(button);
        }
        frame.revalidate(); // 重新驗證 frame
        frame.repaint(); // 重新繪製 frame
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 設置選擇的按鈕索引
        for (int i = 0; i < chooseButtons.size(); i++) {
            JButton button = chooseButtons.get(i);
            if (e.getSource() == button) {
                choose = i;
                break; // 退出循環，一旦找到匹配的按鈕
            }
        }

        // 禁用所有按鈕
        for (JButton button : chooseButtons) {
            button.setEnabled(false);
        }

        synchronized (this) {
            notify(); // 通知等待的線程
        }
    }

    public int getResult() {
        return choose;
    }
}
