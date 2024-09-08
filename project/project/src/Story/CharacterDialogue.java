package Story;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class CharacterDialogue implements GameEvent {
    private String name;
    private String dialogue;
    private JTextArea textArea; // 用於顯示文本的 JTextArea
    private volatile boolean eventEnded = false; // 標誌是否結束事件

    public CharacterDialogue(String name, String dialogue) {
        this.name = name;
        this.dialogue = dialogue;
    }

    @Override
    public void startEvent(StoryGui gui) {
        // 創建 JTextArea 來顯示文本
        textArea = new JTextArea();
        textArea.setText("[" + name + "]" + "\n" + dialogue);
        textArea.setEditable(false); // 設置為不可編輯
        textArea.setEnabled(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(true); // 行自動換行
        textArea.setWrapStyleWord(true); // 換行時保留整詞
        textArea.setBackground(new Color(0, 0, 0, 150)); // 背景透明
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 30)); // 使用 Microsoft YaHei 字體
        textArea.setBorder(BorderFactory.createEmptyBorder()); // 無邊框
        textArea.setBounds(0, gui.getFrame().getHeight()-300, gui.getFrame().getWidth(), 300);
        System.out.println("frame:" + gui.getFrame().getHeight() + "/textArea:" + textArea.getHeight());
        gui.getFrame().setLayout(null); // 確保使用 null 佈局
        gui.getFrame().add(textArea);
        gui.getFrame().revalidate();
        gui.getFrame().repaint();
        try{
            Thread.sleep(2000);
        } catch(Exception e){}

        // 添加事件處理來結束對話
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (CharacterDialogue.this) {
                    eventEnded = true;
                    CharacterDialogue.this.notify();
                }
            }
        };
        
        gui.getFrame().addMouseListener(mouseListener);
        // 等待事件完成
        waitForEventCompletion();
        // 移除 MouseListener
        gui.getFrame().removeMouseListener(mouseListener);
        endEvent(gui); // 確保在事件隊列中執行結束操作
    }

    private void waitForEventCompletion() {
        synchronized (this) {
            while (!eventEnded) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void endEvent(StoryGui gui) {
        if (textArea != null) {
            // 從主面板中移除 textPanel
            gui.getPanel().remove(textArea);
            gui.getPanel().revalidate();
            gui.getPanel().repaint();
        }
    }

    public int getResult() {
        return 0;
    }

}
