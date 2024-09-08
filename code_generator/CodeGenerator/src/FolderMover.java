import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FolderMover {

    public static void main(String[] args) {
        String sourceFolder = "src\\old_B";  // 替換為A資料夾的路徑
        String destinationFolder = "src\\new_A";  // 替換為B資料夾的路徑

        moveFilesWithNewInName(sourceFolder, destinationFolder);
    }

    public static void moveFilesWithNewInName(String sourceFolder, String destinationFolder) {
        File sourceDir = new File(sourceFolder);
        File[] files = sourceDir.listFiles((dir, name) -> name.contains("new"));

        if (files != null) {
            for (File file : files) {
                try {
                    // 建立目標路徑
                    Path destinationPath = new File(destinationFolder, file.getName()).toPath();
                    // 移動檔案或資料夾
                    Files.move(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved: " + file.getName());
                } catch (IOException e) {
                    System.out.println("Failed to move: " + file.getName() + ". Error: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No files or folders containing 'new' found.");
        }
    }
}
