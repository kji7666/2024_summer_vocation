import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InfoChanger {
    private String bigImg = null;
    private String smlImg = null;
    private String name;
    private String type;
    private List<String> cover;
    private List<String> insidePage;
    private List<String> advertise;
    private int state = 0;
    private final int COVER = 0;
    private final int INSIDE_PAGE = 1;
    private final int ADV = 2;
    private int coverSize = 0;
    private int insidePageSize = 0;
    private int advertiseSize = 0;

    public InfoChanger() {
        cover = new ArrayList<>();
        insidePage = new ArrayList<>();
        advertise = new ArrayList<>();
    }

    public void inputParser(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains("img src=\"")) {
                    if(bigImg == null) {
                        bigImg = line.substring(line.indexOf("img src=\"")+"img src=\"".length(), line.indexOf("\" width"));
                    } else {
                        smlImg = line.substring(line.indexOf("img src=\"")+"img src=\"".length(), line.indexOf("\" width"));
                    }
                } else if(line.contains("品名")) {
                    int nameStart = line.indexOf("<span >品名:</span>") + "<span >品名:</span>".length();
                    // int nameEnd = line.indexOf("-<strong>");
                    // if (nameStart != -1 && nameEnd != -1 && nameStart < nameEnd) {
                    //     name = line.substring(nameStart, nameEnd);
                    //     type = line.substring(nameEnd + "-<strong>".length(), line.indexOf("</strong>"));
                    // }
                    int nameEnd = line.indexOf("</span></td>");
                    if (nameStart != -1 && nameEnd != -1 && nameStart < nameEnd) {
                        String[] parts = line.substring(nameStart, nameEnd).split("-");
                        if(parts.length == 5) {
                            name = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3];
                            type = parts[4];
                        } else {
                            name = parts[0] + "-" + parts[1] + "-" + parts[2];
                            type = parts[3];
                        }
                    }
                } else if(line.contains("<strong>封面</strong>")) {
                    state = COVER;
                    System.out.println("change to cover");
                } else if(line.contains("<strong>內頁</strong>")) {
                    state = INSIDE_PAGE;
                } else if(line.contains("<span class=\"text\">廣告</span>")) {
                    state = ADV;
                } else if(state == COVER && line.contains("<td nowrap bgcolor=\"#FFFFFF\" class=\"text\">")) {
                    int start = line.indexOf("class=\"text\">") + "class=\"text\">".length();
                    int end = line.indexOf("</td>");
                    if (start != -1 && end != -1 && start < end) {
                        cover.add(line.substring(start, end));
                    }
                } else if(state == INSIDE_PAGE && line.contains("class=\"text\">")) {
                    int start = line.indexOf("class=\"text\">") + "class=\"text\">".length();
                    int end = line.indexOf("</");
                    if (start != -1 && end != -1 && start < end) {
                        insidePage.add(line.substring(start, end));
                        //System.out.println(line.substring(start, end));
                    } else {
                        //System.out.println(start + "/" + end);
                    }
                } else if(state == ADV && line.contains("<td bgcolor=\"#FFFFFF\"><span class=\"text\">")) {
                    int start = line.indexOf("class=\"text\">") + "class=\"text\">".length();
                    int end = line.indexOf("</span></td>");
                    if (start != -1 && end != -1 && start < end) {
                        advertise.add(line.substring(start, end));
                    }
                }
            }
            System.out.println(bigImg);
            System.out.println(smlImg);
            System.out.println(name);
            System.out.println(type);
            //System.out.println(cover.size());
            for(String c : cover) {
                System.out.println(c);
            }
            for(String i : insidePage) {
                System.out.println(i);
            }
            for(String a : advertise) {
                System.out.println(a);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                System.out.println("An error occurred while closing the file: " + ex.getMessage());
            }
        }
    }

    /**
     * 
     * @param path infoCode
     */
    public void infoChanger(String path) {
        BufferedReader reader = null;
        FileWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            // 使用附加模式（true）打开文件

            writer = new FileWriter("src\\infoOuput.txt", true); // true 代表附加模式

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("[name]", name)
                .replace("[type]", type)
                .replace("[bigImg]", bigImg)
                .replace("[smlImg]", smlImg);
                if(line.contains("[cover]")) {
                    line = line.replace("[cover]", cover.get(coverSize++));
                } else if(line.contains("[insidePage]")) {
                    line = line.replace("[insidePage]", insidePage.get(insidePageSize++));
                } else if(line.contains("[advertise]")) {
                    line = line.replace("[advertise]", advertise.get(advertiseSize++));
                }
                writer.append(line).append(System.lineSeparator()); // 确保行与行之间有换行
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close(); // 确保 FileWriter 也被关闭
                }
            } catch (IOException ex) {
                System.out.println("An error occurred while closing the file: " + ex.getMessage());
            }
        }
    }
}
