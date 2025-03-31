package Foundations;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.*;

public class FrameSetter extends JFrame {
    private JLabel background;
    public FrameSetter() {
        //Foundation Layout settings.
        setTitle("Quiz Quest");
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Icon
        ImageIcon icon = new ImageIcon(getClass().getResource("pics/icon.png"));
        setIconImage(icon.getImage());

        //Background
        ImageIcon bg = new ImageIcon(getClass().getResource("/Foundations/pics/backGround/BG1.jpg"));
        background = new JLabel(new ImageIcon(bg.getImage()
        .getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, 
                           Toolkit.getDefaultToolkit().getScreenSize().height, 
                           Image.SCALE_SMOOTH)));
        setContentPane(background);
        setLayout(new BorderLayout());
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}