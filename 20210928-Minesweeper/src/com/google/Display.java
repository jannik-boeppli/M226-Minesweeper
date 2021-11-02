package com.google;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Display extends JFrame implements MouseListener {
    private JButton resetButton;
    private JLabel label;
    private int amountBombs;
    private boolean loadGrid;
    private boolean gameIsOver;

    private String flagIcon = "🚩";
    private String bombIcon = "B";

    private Color lightGreen = new Color(170, 215, 81);
    private Color darkGreen = new Color(124, 164, 60);
    private Color lightBrown = new Color(229, 194, 159);
    private Color darkBrown = new Color(215, 184, 153);

    private boolean[][] revealGrid;
    private boolean[][] flagGrid;
    private int[][] grid;
    private JButton[][] buttons;

    private final int BOMB = 9;

    Display(int width, int height, int sizeX, int sizeY, int amountBombs) {
        //Set min values
        sizeX = minSize(sizeX);
        sizeY = minSize(sizeY);
        this.amountBombs = minMaxAmountBombs(sizeX, sizeY, amountBombs);
        gameIsOver = false;

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
        createButtons();
        createLabel();
        generateDisplay(width, height);
        setListener();
    }

    public void setPosition() {
        setButtonPosition();
        setLabelPosition();
    }

    public void setLabelPosition() {
        label.setBounds((int) (getBoundSize()[1] * 0.05), (int) (getBoundSize()[0] * 0.02),
                (int) (getBoundSize()[1] * 0.25), (int) (getBoundSize()[0] * 0.1));
    }

    public void gameOver() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == BOMB) {
                    buttons[y][x].setText(bombIcon);
                }
            }
        }
        gameIsOver = true;
        label.setText("Game Over");
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
            label.setText("You won");
            gameIsOver = true;
        }
    }

    public void restartGame() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                buttons[y][x].setText("");
                flagGrid[y][x] = false;
                revealGrid[y][x] = false;
                buttons[y][x].setBackground((y + x) % 2 == 0 ? darkGreen : lightGreen);
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
                    if (isInBound(y - 1, x) && !revealGrid[y - 1][x]) {
                        borderValue[0] = 3;
                    } else {
                        borderValue[0] = 0;
                    }
                    if (isInBound(y, x - 1) && !revealGrid[y][x - 1]) {
                        borderValue[1] = 3;
                    } else {
                        borderValue[1] = 0;
                    }
                    if (isInBound(y + 1, x) && !revealGrid[y + 1][x]) {
                        borderValue[2] = 3;
                    } else {
                        borderValue[2] = 0;
                    }
                    if (isInBound(y, x + 1) && !revealGrid[y][x + 1]) {
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

    public void leftClick(int y, int x) {
        if (!flagGrid[y][x] && buttons[y][x].getText().equals("")) {
            if (grid[y][x] == 0) {
                buttons[y][x].setText(" ");
                revealSurrounding(y, x);
                revealGrid[y][x] = true;
                buttons[y][x].setBackground((y + x) % 2 == 0 ? darkBrown : lightBrown);
            } else if (grid[y][x] == BOMB) {
                buttons[y][x].setText(bombIcon);
                gameOver();
            } else {
                revealGrid[y][x] = true;
                buttons[y][x].setText(grid[y][x] + "");
                buttons[y][x].setBackground((y + x) % 2 == 0 ? darkBrown : lightBrown);
            }
        }
    }

    public void revealSurrounding(int yPosition, int xPosition) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                //Check if position is in bound
                if (isInBound(yPosition + y, xPosition + x)) {
                    leftClick(yPosition + y, xPosition + x);
                }
            }
        }
    }

    public boolean isInBound(int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length);
    }

    public void rightClick(int y, int x) {
        if (buttons[y][x].getText().equals("") || buttons[y][x].getText().equals(flagIcon)) {
            if (!flagGrid[y][x]) {
                buttons[y][x].setText(flagIcon);
                flagGrid[y][x] = true;
            } else {
                buttons[y][x].setText("");
                flagGrid[y][x] = false;
            }
        }
    }

    public void middleClick(int yPosition, int xPosition) {
        int counter = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (isInBound(yPosition + y, xPosition + x)) {
                    if (flagGrid[yPosition + y][xPosition + x]) {
                        counter++;
                    }
                }
            }
        }
        if (counter == grid[yPosition][xPosition]) {
            revealSurrounding(yPosition, xPosition);
        }
    }

    public void generateDisplay(int width, int height) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(width, height));
        this.getContentPane().setBackground(new Color(68, 84, 31));
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

    public void createLabel() {
        label = new JLabel();
        label.setFocusable(false);
        label.setVisible(true);

        label.setBorder(null);
        label.setFont(new Font("MV Boli", Font.BOLD, 25));
        label.setBackground(Color.ORANGE);

        label.setBounds((int) (getBoundSize()[1] * 0.05), (int) (getBoundSize()[0] * 0.02),
                (int) (getBoundSize()[1] * 0.25), (int) (getBoundSize()[0] * 0.1));

        this.add(label);
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

    public int[] getBoundSize() {
        Rectangle r = this.getBounds();
        return new int[]{r.height, r.width};
    }

    public void generateField(int yPosition, int xPosition) {
        Random random = new Random();
        for (int i = 0; i < amountBombs; i++) {
            boolean setBomb = false;
            while (!setBomb) {
                int x = random.nextInt(grid[0].length);
                int y = random.nextInt(grid.length);
                if (grid[y][x] != BOMB && !inRange(y, x, yPosition, xPosition)) {
                    grid[y][x] = BOMB;
                    setBomb = true;
                }
            }
        }
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = getBombs(y, x);
            }
        }
    }

    public boolean inRange(int bombY, int bombX, int clickY, int clickX) {
        boolean inRange = false;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (isInBound(y + clickY, x + clickX)) {
                    if (bombY == clickY + y && bombX == clickX + x) {
                        inRange = true;
                    }
                }
            }
        }
        return inRange;
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
        while (x * y < var * 2) {
            var /= 2;
        }
        return var;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == resetButton) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                restartGame();
            }
        } else if (!gameIsOver) {
            boolean foundButton = false;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    if (e.getSource() == buttons[y][x]) {
                        if (!loadGrid) {
                            generateField(y, x);
                            loadGrid = true;
                        }
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            leftClick(y, x);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            rightClick(y, x);
                        } else if (SwingUtilities.isMiddleMouseButton(e)) {
                            middleClick(y, x);
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
}
