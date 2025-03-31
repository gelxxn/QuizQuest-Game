package Foundations.PUZZLE;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import Foundations.SoundPlayer;
import Foundations.LevelController;
import java.io.File;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Grid extends JPanel implements ActionListener {
    JPanel grid;
    JLabel[][] label;
    JButton[][] button;
    int[][] board;
    int dimension, level = 0;
    PictureImport p;
    ArrayList<File> files;
    JButton nextBtn;
    Border hintBtn = null;
    int hintRow = -1, hintCol = -1;

    public Grid() {
        this.dimension = 3; //demo var
        board = new int[dimension][dimension];

        int lastLevelP = LevelController.loadLevelPuzzle();
        this.level = lastLevelP;
        p = new PictureImport(level /*lv */);
        files = p.getFolderImages();
        PlacingGrid();
    }

    public void PlacingGrid() {
        grid = new JPanel();
        grid.setLayout(new GridLayout(dimension, dimension, 1, 1));
        grid.setPreferredSize(new Dimension(410, 410));
        button = new JButton[dimension][dimension];
        label = new JLabel[dimension][dimension];

        this.shuffleBoard();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                button[i][j] = new JButton();
                String marked = i + "," + j;
                button[i][j].setPreferredSize(new Dimension(135,135));
                button[i][j].setBorder(BorderFactory.createLineBorder(new Color(139, 138, 138), 1));
                button[i][j].setText(marked);
                button[i][j].setForeground(new Color(255, 245, 223));
                button[i][j].setLayout(new GridBagLayout());
                button[i][j].setFocusPainted(false);
                button[i][j].addActionListener(this);

                int val = board[i][j];
                String filename;

                if (val != -1) {
                    filename = files.get(val - 1).toString();
                    ImageIcon icon = new ImageIcon(filename);
                    Image img = icon.getImage().getScaledInstance(135, 135, Image.SCALE_SMOOTH);
                    label[i][j] = new JLabel(new ImageIcon(img), JLabel.CENTER);
                    label[i][j].setPreferredSize(new Dimension(135,135));
                    label[i][j].setBackground(new Color(255, 245, 223));
                    label[i][j].setOpaque(true);
                } else {
                    label[i][j] = new JLabel("");
                    button[i][j].setBackground(new Color(255, 245, 223));
                    label[i][j].setOpaque(true);
                }

                button[i][j].add(label[i][j]);
                grid.add(button[i][j]);
            }
        }
    }

    private boolean isSolvable(int[] puzzle) {
        int inversions = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = i + 1; j < puzzle.length; j++) {
                if (puzzle[i] > puzzle[j] && puzzle[i] != -1 && puzzle[j] != -1) {
                    inversions++;
                }
            }
        }
        return (inversions % 2 == 0);
    }

    public void shuffleBoard() {
        Random rnd = new Random();
        int[] arr = new int[dimension*dimension];

        do {
            for (int  i = 0; i < dimension*dimension; i++) {
                arr[i] = i+1;
            }

            arr[(dimension*dimension) - 1] = -1;

            for (int i = arr.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                int temp = arr[index];
                arr[index] = arr[i];
                arr[i] = temp;
            }

        } while (!isSolvable(arr));
        
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = arr[count];
                count++;
            }
        }
    }

    Boolean isWin() {
        int count = 1;
        for (int i = 0; i< dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != count && board[i][j] != -1) {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    public void displayWinMsg() {
        JFrame frame = new JFrame("Game Win");

        JPanel bg = new JPanel();
        bg.setBackground(new Color(231, 215, 249));
        bg.setLayout(new BorderLayout());

        JLabel label = new JLabel("You Solve The Puzzle",JLabel.CENTER);
        label.setFont(new Font("TimesRoman",Font.BOLD,20));
        label.setOpaque(false);

        nextBtn = new JButton("next");
        nextBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        nextBtn.setBackground(new Color(105, 80, 126 )); 
        nextBtn.setForeground(new Color(231, 215, 249));
        nextBtn.setFocusPainted(false);
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GamePlayer.next();
            }
        });
    
        bg.add(label, BorderLayout.CENTER);
        bg.add(nextBtn, BorderLayout.SOUTH);
    
        frame.add(bg);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        SoundPlayer.playSound("popSoap.wav");
        Boolean flag = isWin();

        if (flag == false) {
            String s = ae.getActionCommand().toString();
            int r = Integer.parseInt(s.split(",")[0]);
            int c = Integer.parseInt(s.split(",")[1]);

            if (r == hintRow && c == hintCol) {
                if (label[r][c].getClientProperty("originalIcon") != null) {
                    label[r][c].setBackground(new Color(255, 245, 223));
                    button[r][c].setForeground(new Color(255, 245, 223));
                    label[r][c].setText("");
                    label[r][c].setIcon((Icon) label[r][c].getClientProperty("originalIcon"));
                }
                button[r][c].setBackground(new Color(255, 245, 223));
                hintRow = -1;
                hintCol = -1;
                hintBtn = null;
            }

            if (board[r][c] != -1) {
                if (r+1 < dimension && board[r+1][c] == -1) {
                    swapTiles(r, c, r + 1, c);
                } else if (r-1 > -1 && board[r-1][c] == -1) {
                    swapTiles(r, c, r - 1, c);
                } else if (c+1 < dimension && board[r][c+1] == -1) {
                    swapTiles(r, c, r, c + 1);
                } else if (c-1 > -1 && board[r][c-1] == -1) {
                    swapTiles(r, c, r, c - 1);
                }
            }
            flag = isWin();
            if (flag) {
                SoundPlayer.playSound("smallWin.wav");
                GamePlayer.showFullImage();
                javax.swing.Timer t = new javax.swing.Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        displayWinMsg();
                    }
                });
                t.setRepeats(false);
                t.start();

                if (level < 4) {
                    level++;
                }else level = 0;
                LevelController.saveLevelP(level);
                System.out.println("Current Level: " + level);
            }
        }
    }

    private void swapTiles(int r1, int c1, int r2, int c2) {
        int temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
    
        if (board[r2][c2] == -1) {
            label[r2][c2].setIcon(null);
            label[r2][c2].setOpaque(true);
            label[r2][c2].setBackground(new Color(255, 245, 223));
            button[r2][c2].setBackground(new Color(255, 245, 223));
        } else {
            String filename = files.get(board[r2][c2] - 1).toString();
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage().getScaledInstance(135, 135, Image.SCALE_SMOOTH);
            label[r2][c2].setIcon(new ImageIcon(img));
            label[r2][c2].setOpaque(false);
        }
    
        if (board[r1][c1] == -1) {
            label[r1][c1].setIcon(null);
            label[r1][c1].setOpaque(true);
            label[r1][c1].setBackground(new Color(255, 245, 223));
            button[r1][c1].setBackground(new Color(255, 245, 223));
        } else {
            String filename = files.get(board[r1][c1] - 1).toString();
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage().getScaledInstance(135, 135, Image.SCALE_SMOOTH);
            label[r1][c1].setIcon(new ImageIcon(img));
            label[r1][c1].setOpaque(false);
        }
    
        label[r1][c1].revalidate();
        label[r1][c1].repaint();
        label[r2][c2].revalidate();
        label[r2][c2].repaint();
    }

    public JPanel GridGenerator() {
        JPanel centerPanel = new RoundedPanel(30, new Color(241, 227, 255), new Color(179, 169, 190));
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerPanel.setPreferredSize(new Dimension(450, 450));
        centerPanel.setOpaque(false);
        centerPanel.add(grid);
        return centerPanel;
    }

    public JPanel originalPanel() {
        JPanel original = new RoundedPanel(30, new Color(241, 227, 255) , new Color(179, 169, 190));
        original.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        original.setPreferredSize(new Dimension(450, 450));
        original.setOpaque(false);

        Original org = new Original(level);
        JLabel picture = org.getOriginal();
        picture.setPreferredSize(new Dimension(410,410));
        original.add(picture);
        return original;
    }

    public void getHint() {
        new HintGenerator(this.board, dimension).Hintgenerate();
    }

    class HintGenerator {
        final int[][] finalBoard = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, -1}
        };
        int dimension;
        int[][] board;
        int[] moveRow = {-1, 1, 0, 0};
        int[] moveCol = {0, 0, -1, 1};
        HashSet<String> visited = new HashSet<>();

        HintGenerator(int[][] board, int dimension) {
            this.dimension = dimension;
            DeepCopy(board);
        }

        private void DeepCopy(int[][] board) {
            this.board = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                this.board[i] = board[i].clone();
            }
        }

        class Node {
            int[][] board;
            int coRow, coCol, cost, depth;
            Node parent;

            Node(int[][] b, int x, int y, int d, Node node) {
                this.board = new int[b.length][b.length];
                for (int i = 0; i < b.length; i++) {
                    this.board[i] = b[i].clone();
                }

                coRow = x; coCol = y;
                cost = Manhattan(b) + d; depth = d;
                parent = node;
            }

        }

        private int Manhattan(int[][] b) {
            int distance = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (b[i][j] == -1) continue;
                    int negRow = (b[i][j] - 1) / 3;
                    int negCol = (b[i][j] - 1) % 3;
                    distance += Math.abs(negRow - i) + Math.abs(negCol - j);
                }
            }

            return distance;
        }

        private boolean isFinal(int[][] board) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] != finalBoard[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }

        private int[] NegCoordinate(int target, int[][] b) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (b[i][j] == -1) {
                        return new int[]{i, j};
                    }
                }
            }

            return new int[]{-1, -1};
        }

        private String boardToString(int[][] board) {
            StringBuilder sb = new StringBuilder();
            for (int[] row : board) {
                for (int num : row) {
                    sb.append(num).append(",");
                }
            }
            return sb.toString();
        }

        public void Hintgenerate() {
            PriorityQueue<Node> Tree = new PriorityQueue<>(
                new Comparator<Node>() {
                    @Override
                    public int compare(Node n1, Node n2) {
                        return n1.cost - n2.cost;
                    }
            });

            int[] negCo = NegCoordinate(-1, board);
            Tree.add(new Node(board, negCo[0], negCo[1], 0, null));

            while (!Tree.isEmpty()) {
                Node span = Tree.poll();
                board = span.board;
                String Key = boardToString(board);

                if (visited.contains(Key)) continue;

                if (isFinal(board)) {
                    printPuzzle(span);
                    return;
                }

                visited.add(Key);

                for (int i = 0; i < 4; i++) {
                    int nextRow = span.coRow + moveRow[i];
                    int nextCol = span.coCol + moveCol[i];
                    if (nextCol > -1 && nextCol < dimension && nextRow > -1 && nextRow < dimension) {
                        int[][] instBoard = new int[dimension][dimension];

                        for (int r = 0; r < dimension; r++) {
                            instBoard[r] = board[r].clone();
                        }

                        instBoard[span.coRow][span.coCol] = instBoard[nextRow][nextCol];
                        instBoard[nextRow][nextCol] = -1;

                        String InstKey = boardToString(instBoard);
                        if (!visited.contains(InstKey)) {
                            Tree.add(new Node(instBoard, nextRow, nextCol, span.depth + 1, span));
                        }

                    }
                }
            }
        }

        private void paintHint(int[][] Ori, int val) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (Ori[i][j] == val) {
                        hintBtn = button[i][j].getBorder();
                        if (label[i][j].getIcon() != null) {
                            label[i][j].putClientProperty("originalIcon", label[i][j].getIcon());
                        }

                        button[i][j].setBackground(new Color(224, 132, 112));
                        button[i][j].setForeground(new Color(224, 132, 112));
                        label[i][j].setBackground(new Color(224, 132, 112));
                        label[i][j].setIcon(null);
                        label[i][j].setText("Move Here");
                        label[i][j].setOpaque(true);

                        hintRow = i;
                        hintCol = j;
                    }
                    button[i][j].repaint();
                    button[i][j].revalidate();
                }
            }
        }

        private void printPuzzle(Node n) {
            ArrayList<int[][]> path = new ArrayList<>();
            while (n != null) {
                path.add(n.board);
                n = n.parent;
            }
            Collections.reverse(path);
            int[][] Origi = path.get(0);
            int[][] nextMove = path.get(1);
    
            int firstTile = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (Origi[i][j] == -1) {
                        firstTile = nextMove[i][j];
                        break;
                    }
                }
            }
            paintHint(Origi, firstTile);
            System.out.println("Fisrt Tile To Move : " + firstTile);
        }
    }

    class RoundedPanel extends JPanel {
        private Color color, bgColor;
        private int roundness;
        
        public RoundedPanel(int r, Color c, Color bgc) {
            super();
            color = c;
            roundness = r;
            bgColor = bgc;
            setOpaque(false);
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