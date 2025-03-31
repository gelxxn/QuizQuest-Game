package Foundations.PUZZLE;
import Foundations.*;
import Windows.HomePage;
import Windows.QuizQuest;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GamePlayer {
    static FrameSetter frame;
    static JPanel swap;
    JPanel mainPanel, topPanel, centerPanel, bottomPanel;
    JButton homeBtn, picBtn, soundBtn, hintBtn;
    int btnWidth, btnHight;
    BackgroundMusic ms;
    private boolean isMusicPlaying = true;

    public GamePlayer() {
        frame = new FrameSetter();
        ms = QuizQuest.getBackgroundMusic();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBackground(new Color(235, 218, 192));
        mainPanel.setOpaque(false);

        /* ----------------------------- Top Section --------------------------------- */

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(15,20,15,20));
        topPanel.setBackground(new Color(106, 143, 173));
        topPanel.setMaximumSize(new Dimension(1920, 80));

        ImageIcon img_home = new ImageIcon(getClass().getResource("/Foundations/pics/home.png"));
        Image home = img_home.getImage().getScaledInstance(95, 38, Image.SCALE_SMOOTH);

        homeBtn = new JButton(new ImageIcon(home));
        homeBtn.setBorderPainted(false);
        homeBtn.setContentAreaFilled(false);
        homeBtn.setFocusPainted(false);
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("bubble.wav");
                frame.dispose();
                new HomePage();
            }
        });
        topPanel.add(homeBtn, BorderLayout.WEST);

        JLabel lvTeller = new JLabel("Puzzle8  "); 
        lvTeller.setFont(new Font("Poppins", Font.BOLD, 30));
        lvTeller.setForeground(Color.WHITE);
        lvTeller.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(lvTeller, BorderLayout.CENTER);

        ImageIcon img_son = new ImageIcon(getClass().getResource("/Foundations/pics/musicon.png"));
        ImageIcon img_soff = new ImageIcon(getClass().getResource("/Foundations/pics/musicOff.png"));
        Image soundon = img_son.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image soundoff = img_soff.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        soundBtn = new JButton(isMusicPlaying ? new ImageIcon(soundon) : new ImageIcon(soundoff));
        soundBtn.setBorderPainted(false);
        soundBtn.setContentAreaFilled(false);
        soundBtn.setFocusPainted(false);
        soundBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("popSoap.wav");
                if (ms != null) {
                    if (ms.isPlaying()) {
                        ms.stopMusic();
                        soundBtn.setIcon(new ImageIcon(soundoff));
                    } else {
                        ms.startMusic();
                        soundBtn.setIcon(new ImageIcon(soundon));
                    }
                    isMusicPlaying = !isMusicPlaying;
                }
            }
        });
        topPanel.add(soundBtn, BorderLayout.EAST);
        
        mainPanel.add(topPanel);

        /* ------------------------ End Of Top Section ------------------------------ */

        mainPanel.add(Box.createRigidArea(new Dimension(0, 65)));

        /* --------------------------- Grid Section -------------------------------- */
        
        Grid grid = new Grid();
        JPanel centerPanel = grid.GridGenerator();
        
        /* ----------------------- End Of Grid Section ----------------------------- */
        
        /* ------------------------- Original Section ------------------------------ */

        JPanel originalPanel = grid.originalPanel();

        // swap original panel & center panel
        swap = new JPanel(new CardLayout(0,0));
        swap.setMaximumSize(new Dimension(450,450));
        swap.add(centerPanel, "Main");
        swap.add(originalPanel,"Overlay");

        /* ---------------------- end original section ------------------------------- */

        /* --------------------------- Bar Section ----------------------------------- */

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,15));
        bottomPanel.setBackground(new Color(1, 1, 1));
        bottomPanel.setMaximumSize(new Dimension(1920, 200));
        bottomPanel.setOpaque(false);

        /* --------------------  show Original Image Section  ------------------------ */

        ImageIcon img = new ImageIcon(getClass().getResource("/Foundations/pics/Picture.png"));
        Image pic = img.getImage().getScaledInstance(210, 70, Image.SCALE_SMOOTH);

        picBtn = new JButton(new ImageIcon(pic));
        picBtn.setMaximumSize(new Dimension(210, 70));
        picBtn.setBorderPainted(false);
        picBtn.setContentAreaFilled(false);
        picBtn.setFocusPainted(false);
        picBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("popSoap.wav");
                CardLayout c = (CardLayout) swap.getLayout();
                c.show(swap, "Overlay");

                javax.swing.Timer t =new javax.swing.Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        c.show(swap, "Main");
                    }
                });
                t.setRepeats(false);
                t.start();
            }
        });
        swap.setOpaque(false);
        mainPanel.add(swap);
        /* ---------------------------------  end Show Original  --------------------------------- */

        /* -----------------------------------  Show Hint  --------------------------------------- */
        ImageIcon img_hint = new ImageIcon("C:\\Users\\pc\\Documents\\oop\\QuizQuest\\Foundations\\pics\\hint.png");
        Image hint = img_hint.getImage().getScaledInstance(210, 80, Image.SCALE_SMOOTH);

        hintBtn = new JButton(new ImageIcon(hint));
        hintBtn.setMaximumSize(new Dimension(210,80));
        hintBtn.setBorderPainted(false);
        hintBtn.setContentAreaFilled(false);
        hintBtn.setFocusPainted(false);
        hintBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.getHint();
            }
        });

        /* --------------------------  Show Hint  ------------------------------ */
        bottomPanel.add(hintBtn);
        bottomPanel.add(picBtn);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(bottomPanel);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int option = JOptionPane.showConfirmDialog(frame, 
                    "Are you sure you want to exit the game?", 
                    "Exit Confirmation", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                
                if(option == JOptionPane.YES_OPTION) {
                    frame.setVisible(false);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    new HomePage();
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    public static void showFullImage() {
        CardLayout c = (CardLayout) swap.getLayout();
        c.show(swap, "Overlay");
    }

    public static void next() {
        frame.dispose();
        new GamePlayer();
    }

    class RoundedPanel extends JPanel {
        private Color color, bgColor;
        private int roundness;

        public RoundedPanel(int r, Color c, Color bgc) {
            super();
            color = c;
            roundness = r;
            bgColor = bgc;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Dimension a = new Dimension(roundness, roundness);
            int width = getWidth();
            int height = getHeight();

            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            graphics.setColor(bgColor);
            graphics.fillRect(0, 0, width, height);

            if (color != null) {
                graphics.setColor(color);
            } else graphics.setColor(getBackground());

            graphics.fillRoundRect(0, 0, width, height, a.width, a.height);
        }
    }
}