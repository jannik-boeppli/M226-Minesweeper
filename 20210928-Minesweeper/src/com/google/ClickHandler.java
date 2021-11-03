package com.google;

import javax.swing.*;

public class ClickHandler {
    public void rightClick(Display display, int y, int x) {
        JButton [][] buttons = display.getButtons();
        if (buttons[y][x].getText().equals("") || buttons[y][x].getText().equals(DefaultValues.FLAG_ICON)) {
            if (!display.getFlagGrid()[y][x]) {
                buttons[y][x].setText(DefaultValues.FLAG_ICON);
                display.setFlagGrid(y , x, true);
            } else {
                buttons[y][x].setText("");
                display.setFlagGrid(y , x, false);
            }
        }
    }

    public void middleClick(Display display, int yPosition, int xPosition) {
        int counter = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (Helper.isInBound(display.getGrid(),yPosition + y, xPosition + x)) {
                    if (display.getFlagGrid()[yPosition + y][xPosition + x]) {
                        counter++;
                    }
                }
            }
        }
        if (counter == display.getGrid()[yPosition][xPosition]) {
            display.revealSurrounding(this, yPosition, xPosition);
        }
    }

    public void leftClick(Display display, int y, int x) {
        if (!display.getFlagGrid()[y][x] && display.getButtons()[y][x].getText().equals("")) {
            if (display.getGrid()[y][x] == 0) {
                display.getButtons()[y][x].setText(" ");
                display.revealSurrounding(this, y, x);
                display.setRevealGrid(y,x, true);
                display.getButtons()[y][x].setBackground((y + x) % 2 == 0 ? DefaultValues.DARK_BROWN : DefaultValues.LIGHT_BROWN);
            } else if (display.getGrid()[y][x] == DefaultValues.BOMB) {
                display.getButtons()[y][x].setText(DefaultValues.BOMB_ICON);
                display.gameOver();
            } else {
                display.setRevealGrid(y,x, true);
                display.getButtons()[y][x].setText(display.getGrid()[y][x] + "");
                display.getButtons()[y][x].setBackground((y + x) % 2 == 0 ? DefaultValues.DARK_BROWN : DefaultValues.LIGHT_BROWN);
            }
        }
    }
}
