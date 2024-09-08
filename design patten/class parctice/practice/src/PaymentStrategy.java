import java.util.*;
/**
 * 需求 : 實作支付系統, 使其可以應對各種支付方法
 * 考量 : 支付方法會不斷更新
 * 練習 : 策略模式
 * 功能 : 抽離支付策略, 使其可各自實作
 * code :
 * interface Strategy { // 抽象策略
 *      method
 * }
 * class StrategyA implements Strategy {
 *      method // 各自實作策略
 * }
 * class StrategyB implements Strategy {
 *      method // 各自實作策略
 * }
 * class Context { // 客户端通过上下文来和策略对象交互
 *      Strategy
 *      method {this.strategy.method;}
 * }
 * test {
 *      context.method(new StrategyA());
 * }
 */
// 支付策略接口，所有具体策略都实现该接口
public interface PaymentStrategy {
    void pay(int amount);
}

// 具体策略：信用卡支付
class CreditCardPayment implements PaymentStrategy {
    private String name;
    private String cardNumber;
    private String cvv;
    private String dateOfExpiry;

    public CreditCardPayment(String name, String cardNumber, String cvv, String dateOfExpiry) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dateOfExpiry = dateOfExpiry;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid with credit card");
    }
}

// 具体策略：PayPal支付
class PayPalPayment implements PaymentStrategy {
    private String emailId;
    private String password;

    public PayPalPayment(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    @Override
    public void pay(int amount) {
        System.out.println(amount + " paid using PayPal");
    }
}

// 购物车类（上下文），使用支付策略来完成支付
class ShoppingCart {
    private List<Item> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public int calculateTotal() {
        int sum = 0;
        for (Item item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    public void pay(PaymentStrategy paymentStrategy) {
        int amount = calculateTotal();
        paymentStrategy.pay(amount);
    }
}

// 商品类
class Item {
    private String upcCode;
    private int price;

    public Item(String upcCode, int price) {
        this.upcCode = upcCode;
        this.price = price;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public int getPrice() {
        return price;
    }
}

// 测试策略模式的主类
class StrategyPatternDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        Item item1 = new Item("1234", 100);
        Item item2 = new Item("5678", 300);

        cart.addItem(item1);
        cart.addItem(item2);

        // 使用信用卡支付
        cart.pay(new CreditCardPayment("Pankaj Kumar", "1234567890123456", "786", "12/15"));

        // 使用PayPal支付
        cart.pay(new PayPalPayment("myemail@example.com", "mypwd"));
    }
}
