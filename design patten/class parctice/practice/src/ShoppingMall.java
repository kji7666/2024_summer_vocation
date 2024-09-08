import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class ShoppingMall {

}

class ImageTextPanelExample {
    public static void main(String[] args) {
        // 創建主框架
        JFrame frame = new JFrame("Clickable Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // 創建主面板
        JPanel mainPanel = new JPanel(new GridLayout(3, 4));

        // 創建並添加包含圖像和文字的面板
        for (int i = 0; i < 12; i++) {
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // 添加圖像和文字
        JLabel label = new JLabel();
        label.setText(name);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);

        // 設置圖像
        ImageIcon icon = new ImageIcon("src\\images\\運動.png"); // 替換成你的圖像路徑
        label.setIcon(icon);

        panel.add(label);
        
        // 添加滑鼠點擊事件監聽器
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(frame, "Panel clicked!");
            }
        });

            mainPanel.add(panel);
        }

        // 添加主面板到框架
        frame.add(mainPanel);

        // 設置框架可見
        frame.setVisible(true);
    }
}


class GUI {
    Map<String, Product> productMap;
    Cart cart; //
}

class Product {
    private String ID;
    private String icon; // 商品圖像位址
    private String name; // 商品名
    private String price; // 價格
    private String description; // 描述
    private String discount; // 折扣
    public Product(String ID, String icon, String name, String price, String description, String discount) {
        // init
    }
    // getter
}
