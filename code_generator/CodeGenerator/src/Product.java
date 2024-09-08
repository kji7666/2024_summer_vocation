public class Product {
    private final String MENU = "2-9";
    private String type;
    private String id;
    private String size;
    private String imgPath;
    public Product(String id, String size, String imgPath) {
        // MC-202-1/"path"
        type = MENU;
        this.id = id;
        this.size = size;
        this.imgPath = imgPath;
    }
    public String getType() {
        return type;
    }
    public String getId() {
        return id;
    }
    public String getSize() {
        return size;
    }
    public String getImgPath() {
        return imgPath;
    }
}
