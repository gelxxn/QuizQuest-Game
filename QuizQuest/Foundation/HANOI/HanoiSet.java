package Foundations.HANOI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import Foundations.SoundPlayer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class HanoiSet extends JPanel {
    private int Num_DISCS;
    private int NUM_RODS;
    private Stack<Integer>[]rods;
    private final Stack<Integer>sourceRodHistory = new Stack<>();
    private final Stack<Integer>targetRodHistory = new Stack<>();
    private final Stack<Integer>undoneSourceRodHistory = new Stack<>();
    private final Stack<Integer>undoneTargetRodHiatory = new Stack<>();
    private Runnable onWinListener;

    private JPanel rodsButtonPanel;

    private int draggedDisc = -1;
    private int draggedRod = -1;

    public HanoiSet(int numDiscs, int numRods){
        Num_DISCS = numDiscs;
        NUM_RODS = numRods;
        setLayout(new BorderLayout());
        initializeRods();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int rodIndex = getRodAtPosition(e.getX());
                if (!rods[rodIndex].isEmpty()) {
                    draggedRod = rodIndex;
                    draggedDisc = rods[rodIndex].peek();
                    SoundPlayer.playSound("unlock.wav");
                }
            }
            @Override
                public void mouseReleased(MouseEvent e) {
                    if (draggedDisc != -1) {
                        SoundPlayer.playSound("popSoap.wav");
                        int targetRod = getRodAtPosition(e.getX());
                        moveDisc(draggedRod, targetRod);
                        draggedDisc = -1;
                        draggedRod = -1;
                        repaint();
                    }
                }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });

        rodsButtonPanel = new JPanel();
        updateMoveButtonPanel();
        rodsButtonPanel.setPreferredSize(new Dimension(getWidth(), 30));
        add(rodsButtonPanel, BorderLayout.SOUTH);
        
    }

    private int getRodAtPosition(int x) {
        int rodWidth = getWidth() / (2 * NUM_RODS + 1);
        return Math.min(x / (2 * rodWidth), NUM_RODS - 1);
    }
    
    private void initializeRods(){
        rods = new Stack[NUM_RODS];
        for (int i = 0; i< NUM_RODS; i++){
            rods[i] = new Stack<>();
        }

        for(int i = Num_DISCS;i > 0; i--){
            rods[0].push(i);
        }
    }

    public void moveDisc(int sourceRod, int targetRod) {
        if (sourceRod != -1 && targetRod != -1 && sourceRod != targetRod) {
            if (!rods[sourceRod].isEmpty() && (rods[targetRod].isEmpty() || rods[sourceRod].peek() < rods[targetRod].peek())) {
                
                rods[targetRod].push(rods[sourceRod].pop());
                sourceRodHistory.push(sourceRod);
                targetRodHistory.push(targetRod);
                undoneSourceRodHistory.clear();
                undoneTargetRodHiatory.clear();
                
                

                if (isGameWon()) {
                    if (onWinListener != null) {
                        Timer timer = new Timer(500, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                onWinListener.run();
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
        }
    }

    public void reset(){
        initializeRods();

        sourceRodHistory.clear();
        targetRodHistory.clear();
        undoneSourceRodHistory.clear();
        undoneTargetRodHiatory.clear();
        repaint();
    }

    public void setOnWinListener(Runnable onWinListener){this.onWinListener = onWinListener;}

    private boolean isGameWon(){
        Stack<Integer> tagetRod = rods[NUM_RODS -1];
        return tagetRod.size() == Num_DISCS;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int discHeight = getHeight()*2 / (3 * Num_DISCS);
        int rodWidth = getWidth() / (2 * NUM_RODS);
        
        for (int i = 0; i < NUM_RODS; i++) {
            int rodX = (2 * i + 1) * rodWidth;
            if(i == NUM_RODS-1){g.setColor(new Color(255, 179, 239));
            }else{g.setColor(new Color(190, 205, 248));}
            g2.fill(new RoundRectangle2D.Float(rodX, getHeight()-discHeight*Num_DISCS, 5, discHeight*Num_DISCS,20 ,20));

            for (int j = 0; j < rods[i].size(); j++) {
                int discValue = rods[i].get(j);
                 
                int discWidth;
                if(NUM_RODS == 3){
                    discWidth = (rodWidth == 0 || discValue == 0) ? 0 : rodWidth * discValue / 2;    
                }else if(NUM_RODS == 4){
                    discWidth = (rodWidth == 0 || discValue == 0) ? 0 : rodWidth * discValue / 5;
                }else{
                    discWidth = (rodWidth == 0 || discValue == 0) ? 0 : rodWidth * discValue / 8;
                }
                int discX = rodX - discWidth / 2;
                int discY = getHeight() - (j + 1) * discHeight;

                if (j % 2 == 0) {
                    g2.setColor(Color.PINK);
                } else {
                    g2.setColor(Color.CYAN);
                }

                g2.fill(new RoundRectangle2D.Float(discX, discY, discWidth, discHeight, 30 , 30));
            }
            
            if (i == draggedRod) {
                g2.setColor(new Color(242, 199, 134, 100));
                g2.fillRect(rodX - rodWidth / 2, 0, rodWidth, getHeight());
            }
        }
    }

    public int getNumDiscs() {
        return Num_DISCS;
    }
    public void setNumDiscs(int numDiscs) {
        this.Num_DISCS = numDiscs;
        initializeRods();
        repaint();
    }

    public int getNumRods() {
        return NUM_RODS;
    }

    public void undoMove() {
        if (!sourceRodHistory.isEmpty() && !targetRodHistory.isEmpty()) {
            int lastSourceRod = sourceRodHistory.pop();
            int lastTargetRod = targetRodHistory.pop();
    
            rods[lastSourceRod].push(rods[lastTargetRod].pop());
            undoneSourceRodHistory.push(lastSourceRod);
            undoneTargetRodHiatory.push(lastTargetRod);
    
            repaint();
        }
    }

    private void updateMoveButtonPanel() {
        rodsButtonPanel.removeAll();

        int n = getNumRods();
        rodsButtonPanel.setLayout(new GridLayout(1, n));
        
        for (int i = 0; i < n; i++) {
            JButton rodsButton;
            if(i == 0){rodsButton = new JButton("Start");
            }else if ( i == n-1){ rodsButton = new JButton("Destination");
            }else{rodsButton = new JButton(" Temtorary " + (i));}
            rodsButton.setBackground(new Color(255, 182, 193));
            rodsButton.setForeground(Color.BLACK);
            rodsButtonPanel.add(rodsButton);
        
        }
        rodsButtonPanel.revalidate(); 
        rodsButtonPanel.repaint();
    }
}