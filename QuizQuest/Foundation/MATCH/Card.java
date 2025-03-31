package Foundations.MATCH;

import javax.swing.*;

public class Card extends JButton {
    private int value;
    private boolean matched = false;

    public Card(int value) {
        this.value = value;
        this.setText(""); // เริ่มต้นให้ไพ่ปิดไว้
        GameStyle.applyCardStyle(this); // ใช้สไตล์จาก GameStyle
    }

    public int getValue() {
        return value;
    }
    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
        if (matched) {
            this.setBackground(GameStyle.MATCHED_COLOR);
        }
    }

    public void showFront() {
        ImageIcon bg = new ImageIcon(getClass().getResource("/Foundations/pics/machingpic/"+ value +".png"));
        this.setIcon(bg);
    }

    public void showBack() {
        if (!matched) {
           this.setText("");
           this.setIcon(null);
        }
    }
}