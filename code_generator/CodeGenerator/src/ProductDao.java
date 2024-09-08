import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDao {
    public static void main(String[] args) {
        new ProductDao();
    }
    private Map<String, List<String>> idMap = new HashMap<>(); // MC-200 to MC-200-1, MC-200-2
    private Map<String, Product> productMap = new HashMap<>(); // MC-200-1 to product
    private final String HTML_PATH = "src\\html_test_folder";
    private final String IMG_PATH = "src\\img_test_folder";

    public ProductDao() {
        recordHtml(HTML_PATH);
        recordProduct(IMG_PATH);
        sortId();
        //show();
    }

    private void sortId() {
        for (Map.Entry<String, List<String>> entry : idMap.entrySet()) {
            List<String> list = entry.getValue();
            Collections.sort(list); // 對 List 進行原地排序
        }
    }

    private void show() {
        for(Entry<String, List<String>> entry : idMap.entrySet()) {
            System.out.println("KEY : " + entry.getKey());
            for(String id : entry.getValue()) {
                System.out.println(id);
            }
        }
        for(Entry<String, Product> entry : productMap.entrySet()) {
            System.out.println("id : " + entry.getKey());
            System.out.println(entry.getValue().getId() + " path : " + entry.getValue().getImgPath());
        }
    }

    private void recordHtml(String path) {
        String folderPath = path;
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("The specified path for folder B is not a valid directory.");
            return;
        }
        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("No files found in folder.");
            return;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.endsWith(".html")) continue; // 忽略非 HTML 檔案
            // MC-200-1
            String id = fileName.substring("2-9-".length(), fileName.indexOf(".html"));
            //System.out.println(id);
            String[] parts = id.split("-");
            String mainId = parts[0] + "-" + parts[1];
            if(idMap.containsKey(mainId)) {
                idMap.get(mainId).add(id);
            } else {
                List<String> newList = new ArrayList<>();
                newList.add(id);
                System.out.println(id);
                idMap.put(mainId, newList);
            }
        }
    }

    private void recordProduct(String path) {
        String folderPath = path; // 資料夾 A 路徑
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("The specified path for folder B is not a valid directory.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("No files found in folder.");
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.endsWith(".jpg")) continue;
            String id = fileName.substring(0, fileName.indexOf(".jpg")).toUpperCase();
            String[] parts = id.split("-");
            if(!(parts.length <= 5 && parts.length >= 4)) {
                continue;
            }
            String headId = null;
            String codeId = null;
            String lastId = null;
            String size = null;
            for(int i=0; i<parts.length-1; i++) { // 去除最尾的s
                String part = parts[i];
                if (part.matches("[a-zA-Z]+")) {
                    // 全英文
                    headId = part;
                } else if (part.matches("\\d+")) {
                    // 全數字
                    if(part.matches("\\d")) {
                        lastId = part;
                    } else {
                        codeId = part;
                    }
                } else if (part.matches("[a-zA-Z0-9]+")) {
                    // 包含英文與數字"
                    size = part;
                }
            }
            // if(size.equals("B")) continue;
            String fullId = headId + "-" + codeId + (lastId != null ? "-" + lastId : "");
            Product product = new Product(fullId, size, fileName);
            productMap.put(fullId, product);
        }
    }

    public Product getProduct(String id) {
        if(productMap.containsKey(id)){ 
            System.out.println("exist" + productMap.get(id).getId());
            return productMap.get(id);
        } else {
            return null;
        }
    }

    public List<String> getSameIdList(String id) {
        System.out.println("try to get " + id);
        if(idMap.containsKey(id)) {
            for(String Id : idMap.get(id)) {
                System.out.println("try to get " + Id);
            }
            return idMap.get(id);
        } else {
            return null;
        }
    }
}
