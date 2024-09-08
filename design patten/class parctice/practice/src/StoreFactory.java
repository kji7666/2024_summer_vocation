import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

/**
 * 需求 : 用積分換取商品
 * 考量 : 隨著業務發展, 新增其他商品類型
 * 練習 : 工廠模式
 * 功能 : 在factory新增商品代碼及code, 即可在極小的改動下完成商品新增
 * code :
    commodity interface定義
    Service class實現 : {
        呼叫commodity method送出commodity instance
        instance由result class包裝傳遞
    }

    factory 依照傳入的需求, return 相應的service
*/

/**
 * 對於所有獎品, 都由此接口處理
 */
interface ICommodity {
    /**
     * 發送商品的方法
     * 
     * @param userId 用戶ID
     * @param commodityId 商品ID
     * @param bussessId 業務ID
     * @param extMap 其他參數
     * @throws Exception 當發送過程中出現錯誤時拋出異常
     */
    public void sendCommodity(String userId, String commodityId, String bussessId, Map<String, String> extMap) throws Exception;
}

/**
 * 區分 CouponCommodityService 和 CouponService 是為了遵循單一職責原則
 * CouponCommodityService：
    負責處理與優惠券商品相關的業務邏輯。
    負責調用 CouponService 發送優惠券。
    負責日誌記錄和錯誤處理。
 * CouponService：
    專注於實現優惠券的發送功能。
    不涉及其他業務邏輯或日誌記錄等非核心功能。
 * 優惠券商品服務
 * 實現了 ICommodity 接口，用於發送優惠券
 */
class CouponCommodityService implements ICommodity {
    private static final Logger logger = Logger.getLogger(CouponCommodityService.class.getName());
    private CouponService couponService = new CouponService();

    @Override
    public void sendCommodity(String userId, String commodityId, String bussessId, Map<String, String> extMap) throws Exception {
        // 發送優惠券
        CouponResult couponResult = couponService.sendCoupon(userId, commodityId, bussessId);
        
        // 記錄日誌信息
        logger.info("coupon request: user ID {" + userId + "}, commodity ID {" + commodityId + "}, bussess ID {" + bussessId + "}");
        
        // 檢查發送結果，如果失敗則拋出異常
        if (!"0000".equals(couponResult.getCode())) {
            throw new RuntimeException(couponResult.getInfo());
        }
    }
}

/**
 * 優惠券服務
 * 負責發送優惠券的具體實現
 */
class CouponService {
    public CouponResult sendCoupon(String userId, String commodityId, String bussessId) {
        // 實際的發送邏輯
        return new CouponResult();
    }
}

/**
 * 使用 CouponResult 的主要目的是遵循設計模式中的返回值模式（Return Value Pattern）
 * 模式的主要優點包括：
    分離邏輯：方法本身只關注業務邏輯，而返回值類則負責封裝結果，這樣可以更清晰地分離邏輯。
    提高可讀性：使用返回值類可以使方法的返回結果更加明確和可讀。例如，可以直接看到方法返回的 CouponResult 物件中包含了哪些信息。
    統一處理結果：可以統一處理方法的結果，例如檢查結果代碼、記錄日誌或拋出異常。

 * 優惠券結果
 * 用於封裝發送優惠券的結果
 */
class CouponResult {
    private String commodityCode;
    private String info;

    /**
     * 構造函數
     * 默認結果為成功
     */
    public CouponResult() {
        this.commodityCode = "0000";
        this.info = "Success";
    }

    public String getCode() {
        return this.commodityCode;
    }

    public String getInfo() {
        return this.info;
    }
}

public class StoreFactory {
    // 根據 commodityType 返回對應的 ICommodity 實例
    public ICommodity getCommodityService(int commodityType) {
        switch (commodityType) {
            case 1:
                return new CouponCommodityService();
            // 可以在這裡添加其他 commodityType 的處理邏輯
            default:
                throw new RuntimeException("不存在此獎品");
        }
    }

    // 根據傳入的類返回對應的 ICommodity 實例
    public ICommodity getCommodityService(Class<? extends ICommodity> clazz) throws IllegalArgumentException, InstantiationException {
        if (clazz == null) return null;
        try {
            return clazz.getDeclaredConstructor().newInstance(); // newInstance 方法不使用單例模式。它每次調用都會創建一個新的對象實例
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InstantiationException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try{
            StoreFactory.testStoreFactory_01();
            StoreFactory.testStoreFactory_02();
        } catch (Exception e) {
            System.out.println("失敗");
            System.out.println(e.getMessage());
        }

    }

    /**
     * @Test
     */
    public static void testStoreFactory_01() throws Exception {
        StoreFactory storeFactory = new StoreFactory();
        
        ICommodity commodityService_1 = storeFactory.getCommodityService(1);
        commodityService_1.sendCommodity("10001", "E601", "788", null);
    }

    public static void testStoreFactory_02() throws Exception {
        StoreFactory storeFactory = new StoreFactory();
        
        ICommodity commodityService_1 = storeFactory.getCommodityService(CouponCommodityService.class);
        commodityService_1.sendCommodity("10001", "E601", "788", null);
    }

}


