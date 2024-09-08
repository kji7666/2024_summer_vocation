import java.util.*;

/**
 * 需求 : 由大量選擇題, 問答題組成考卷
 * 考量 : 可能會調轉題目順序或新增題目
 * 練習 : 原型模式
 * 功能 : 通過複製已有的對象來創建新對象，避免了昂貴的對象創建過程, 可以在運行時動態地增加或修改對象的屬性，而無需改變其類定義，這樣可以提高系統的靈活性。
            在考題庫例子中，使用原型模式可以輕鬆地克隆整個考題庫，並隨機打亂題目和選項的順序，而無需重新創建和初始化每個考題。
 * code : 
 * class BankController { 
 *      Bank bank
 *      constructor {
 *          new Bank // 以append控制複製對象內容
 *          Bank.append(...)
 *      }
 *      createObject {
 *          bankClone = bank.clone() // 調用自訂的clone
 *          return 目的Object
 *      }
 * }
 * class Bank implements Cloneable {
 *      public append // 提供method協助定義複製對象內容
 *      public clone // 定義複製方式(對題庫進行操作並生成新考卷, 細節於TopicOperator method定義)
 * }
 * class TopicOperator // 對單一對象進行操作並生成新result
 * class Topic // result
 */

// 工具類，負責隨機打亂選項並返回包含打亂選項和答案的Topic對象
class TopicRandomUtil {
    public static Topic random(Map<String, String> option, String ans) {
        // 將選項列表轉換為可修改的列表並打亂順序
        List<Map.Entry<String, String>> entryList = new ArrayList<>(option.entrySet());
        Collections.shuffle(entryList);

        // 將打亂後的選項存入新Map中
        Map<String, String> shuffledOption = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : entryList) {
            shuffledOption.put(entry.getKey(), entry.getValue());
        }

        // 返回新的Topic對象，包含打亂的選項和原答案
        return new Topic(shuffledOption, ans);
    }
}

// Topic類，包含選項和答案
class Topic {
    private Map<String, String> option;
    private String ans;

    public Topic(Map<String, String> option, String ans) {
        this.option = option;
        this.ans = ans;
    }

    public Map<String, String> getOption() {
        return option;
    }

    public void setOption(Map<String, String> option) {
        this.option = option;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}

// 主控類，負責創建考題庫並克隆
public class QuestionBankController {
    private QuestionBank questionBank = new QuestionBank();
    public QuestionBankController() {
        // 創建考題庫對象
        QuestionBank questionBank = new QuestionBank();
        questionBank.append(new ChoiceQuestion("1 + 1 = ?", Map.of("A", "1", "B", "2", "C", "3", "D", "4"), "B"))
                    .append(new AnswerQuestion("簡述Java的四大特性", "封裝、繼承、多態、抽象"));


    }

    public String createPaper(String candidate, String number) throws CloneNotSupportedException {
        QuestionBank questionBankClone = (QuestionBank) questionBank.clone();
        questionBankClone.setCandidate(candidate);
        questionBankClone.setNumber(number);
        return questionBankClone.toString();
    }
}

// 考題庫類，包含選擇題和問答題的列表
class QuestionBank implements Cloneable {
    private String candidate;
    private String number;
    private List<ChoiceQuestion> choiceQuestionsList = new ArrayList<>();
    private List<AnswerQuestion> answerQuestionsList = new ArrayList<>();

    // 增加選擇題
    public QuestionBank append(ChoiceQuestion choiceQuestion) {
        choiceQuestionsList.add(choiceQuestion);
        return this;
    }

    // 增加問答題
    public QuestionBank append(AnswerQuestion answerQuestion) {
        answerQuestionsList.add(answerQuestion);
        return this;
    }

    // 克隆方法，實現深拷貝
    @Override
    public Object clone() throws CloneNotSupportedException {
        QuestionBank questionBank = (QuestionBank) super.clone();
        questionBank.choiceQuestionsList = new ArrayList<>(choiceQuestionsList.size());
        questionBank.answerQuestionsList = new ArrayList<>(answerQuestionsList.size());

        // 克隆選擇題列表
        for (ChoiceQuestion question : choiceQuestionsList) {
            questionBank.choiceQuestionsList.add(question.clone());
        }

        // 克隆問答題列表
        for (AnswerQuestion question : answerQuestionsList) {
            questionBank.answerQuestionsList.add(question.clone());
        }

        // 題目混排
        Collections.shuffle(questionBank.choiceQuestionsList);
        Collections.shuffle(questionBank.answerQuestionsList);

        // 選擇答案混排
        for (ChoiceQuestion question : questionBank.choiceQuestionsList) {
            Topic random = TopicRandomUtil.random(question.getOption(), question.getAns());
            question.setOption(random.getOption());
            question.setAns(random.getAns());
        }

        return questionBank;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "QuestionBank{" +
                "candidate='" + candidate + '\'' +
                ", number='" + number + '\'' +
                ", choiceQuestionsList=" + choiceQuestionsList +
                ", answerQuestionsList=" + answerQuestionsList +
                '}';
    }
}

// 選擇題類，包含問題、選項和答案
class ChoiceQuestion implements Cloneable {
    private String question;
    private Map<String, String> option;
    private String ans;

    public ChoiceQuestion(String question, Map<String, String> option, String ans) {
        this.question = question;
        this.option = option;
        this.ans = ans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getOption() {
        return option;
    }

    public void setOption(Map<String, String> option) {
        this.option = option;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    // 克隆方法，實現深拷貝
    @Override
    public ChoiceQuestion clone() throws CloneNotSupportedException {
        ChoiceQuestion cloned = (ChoiceQuestion) super.clone();
        cloned.option = new HashMap<>(this.option);
        return cloned;
    }

    @Override
    public String toString() {
        return "ChoiceQuestion{" +
                "question='" + question + '\'' +
                ", option=" + option +
                ", ans='" + ans + '\'' +
                '}';
    }
}

// 問答題類，包含問題和答案
class AnswerQuestion implements Cloneable {
    private String question;
    private String ans;

    public AnswerQuestion(String question, String ans) {
        this.question = question;
        this.ans = ans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    // 克隆方法，實現深拷貝
    @Override
    public AnswerQuestion clone() throws CloneNotSupportedException {
        return (AnswerQuestion) super.clone();
    }

    @Override
    public String toString() {
        return "AnswerQuestion{" +
                "question='" + question + '\'' +
                ", ans='" + ans + '\'' +
                '}';
    }
}
