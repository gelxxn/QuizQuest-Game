package Foundations;
import javax.swing.*;
import java.awt.Color;

public abstract class GameContainer {

    protected JFrame frame;

    protected GameContainer() {
        //Foundation Layout settings.
        frame = new JFrame();
        frame.setTitle("Quiz Quest");
        frame.setSize(520, 750);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(235, 218, 192));

        //Icon
        ImageIcon icon = new ImageIcon("pics/icon.png");
        frame.setIconImage(icon.getImage());

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}