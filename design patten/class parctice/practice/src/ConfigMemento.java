import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 需求 : 編輯軟體需要返回功能
 * 考量 : 不能破壞原有的封裝性
 * 練習 : 備忘錄模式
 * 功能 : 備忘錄模式不破壞封裝性，對象的內部狀態不會暴露給外部。可以很方便地保存和恢復對象的狀態。
 * code :
 * class File // 單位返回狀態
 * class Memento { // 單位備忘錄
 *      File // 儲存一單位返回狀態
 *      setter, getter
 * }
 * class Originator { // 發起人, 負責當前資料
 *      file // 當前處理資料
 *      setter, getter
 *      Memento saveMemento() { // 生成當前資料備忘錄
 *          return new Memento(file);
 *      }
 *      void getMemento(Memento) { // 將當前資料改為備忘錄資料
 *          file = memento.getfile()
 *      }
 * }
 * 
 * Caretaker { // 資料儲存者
 *      List<Memento>
 *      int topIndex // 因為有上一步或下一步的可能, 用list + index取代stack
 *      Map<String, Memento> // 直接恢復至指定步
 *      append // 將備忘錄加入儲存
 *      undo, redo // 上一步或下一步
 *      getter
 * }
 */
// 備忘錄類：負責存儲發起人的內部狀態
public class ConfigMemento {
    private ConfigFile configFile;

    // 構造方法
    public ConfigMemento(ConfigFile configFile) {
        this.configFile = configFile;
    }

    // 獲取保存的配置文件
    public ConfigFile getConfigFile() {
        return configFile;
    }

    // 設置配置文件
    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }
}

// 配置文件類：代表發起人內部狀態的具體內容
class ConfigFile {
    private String versionNo;  // 版本號
    private String content;    // 配置內容
    private Date dateTime;     // 配置時間
    private String operator;   // 操作員

    // 构造函数
    public ConfigFile(String versionNo, String content, Date dateTime, String operator) {
        this.versionNo = versionNo;
        this.content = content;
        this.dateTime = dateTime;
        this.operator = operator;
    }

    // Getters and Setters
    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}

// 發起人類：負責創建和恢復備忘錄
class ConfigOriginator {
    private ConfigFile configFile;

    // 獲取當前配置文件
    public ConfigFile getConfigFile() {
        return configFile;
    }

    // 設置配置文件
    public void setConfigFile(ConfigFile configFile) {
        this.configFile = configFile;
    }

    // 保存當前配置文件到備忘錄
    public ConfigMemento saveMemento() {
        return new ConfigMemento(configFile);
    }

    // 從備忘錄恢復配置文件
    public void getMemento(ConfigMemento memento) {
        this.configFile = memento.getConfigFile();
    }
}

// 管理者類：負責管理備忘錄的保存和恢復
class Admin {
    private int cursorIdx = 0;
    private List<ConfigMemento> mementoList = new ArrayList<>();
    private Map<String, ConfigMemento> mementoMap = new ConcurrentHashMap<>();

    // 添加新的備忘錄
    public void append(ConfigMemento memento) {
        mementoList.add(memento);
        mementoMap.put(memento.getConfigFile().getVersionNo(), memento);
        cursorIdx++;
    }

    // 撤銷操作，恢復到上一個備忘錄
    public ConfigMemento undo() {
        if (--cursorIdx <= 0) {
            cursorIdx = 0;
            return mementoList.get(0);
        }
        return mementoList.get(cursorIdx);
    }

    // 重做操作，恢復到下一個備忘錄
    public ConfigMemento redo() {
        if (++cursorIdx >= mementoList.size()) {
            cursorIdx = mementoList.size() - 1;
            return mementoList.get(cursorIdx);
        }
        return mementoList.get(cursorIdx);
    }

    // 根據版本號獲取對應的備忘錄
    public ConfigMemento get(String versionNo) {
        return mementoMap.get(versionNo);
    }
}

// 測試類
class MementoPatternDemo {
    public static void main(String[] args) {
        ConfigOriginator originator = new ConfigOriginator();
        Admin admin = new Admin();

        // 創建一些配置文件並保存到備忘錄
        originator.setConfigFile(new ConfigFile("1.0", "Initial Configuration", new Date(), "Alice"));
        admin.append(originator.saveMemento());

        originator.setConfigFile(new ConfigFile("1.1", "Added Feature A", new Date(), "Bob"));
        admin.append(originator.saveMemento());

        originator.setConfigFile(new ConfigFile("1.2", "Fixed Bug B", new Date(), "Charlie"));
        admin.append(originator.saveMemento());

        // 撤銷到上一個版本
        originator.getMemento(admin.undo());
        System.out.println("Current Version: " + originator.getConfigFile().getVersionNo());

        // 重做到下一個版本
        originator.getMemento(admin.redo());
        System.out.println("Current Version: " + originator.getConfigFile().getVersionNo());

        // 根據版本號獲取對應的配置
        ConfigMemento memento = admin.get("1.1");
        originator.getMemento(memento);
        System.out.println("Restored to Version: " + originator.getConfigFile().getVersionNo());
    }
}
