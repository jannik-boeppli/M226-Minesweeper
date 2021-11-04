package com.google;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Random;

import static com.google.Helper.getBoundSize;

public class Generator {
    /**
     * This methode creates all button and sets its default values
     * @param display is a reference of the object which contains the frame
     * @param mouseListener the mouse listener, which receive the actions performed (registers mouse clicks)
     */
    public void createButtons(Display display, MouseListener mouseListener) {
        int [][] grid = display.getGrid();
        final double fieldXSize = 0.98 / grid[0].length;
        final double fieldYSize = 0.78 / grid.length;
        double positionY = 0.15;
        JButton[][] buttons = display.getButtons();
        for (int y = 0; y < grid.length; y++) {
            double positionX = 0;
            for (int x = 0; x < grid[0].length; x++) {
                buttons[y][x] = new JButton();

                buttons[y][x].addMouseListener(mouseListener);
                buttons[y][x].setFocusable(false);
                buttons[y][x].setVisible(true);

                buttons[y][x].setBorder(null);
                buttons[y][x].setFont(new Font("MV Boli", Font.BOLD, 25));
                buttons[y][x].setBackground((y + x) % 2 == 0 ? DefaultValues.DARK_GREEN : DefaultValues.LIGHT_GREEN);

                buttons[y][x].setMargin(new Insets(0, 0, 0, 0));
                int[] bounds = Helper.getBoundSize(display);
                buttons[y][x].setBounds((int) (bounds[1] * positionX), (int) (bounds[0] * positionY),
                        (int) (bounds[1] * fieldXSize), (int) (bounds[0] * fieldYSize));

                display.add(buttons[y][x]);

                positionX += fieldXSize;

            }
            positionY += fieldYSize;
        }
        JButton resetButton = display.getResetButton();
        resetButton.addMouseListener(mouseListener);
        resetButton.setFocusable(false);
        resetButton.setVisible(true);

        resetButton.setBorder(null);
        resetButton.setFont(new Font("MV Boli", Font.BOLD, 25));
        resetButton.setBackground(DefaultValues.LIGHT_GREEN);

        resetButton.setMargin(new Insets(0, 0, 0, 0));
        int[] bounds = Helper.getBoundSize(display);
        resetButton.setBounds((int) (bounds[1] * 0.65), (int) (bounds[0] * 0.02),
                (int) (bounds[1] * 0.25), (int) (bounds[0] * 0.1));

        resetButton.setText("Restart");
        display.add(resetButton);
    }

    /**
     * This methode creates the frame and sets its default values
     * @param display is a reference of the object which contains the frame
     * @param width is the width, set in the main file as min width
     * @param height is the height, set in the main file as min height
     */
    public void generateDisplay(Display display, int width, int height) {
        display.setSize(width, height);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(null);
        display.setVisible(true);
        display.setMinimumSize(new Dimension(width, height));
        display.getContentPane().setBackground(new Color(68, 84, 31));
    }

    /**
     * This methode generated the field in the background and sets its bombs
     * @param display is a reference of the object which contains the frame and the grid in the background
     * @param yPosition is the y-position of the clicked button
     * @param xPosition is the x-position of the clicked button
     */
    public void generateField(Display display, int yPosition, int xPosition) {
        Random random = new Random();
        for (int i = 0; i < display.getAmountBombs(); i++) {
            boolean setBomb = false;
            while (!setBomb) {
                int x = random.nextInt(display.getGrid()[0].length);
                int y = random.nextInt(display.getGrid().length);
                if (display.getGrid()[y][x] != DefaultValues.BOMB && !Helper.inRange(display.getGrid(), y, x, yPosition, xPosition)) {
                    display.getGrid()[y][x] = DefaultValues.BOMB;
                    setBomb = true;
                }
            }
        }
        for (int y = 0; y < display.getGrid().length; y++) {
            for (int x = 0; x < display.getGrid()[0].length; x++) {
                display.getGrid()[y][x] = display.getBombs(y, x);
            }
        }
    }

    /**
     * Created a label and sets its default values
     * @param display is a reference of the object which contains the frame
     */
    public void createLabel(Display display) {
        JLabel label = display.getLabel();
        label.setFocusable(false);
        label.setVisible(true);

        label.setBorder(null);
        label.setFont(new Font("MV Boli", Font.BOLD, 25));
        label.setBackground(Color.ORANGE);

        label.setBounds((int) (getBoundSize(display)[1] * 0.05), (int) (getBoundSize(display)[0] * 0.02),
                (int) (getBoundSize(display)[1] * 0.25), (int) (getBoundSize(display)[0] * 0.1));

        display.add(label);
    }

}
