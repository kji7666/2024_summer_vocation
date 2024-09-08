package Story;


import java.util.List;
import java.util.NoSuchElementException;

import Tool.EventParser;
import Tool.LineParser;

import java.util.ArrayList;


public class Process {
    private List<GameEvent> eventList;
    private int eventNumber;

    public Process(String fileName) {
        System.out.println("File path: " + fileName);
        eventNumber = 0;
        eventList = new ArrayList<>();

        List<String> events = LineParser.readFile(fileName);
        for(String event : events) {
            int eventType = EventParser.getEventType(event);
            String[] eventParameter = EventParser.getEventParameter(event);
            System.out.println("Event Type: " + EventParser.getEventType(event));
            String[] parameters = EventParser.getEventParameter(event);
            for (String param : parameters) {
                System.out.println("Event Parameter: " + param);
            }
            switch(eventType) {
                case 0 : eventList.add(new BackgroundImageChange(eventParameter[0])); break; // 背景圖片轉換
                case 1 : eventList.add(new CharacterImageChange(eventParameter[0])); break; // 腳色圖片轉換
                case 2 : eventList.add(new CharacterDialogue(eventParameter[0], eventParameter[1])); break;// 腳色對話框
                case 3 : eventList.add(new PlayerDialogue(eventParameter)); break; // 玩家對話框
            }
        }
    }

    // 獲取事件列表
    public List<GameEvent> getEventList() {
        return eventList;
    }

    // 獲取下一個事件
    public GameEvent getNextEvent() {
        if (eventNumber < eventList.size()) {
            return eventList.get(eventNumber++);
        } else {
            throw new NoSuchElementException("No more events.");
        }
    }

    // 重置事件指標，允許重新開始事件處理
    public void resetEvents() {
        eventNumber = 0;
    }

    // 添加新事件到事件列表
    public void addEvent(GameEvent event) {
        eventList.add(event);
    }

    // 移除指定索引的事件
    public void removeEvent(int index) {
        if (index >= 0 && index < eventList.size()) {
            eventList.remove(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid event index.");
        }
    }

    // 獲取當前事件索引
    public int getCurrentEventIndex() {
        return eventNumber;
    }
}
