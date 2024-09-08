import java.util.*;
import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * 使OrderService obj也能使用isFirst method
 * 使用其method完成isFirst邏輯, 且可以以OrderAdapterService調用此方法(適用於有需求的複數class)
 * 需求 : 業務有許多MQ消息需要處理
 * 考量 : 隨著業務發展, 不再只是接收MQ消息並執行, 還需加入判斷條件
 * 練習 : 適配器模式
 * 功能 : 如何在不更動主程式碼的情況下, 為指定種類的MQ消息加入判斷method
 * code : 
 * interface Service{
 *      methodAtoC
 * }
 * class Service1 {
 *      methodAtoB
 * }
 * Adapter {
 *      Service1 service1
 *      methodAtoC{
 *          return service1.methodAtoB.doSomethingToC
 *      }
 * }
 */

 // 訂單服務類
class OrderService {
    private Logger logger = Logger.getLogger(OrderService.class.getName());
    
    // 內部商家查詢用戶訂單數量
    public long queryUserOrderCount(String userId) {
        logger.info("內部商家查詢user下單數 : {" + userId + "}"); // 記錄日誌
        return 10L; // 假設返回下單數量
    }
}

// POP 訂單服務類
class PopOrderService {
    private Logger logger = Logger.getLogger(PopOrderService.class.getName());
    
    // POP 商家查詢用戶是否為首單
    public boolean isFirstOrder(String userId) {
        logger.info("POP商家查詢用戶訂單是否為首單"); // 記錄日誌
        return true; // 假設返回結果
    }
}

interface OrderAdapterService {
    boolean isFirst(String userId);
}

class InsideOrderService implements OrderAdapterService { // 這個才是定義上的adapter
    private OrderService orderService = new OrderService();
    public boolean isFirst(String userId) {
        return orderService.queryUserOrderCount(userId) <= 1;
    }
}

class PopOrderAdapterServiceImpl implements OrderAdapterService {
    private PopOrderService popOrderService = new PopOrderService();
    public boolean isFirst(String userId) {
        return popOrderService.isFirstOrder(userId);
    }
}

