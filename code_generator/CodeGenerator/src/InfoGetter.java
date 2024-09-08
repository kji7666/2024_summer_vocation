import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InfoGetter {
    public static void main(String[] args) {
        // 注意：路徑中的分隔符可以使用斜線（/）或雙反斜線（\\）
        infoSplit("src\\all_input.txt", "src/infoInput.txt");
    }

    public static String infoSplit(String fileName, String newFileName) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        int lineCount = 1;
        int instanceEndEditableCount = 0;
        String id = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            writer = new BufferedWriter(new FileWriter(newFileName));
            String line;
            

            while ((line = reader.readLine()) != null) {
                if (line.contains("<!-- InstanceEndEditable -->")) {
                    instanceEndEditableCount++;
                    if(instanceEndEditableCount >= 4) break;
                }
                if (lineCount >= 35) {
                    if(line.contains("品名")) {
                        id = line.substring(line.indexOf("<span >品名:</span>")+"<span >品名:</span>".length(), line.indexOf("</span></td>"));
                        String[] parts = id.split("-");
                        id = parts[0] + "-" + parts[1];
                        System.out.println(id);
                    }
                    writer.write(line);
                    writer.newLine(); // 使用 newLine() 來確保平台獨立的換行
                }
                lineCount++;
            }
        } catch (IOException e) {
            // 輸出錯誤資訊
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println("An error occurred while closing the files: " + e.getMessage());
            }
        }
        return id;
    }
}
