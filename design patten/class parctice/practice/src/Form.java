import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

/**
 * @param problems 讀取指定txt內的問題集出題
 * @param dao 收集答案
 * @param formResult 暫存本次回答
 */
public class Form {
    private List<String> problems;
    private DataDao dao;
    private FormResult formResult;
    private int problemIndex;

    // 準備表單
    public Form() throws IOException {
        this.problems = FileOperator.fileReader("src\students.txt");
        dao = DataDao.getInstance();
    }

    // 開啟表單
    public void start() {
        problemIndex = -1;
        formResult = new FormResult(problems.size());
    }

    // 輸出下一個問題
    public String getProblem() {
        return problems.get(++ problemIndex);
    }

    // 提交是非問題回答, 記錄在result中
    public void submitAns(boolean isTrue) {
        formResult.setAns(problemIndex, isTrue);
    }

    // result的ans存入dao的bitmap中
    public void recordAns(String userName) {
        boolean[] ansSet = formResult.getAns();
        for(int i=0; i<problems.size(); i++){
            dao.recordAns(problems.get(i), userName, ansSet[i]);
        }
    }
}

class FileOperator {
    public static List<String> fileReader(String fileName) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
            reader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("資料讀取失敗");
        }
    }
}

class FormResult {
    private int problemCount;
    private boolean[] ans;

    public FormResult(int problemCount) {
        this.problemCount = problemCount;
        this.ans = new boolean[problemCount-1];
    }
    public void setAns(int problem, boolean ans) {
        if(problem < 0 || problem > problemCount-1) {
            throw new IllegalArgumentException("超出題目數目");
        }
        this.ans[problem] = ans;
    }
    public int getProblemCount() {
        return problemCount;
    }
    public boolean[] getAns() {
        return ans;
    }
}

class DataDao {
    private Map<String, Integer> bitIndexMap; // name get bitIndex
    private Map<String, Bitmap> ansMap; // desription get ans
    private int respondentCount;
    private static DataDao instance;

    public static DataDao getInstance(){
        if(instance == null){
            instance = new DataDao();
        } 
        return instance;
    }

    private DataDao(){
        bitIndexMap = new HashMap<>();
        ansMap = new HashMap<>();
        respondentCount = 0;
    }

    public void addRespondent(String name) {
        bitIndexMap.put(name, respondentCount ++);
    }

    public void recordAns(String desription, String name, boolean ans) {
        if(!bitIndexMap.containsKey(name)) {
            addRespondent(name);
            if(!ansMap.containsKey(desription)) {
                ansMap.put(desription, new Bitmap(desription, respondentCount));
            }
        }
        Bitmap bitmap = ansMap.get(desription);
        bitmap.expansion(respondentCount);
        bitmap.setData(bitIndexMap.get(name), ans);
    }
}

class Bitmap {
    private String description; // whether people fit this description
    private ArrayList<Long> map;
    private int size; // people count

    public Bitmap(String description, int size) {
        this.description = description;
        this.size = size;
        int mapSize = getMapIndex(size - 1) + 1;
        this.map = new ArrayList<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            map.add(0L); // 初始化所有元素為0
        }
    }

    public String getDescription() {
        return description;
    }

    private int getMapIndex(int bitIndex) {
        return bitIndex >> 6;
    }

    public boolean getData(int bitIndex) {
        if (bitIndex < 0 || bitIndex > size - 1) {
            throw new IndexOutOfBoundsException("超過bitmap有效範圍");
        }
        int mapIndex = getMapIndex(bitIndex);
        return (map.get(mapIndex) & (1L << bitIndex)) != 0; // 1L == 1 long
    }

    public void setData(int bitIndex, boolean isTrue) {
        if (bitIndex < 0 || bitIndex > size - 1) {
            throw new IndexOutOfBoundsException("超過bitmap有效範圍");
        } 
        if (isTrue){
            int mapIndex = getMapIndex(bitIndex);
            map.set(mapIndex, map.get(mapIndex) | (1L << bitIndex));
        }
    }

    public void expansion(int newSize) {
        if (newSize > size) {
            int newMapSize = getMapIndex(newSize - 1) + 1;
            for (int i = map.size(); i < newMapSize; i++) {
                map.add(0L);
            }
            size = newSize;
        }
    }
}

class FormGui {

    public static void createAndShowGUI() {
        // 建立 JFrame
        JFrame frame = new JFrame("性別選擇");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // 建立主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel);

        // 建立內容面板，放置單選按鈕
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 建立標籤
        JLabel label = new JLabel("性別");
        panel.add(label);

        // 建立單選按鈕
        JRadioButton femaleButton = new JRadioButton("女");
        JRadioButton maleButton = new JRadioButton("男");
        JRadioButton otherButton = new JRadioButton("其他");
        JRadioButton addOptionButton = new JRadioButton("新增選項");

        // 將單選按鈕添加到按鈕組
        ButtonGroup group = new ButtonGroup();
        group.add(femaleButton);
        group.add(maleButton);
        group.add(otherButton);
        group.add(addOptionButton);

        // 將單選按鈕添加到面板
        panel.add(femaleButton);
        panel.add(maleButton);
        panel.add(otherButton);
        panel.add(addOptionButton);

        // 將內容面板添加到主面板的中央
        mainPanel.add(panel, BorderLayout.CENTER);

        // 建立按鈕面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // 建立確定按鈕
        JButton confirmButton = new JButton("確定");
        buttonPanel.add(confirmButton);

        // 將按鈕面板添加到主面板的底部
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 顯示窗口
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 啟動 GUI 程序
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
  

    }
}