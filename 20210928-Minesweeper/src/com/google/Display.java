package com.google;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Display extends JFrame implements MouseListener {
    private JButton resetButton;
    private JLabel label;
    private int amountBombs;

    private boolean[][] flagGrid;
    private int[][] grid;
    private JButton[][] buttons;

    private Color lightGreen = new Color(170, 215, 81);
    private Color darkGreen = new Color(124, 164, 60);
    private Color lightBrown = new Color(229, 194, 159);
    private Color darkBrown = new Color(215, 184, 153);

    private final int BOMB = 10;

    Display(int width, int height, int sizeX, int sizeY, int amountBombs) {
        //Set min values
        sizeX = minSize(sizeX);
        sizeY = minSize(sizeY);
        this.amountBombs = minMaxAmountBombs(sizeX, sizeY, amountBombs);

        //create new objects
        this.resetButton = new JButton();
        this.label = new JLabel();
        this.flagGrid = new boolean[sizeY][sizeX];
        this.grid = new int[sizeY][sizeX];
        this.buttons = new JButton[sizeY][sizeX];

        manager(width,height);
    }

    public void manager(int width, int height) {
        generateField();
        createButtons();
        generateDisplay(width, height);
        setListener();
    }

    public void setListener() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setButtonPosition();
            }
        });
    }
    
    public void generateField() {
        Random random = new Random();
        for (int i = 0; i < amountBombs; i++) {
            boolean setBomb = false;
            while (!setBomb) {
                int x = random.nextInt(grid[0].length);
                int y = random.nextInt(grid.length);
                if (grid[y][x] != BOMB) {
                    grid[y][x] = BOMB;
                    setBomb = true;
                }
            }
        }

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = getBombs(y,x);
            }
        }
    }

    public int getBombs(int y, int x) {
        if (grid[y][x] == BOMB) {
            return BOMB;
        }
        int counter = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == 0 && i == 0) {
                    continue;
                }
                if (y + i < 0 || y + i >= grid.length) {
                    continue;
                }
                if (x + j < 0 || x + j >= grid[0].length) {
                    continue;
                }
                if (grid[y + i][x + j] == BOMB) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public int minSize(int var) {
        if (var < 3) {
            var = 3;
        }
        return var;
    }

    public int minMaxAmountBombs(int x, int y, int var) {
        if (var < 1) {
            var = 1;
        }
        while (x * y < var * 2) {
            var /= 2;
        }
        return var;
    }

    public void generateDisplay(int width, int height) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(width, height));
        this.getContentPane().setBackground(new Color(68, 84, 31));
    }

    public void setButtonPosition() {
        final double fieldXSize = 0.98 / grid[0].length;
        final double fieldYSize = 0.78 / grid.length;
        double positionY = 0.15;
        for (int y = 0; y < grid.length; y++) {
            double positionX = 0;
            for (int x = 0; x < grid[0].length; x++) {
                buttons[y][x].setBounds((int) (getBoundSize()[1] * positionX), (int) (getBoundSize()[0] * positionY),
                        (int) (getBoundSize()[1] * fieldXSize), (int) (getBoundSize()[0] * fieldYSize));
                positionX += fieldXSize;
            }
            positionY += fieldYSize;
        }
        resetButton.setBounds((int) (getBoundSize()[1] * 0.65), (int) (getBoundSize()[0] * 0.02),
                (int) (getBoundSize()[1] * 0.25), (int) (getBoundSize()[0] * 0.1));
    }

    public void createButtons() {
        final double fieldXSize = 0.98 / grid[0].length;
        final double fieldYSize = 0.78 / grid.length;
        double positionY = 0.15;
        for (int y = 0; y < grid.length; y++) {
            double positionX = 0;
            for (int x = 0; x < grid[0].length; x++) {
                buttons[y][x] = new JButton();

                buttons[y][x].addMouseListener(this);
                buttons[y][x].setFocusable(false);
                buttons[y][x].setVisible(true);

                buttons[y][x].setBorder(null);
                buttons[y][x].setFont(new Font("MV Boli", Font.BOLD, 25));
                buttons[y][x].setBackground((y + x) % 2 == 0 ? darkGreen : lightGreen);

                buttons[y][x].setMargin(new Insets(0, 0, 0, 0));
                buttons[y][x].setBounds((int) (getBoundSize()[1] * positionX), (int) (getBoundSize()[0] * positionY),
                        (int) (getBoundSize()[1] * fieldXSize), (int) (getBoundSize()[0] * fieldYSize));

                this.add(buttons[y][x]);

                positionX += fieldXSize;
            }
            positionY += fieldYSize;
        }
        resetButton = new JButton();
        resetButton.addMouseListener(this);
        resetButton.setFocusable(false);
        resetButton.setVisible(true);

        resetButton.setBorder(null);
        resetButton.setFont(new Font("MV Boli", Font.BOLD, 25));
        resetButton.setBackground(lightGreen);

        resetButton.setMargin(new Insets(0, 0, 0, 0));
        resetButton.setBounds((int) (getBoundSize()[1] * 0.65), (int) (getBoundSize()[0] * 0.02),
                (int) (getBoundSize()[1] * 0.25), (int) (getBoundSize()[0] * 0.1));

        resetButton.setText("Restart");

        this.add(resetButton);
    }

    public int[] getBoundSize() {
        Rectangle r = this.getBounds();
        return new int[]{r.height, r.width};
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
