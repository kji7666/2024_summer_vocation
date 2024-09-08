import java.util.*;
/**
 * 需求 : 設計一個音樂撥放器, 音樂撥放器會有各個階段, 例如播放中, 暫停等
 * 考量 : 音樂撥放器會有各個階段, 依需求可能會增加
 * 練習 : 狀態模式
 * 功能 : 它允許對象在其內部"狀態改變時"改變其行為(method實現)，使對象看起來好像修改了其類型。
 * 這個模式主要用來解決對象在多種狀態之間轉換時，如何管理這些狀態及其行為。
 * code : 
 * class Target {
 *      fields // 本身的fields
 *      State // 當前state
 *      changeState(state) {this.state = state} // 轉state
 *      stateMethods {do state.stateMethods} // 在各state下這些特定method實現可能會不同, 其中也會包刮轉state的method
 *      normalMethods // 各state下實現都相同的method
 * }
 * abstract class State {
 *      Target
 *      abstract stateMethods 
 * }
 * class State1 extends State {
 *      stateMethods
 * }
 * class State2 extends State {
 *      stateMethods
 * }
 */
// 音频播放器（AudioPlayer）类即为上下文。它维护指向状态类实例的引用，该状态类则用于表示音频播放器当前的状态。
public class AudioPlayer {
    private State state; // 当前状态
    public UserInterface UI; // 用户界面
    public int volume;
    public List<String> playlist;
    public String currentSong;
    public boolean playing; // 是否正在播放

    public AudioPlayer() {
        this.state = new ReadyState(this); // 初始状态为ReadyState
        this.playlist = new ArrayList<>();
        this.currentSong = "";
        this.playing = false;

        // 上下文会将处理用户输入的工作委派给状态对象。由于每个状态都以不同的方式处理输入，其结果自然将依赖于当前所处的状态。
        UI = new UserInterface();
        UI.lockButton.onClick(this::clickLock);
        UI.playButton.onClick(this::clickPlay);
        UI.nextButton.onClick(this::clickNext);
        UI.prevButton.onClick(this::clickPrevious);
    }

    // 其他对象必须能切换音频播放器当前所处的状态。
    public void changeState(State state) {
        this.state = state;
    }

    // UI 方法会将执行工作委派给当前状态。
    public void clickLock() {
        state.clickLock();
    }

    public void clickPlay() {
        state.clickPlay();
    }

    public void clickNext() {
        state.clickNext();
    }

    public void clickPrevious() {
        state.clickPrevious();
    }

    // 状态可调用上下文的一些服务方法。
    public void startPlayback() {
        System.out.println("Starting playback");
        playing = true;
    }

    public void stopPlayback() {
        System.out.println("Stopping playback");
        playing = false;
    }

    public void nextSong() {
        System.out.println("Next song");
    }

    public void previousSong() {
        System.out.println("Previous song");
    }

    public void fastForward(int time) {
        System.out.println("Fast forwarding " + time + " seconds");
    }

    public void rewind(int time) {
        System.out.println("Rewinding " + time + " seconds");
    }
}

// 状态基类
abstract class State {
    protected AudioPlayer player;

    // 上下文将自身传递给状态构造函数。这可帮助状态在需要时获取一些有用的上下文数据。
    public State(AudioPlayer player) {
        this.player = player;
    }

    public abstract void clickLock();
    public abstract void clickPlay();
    public abstract void clickNext();
    public abstract void clickPrevious();
}

// 具体状态：LockedState
class LockedState extends State {
    public LockedState(AudioPlayer player) {
        super(player);
    }

    @Override
    public void clickLock() {
        if (player.playing) {
            player.changeState(new PlayingState(player));
        } else {
            player.changeState(new ReadyState(player));
        }
    }

    @Override
    public void clickPlay() {
        // 已锁定，什么也不做。
    }

    @Override
    public void clickNext() {
        // 已锁定，什么也不做。
    }

    @Override
    public void clickPrevious() {
        // 已锁定，什么也不做。
    }
}

// 具体状态：ReadyState
class ReadyState extends State {
    public ReadyState(AudioPlayer player) {
        super(player);
    }

    @Override
    public void clickLock() {
        player.changeState(new LockedState(player));
    }

    @Override
    public void clickPlay() {
        player.startPlayback();
        player.changeState(new PlayingState(player));
    }

    @Override
    public void clickNext() {
        player.nextSong();
    }

    @Override
    public void clickPrevious() {
        player.previousSong();
    }
}

// 具体状态：PlayingState
class PlayingState extends State {
    public PlayingState(AudioPlayer player) {
        super(player);
    }

    @Override
    public void clickLock() {
        player.changeState(new LockedState(player));
    }

    @Override
    public void clickPlay() {
        player.stopPlayback();
        player.changeState(new ReadyState(player));
    }

    @Override
    public void clickNext() {
        if (eventIsDoubleClick()) {
            player.nextSong();
        } else {
            player.fastForward(5);
        }
    }

    @Override
    public void clickPrevious() {
        if (eventIsDoubleClick()) {
            player.previousSong();
        } else {
            player.rewind(5);
        }
    }

    private boolean eventIsDoubleClick() {
        // 假設這裡有雙擊事件的邏輯
        return false;
    }
}

// 用户界面类，用于模拟UI按钮和事件绑定
class UserInterface {
    public Button lockButton;
    public Button playButton;
    public Button nextButton;
    public Button prevButton;

    public UserInterface() {
        this.lockButton = new Button();
        this.playButton = new Button();
        this.nextButton = new Button();
        this.prevButton = new Button();
    }

    // 模擬Button類
    class Button {
        private Runnable onClick;

        public void onClick(Runnable onClick) {
            this.onClick = onClick;
        }

        public void click() {
            if (onClick != null) {
                onClick.run();
            }
        }
    }
}

// 測試類，用於測試狀態模式
class StatePatternDemo {
    public static void main(String[] args) {
        AudioPlayer player = new AudioPlayer();

        player.clickPlay(); // 開始播放
        player.clickNext(); // 下一首歌
        player.clickLock(); // 鎖定
        player.clickPlay(); // 嘗試播放（被鎖定，無反應）
        player.clickLock(); // 解鎖
        player.clickPlay(); // 停止播放
    }
}
