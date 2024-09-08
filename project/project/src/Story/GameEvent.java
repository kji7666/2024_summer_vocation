package Story;

public interface GameEvent {
    void startEvent(StoryGui gui);
    void endEvent(StoryGui gui);
    int getResult();
}
