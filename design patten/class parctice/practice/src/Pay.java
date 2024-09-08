import java.math.BigDecimal;
import java.util.logging.*;
/**
 * 需求 : 在支付系統中加入驗證功能
 * 考量 : 如何在不影響原代碼的情況下加入驗證系統
 * 練習 : 橋接模式
 * 功能 : 系統1需要系統2的功能, 引入系統2功能後, 仍然能自由修改各自代碼
 * Pay 類（Abstraction）和 IPayMode 介面（Implementor）分別作為抽象部分和實現部分，
 * 它們可以獨立地進行修改和擴展。例如，添加新的支付方式或新的驗證方式都不會影響已有的部分。
 * code : 
 * abstract class Abstraction { // 系統1
 *      Implementor implementor // 有另一系統的實例
 *      constructor {implementor}
 *      method // 有此系統需要實現的功能method
 * }
 * class extends Abstraction {
 *      constructor : super
 *      override method {// 各自實現
 *          implement.method // 在此實作兩系統融合功能
 *      }
 * }
 * 
 * interface Implementor{ // 系統2
 *      method // 有此系統需要實現的功能method
 * }
 * class implements Implementor {
 *      method // 各自實現
 * }
 */

public abstract class Pay {
    protected Logger logger = Logger.getLogger(Pay.class.getName());
    protected IPayMode payMode;
    public Pay(IPayMode payMode) {
        this.payMode = payMode;
    }
    public abstract String transfer(String userId, String tradeId, BigDecimal amount);


    public static void main(String[] args) {
        Pay linePay = new LinePay(new PayFaceMode());
        linePay.transfer("user1", "10001", new BigDecimal(100));
        Pay zfbPay = new ZfbPay(new PayFingerprintMode());
        zfbPay.transfer("user2", "10002", new BigDecimal(200));
    }
}

class LinePay extends Pay {
    public LinePay(IPayMode payMode) {
        super(payMode);
    }
    public String transfer(String userId, String tradeId, BigDecimal amount) {
        logger.info("Line支付開始");
        boolean security = payMode.security(userId);
        logger.info("Line身分驗證");
        if(!security) {
            logger.info("Line支付攔截");
            return "0001";
        }
        logger.info("Line支付成功");
        return "0000";
    }
}

class ZfbPay extends Pay {
    public ZfbPay(IPayMode payMode) {
        super(payMode);
    }
    public String transfer(String userId, String tradeId, BigDecimal amount) {
        logger.info("支付保支付開始");
        boolean security = payMode.security(userId);
        logger.info("支付保身分驗證");
        if(!security) {
            logger.info("支付保支付攔截");
            return "0001";
        }
        logger.info("支付保支付成功");
        return "0000";
    }
}

interface IPayMode {
    boolean security(String userId);
}

class PayFaceMode implements IPayMode {
    protected Logger logger = Logger.getLogger(PayFaceMode.class.getName());
    public boolean security(String userId) {
        logger.info("人臉辨識");
        return true;
    }
}

class PayFingerprintMode implements IPayMode {
    protected Logger logger = Logger.getLogger(PayFingerprintMode.class.getName());
    public boolean security(String userId) {
        logger.info("指紋辨識");
        return true;
    }
}