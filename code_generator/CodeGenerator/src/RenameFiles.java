import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameFiles {

    public static void main(String[] args) {
        String folderPath = "src\\new_A"; // 替換為資料夾的路徑
        renameFilesWithNew(folderPath);
    }

    public static void renameFilesWithNew(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.contains("_new"));

        if (files != null) {
            for (File file : files) {
                String oldName = file.getName();
                String newName = oldName.replace("_new", "");

                // 建立新檔案的路徑
                File newFile = new File(folderPath, newName);

                // 檢查新名稱的檔案是否已存在
                if (newFile.exists()) {
                    System.out.println("Cannot rename " + oldName + " to " + newName + " because a file with the new name already exists.");
                } else {
                    boolean success = file.renameTo(newFile);
                    if (success) {
                        System.out.println("Renamed: " + oldName + " to " + newName);
                    } else {
                        System.out.println("Failed to rename: " + oldName);
                    }
                }
            }
        } else {
            System.out.println("No files containing '_new' found.");
        }
    }
    
    public static void renameFiles(String folderPath) {
        // 取得資料夾
        File folder = new File(folderPath);
        
        // 確認資料夾是否存在且是資料夾
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定的路徑不是一個有效的資料夾！");
            return;
        }
        
        // 取得資料夾中的所有檔案
        File[] files = folder.listFiles();
        
        // 正則表達式模式
        Pattern pattern = Pattern.compile("^(M[A-Z])-(\\w+)-(\\d+)-(\\w)\\.jpg$");
        
        if (files != null) {
            for (File file : files) {
                // 檢查是否為檔案
                if (file.isFile()) {
                    // 取得檔案名稱
                    String fileName = file.getName();
                    
                    // 檢查檔案名稱是否符合模式
                    Matcher matcher = pattern.matcher(fileName);
                    if (matcher.matches()) {
                        // 提取匹配到的各部分
                        String letter = matcher.group(1); // 大寫字母
                        String size = matcher.group(2); // 尺寸
                        String code = matcher.group(3); // 編碼
                        String suffix = matcher.group(4); // 小寫字母
                        
                        // 建立新檔名
                        String newFileName = String.format("%s-%s-%s-%s.jpg", letter, code, size, suffix);
                        File newFile = new File(folderPath + File.separator + newFileName);
                        
                        // 重命名檔案
                        if (file.renameTo(newFile)) {
                            System.out.println("檔案已重命名為: " + newFileName);
                        } else {
                            System.out.println("無法重命名檔案: " + fileName);
                        }
                    }
                }
            }
        } else {
            System.out.println("資料夾中沒有檔案。");
        }
    }
}
