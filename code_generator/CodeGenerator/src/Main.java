public class Main {
    public static void main(String[] args) {
        ProductChanger.clear("src\\infoInput.txt");
        ProductChanger.clear("src\\infoOuput.txt");
        ProductChanger.clear("src\\productOuptut.txt");
        String id = InfoGetter.infoSplit("src\\all_input.txt", "src\\infoInput.txt");
        InfoChanger ic = new InfoChanger();
        ic.inputParser("src\\infoInput.txt");
        ic.infoChanger("src\\infoCode.txt");

        ProductChanger pc = new ProductChanger();
        pc.inputParser(id);
    }
}
