package com.google;

import javax.swing.*;
import java.awt.*;
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

    private final int BOMB = 10;

    Display(int width, int height, int sizeX, int sizeY, int amountBombs) {
        //Set min values
        sizeX = minSize(sizeX);
        sizeY = minSize(sizeY);
        this.amountBombs = minMaxAmountBombs(sizeX, sizeY, amountBombs);

        resetButton = new JButton();
        label = new JLabel();

        flagGrid = new boolean[sizeY][sizeX];
        grid = new int[sizeY][sizeX];
        createButtons();

        manager();
    }

    public void manager() {
        generateField();
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

    public void createButtons() {

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
