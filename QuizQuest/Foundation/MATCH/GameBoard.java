package Foundations.MATCH;

import javax.swing.*;

import Foundations.BackgroundMusic;
import Windows.HomePage;
import Windows.QuizQuest;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Foundations.SoundPlayer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.ArrayList;


public class GameBoard {
    private JFrame frame;
    private Card[] cards;
    private int mistakes = 0;
    private final int MAX_MISTAKES = 3;
    private Card firstCard = null;
    private int pairsFound = 0;
    private boolean isProcessing = true;
    private int rows, cols, totalPairs;

    BackgroundMusic music =  QuizQuest.getBackgroundMusic();
    private boolean isMusicPlaying = music.isPlaying();

    private JButton musicButton;
    private ImageIcon soundOnIcon = new ImageIcon(getClass().getResource("/Foundations/pics/musicOn.png"));
    private ImageIcon soundOffIcon = new ImageIcon(getClass().getResource("/Foundations/pics/musicOff.png"));
    private ImageIcon homeIcon  = new ImageIcon(getClass().getResource("/Foundations/pics/hhome.png"));
    private ImageIcon icon = new ImageIcon(getClass().getResource("/Foundations/pics/icon.png"));
    private String[] images = {
        "/Foundations/pics/countDown/Remember.png",
        "/Foundations/pics/countDown/count3.png",
        "/Foundations/pics/countDown/count2.png",
        "/Foundations/pics/countDown/count1.png"
    };
    private int currentIndex = 0;
   // constructor when start
    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.totalPairs = (rows * cols) / 2;
        frame = new JFrame("Matching Tile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
     
        frame.setIconImage(icon.getImage());

        ImageIcon bgImage = new ImageIcon(getClass().getResource("/Foundations/pics/backGround/BG1.jpg"));
        frame.setContentPane(new JLabel(new ImageIcon(bgImage.getImage().getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH))));

        JPanel gamePanel = new JPanel(new GridLayout(rows, cols));
        gamePanel.setOpaque(true);
        gamePanel.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7), (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7)));
        
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new BorderLayout());

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
            frame.dispose();
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
        frame.add(controlPanel, BorderLayout.NORTH);

        frame.setLayout(new BorderLayout());
        frame.add(controlPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); 
        centerPanel.setOpaque(false); 
        centerPanel.add(gamePanel); 

        frame.add(centerPanel, BorderLayout.CENTER);

        setupCards(gamePanel);
        frame.pack();
        frame.setVisible(true);
        showCountdown();
        Timer timer = new Timer(4500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllCardsTemporarily();
            }
        });
        timer.setRepeats(false);
        timer.start();
        
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the game?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedIcon );
        
                if (confirmed == JOptionPane.NO_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                } else {frame.setVisible(false);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    new HomePage();
                }
            }
        });
    }   

    private void showCountdown() {
        if (currentIndex >= images.length) {
            return;
        }

        String imagePath = images[currentIndex];
        currentIndex++;

        JDialog dialog = new JDialog((Frame) null, true);
        dialog.setUndecorated(false);
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        dialog.setModal(false);
        dialog.add(label);
        dialog.setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                showCountdown();
            }
        });

        timer.setRepeats(false);
        timer.start();
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

    private void setupCards(JPanel gamePanel) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= totalPairs; i++) {
            values.add(i);
            values.add(i);
        }

        Collections.shuffle(values);

        cards = new Card[rows * cols];
        Color[] pastelColors = { new Color(255, 182, 193), new Color(255, 218, 185), new Color(221, 160, 221), 
            new Color(176, 224, 230), new Color(174, 175, 249) };
        
        for (int i = 0; i < rows * cols; i++) {
            cards[i] = new Card(values.get(i));
            cards[i].setBackground(pastelColors[i % pastelColors.length]);
            cards[i].addActionListener(new CardClickListener(cards[i]));
            cards[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 6, true)); // Rounded border
            cards[i].setFocusPainted(false);
            cards[i].setContentAreaFilled(true);
            gamePanel.add(cards[i]);
        }
    }

    private void showAllCardsTemporarily() {
        for (Card card : cards) {
            card.showFront();
        }
        Timer timer = new Timer(3000, e -> {
            for (Card card : cards) {
                card.showBack();
            }
            isProcessing = false;
        });
        timer.setRepeats(false);
        timer.start();
    }

    private class CardClickListener implements ActionListener {
        private Card card;
        public CardClickListener(Card card) {
            this.card = card;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isProcessing || card.isMatched() || card == firstCard) {
                    return;
            }
            
            SoundPlayer.playSound("unlock.wav");
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

            card.showFront();
            if (firstCard == null) {
                firstCard = card;
            } else {
                isProcessing = true;
                if (firstCard.getValue() == card.getValue()) {
                    firstCard.setMatched(true);
                    card.setMatched(true);
                    pairsFound++;
                    SoundPlayer.playSound("winSound.wav");
                    if (pairsFound == totalPairs) {
                        SoundPlayer.playSound("smallWin.wav");
                        JOptionPane.showMessageDialog(frame, "\t\tyou win!", "Congratulations", JOptionPane.INFORMATION_MESSAGE, resizedIcon);
                        frame.dispose();
                        new MatchingGame();
                        //invoke Gameplayer class
                        return;
                    }
                    firstCard = null;
                    isProcessing = false;
                } else {
                    mistakes++;

                    if (mistakes == MAX_MISTAKES) {
                            SoundPlayer.playSound("lose.wav");
                            JOptionPane.showMessageDialog(frame, "you lose!" ,"lose", JOptionPane.INFORMATION_MESSAGE, resizedIcon);
                            frame.dispose();
                            new MatchingGame();
                            //invoke Gameplayer class
                            return;
                        }

                    Timer timer = new Timer(1000, ev -> {
                        firstCard.showBack();
                        card.showBack();
                        firstCard = null;
                        isProcessing = false;
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
    }
}