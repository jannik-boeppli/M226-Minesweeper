package com.google;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;

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

    /**
     * This is the constructor, which is called from the main, in it the default values are set
     * and the manager is called
     * @param width is the window width when started
     * @param height is the window height when started
     * @param sizeX is the amount of fields in one row
     * @param sizeY is the amount of fields in one column
     * @param amountBombs is the amount of bombs in the field
     */
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

    /**
     * This methode creates the buttons labels and adds the listener
     * @param width is the width of the window
     * @param height is the height of the window
     */
    public void manager(int width, int height) {
        Generator generator = new Generator();
        generator.createButtons(this, this);
        generator.createLabel(this);
        generator.generateDisplay(this, width, height);
        setListener();
    }

    /**
     * Gets called when the player clicks on a bomb,
     * disables playing field, a game over text is shown and reveals all bombs
     */
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

    /**
     * Gets called when all fields are revealed except the bombs,
     * the buttons get disabled, a winning text is shown
     */
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
            label.setText("You won");
            gameIsOver = true;
        }
    }

    /**
     * The game gets restarted, the background color is reset, the buttons get activated
     */
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

    /**
     * Sets the border between the revealed and not revealed fields
     */
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

    /**
     * Reveals all buttons surrounded by the field by executing a left click on them
     * @param clickHandler the object which contains the left click
     * @param yPosition the y-position of the clicked field
     * @param xPosition the x-position of the clicked field
     */
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

    /**
     * sets all positions of the fields on the display
     */
    public void setPosition() {
        setButtonPosition();
        setLabelPosition();
    }

    /**
     * Sets position of label relative to the current window size
     */
    public void setLabelPosition() {
        label.setBounds((int) (getBoundSize(this)[1] * 0.05), (int) (getBoundSize(this)[0] * 0.02),
                (int) (getBoundSize(this)[1] * 0.25), (int) (getBoundSize(this)[0] * 0.1));
    }

    /**
     * Sets position all buttons relative to the current window size
     */
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

    /**
     * Adds a listener to the window, when called execute the setPosition methode
     */
    public void setListener() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setPosition();
            }
        });
    }

    /**
     * This methode is used to get the amount of bombs around a field
     * @param y the y-position of the field to check
     * @param x the x-position of the field to check
     * @return returns the amount of bombs around the field, if field is a bomb returns  the value of the bomb
     */
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

    /**
     * This methode checks if field size is higher than 3, if not returns 3
     * @param var the value to check
     * @return returns 3 if number is lower than 3 else returns var
     */
    public int minSize(int var) {
        if (var < 3) {
            var = 3;
        }
        return var;
    }

    /**
     * This methode checks if there is a reasonable amount of bombs in the field
     * @param x the amount of rows of the playing field
     * @param y the amount of columns of the playing field
     * @param var the amount of bombs
     * @return if the amount of bombs (var) is lower than 1 returns 1,
     * if there are more bombs as the field size / 2 than it returns field size / 2,
     * else it returns the amount of bombs (var)
     */
    public int minMaxAmountBombs(int x, int y, int var) {
        if (var < 1) {
            var = 1;
        }
        while (x * y <= var * 2) {
            var /= 2;
        }
        return var;
    }

    //mouse event
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

    //getter and setter
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
