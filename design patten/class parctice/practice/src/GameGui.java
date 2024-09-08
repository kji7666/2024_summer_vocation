import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/* 1.
 * GameResult, range算是一種設計的風格
 * 類似你去看醫生, 他給你一份報告, 但這報告一定有格式唯讀, 不能讓病人自己可以改(no setter)
 * 
 * 2. 
 * 程式都缺少了檢查
 * 
 * 3.
 * 一層包一層的程式, range拋出Exception後, game的catch要怎麼處理
 * 看是什麼錯 例如輸入abc,就警告即可 輸入範圍之外也是
 * 可從range不斷傳到gui
 */
public class GameGui {
    private JFrame frame;
    private JLabel rangeLabel;
    private JTextField inputField;
    private JButton guessButton;
    private JButton revealButton;
    private JLabel resultLabel;

    private GuessNumberGame guessNumberGame;

    public GameGui(int start, int end) {
        if(start > end) throw new IllegalArgumentException("range error");
        try{
            this.guessNumberGame = new GuessNumberGame(start, end);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        createGUI(start, end);
    }
    
    private void createGUI(int start, int end) {
        frame = new JFrame("猜數字");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JLabel promptLabel = new JLabel("請在以下區間猜出正確數值");
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(promptLabel);

        rangeLabel = new JLabel("[" + start + ", " + end + "]");
        rangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(rangeLabel);

        inputField = new JTextField();
        panel.add(inputField);

        guessButton = new JButton("猜看看");
        guessButton.addActionListener(new GuessButtonListener());
        panel.add(guessButton);

        revealButton = new JButton("看答案");
        revealButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "答案是: " + guessNumberGame.getAns()));
        panel.add(revealButton);

        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int userGuess = Integer.parseInt(inputField.getText());
                GameResult gameResult = guessNumberGame.guess(userGuess);
                handleResult(gameResult);
            } catch (NumberFormatException numEx) {
                resultLabel.setText("請輸入有效的數字");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage().substring(ex.getMessage().indexOf(":")));
            }
        }
    }

    private void handleResult(GameResult gameResult){
        if(gameResult.isCorrect()){
            JOptionPane.showMessageDialog(frame, "Correct Answer");
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong Answer");
            rangeLabel.setText("[" + gameResult.getRange().getStart() + ", " + gameResult.getRange().getEnd() + "]");
        }
    }
}

class GuessNumberGame {
    private int ans;
    private int start;
    private int end;

    public GuessNumberGame(int start, int end) {
        if(start > end) throw new IllegalArgumentException("range error");
        this.start = start;
        this.end = end;
        generateAns(start, end);
    }

    private void generateAns(int start, int end){
        Random random = new Random();
        this.ans = random.nextInt(end - start + 1) + start;
    }

    public GameResult guess(int userGuess){
        try{
            if (userGuess == ans) {
                return new GameResult(null, true);
            } else {
                if (userGuess < ans) {
                    start = ((userGuess + 1 > start) && (userGuess + 1 < end) ? userGuess + 1 : start);
                } else {
                    end = ((userGuess - 1 < end) && (userGuess - 1 > start) ? userGuess - 1 : end);
                }
                return new GameResult(new Range(start, end), false);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage().substring(e.getMessage().indexOf(":")));
        }
    }

    public int getAns(){
        return this.ans;
    }
}

class GameResult {
    private Range range;
    private boolean isCorrect;

    public GameResult (Range range, boolean isCorrect){
        this.range = range;
        this.isCorrect = isCorrect;
    }

    public Range getRange() {
        return range;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}

class Range {
    private int start;
    private int end;

    public Range (int start, int end){
        if(start > end) throw new IllegalArgumentException("range error");
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}

class Unit {
    public static void main(String[] args){
        GameGui gameGui = new GameGui(1, 99);
        //SwingUtilities.invokeLater(() -> new GuessNumberGame(1, 99));
    }
}