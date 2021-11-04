package com.google;

import java.awt.*;

public class Helper {
    /**
     * This method is used to get the window size of the frame
     * @param bounds is a reference of the object which contains the frame
     * @return  returns an array, which contains the height and width of the current window
     */
    public static int[] getBoundSize(Display bounds) {
        Rectangle r = bounds.getBounds();
        return new int[]{r.height, r.width};
    }

    /**
     * This methode checks if the bombs position is in range (one field around the clicked button)
     * @param grid is the playing field, used to check if the position is in bounds
     * @param bombY the y-position of the bomb
     * @param bombX the x-position of the bomb
     * @param clickY the y-position of the clicked button
     * @param clickX the x-position of the clicked button
     * @return it returns true if the position is around the clicked position
     */
    public static boolean inRange(int[][] grid, int bombY, int bombX, int clickY, int clickX) {
        boolean inRange = false;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (isInBound(grid, y + clickY, x + clickX)) {
                    if (bombY == clickY + y && bombX == clickX + x) {
                        inRange = true;
                    }
                }
            }
        }
        return inRange;
    }

    /**
     * This method checks if the position is in the bound of the grid
     * @param grid the grid, used to get the max size
     * @param y the y-position of the field to check
     * @param x the x-position of the field to check
     * @return it returns true if the position is in bound of the grid size
     */
    public static boolean isInBound(int[][] grid, int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length);
    }
}
