package com.google.test;

import com.google.ClickHandler;
import com.google.Display;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class ClickHandlerTest {
    Display display;
    ClickHandler clickHandler;

    /**
     * Sets up all necessary object for the tests
     */
    @BeforeEach
    void setUp(){
         display = new Display(500, 550, 15, 15, 99);
         clickHandler = new ClickHandler();
         display.setGrid(1,1);
    }

    /**
     * checks if the right click methode works
     */
    @Test
    void rightClick() {
        clickHandler.leftClick(display, 3,2);
        assertTrue(display.getRevealGrid()[3][2]);
    }

    /**
     * checks if the game over implementation is working
     */
    @Test
    void clickOnBomb_GameOver() {
        clickHandler.leftClick(display, 3,2);
        assertTrue(display.isGameIsOver());
    }

    /**
     * checks if the left click methode works
     */
    @Test
    void leftClick() {
        clickHandler.rightClick(display, 7,7);
        assertTrue(display.getFlagGrid()[7][7]);
    }
}