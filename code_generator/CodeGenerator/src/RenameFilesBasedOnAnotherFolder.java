import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RenameFilesBasedOnAnotherFolder {

    public static void main(String[] args) {
        // 設定資料夾路徑
        String folderBPath = "D:\\2024 summer vocation\\code_generator\\2_9_menu"; // 資料夾 A 路徑
        String folderAPath = "D:\\2024 summer vocation\\code_generator\\new"; // 資料夾 B 路徑
        
        File folderA = new File(folderAPath);
        File folderB = new File(folderBPath);

        // 確保資料夾存在
        if (!folderA.exists() || !folderA.isDirectory()) {
            System.out.println("The specified path for folder A is not a valid directory.");
            return;
        }
        if (!folderB.exists() || !folderB.isDirectory()) {
            System.out.println("The specified path for folder B is not a valid directory.");
            return;
        }

        // 獲取資料夾 B 的檔案名稱
        File[] filesB = folderB.listFiles();
        if (filesB == null) {
            System.out.println("No files found in folder B.");
            return;
        }
        List<String> newFileNames = Arrays.asList(filesB).stream()
                .map(File::getName)
                .toList();

        // 獲取資料夾 A 的檔案
        File[] filesA = folderA.listFiles();
        if (filesA == null) {
            System.out.println("No files found in folder A.");
            return;
        }

        // 檢查檔案數量是否一致
        if (filesA.length != newFileNames.size()) {
            System.out.println(filesA.length + "/" + newFileNames.size());
            System.out.println("The number of files in folder A and folder B do not match.");
            return;
        }

        // 重命名檔案
        int i = 0;
        for (File fileA : filesA) {
            if (fileA.isFile()) {
                String newName = newFileNames.get(i);

                // 檢查是否是 .html 檔案
                if (newName.endsWith(".html")) {
                    newName = newName.substring(0, newName.lastIndexOf(".html")) + "_new.html";
                }

                File newFile = new File(folderA, newName);
                if (fileA.renameTo(newFile)) {
                    System.out.println("Renamed file: " + fileA.getName() + " to " + newName);
                } else {
                    System.out.println("Failed to rename file: " + fileA.getName());
                }
                i++;
            }
        }
    }
}
