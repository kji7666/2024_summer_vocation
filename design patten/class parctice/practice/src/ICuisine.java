import java.util.logging.*;
import java.util.*;

/**
 * 需求 : 製作一個點餐系統, 使點餐結果能清楚地送至廚房
 * 考量 : 菜品隨時有可能更新, 也會有新進廚師, 指定菜品會交由指定廚師處里
 * 練習 : 命令模式
 * 功能 : 不須知道有何廚師, 只管點餐, 餐點與廚師的關係由餐廳事先訂定
 * (調用者不需要知道接收者的具體實現，只需要知道如何發送命令。)
 * code : 
 * interface Command { // 點餐命令
 *      method // 命令接收者工作
 * }
 * class implements Command {
 *      receiver // 接收者
 *      method { // 調用接收者工作實現method
 *          receiver.work
 *      }
 * }
 * 
 * interface Receiver { // 接收者的具體實現由Client 決定, 其他class 只知道接收者接口
 *      work // 定義工作
 * }
 * class implements Receiver {
 *      work // 具體實現如何工作
 * }
 * 
 * class Invoker { // 調用者只管Command
 *      method // 依需求實現, 例如 : 點餐需要list 收集點餐Command
 * }
 * 
 * main { // client
 *      CommandA command = new CommandA(new ReceiverA()); // 定義命令與接收者
 *      Invoker.method(command); // 調用者執行命令
 * }
 * 
 */
// 命令（Command）
// 命令是對某些行為的抽象表示。在這裡，ICuisine 介面及其實現類 GuangDongCuisine 和 SiChuanCuisine 是命令的具體實現。
public interface ICuisine {
    void cook();
}

class GuangDongCuisine implements ICuisine {
    private ICook cook;
    public GuangDongCuisine(ICook cook) {
        this.cook = cook;
    }
    public void cook() {
        cook.doCooking();
    }
}

class SiChuanCuisine implements ICuisine {
    private ICook cook;
    public SiChuanCuisine(ICook cook) {
        this.cook = cook;
    }
    public void cook() {
        cook.doCooking();
    }
}

// 接收者（Receiver）
// 接收者是實際執行命令的對象。在這裡，ICook 介面及其實現類 GuangDongCooker 和 SiChuanCooker 是接收者的具體實現。
interface ICook {
    void doCooking();
}

class GuangDongCooker implements ICook {
    private Logger logger = Logger.getLogger(GuangDongCooker.class.getName());
    public void doCooking() {
        logger.info("廣東廚師烹飪廣東菜");
    }
}

class SiChuanCooker implements ICook {
    private Logger logger = Logger.getLogger(GuangDongCooker.class.getName());
    public void doCooking() {
        logger.info("四川廚師烹飪川菜");
    }
}

// 調用者（Invoker）
// 調用者負責接收命令並執行命令。在這裡，Waiter 類是調用者，它負責將命令（ICuisine）存儲到清單中並在適當的時候執行它們。
class Waiter {
    private Logger logger = Logger.getLogger(Waiter.class.getName());
    private List<ICuisine> cuisineList = new ArrayList<>();
    public void order(ICuisine cuisine) {
        cuisineList.add(cuisine);
    }
    public synchronized void placeOrder() {
        for(ICuisine cuisine : cuisineList) {
            cuisine.cook();
        }
        cuisineList.clear();
    }

    public static void main(String[] args) {
        // 客戶端（Client）
        // 客戶端是命令模式的使用者。在這裡，main 方法充當客戶端，創建了具體的命令對象，並將它們傳遞給調用者（Waiter）。
        ICuisine guangDongCuisine = new GuangDongCuisine(new GuangDongCooker());
        ICuisine sichuaCuisine = new SiChuanCuisine(new SiChuanCooker());

        Waiter waiter = new Waiter();
        waiter.order(sichuaCuisine);
        waiter.order(guangDongCuisine);
        
        waiter.placeOrder();
    }
}

