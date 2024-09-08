import java.util.*;
import java.util.logging.*;
/**
 * 需求 : 用戶進行抽獎後, 儲存結果並發送給用戶
 * 考量 : 抽獎後希望能新增其他行為, 而不只是儲存結果並發送給用戶
 * 練習 : 觀察者模式
 * 功能 : 將主服務(主題對象)與附帶服務(觀察者對象)分離, 使添加附帶模式時不會影響主服務
 * 適合A 發生時B 也得更動或執行任務的情形
 * code :
 * class MainService { // 定義主服務
 *      method
 * }
 * abstract class Service { // 控制訂閱, 在主服務執行後叫用副服務(subscribed Event)
 *      EventManager
 *      constructor {// 分配subscribe}
 *      method {
 *          mainEvent // 調用主服務
 *          eventManager.notify // 叫用副服務
 *      }
 *      abstract mainEvent
 * }
 * class Impl extends Service { // 實現主服務
 *      Override mainEvent {
 *          MainService.method
 *      }
 * }
 * 
 * interface Listener // 事件發生後的處理器(無視事件類型, 叫用即處理)
 * class implements Listener
 * 
 * class EventManager { // 負責儲存訂閱事件
 *      Map<Enum, List<Listener>> // 訂閱事件, 觸發此事件的處理器
 *      Enum{//各event type }
 *      subscribe(Event, Listener);
 *      unsubscribe(Event, Listener);
 *      notify(Event, Result)
 * }
 * 
 */


// 主服務，進行抽獎操作
public class MinibusTargetService {
    public String lottery(String userId) {
        return Math.abs(userId.hashCode()) % 2 == 0 ? "恭喜你中獎" : "未中獎"; // 隨機抽獎
    }
}

// 抽獎結果類，用於保存抽獎的結果
class LotteryResult {
    private String userId;
    private String lotteryMsg;
    private Date date;

    public LotteryResult(String userId, String lotteryMsg, Date date) {
        this.userId = userId;
        this.lotteryMsg = lotteryMsg;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getLotteryMsg() {
        return lotteryMsg;
    }

    public Date getDate() {
        return date;
    }
}


// 抽象抽獎服務類，使用觀察者模式進行事件通知
// 配置訂閱, 執行主事件, 調用notify
abstract class LotteryService {
    private EventManager eventManager;

    public LotteryService() {
        eventManager = new EventManager(EventManager.EventType.MQ, EventManager.EventType.Message);
        eventManager.subscribe(EventManager.EventType.MQ, new MQEventListener());
        eventManager.subscribe(EventManager.EventType.Message, new MessageEventListener());
    }

    public LotteryResult draw(String userId) {
        LotteryResult lotteryResult = doDraw(userId);
        eventManager.notify(EventManager.EventType.MQ, lotteryResult);
        eventManager.notify(EventManager.EventType.Message, lotteryResult);
        return lotteryResult;
    }

    protected abstract LotteryResult doDraw(String userId);
}

// 具體的抽獎服務實現類
class LotteryServiceImpl extends LotteryService {
    private Logger logger = Logger.getLogger(LotteryServiceImpl.class.getName());
    private MinibusTargetService minibusTargetService = new MinibusTargetService();

    @Override
    protected LotteryResult doDraw(String userId) {
        String lottery = minibusTargetService.lottery(userId);
        return new LotteryResult(userId, lottery, new Date());
    }
}

// 事件監聽器接口，所有具體的事件監聽器都需要實現這個接口
// 觀察者實現
interface EventListener {
    void doEvent(LotteryResult result);
}

// 具體的消息事件監聽器實現類
class MessageEventListener implements EventListener {
    private Logger logger = Logger.getLogger(MessageEventListener.class.getName());

    @Override
    public void doEvent(LotteryResult result) {
        logger.info("給用戶 " + result.getUserId() + " 發送訊息: " + result.getLotteryMsg());
    }
}

// 具體的消息隊列事件監聽器實現類
class MQEventListener implements EventListener {
    private Logger logger = Logger.getLogger(MQEventListener.class.getName());

    @Override
    public void doEvent(LotteryResult result) {
        logger.info("紀錄用戶 " + result.getUserId() + " 抽獎結果: " + result.getLotteryMsg());
    }
}


// 事件管理類，負責管理訂閱和通知事件
// 只負責儲存, 由service調用
class EventManager {
    // 事件類型和對應的事件監聽器列表的映射
    Map<Enum<EventType>, List<EventListener>> listeners = new HashMap<>();

    // 構造方法，初始化事件類型及其對應的監聽器列表
    public EventManager(Enum<EventType>... operations) {
        for (Enum<EventType> operation : operations) {
            // 為每種事件類型創建一個新的監聽器列表
            // 每種事件類型的監聽器列表是獨立的，這樣可以避免不同事件類型的監聽器之間的干擾。
            // 當需要添加新的事件類型時，只需要在構造方法中添加相應的事件類型即可，而不會影響到已經存在的事件類型。
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    // 事件類型的枚舉
    public enum EventType {
        MQ, Message
    }

    // 訂閱方法，添加監聽器到對應的事件類型中
    public void subscribe(Enum<EventType> eventType, EventListener listener) {
        // 獲取對應事件類型的監聽器列表
        List<EventListener> users = listeners.get(eventType);
        // 添加監聽器到列表中
        users.add(listener);
    }

    // 取消訂閱方法，從對應的事件類型中移除監聽器
    public void unsubscribe(Enum<EventType> eventType, EventListener listener) {
        // 獲取對應事件類型的監聽器列表
        List<EventListener> users = listeners.get(eventType);
        // 從列表中移除監聽器
        users.remove(listener);
    }

    // 通知方法，通知所有訂閱了特定事件類型的監聽器
    public void notify(Enum<EventType> eventType, LotteryResult result) {
        // 獲取對應事件類型的監聽器列表
        List<EventListener> users = listeners.get(eventType);
        // 遍歷列表並通知每個監聽器
        for (EventListener listener : users) {
            listener.doEvent(result);
        }
    }
}


// 測試類，用於測試觀察者模式
class MementoPatternDemo {
    public static void main(String[] args) {
        LotteryService lotteryService = new LotteryServiceImpl();
        
        // 註冊觀察者
        EventManager eventManager = new EventManager(EventManager.EventType.MQ, EventManager.EventType.Message);
        eventManager.subscribe(EventManager.EventType.MQ, new MQEventListener());
        eventManager.subscribe(EventManager.EventType.Message, new MessageEventListener());

        // 進行抽獎操作
        LotteryResult result = lotteryService.draw("user123");

        System.out.println("抽獎結果: " + result.getLotteryMsg() + " 用戶ID: " + result.getUserId());
    }
}
