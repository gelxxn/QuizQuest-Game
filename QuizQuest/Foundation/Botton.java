package Foundations;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Botton {
    JButton button;

    public Botton() {
        button  = new JButton();
        button.setFont(new Font("Poppins", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(420,100));
        button.setBackground(new Color(221,209,200));
        button.setBounds(0, 0, 180, 80);
    }
}