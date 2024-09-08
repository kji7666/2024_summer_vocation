import java.util.*;
import java.util.logging.Logger;
/**
 * 需求 : 有老師與同學的資料可以調用, 但調用者所關心的內容可能不同
 * 考量 : 家長關心學生排名, 校長關心班級升學率
 * 練習 : 訪問者模式
 * 功能 : 訂定訪問者調用資料時, 資料會顯示哪些東西
 * code : 
 * abstract Element {
 *      data field
 *      abstract accept(Visitor); // 聲明一個接受訪問者的方法
 * }
 * class ElementA extends Element {
 *      accept(Visitor){
 *          visitor.visit; // 由Visitor實作調用操作
 *      }
 * }
 * 
 * interface Visitor {
 *      visit(ElementA); // 對於每個具體元素類，都聲明訪問方法。
 * }
 * class VisitorA implements Visitor {
 *      visit(ElementA) {
 *          elementA.getter // 調用元素類getter實作訪問操作
 *      }
 * }
 * 
 * class ObjectStructure { 
 *      List<Element> 
 *      constructor { // 初始化資料
 *          list.add(new ElementA(...));
 *      }
 *      show(Visitor) {
 *          for(element : list) element.accept(visitor);
 *      }
 * }
 */
abstract class User { // 元素接口（Element)
    public String name;
    public String identity;
    public String clazz;
    public User(String name, String identity, String clazz) {
        this.name = name;
        this.identity = identity;
        this.clazz = clazz;
    }
    public abstract void accept(Visitor visitor); // 聲明了一個接受訪問者的方法

}

class Teacher extends User { // 具體元素類（ConcreteElement）
    public Teacher(String name, String identity, String clazz) {
        super(name, identity, clazz);
    }
    public void accept(Visitor visitor) { // 實現元素接口中的接受方法，並在方法內調用訪問者的方法。
        visitor.visit(this);
    }
    public double entranceRatio() { // 其他method
        return 0.7; // 假設升學率0.7
    }
}

class Student extends User { // 具體元素類（ConcreteElement）
    public Student(String name, String identity, String clazz) {
        super(name, identity, clazz);
    }
    public void accept(Visitor visitor) { // 實現元素接口中的接受方法，並在方法內調用訪問者的方法。
        visitor.visit(this);
    }
    public int ranking() {
        return (int) (Math.random() * 100); // 回傳隨機排名
    }
}

public interface Visitor { // 訪問者接口（Visitor）：聲明訪問方法，用於每個具體元素類。
    void visit(Teacher teacher); 
    void visit(Student student);
}

class Principal implements Visitor { // 具體訪問者類（ConcreteVisitor）：實現訪問者接口中的方法，定義了對每個具體元素類的操作。
    private Logger logger = Logger.getLogger(Principal.class.getName());
    public void visit(Student student) {
        logger.info("學生姓名 : " + student.name + ",班級 : " + student.clazz + ", 人數 : ");
    }
    public void visit(Teacher teacher) {
        logger.info("老師姓名 : " + teacher.name + ", 班級 : " + teacher.clazz + ", 升學率" + teacher.entranceRatio());
    }
}

class Parent implements Visitor {
    private Logger logger = Logger.getLogger(Principal.class.getName());
    public void visit(Student student) {
        logger.info("學生姓名 : " + student.name + ",班級 : " + student.clazz + ", 排名 : " + student.ranking());
    }
    public void visit(Teacher teacher) {
        logger.info("老師姓名 : " + teacher.name + ", 班級 : " + teacher.clazz + ", 級別" + teacher.identity);
    }
}

class DataView { // 對象結構（ObjectStructure）：這是一個能枚舉它的元素，可以提供高層的接口來允許訪問者訪問它的元素。
    List<User> userList = new ArrayList<>();
    public DataView() {
        userList.add(new Student("Amy", "普通班", "101班"));
        userList.add(new Student("Tom", "資優班", "102班"));
        userList.add(new Teacher("Mr.A", "班導", "103班"));
        userList.add(new Teacher("Mr.B", "實習教師", "104班"));
    }
    public void show(Visitor visitor) {
        for(User user : userList) {
            user.accept(visitor);
        }
    }
}

class VisitorPatternDemo {
    public static void main(String[] args) {
        DataView dataView = new DataView();

        // 創建訪問者
        Visitor principal = new Principal();
        Visitor parent = new Parent();

        // 使用訪問者訪問元素
        System.out.println("校長訪問：");
        dataView.show(principal);

        System.out.println("\n家長訪問：");
        dataView.show(parent);
    }
}