package Foundations.MATCH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class SlidingPuzzleGame extends JFrame {
    private static final int SIZE = 3;
    private static final int TILE_SIZE = 100;
    private static final int BOARD_SIZE = SIZE * TILE_SIZE;
    private BufferedImage image;
    private BufferedImage[][] tiles;
    private int emptyRow, emptyCol;

    public SlidingPuzzleGame(String imagePath) {
        try {
            image = javax.imageio.ImageIO.read(new java.io.File(imagePath));
            if (image == null) {
                throw new Exception("Image not found or invalid.");
            }
            tiles = new BufferedImage[SIZE][SIZE];
            splitImage();
            initializeBoard();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Sliding Puzzle");
        setSize(BOARD_SIZE, BOARD_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TILE_SIZE;
                int y = e.getY() / TILE_SIZE;
                if (isValidMove(x, y)) {
                    moveTile(x, y);
                    repaint();
                    if (isSolved()) {
                        JOptionPane.showMessageDialog(null, "You solved the puzzle!");
                    }
                }
            }
        });
    }

    // แบ่งภาพออกเป็น tiles
    private void splitImage() {
        int tileWidth = image.getWidth() / SIZE;
        int tileHeight = image.getHeight() / SIZE;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tiles[row][col] = image.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    // สุ่มตำแหน่งการวาง tiles
    private void initializeBoard() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        int index = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int position = positions.get(index++);
                if (position == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }
    }

    // ตรวจสอบการเคลื่อนที่ที่ถูกต้อง
    private boolean isValidMove(int row, int col) {
        return (Math.abs(row - emptyRow) == 1 && col == emptyCol) ||
               (Math.abs(col - emptyCol) == 1 && row == emptyRow);
    }

    // เคลื่อนย้าย tiles
    private void moveTile(int row, int col) {
        tiles[emptyRow][emptyCol] = tiles[row][col];
        tiles[row][col] = null;
        emptyRow = row;
        emptyCol = col;
    }

    // ตรวจสอบว่าเกมเสร็จสมบูรณ์หรือยัง
    private boolean isSolved() {
        int correctValue = 1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (row == SIZE - 1 && col == SIZE - 1) {
                    return tiles[row][col] == null; // ช่องสุดท้ายต้องเป็นช่องว่าง
                }
                if (tiles[row][col] == null || !isCorrectTile(row, col, correctValue++)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ตรวจสอบว่า tiles ตรงกับลำดับที่ถูกต้อง
    private boolean isCorrectTile(int row, int col, int value) {
        int tileWidth = image.getWidth() / SIZE;
        int tileHeight = image.getHeight() / SIZE;
        BufferedImage expectedTile = image.getSubimage((value - 1) % SIZE * tileWidth, (value - 1) / SIZE * tileHeight, tileWidth, tileHeight);
        return tiles[row][col].equals(expectedTile);
    }

    // วาดกริดและ tiles
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (tiles != null) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (tiles[row][col] != null) {
                        g.drawImage(tiles[row][col], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE); // วาดช่องว่างเป็นสีขาว
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // ระบุพาธของภาพที่ต้องการใช้ในเกม
        String imagePath = "pt/Images.jpg"; // เปลี่ยนเป็นพาธภาพของคุณ
        SwingUtilities.invokeLater(() -> new SlidingPuzzleGame(imagePath).setVisible(true));
    }
}
