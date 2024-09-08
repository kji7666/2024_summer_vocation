package Tool;

public class EventParser {
    // 格式 : [eventType]{eventParameter1}{eventParameter2}{...}
    public static void main(String[] args) {
        String line = "[2]{???}{嘿嘿, 抓到你了}";
        System.out.println("Event Type: " + getEventType(line));
        String[] parameters = getEventParameter(line);
        for (String param : parameters) {
            System.out.println("Event Parameter: " + param);
        }
    }

    public static int getEventType(String line) {
        line = line.trim();
        // 提取方括號內的數字
        return Integer.parseInt(line.substring(line.indexOf("[") + 1, line.indexOf("]")));
    }

    public static String[] getEventParameter(String line) {
        line = line.trim();
        // 提取所有花括號內的部分
        String parametersPart = line.substring(line.indexOf("{") + 1, line.lastIndexOf("}"));
        // 去掉開頭的花括號，並分割每個參數
        return parametersPart.split("\\}\\{");
    }
}
