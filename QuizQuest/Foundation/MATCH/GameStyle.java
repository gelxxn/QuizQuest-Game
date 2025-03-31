package Foundations.MATCH;
import javax.swing.*;
import java.awt.*;

public class GameStyle {
   public static final int WIDTH = 400;
   public static final int HEIGHT = 400;

   public static final Color BACKGROUND_COLOR = new Color(240, 240, 255);
   public static final Color MATCHED_COLOR = new Color(144, 238, 144);
   public static final Font CARD_FONT = new Font("Arial", Font.BOLD, 18);

   public static void applyCardStyle(JButton button) {
       button.setFont(CARD_FONT);
       button.setFocusPainted(false);
   }
}