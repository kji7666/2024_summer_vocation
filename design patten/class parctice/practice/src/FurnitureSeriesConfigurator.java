/**
 * 抽象工廠模式是用於創建一系列相關或互相依賴的對象，而不僅僅是單個對象。
 * 這裡的「一系列」指的是一個完整的產品家族，包括多個相關的產品類型，這些產品類型在功能上或者是在概念上有密切的關聯性。
 */
/**
 * 需求 : 挑選製造一系列家具
 * 考量 : 隨著業務發展, 新增其他系列
 * 練習 : 抽象工廠模式
 * 功能 : 可隨意新增以系列(家具種類不變,風格不同)為單位的商品
 * code :
    interface factory { // 定義抽象工廠(每個系列都會製造以下家具)
        createfurnitureA;
    }
    factorySeriesA // 實現如何"製造"家具
    factorySeriesB

    interface furnitureA // 定義此家具必要特徵
    SeriesAfurnitureA // 實現此系列特徵
    SeriesBfurnitureA

    SeriesConfig {
        factory
        furnitureA
        constructor (factory) {
            this.factory = factory
        }
        method createfurnitureA {
            furnitureA = factory.createfurnitureA;
        }
    }

    test {
        if (series == A) {
            factory = new factorySeriesA
        }
        config = SeriesConfig.constructor(factory)
        config.createfurnitureA
    }
 */
// 家具工廠接口
interface FurnitureFactory {
    Chair createChair();
    Table createTable();
}

// 維多利亞風格家具工廠
class VictorianFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new VictorianChair();
    }

    @Override
    public Table createTable() {
        return new VictorianTable();
    }
}

// 現代風格家具工廠
class ModernFurnitureFactory implements FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ModernChair();
    }

    @Override
    public Table createTable() {
        return new ModernTable();
    }
}

// 椅子接口
interface Chair {
    void sitOn();
}

// 維多利亞風格椅子
class VictorianChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a Victorian chair.");
    }
}

// 現代風格椅子
class ModernChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a modern chair.");
    }
}

// 桌子接口
interface Table {
    void use();
}

// 維多利亞風格桌子
class VictorianTable implements Table {
    @Override
    public void use() {
        System.out.println("Using a Victorian table.");
    }
}

// 現代風格桌子
class ModernTable implements Table {
    @Override
    public void use() {
        System.out.println("Using a modern table.");
    }
}

// 客戶端家具應用程序
class FurnitureSeriesApp {
    private FurnitureFactory factory;
    private Chair chair;
    private Table table;

    public FurnitureSeriesApp(FurnitureFactory factory) {
        this.factory = factory;
    }

    public void createChair() {
        chair = factory.createChair();
        chair.sitOn();
    }

    public void createTable() {
        table = factory.createTable();
        table.use();
    }
}

// 程序配置類
public class FurnitureSeriesConfigurator {
    public static void main(String[] args) throws Exception {
        int choice;
        FurnitureFactory factory;
        FurnitureSeriesApp app;

        // 假設從輸入獲取選擇
        choice = 1;

        if (choice == 0) {
            factory = new VictorianFurnitureFactory();
        } else if (choice == 1) {
            factory = new ModernFurnitureFactory();
        } else {
            throw new Exception("錯誤！未知的風格選擇。");
        }
        
        app = new FurnitureSeriesApp(factory);
        app.createChair();
        app.createTable();
    }
}
