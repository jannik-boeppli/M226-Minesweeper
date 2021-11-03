package com.google;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class ClickHandlerTest {
    Display display;
    ClickHandler clickHandler;

    @BeforeEach
    void setUp(){
         display = new Display(500, 550, 15, 15, 99);
         clickHandler = new ClickHandler();
         display.setGrid(1,1);
    }


    @Test
    void rightClick() {
        clickHandler.leftClick(display, 3,2);
        assertTrue(display.getRevealGrid()[3][2]);
    }

    @Test
    void clickOnBomb_GameOver() {
        clickHandler.leftClick(display, 3,2);
        assertTrue(display.isGameIsOver());
    }

    @Test
    void leftClick() {
        clickHandler.rightClick(display, 7,7);
        assertTrue(display.getFlagGrid()[7][7]);
    }
}