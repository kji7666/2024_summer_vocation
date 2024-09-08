/**
 * 需求 : 實作各種AI模型, 使其有不同趨勢
 * 考量 : 這幾種AI大致相同, 卻因為演算法的差異而有不同的效果
 * 練習 : 模板模式
 * 功能 : 抽象class定義AI算法的框架, 子類可以在不改變算法結構的情況下重新定義算法的步驟
 * code :
 * abstract class {
 *      templateMethod // 定義methods順序(框架), 模板方法不限一個
 *      methods // 一些可以在abstract class實作
 *      abstract class // 一些可以交付子類實作
 * }
 * class extends abstract class {
 *      Override methods // 可以覆寫或實作methods
 * }
 * 
 * test {
 *      abstract class = new class
 *      class.templateMethod()
 * }
 */

// 抽象类 GameAI 定义了一个模板方法 turn 和一些抽象方法
public abstract class GameAI {
    // 模板方法定义了算法的框架。它是由一些抽象方法和具体方法组成的。
    public final void turn() {
        collectResources();
        buildStructures();
        buildUnits();
        attack();
    }

    // 具体方法 collectResources，收集资源
    // 一些步骤可以在基类中直接实现
    public void collectResources() {
        // 遍历已建造的建筑，收集资源
        System.out.println("Collecting resources...");
    }

    // 抽象方法 buildStructures，建造建筑
    // 具体子类需要实现这些抽象方法
    protected abstract void buildStructures();

    // 抽象方法 buildUnits，建造单位
    protected abstract void buildUnits();

    // 具体方法 attack，进行攻击
    // 一个类可以包含多个模板方法
    public void attack() {
        String enemy = closestEnemy();
        if (enemy == null) {
            sendScouts("map center");
        } else {
            sendWarriors(enemy);
        }
    }

    // 抽象方法 sendScouts，派遣侦查部队
    protected abstract void sendScouts(String position);

    // 抽象方法 sendWarriors，派遣战斗部队
    protected abstract void sendWarriors(String position);

    // 具体方法 closestEnemy，找到最近的敌人
    private String closestEnemy() {
        // 简单的示例逻辑，返回最近的敌人位置
        return "enemy base";
    }
}

// 具体类 OrcsAI 实现了基类中的所有抽象方法
class OrcsAI extends GameAI {
    @Override
    protected void buildStructures() {
        // 建造农场，接着是谷仓，然后是要塞
        System.out.println("Orcs build farm, barn, and fortress.");
    }

    @Override
    protected void buildUnits() {
        // 如果有足够资源，建造单位
        System.out.println("Orcs build units.");
    }

    @Override
    protected void sendScouts(String position) {
        // 将侦查编组送到指定位置
        System.out.println("Orcs send scouts to " + position);
    }

    @Override
    protected void sendWarriors(String position) {
        // 将战斗编组送到指定位置
        System.out.println("Orcs send warriors to " + position);
    }
}

// 具体类 MonstersAI 实现了基类中的所有抽象方法
// 子类可以重写部分默认的操作
class MonstersAI extends GameAI {
    @Override
    public void collectResources() {
        // 怪物不会采集资源
        System.out.println("Monsters do not collect resources.");
    }

    @Override
    protected void buildStructures() {
        // 怪物不会建造建筑
        System.out.println("Monsters do not build structures.");
    }

    @Override
    protected void buildUnits() {
        // 怪物不会建造单位
        System.out.println("Monsters do not build units.");
    }

    @Override
    protected void sendScouts(String position) {
        // 怪物不会派遣侦查部队
        System.out.println("Monsters do not send scouts.");
    }

    @Override
    protected void sendWarriors(String position) {
        // 将战斗编组送到指定位置
        System.out.println("Monsters send warriors to " + position);
    }
}

// 主类，用于测试模板方法模式
class TemplateMethodPatternExample {
    public static void main(String[] args) {
        GameAI orcsAI = new OrcsAI();
        GameAI monstersAI = new MonstersAI();

        System.out.println("Orcs AI turn:");
        orcsAI.turn();

        System.out.println("\nMonsters AI turn:");
        monstersAI.turn();
    }
}