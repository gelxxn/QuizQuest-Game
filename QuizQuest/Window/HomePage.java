package Windows;
import java.awt.*;
import javax.swing.*;
import Foundations.*;
import Foundations.HANOI.Hanoi;
import Foundations.MATCH.MatchingGame;
import Foundations.PUZZLE.GamePlayer;

import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import Foundations.SoundPlayer;

public class HomePage {
    FrameSetter frame;
    JPanel panel;
    JLabel game_name;
    JButton start, Mapping, Hanoi;
    BackgroundMusic ms;

    public HomePage() {
        frame = new FrameSetter();
        ms = QuizQuest.getBackgroundMusic();

        //Panel Creation
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(new Color(235, 218, 192));
        panel.setPreferredSize(new Dimension(520, 600));
        panel.setOpaque(false);

        //Game name section
        ImageIcon img_quiz = new ImageIcon(getClass().getResource("/Foundations/pics/Quiz.png"));
        Image quiz = img_quiz.getImage().getScaledInstance(360, 210, Image.SCALE_SMOOTH);
        
        game_name = new JLabel(new ImageIcon(quiz));
        game_name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        game_name.setBorder(new EmptyBorder(100, 0, 0, 0));

        panel.add(game_name);
        
        // ------------------------------ Create Button Game -------------------------------------- //
        //Puzzel Game
        ImageIcon img_pz = new ImageIcon(getClass().getResource("/Foundations/pics/Puzzle8.png"));
        Image puzzle = img_pz.getImage().getScaledInstance(360, 110, Image.SCALE_SMOOTH);

        start = new JButton(new ImageIcon(puzzle));
        start.setAlignmentX(JButton.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(360, 110));
        start.setBorderPainted(false);
        start.setContentAreaFilled(false);
        start.setFocusPainted(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("popSoap.wav");
                frame.dispose();
                new GamePlayer();
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        panel.add(start);

        //Match
        ImageIcon img_m = new ImageIcon(getClass().getResource("/Foundations/pics/matching.png"));
        Image matching = img_m.getImage().getScaledInstance(360, 110, Image.SCALE_SMOOTH);
        
        Mapping = new JButton(new ImageIcon(matching));
        Mapping.setAlignmentX(JButton.CENTER_ALIGNMENT);
        Mapping.setMaximumSize(new Dimension(360, 110));
        Mapping.setBorderPainted(false);
        Mapping.setContentAreaFilled(false);
        Mapping.setFocusPainted(false);
        Mapping.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("popSoap.wav");
                frame.dispose();
                new MatchingGame();
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        panel.add(Mapping);

        //Hanoi
        ImageIcon img_hanoi = new ImageIcon(getClass().getResource("/Foundations/pics/hanoi.png"));
        Image hanoi = img_hanoi.getImage().getScaledInstance(360, 110, Image.SCALE_SMOOTH);
        
        Hanoi = new JButton(new ImageIcon(hanoi));
        Hanoi.setAlignmentX(JButton.CENTER_ALIGNMENT);
        Hanoi.setMaximumSize(new Dimension(360, 110));
        Hanoi.setBorderPainted(false);
        Hanoi.setContentAreaFilled(false);
        Hanoi.setFocusPainted(false);
        Hanoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("popSoap.wav");
                frame.dispose();
                new Hanoi();
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        panel.add(Hanoi);

        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}