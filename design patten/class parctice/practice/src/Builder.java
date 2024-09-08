import java.math.BigDecimal;
import java.util.*;
/**
 * 需求 : 自選材料建造房屋
 * 考量 : 每個房屋都有物件, 但物件的實現要求不一定相同, 有些可有可無
 * 練習 : 建造者模式
 * 功能 : 廠商可以自由挑選材料組成販賣組合, menu的存在可以幫助統計此組合的各種屬性
 * code : 
 * interface Matter // 定義材料"說明書"格式
 * class MatterA implements Matter // 依需求實現材料內容(假設MatterA為Chair)
 * 
 * interface Menu { // 定義有哪種材料可append進清單
 *      Menu appendChair // 每次append後, 以return menu方式更新
 * }
 * class DecorationMenu implements Menu {
 *      List<Matter> // 儲存append的材料
 *      // 其他業務資料
 *      Menu appendChair(Matter matter) {
 *          list.add(matterA);
 *          return this; // return menu == return this
 *      }
 * }
 * class Builder { // 提供method組成套餐
 *      public Menu levelOne(Double area) {
 *          return new DecorationMenu("說明").appendChair(new MatterA);
 *      }
 * }
 * 
 * test {
 *      Builder builder = new Builder();
 *      builder.levelOne(12);
 * }
 
 * 提問 :
 為何把所有種類的材料都歸類為matter?
    有助於統一管理和操作各種材料，便於擴展和維護。這樣可以使用同一套接口來處理不同種類的材料
 各自分位地板, 吊頂......, 會有什麼問題?
    導致系統的複雜度增加，每種材料都需要獨立的處理邏輯和接口，增加了開發和維護的難度。
 跟抽象工廠的差異是?
    建造者模式：關注於逐步構建一個複雜對象，允許通過調用不同的方法來定制對象的組成部分。適用於需要構建過程複雜且需要多步驟構建的情況。
    抽象工廠模式：關注於提供一系列相關或依賴對象的接口，適用於需要創建一組相關對象的情況，而不需要關注它們的具體類型。
 DecorationPackageMenu return this有何優點?
    可以實現方法鏈式調用，使代碼更加簡潔、易讀。
 */

/**
 * 需求 : 自選材料建造房屋
 * 考量 : 每個房屋都有物件, 但物件的實現要求不一定相同, 有些可有可無
 * 練習 : 建造者模式
 * 功能 : 廠商可以自由挑選材料組成販賣組合, menu的存在可以幫助統計此組合的各種屬性
 */
public class Builder {
    // 提供組合套餐的方法
    public IMenu levelOne(Double area) {
        // 建立一個新的DecorationPackageMenu並添加吊頂和地板
        return new DecorationPackageMenu(area, "豪華歐式")
            .appendCeiling(new LevelTwoCeiling())
            .appendFloor(new DerFloor());
    }

    public IMenu levelTwo(Double area) {
        return new DecorationPackageMenu(area, "現代簡約")
            .appendCeiling(new LevelOneCeiling())
            .appendFloor(new DerFloor());
    }
}

// 定義所有材料的接口，不分類
interface Matter {
    String scene(); // 材料場景，例如地板，地磚，塗料，吊頂等
    String brand(); // 材料品牌
    String model(); // 材料型號
    BigDecimal price(); // 材料價格
    String desc(); // 材料描述
}

// 吊頂實現類 - 一級吊頂
class LevelOneCeiling implements Matter {
    public String scene() {
        return "吊頂";
    }
    public String brand() {
        return "裝修自帶";
    }
    public String model() {
        return "一級頂";
    }
    public BigDecimal price() {
        return new BigDecimal(260);
    }
    public String desc() {
        return "基本造型";
    }
}

// 吊頂實現類 - 二級吊頂
class LevelTwoCeiling implements Matter {
    public String scene() {
        return "吊頂";
    }
    public String brand() {
        return "裝修自帶";
    }
    public String model() {
        return "二級頂";
    }
    public BigDecimal price() {
        return new BigDecimal(850);
    }
    public String desc() {
        return "兩層式造型";
    }
}

// 地板實現類
class DerFloor implements Matter {
    public String scene() {
        return "地板";
    }
    public String brand() {
        return "Der";
    }
    public String model() {
        return "A+";
    }
    public BigDecimal price() {
        return new BigDecimal(119);
    }
    public String desc() {
        return "Der專業木地板製造";
    }
}

// 定義菜單接口，提供添加吊頂和地板的方法
interface IMenu {
    IMenu appendCeiling(Matter matter); // 添加吊頂
    IMenu appendFloor(Matter matter); // 添加地板
}

// 實現菜單接口的類，用於構建裝修套餐
class DecorationPackageMenu implements IMenu {
    private List<Matter> list = new ArrayList<Matter>(); // 儲存材料列表
    private BigDecimal price = BigDecimal.ZERO; // 總價格
    private BigDecimal area; // 裝修面積
    private String grade; // 裝修風格

    private DecorationPackageMenu() {}

    public DecorationPackageMenu(Double area, String grade) {
        this.area = new BigDecimal(area);
        this.grade = grade;
    }

    public IMenu appendCeiling(Matter matter) {
        list.add(matter);
        // 計算吊頂部分的價格：面積 * 單價 * 0.2
        price = price.add(area.multiply(new BigDecimal("0.2")).multiply(matter.price()));
        return this;
    }

    public IMenu appendFloor(Matter matter) {
        list.add(matter);
        // 計算地板部分的價格：面積 * 單價 * 0.4
        price = price.add(area.multiply(new BigDecimal("0.4")).multiply(matter.price()));
        return this;
    }
}
