package com.google;

import java.awt.*;

public class Helper {
    public static int[] getBoundSize(Display bounds) {
        Rectangle r = bounds.getBounds();
        return new int[]{r.height, r.width};
    }
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
    public static boolean isInBound(int[][] grid, int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length);
    }
}
