package Foundations.HANOI;
import javax.swing.*;

import Foundations.BackgroundMusic;
import Windows.HomePage;
import Windows.QuizQuest;
import Foundations.SoundPlayer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HanoiFrame extends JFrame {
    private int level;
    private final HanoiSet hanoiBoard;
    
    private JButton musicButton;
    BackgroundMusic music =  QuizQuest.getBackgroundMusic();
    private boolean isMusicPlaying = music.isPlaying();

    private ImageIcon soundOnIcon = new ImageIcon(getClass().getResource("/Foundations/pics/musicOn.png"));
    private ImageIcon soundOffIcon = new ImageIcon(getClass().getResource("/Foundations/pics/musicOff.png"));
    private ImageIcon homeIcon  = new ImageIcon(getClass().getResource("/Foundations/pics/hhome.png"));
    private ImageIcon icon = new ImageIcon(getClass().getResource("/Foundations/pics/icon.png"));
    private ImageIcon iconReset = new ImageIcon(getClass().getResource("/Foundations/pics/reset.png"));
    private ImageIcon iconUndo = new ImageIcon(getClass().getResource("/Foundations/pics/undo.png"));
    private ImageIcon iconHelp = new ImageIcon(getClass().getResource("/Foundations/pics/help.png"));
    private ImageIcon iconGiveUp = new ImageIcon(getClass().getResource("/Foundations/pics/giveup.png"));
    
    private boolean isGiveUp;

    public HanoiFrame(int level_){
        this.level = level_;

        setTitle("Tower of Hanoi (Level: " + level + ")");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setIconImage(icon.getImage());

        ImageIcon bgImage = new ImageIcon(getClass().getResource("/Foundations/pics/backGround/BG1.jpg"));
        JLabel backgroundLabel = new JLabel(new ImageIcon(bgImage.getImage()
            .getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, 
                               Toolkit.getDefaultToolkit().getScreenSize().height, 
                               Image.SCALE_SMOOTH)));
        setContentPane(backgroundLabel);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
        hanoiBoard = new HanoiSet(getDiscsFromLevel(level), getRodsFromDiscs(getDiscsFromLevel(level)));
        hanoiBoard.setBackground(new Color(55, 28, 70));
        hanoiBoard.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7),
                                                  (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7)));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(hanoiBoard, gbc);
        add(panel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        addButtons(buttonPanel);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setOpaque(false);
        JButton homeButton = new JButton(homeIcon);
        homeButton.setPreferredSize(new Dimension(60, 60));
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setOpaque(false);
        homeButton.addActionListener(e -> {
            SoundPlayer.playSound("bubble.wav");
            new HomePage();
            dispose();
        });
        topLeftPanel.add(homeButton);
        topLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 35, 0));
        topLeftPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
        controlPanel.add(topLeftPanel, BorderLayout.WEST);
 
        JPanel topRightPanel = new JPanel();
        topRightPanel.setOpaque(false);

        musicButton = new JButton(isMusicPlaying ? soundOnIcon : soundOffIcon);
        musicButton.setPreferredSize(new Dimension(60, 60));
        musicButton.setContentAreaFilled(false);
        musicButton.setBorderPainted(false);
        musicButton.setFocusPainted(false);
        musicButton.setOpaque(false);
        musicButton.addActionListener(e -> toggleMusic());

        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 35, 0));
        topRightPanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0)); 
        topRightPanel.add(musicButton);
        controlPanel.add(topRightPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.NORTH);
        setVisible(true);

        hanoiBoard.setOnWinListener(() -> {
            int nextLevel = level + 1;
            HanoiSave.saveLevel(nextLevel);
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            SoundPlayer.playSound("smallWin.wav");
            JOptionPane winPane = new JOptionPane("You Win!", JOptionPane.INFORMATION_MESSAGE, 
            JOptionPane.DEFAULT_OPTION, resizedIcon, new Object[]{}, null);
            JDialog dialog = winPane.createDialog(this, "Congratulation");
            
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                new Hanoi();
                dispose();
                }
            });

            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(false);
            dialog.setVisible(true);


            Timer timer = new Timer(2000, new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                    setVisible(false);
                }
            });

            timer.start();
            
        });
    
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the game?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedIcon );       
        
                if (confirmed == JOptionPane.NO_OPTION) {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                } else {HanoiFrame.this.setVisible(false);
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    new HomePage();
                }
            }
        });

        
    }

    private void toggleMusic() {
        if (music != null) {
            if (music.isPlaying()) {
                music.stopMusic();
                musicButton.setIcon(soundOffIcon);
            } else {
                music.startMusic();
                musicButton.setIcon(soundOnIcon);
            }
            isMusicPlaying = !isMusicPlaying;
        }
    }

    private void addButtons(JPanel panel) {
        JButton restartButton = new JButton(iconReset);
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setOpaque(false);
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(e -> hanoiBoard.reset());

        JButton undoButton = new JButton(iconUndo);
        undoButton.setContentAreaFilled(false);
        undoButton.setBorderPainted(false);
        undoButton.setOpaque(false);
        undoButton.setFocusPainted(false);
        undoButton.addActionListener(e -> hanoiBoard.undoMove());

        JButton helpButton = new JButton(iconHelp);
        helpButton.setContentAreaFilled(false);
        helpButton.setBorderPainted(false);
        helpButton.setOpaque(false);
        helpButton.setFocusPainted(false);
        helpButton.addActionListener(e -> showHelpDialog());

        JButton giveUpButton = new JButton(iconGiveUp);
        giveUpButton.setContentAreaFilled(false);
        giveUpButton.setBorderPainted(false);
        giveUpButton.setOpaque(false);
        giveUpButton.setFocusPainted(false);
        giveUpButton.addActionListener(e -> handleGiveUp());

        panel.add(restartButton);
        panel.add(undoButton);
        panel.add(helpButton);
        panel.add(giveUpButton);
    }

    private void handleGiveUp() {
        
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/Foundations/pics/icon.png"));
        Image scaledImage = icon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to give up?",
                "Give Up",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedIcon );       
        

        if (choice == JOptionPane.YES_OPTION) {
            isGiveUp = true;
            HanoiSave.saveLevel(1);
            setVisible(false);
            new HanoiFrame(1);
            dispose();
        }
    }

    private void showHelpDialog() {
        String message = """
                Tower of Hanoi Rules
        Move One Disk at a Time : Only one disk can be moved per step.
        Follow the Stack Order : A disk can only be placed on top of a larger disk or an empty peg.
        Use Only the Given Pegs : Disks can only be placed on one of the three pegs.
        Goal : Move all disks from the source peg to the destination peg following the above rules.
        """;
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JOptionPane.showMessageDialog(this, message, "Help", JOptionPane.INFORMATION_MESSAGE, resizedIcon);
    }

    private int getDiscsFromLevel(int level) {
        return 2 + level;
    }

    private int getRodsFromDiscs(int numDiscs) {
        if (numDiscs < 5) return 3;
        else if (numDiscs < 11) return 4;
        else if (numDiscs < 14) return 5;
        else return 7;
    }

}