import java.util.*;

/**
 * 需求 : 圖形繪製輸出
 * 考量 : 圖形class都必須有特定method
 * 練習 : 組合模式
 * 功能 : 使指定class都必須有特定method, 並使其有統一管理的class
 * code :
 * interface Type { // 所有implements 此interface 的class 都可以以此type 執行同method
 *      method 
 * }
 * class Leaf implements Type {
 *      method
 * }
 * class Child extends Leaf {
 *      method // child為leaf的擴增功能class, 一樣可以以Type 執行method
 * }
 * // implements 與extends的區別?
 * extends 關鍵字用於繼承一個類別，
 * 這樣子類就可以繼承父類的屬性和方法，並且可以根據需要覆寫或擴展這些方法。
 * 然而依照里氏替換原則, 如果不須繼承屬性(不會使用super), 用implements實現interface就好
 * class CompoundType {
 *      List<Type> // 用add, remove等method彙整需要執行目標method的class
 *      method // for(type : typeList) type.method 遍歷執行
 * }
 */

// 组件接口会声明组合中简单和复杂对象的通用操作。
public interface Graphic {
    void move(int x, int y);
    void draw();
}

// 叶节点类代表组合的终端对象。叶节点对象中不能包含任何子对象。叶节点对象
// 通常会完成实际的工作，组合对象则仅会将工作委派给自己的子部件。
class Dot implements Graphic {
    private int x, y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void draw() {
        // 在坐标位置(x, y)处绘制一个点。
        System.out.println("Drawing dot at (" + x + ", " + y + ")");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

// 所有组件类都可以扩展其他组件。
class Circle extends Dot {
    private int radius;

    public Circle(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
    }

    @Override
    public void draw() {
        // 在坐标位置(x, y)处绘制一个半径为radius的圆。
        System.out.println("Drawing circle at (" + getX() + ", " + getY() + ") with radius " + radius);
    }
}

// 组合类表示可能包含子项目的复杂组件。组合对象通常会将实际工作委派给子项
// 目，然后“汇总”结果。
class CompoundGraphic implements Graphic {
    private List<Graphic> children = new ArrayList<>();

    // 组合对象可在其项目列表中添加或移除其他组件（简单的或复杂的皆可）。
    public void add(Graphic child) {
        children.add(child);
    }

    public void remove(Graphic child) {
        children.remove(child);
    }

    @Override
    public void move(int x, int y) {
        for (Graphic child : children) {
            child.move(x, y);
        }
    }

    // 组合会以特定的方式执行其主要逻辑。它会递归遍历所有子项目，并收集和
    // 汇总其结果。由于组合的子项目也会将调用传递给自己的子项目，以此类推，
    // 最后组合将会完成整个对象树的遍历工作。
    @Override
    public void draw() {
        // 1. 对于每个子部件：
        //     - 绘制该部件。
        //     - 更新边框坐标。
        for (Graphic child : children) {
            child.draw();
        }
        // 2. 根据边框坐标绘制一个虚线长方形。
        System.out.println("Drawing bounding box for compound graphic.");
    }
}

// 客户端代码会通过基础接口与所有组件进行交互。这样一来，客户端代码便可同
// 时支持简单叶节点组件和复杂组件。
class ImageEditor {
    private CompoundGraphic all = new CompoundGraphic();

    public void load() {
        all.add(new Dot(1, 2));
        all.add(new Circle(5, 3, 10));
        // ……
    }

    // 将所需组件组合为复杂的组合组件。
    public void groupSelected(List<Graphic> components) {
        CompoundGraphic group = new CompoundGraphic();
        for (Graphic component : components) {
            group.add(component);
            all.remove(component);
        }
        all.add(group);
        // 所有组件都将被绘制。
        all.draw();
    }
}
