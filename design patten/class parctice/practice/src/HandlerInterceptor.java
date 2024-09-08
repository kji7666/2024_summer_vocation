import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

// 需求 : 公司的登入系統
// 考量 : 團隊隨著發展, 新增許多不同職位人員, 登入權限有所不同
// 練習 : 裝飾器模式
// 功能 : 在不影響原代碼的情況下, 更進某系統功能(為添加, 非修改)
/*
 * code : 
 * interface Handler {
 *      method // 預計更進的功能
 * }
 * class function implements Handler {
 *      method // 基礎功能
 * }
 * abstract class Decorator implements Handler {
 *      Handler
 *      method {Handler.method} // 指定parent.method功能 (不指定為基礎功能, 使其也可裝飾更進後的功能)
 * }
 * class funciotnPlus extends Decorator {
 *      method {
 *          super.method() // 先執行parent.method
 *          // 再添加其他功能代碼
 *      }
 * }
 */

// 定義攔截器接口
public interface HandlerInterceptor {
    boolean preHandle(String request, String response, Object handler);
}

// 實現基礎攔截器
class SsoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(String request, String response, Object handler) {
        // 從請求中截取一段字符作為 ticket
        String ticket = request.substring(1, 8);
        // 第一關驗證(原功能) : 只需要 success 員工認證就可取得全權限
        return ticket.equals("success");
    }
}

// 定義抽象裝飾器類，實現 HandlerInterceptor 接口
abstract class SsoDecorator implements HandlerInterceptor {
    private HandlerInterceptor handlerInterceptor;

    // 禁止無參構造函數
    private SsoDecorator() {}

    // 裝飾器構造函數，接收一個 HandlerInterceptor 對象
    public SsoDecorator(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    // 調用被裝飾對象的 preHandle 方法
    @Override
    public boolean preHandle(String request, String response, Object handler) {
        return handlerInterceptor.preHandle(request, response, handler);
    }
}

// 定義具體的裝飾器，增加額外的驗證功能
class LoginSsoDecorator extends SsoDecorator {
    private Logger logger = Logger.getLogger(LoginSsoDecorator.class.getName());
    private static Map<String, String> authMap = new ConcurrentHashMap<>();

    // 初始化靜態授權數據
    static {
        authMap.put("user1", "queryUserInfo");
        authMap.put("user2", "queryUserInfo");
    }

    // 裝飾器構造函數，接收一個 HandlerInterceptor 對象
    public LoginSsoDecorator(HandlerInterceptor handlerInterceptor) {
        super(handlerInterceptor);
    }

    // 覆寫 preHandle 方法，增加額外的驗證功能
    @Override
    public boolean preHandle(String request, String response, Object handler) {
        // 先通過基礎驗證
        boolean success = super.preHandle(request, response, handler);
        if (!success) return false;

        // 從請求中截取 userId
        String userId = request.substring(8);
        // 根據 userId 查詢對應的方法
        String method = authMap.get(userId);

        // 第二次驗證，確認用戶是否有權限執行 queryUserInfo 方法
        return "queryUserInfo".equals(method);
    }
}

// 測試類，用於驗證裝飾器模式的實現
class TestLogin {
    public static void main(String[] args) {
        // 創建具體的裝飾器對象，並傳入基礎攔截器
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator(new SsoInterceptor());

        // 測試請求
        String request = "Asuccessuser1";
        boolean success = ssoDecorator.preHandle(request, "response", "handler");
        System.out.println("登入驗證 : " + request + (success ? " 通過" : " 不通過"));
    }
}
