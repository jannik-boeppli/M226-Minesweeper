package com.google;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static com.google.DefaultValues.*;
import static com.google.Helper.*;

public class Display extends JFrame implements MouseListener {
    private JButton resetButton;
    private JLabel label;
    private int amountBombs;
    private boolean loadGrid;
    private boolean gameIsOver;

    private boolean[][] revealGrid;
    private boolean[][] flagGrid;
    private int[][] grid;
    private JButton[][] buttons;


    Display(int width, int height, int sizeX, int sizeY, int amountBombs) {
        //Set min values
        sizeX = minSize(sizeX);
        sizeY = minSize(sizeY);
        this.amountBombs = minMaxAmountBombs(sizeX, sizeY, amountBombs);

        //create new objects
        this.resetButton = new JButton();
        this.label = new JLabel();
        this.revealGrid = new boolean[sizeY][sizeX];
        this.flagGrid = new boolean[sizeY][sizeX];
        this.grid = new int[sizeY][sizeX];
        this.buttons = new JButton[sizeY][sizeX];

        manager(width, height);
    }

    //manager
    public void manager(int width, int height) {
        Generator generator = new Generator();
        generator.createButtons(this, this);
        generator.createLabel(this);
        generator.generateDisplay(this, width, height);
        setListener();
    }


    public void setPosition() {
        setButtonPosition();
        setLabelPosition();
    }


    public void gameOver() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == BOMB) {
                    buttons[y][x].setText(BOMB_ICON);
                }
            }
        }
        gameIsOver = true;
        label.setText("Game Over");
    }

    public void setLabelPosition() {
        label.setBounds((int) (getBoundSize(this)[1] * 0.05), (int) (getBoundSize(this)[0] * 0.02),
                (int) (getBoundSize(this)[1] * 0.25), (int) (getBoundSize(this)[0] * 0.1));
    }

    public void winCheck() {
        boolean finished = true;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != BOMB) {
                    if (!revealGrid[y][x]) {
                        finished = false;
                    }
                }
            }
        }
        if (finished) {
            System.out.println("game finished");
        }
    }

    public void restartGame() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                buttons[y][x].setText("");
                flagGrid[y][x] = false;
                revealGrid[y][x] = false;
                buttons[y][x].setBackground((y + x) % 2 == 0 ? DARK_GREEN : LIGHT_GREEN);
                buttons[y][x].setBorder(null);
                grid[y][x] = 0;
            }
        }
        loadGrid = false;
        label.setText("");
        gameIsOver = false;
    }

    //Working
    public void setBorder() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (revealGrid[y][x]) {
                    byte[] borderValue = new byte[4];
                    if (isInBound(grid, y - 1, x) && !revealGrid[y - 1][x]) {
                        borderValue[0] = 3;
                    } else {
                        borderValue[0] = 0;
                    }
                    if (isInBound(grid, y, x - 1) && !revealGrid[y][x - 1]) {
                        borderValue[1] = 3;
                    } else {
                        borderValue[1] = 0;
                    }
                    if (isInBound(grid, y + 1, x) && !revealGrid[y + 1][x]) {
                        borderValue[2] = 3;
                    } else {
                        borderValue[2] = 0;
                    }
                    if (isInBound(grid, y, x + 1) && !revealGrid[y][x + 1]) {
                        borderValue[3] = 3;
                    } else {
                        borderValue[3] = 0;
                    }

                    buttons[y][x].setBorder(new MatteBorder(borderValue[0], borderValue[1], borderValue[2],
                            borderValue[3], Color.black));
                }
            }
        }
    }


    public void revealSurrounding(ClickHandler clickHandler, int yPosition, int xPosition) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                //Check if position is in bound
                if (isInBound(grid, yPosition + y, xPosition + x)) {
                    clickHandler.leftClick(this, yPosition + y, xPosition + x);
                }
            }
        }
    }

    public void setButtonPosition() {
        final double fieldXSize = 0.98 / grid[0].length;
        final double fieldYSize = 0.78 / grid.length;
        double positionY = 0.15;
        int[] bounds = getBoundSize(this);
        for (int y = 0; y < grid.length; y++) {
            double positionX = 0;
            for (int x = 0; x < grid[0].length; x++) {

                buttons[y][x].setBounds((int) (bounds[1] * positionX), (int) (bounds[0] * positionY),
                        (int) (bounds[1] * fieldXSize), (int) (bounds[0] * fieldYSize));
                positionX += fieldXSize;
            }
            positionY += fieldYSize;
        }
        resetButton.setBounds((int) (bounds[1] * 0.65), (int) (bounds[0] * 0.02),
                (int) (bounds[1] * 0.25), (int) (bounds[0] * 0.1));
    }

    public void setListener() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setPosition();
            }
        });
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
        while (x * y <= var * 2) {
            var /= 2;
        }
        return var;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        ClickHandler clickHandler = new ClickHandler();
        Generator generator = new Generator();
        if (e.getSource() == resetButton) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                restartGame();
            }
        } else if(!gameIsOver){
            boolean foundButton = false;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (e.getSource() == buttons[y][x]) {
                        if (!loadGrid) {
                            generator.generateField(this, y, x);
                            loadGrid = true;
                        }
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            clickHandler.leftClick(this, y, x);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            clickHandler.rightClick(this, y, x);
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            clickHandler.middleClick(this, y, x);
                        }
                        foundButton = true;
                        break;
                    }
                }
                if (foundButton) {
                    break;
                }
            }
            setBorder();
            winCheck();
        }
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

    public JButton[][] getButtons() {
        return buttons;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getAmountBombs() {
        return amountBombs;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public boolean[][] getRevealGrid() {
        return revealGrid;
    }

    public void setFlagGrid(int y, int x, boolean value) {
        flagGrid[y][x] = value;
    }

    public void setRevealGrid(int y, int x, boolean value) {
        revealGrid[y][x] = value;
    }

    public boolean[][] getFlagGrid() {
        return flagGrid;
    }

    public boolean isGameIsOver() {
        return gameIsOver;
    }

    public void setGrid(int y, int x) {
        grid[y][x] = 9;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setGameIsOver(boolean gameIsOver) {
        this.gameIsOver = gameIsOver;
    }
}
