
package Foundations.HANOI;
public class Hanoi {
 
    public Hanoi() {
        int lastLevel = HanoiSave.loadLevel();
        HanoiFrame hanoiFrame = new HanoiFrame(lastLevel);
        hanoiFrame.setLocationRelativeTo(null);
        hanoiFrame.setVisible(true);
    }
}