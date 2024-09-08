package Tool;
import java.io.*;
import java.util.*;

public class LineParser {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        // 確保檔案存在
        if (!file.exists()) {
            System.out.println("檔案不存在: " + filePath);
            return null;
        }

        // 使用 BufferedReader 來讀取檔案內容
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 按行讀取檔案內容並輸出到控制台
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 檢查是否是空行，若不是則添加到列表中
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (IOException e) {
            // 捕獲並顯示讀取檔案時的錯誤
            System.out.println("讀取檔案時發生錯誤: " + e.getMessage());
            return null;
        }
    }
}
