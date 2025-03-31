package Foundations.PUZZLE;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Original {
    String path;
    JLabel org;

    public Original(int p) {
        org = new JLabel(new ImageIcon(getClass().getResource("/Foundations/pics/Resize/" + p + ".jpg")));
    }

    public JLabel getOriginal() {
        return org;
    }
}