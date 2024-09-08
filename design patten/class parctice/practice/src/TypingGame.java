import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TypingGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingGameGui game = new TypingGameGui();
            game.setVisible(true);
        });
    }

    private String question;
    private LinkedList<String> wordsDao; // queue

    public TypingGame() throws Exception{
        try{
            wordsDao = new LinkedList<>();
            generateWordDao("src\\words.txt");
            generateQuestion();
        } catch (Exception e) {
            throw new Exception("遊戲未成功啟動", e);
        }
    }

    private void generateWordDao(String fileName) throws Exception{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                wordsDao.add(line.trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("資料未準備");
        }
    }

    public void generateQuestion() throws Exception{
        if(!wordsDao.isEmpty()){
            this.question = wordsDao.pop();
        } else {
            try{
                generateWordDao("src\\words.txt");
                this.question = wordsDao.pop();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("資料未準備");
            }
        }
    }

    public String getQuestion(){
        return this.question;
    }
    
    public TypingGameResult submitAns(String userStr) {
        if(userStr.equals(question)){
            return new TypingGameResult(true);
        } else {
            return new TypingGameResult(false);
        }
    }
}

class TypingGameResult {
    private boolean isCorrect;
    public TypingGameResult(boolean isCorrect){
        this.isCorrect = isCorrect;
    }

    public boolean isCorrect(){
        return this.isCorrect;
    }
}

class TypingGameGui extends JFrame {
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    private JLabel questionLabel;
    private JLabel errorLabel;

    private JTextField answerField;
    // private JButton restartButton;

    private TypingGame typingGame;
    private JTextArea errorTextArea;
    private JTextArea scoreTextArea;
    private JTextArea timeTextArea;

    private int score = 0;
    private int errorCount = 0;
    private int time = 100;

    public TypingGameGui() {
        setTitle("Typing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create components
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));

        answerField = new JTextField();
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));

        errorTextArea = new JTextArea("0");
        errorTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        errorTextArea.setEditable(false);
        errorTextArea.setBackground(getBackground());

        scoreTextArea = new JTextArea("0");
        scoreTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreTextArea.setEditable(false);
        scoreTextArea.setBackground(getBackground());

        timeTextArea = new JTextArea("120");
        timeTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        timeTextArea.setEditable(false);
        timeTextArea.setBackground(getBackground());

        errorLabel = new JLabel("");
        //restartButton = new JButton("重新開始");

        // Create panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("typing game", SwingConstants.LEFT), BorderLayout.WEST);

        JPanel statsPanel = new JPanel();
        statsPanel.add(new JLabel("錯誤："));
        statsPanel.add(errorTextArea);
        statsPanel.add(new JLabel("已答對："));
        statsPanel.add(scoreTextArea);
        statsPanel.add(new JLabel("剩餘："));
        statsPanel.add(timeTextArea);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(questionLabel, BorderLayout.CENTER);
        centerPanel.add(answerField, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(errorLabel);
        //bottomPanel.add(restartButton);

        // Add panels to frame
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Auto focus on the answer field
        SwingUtilities.invokeLater(() -> answerField.requestFocusInWindow());

        startGame();
    }

    private void startGame(){
        try{
            typingGame = new TypingGame();
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "遊戲啟動失敗", "error", JOptionPane.INFORMATION_MESSAGE);
        }

        questionLabel.setText(typingGame.getQuestion());
        answerField.setEditable(true);
        pool.submit(()->countdown());

        // Handle Enter key press in answer field
        answerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText().trim();
                // 在這裡處理使用者輸入的內容
                handleResult(typingGame.submitAns(answerField.getText()));
                answerField.setText(""); // 清空答案欄
            }
        });

        // // Handle Enter key press in answer field
        // restartButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         startGame();
        //     }
        // });
    }

    private void countdown(){
        while(time > 0){
            time-- ;
            timeTextArea.setText(Integer.toString(time));
            try {
                Thread.sleep(1000);
            } catch (Exception e){
                System.exit(0);
            }
        }
        endGame();
    }

    private void handleResult(TypingGameResult gameResult){
        if(gameResult.isCorrect()){
            errorLabel.setText("正確");
            scoreTextArea.setText(Integer.toString(++ score));
            try{
                typingGame.generateQuestion();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "資料讀取失敗", "error", JOptionPane.INFORMATION_MESSAGE);
            }
            questionLabel.setText(typingGame.getQuestion());
        } else {
            errorLabel.setText("錯誤");
            errorTextArea.setText(Integer.toString(++ errorCount));
        }
    }

    private void endGame() {
        answerField.setEditable(false);
        // 停止計時器等遊戲結束的清理動作
        pool.shutdown();

        // 彈出視窗顯示遊戲結果
        String message = "遊戲結束！\n";
        message += "總分： " + scoreTextArea.getText() + "\n";
        message += "錯誤次數： " + errorTextArea.getText() + "\n";

        JOptionPane.showMessageDialog(this, message, "遊戲結果", JOptionPane.INFORMATION_MESSAGE);
    }
}