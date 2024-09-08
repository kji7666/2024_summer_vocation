import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ProductChanger {

    // 2-9-MC-202-A5 = [種類碼]-[種類]-[商品碼]-[size]
    private final String PRODUCT_TAMPLATE = "src\\productCode.txt";
    private final String PRODUCT_OUTPUT = "src\\productOuptut.txt";
    private final String PRODUCT_INPUT = "src\\productInput.txt";

    private ProductDao dao = new ProductDao();

    public ProductChanger() {
        
    }

    public static void clear(String fileName) {
        FileWriter writer;
        try{
            writer = new FileWriter(fileName);
            writer.write("");
            writer.close();
        } catch (Exception e) {
        } 
    }

    public void inputParser(String id) {
        // type : MC-202
        List<String> sameIdList = dao.getSameIdList(id);
        for(String detailId : sameIdList) {
            addBody(dao.getProduct(detailId));
        }
    }

    public void addBody(Product product) {
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(PRODUCT_TAMPLATE));
            // 使用附加模式（true）打开文件
            writer = new FileWriter(PRODUCT_OUTPUT, true); // true 代表附加模式
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("[type]", product.getType())
                           .replace("[id]", product.getId())
                           .replace("[size]", product.getSize())
                           .replace("[image]", product.getImgPath());
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
