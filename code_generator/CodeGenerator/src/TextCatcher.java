import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TextCatcher {
    private List<String> photo = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> type = new ArrayList<>();
    private List<String> coverTexture = new ArrayList<>();
    private List<String> coverSize = new ArrayList<>();
    private List<String> storeSize = new ArrayList<>();

    public static void main(String[] args) {
        TextCatcher tc = new TextCatcher();
        tc.read("src\\menu\\2.txt", "src\\result.txt");
    }

    public void read(String inputPath, String outputPath) {
        int state = 0; // stay = 0 / type = 1 / texture = 2 / coversize = 3 / storesize = 4
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(inputPath));
            // 使用附加模式（true）打开文件
            writer = new FileWriter(outputPath);
            writer.write("");
            writer.close();
            writer = new FileWriter(outputPath, true); // true 代表附加模式
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if(line.contains("<td><strong>")) { // title
                    line = line.substring(line.indexOf("<td><strong>") + "<td><strong>".length(), line.indexOf("</strong></td>"));
                    System.out.println(line);
                    title.add(line);
                    state = 0;
                    // writer.append(line).append(System.lineSeparator()); // 确保行与行之间有换行
                } else if(line.contains("<img src=\"")) {
                    line = line.substring(line.indexOf("<img src=\"") + "<img src=\"".length(), line.indexOf("\" width"));
                    photo.add(line);
                    state = 0;
                } else if(line.contains("型　　號")) {
                    state = 1;
                } else if(line.contains("封面材質")) {
                    count ++;
                    state = 2;
                } else if(line.contains("封面規格")) {
                    state = 3;
                } else if(line.contains("庫存規格")) {
                    state = 4;
                } else if(state == 1) { // type = 1
                    // line = line.substring(line.indexOf("<td bgcolor=\"#FFFFFF\"><span class=\"text\">") + "<td bgcolor=\"#FFFFFF\"><span class=\"text\">".length(), 
                    //                         line.indexOf("</span>"));
                    if(line.contains("<td nowrap=\"nowrap\" bgcolor=\"#FFFFFF\"><span class=\"text\">")) {
                    line = line.substring(line.indexOf("<td nowrap=\"nowrap\" bgcolor=\"#FFFFFF\"><span class=\"text\">") + "<td nowrap=\"nowrap\" bgcolor=\"#FFFFFF\"><span class=\"text\">".length(), 
                                            line.indexOf("</span>"));
                    }
                    if(line.contains("<td bgcolor=\"#FFFFFF\"><span class=\"text\">")) {
                        line = line.substring(line.indexOf("<td bgcolor=\"#FFFFFF\"><span class=\"text\">") + "<td bgcolor=\"#FFFFFF\"><span class=\"text\">".length(), 
                                            line.indexOf("</span>"));
                    }
                    //System.out.println(line);
                    type.add(line);
                    state = 0;
                } else if (state == 2) { // texture = 2
                    try{ // <td bgcolor="#FFFFFF" class="text">合成皮-變色皮</td>
                        int startIdx = line.indexOf("<td bgcolor=\"#FFFFFF\"><span class=\"text\">") + "<td bgcolor=\"#FFFFFF\"><span class=\"text\">".length();
                        int endIdx = line.indexOf("</span></td>");
                        
                        // 檢查第一個 span 的結束索引是否有效
                        if (startIdx >= 0 && endIdx > startIdx) {
                            // 提取第一個 span 的內容
                            String material = line.substring(startIdx, endIdx)
                            .replace("</span><span class=\"text-red\">", "")
                            .replace("<span class=\"text-red\">", "")
                            .replace("</span>", "");
                            System.out.println(count); 
                            coverTexture.add(material);
                        } else {
                            startIdx = line.indexOf("<td bgcolor=\"#FFFFFF\" class=\"text\">") + "<td bgcolor=\"#FFFFFF\" class=\"text\">".length();
                            endIdx = line.indexOf("</td>");
                            String material = line.substring(startIdx, endIdx)
                            .replace("</span><span class=\"text-red\">", "")
                            .replace("<span class=\"text-red\">", "")
                            .replace("</span>", "");
                            System.out.println(count); 
                            coverTexture.add(material);
                        }
                    } catch (Exception e) {
                        System.out.println(count); 
                    }
                    state = 0;
                } else if(state == 3) { // coversize = 3
                    //
                    state = 0;
                } else if(state == 4) { // storesize = 4
                    line = line.substring(line.indexOf("<td bgcolor=\"#FFFFFF\"><span class=\"text\">") +"<td bgcolor=\"#FFFFFF\"><span class=\"text\">".length(), 
                                            line.indexOf("</span></td>"));
                    storeSize.add(line);
                    state = 0;
                }
                
            }
            System.out.println(photo.size());
            System.out.println(type.size());
            System.out.println(coverTexture.size());
            System.out.println(storeSize.size());
            System.out.println(title.size());
            System.out.println(count);
            for(int i=0; i<photo.size(); i++) {
                // writer.append(String.valueOf(i+1)).append(System.lineSeparator());
                // writer.append(photo.get(i)).append(System.lineSeparator());
                // writer.append(type.get(i)).append(System.lineSeparator());
                // writer.append(coverTexture.get(i)).append(System.lineSeparator());
                // //writer.append(coverSize.get(i)).append(System.lineSeparator());
                // writer.append(storeSize.get(i)).append(System.lineSeparator());
                codeChange(i, "src\\menuCode.txt", outputPath);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close(); // 确保 FileWriter 也被关闭
                }
            } catch (IOException ex) {
                System.out.println("An error occurred while closing the file: " + ex.getMessage());
            }
        }
    }

    public void codeChange(int i, String codePath, String output) {
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(codePath));
            // 使用附加模式（true）打开文件

            writer = new FileWriter(output, true); // true 代表附加模式

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("[image]", photo.get(i))
                .replace("[type]", type.get(i))
                .replace("[texture]", coverTexture.get(i))
                .replace("[coverSize]", "")
                .replace("[storeSize]", storeSize.get(i))
                .replace("[title]", title.get(i));
                writer.append(line).append(System.lineSeparator()); // 确保行与行之间有换行
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close(); // 确保 FileWriter 也被关闭
                }
            } catch (IOException ex) {
                System.out.println("An error occurred while closing the file: " + ex.getMessage());
            }
        }
    }
}
