package com.codehousing.example.test;

import com.codehousing.junitgui.gui.TestDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AnotherTest {

    @Test
    public void testExample1() {
        assertEquals(1, 1);
    }

    @Test
    @TestDescription("This one actually has a name")
    public void testExample2() {
        assertEquals(1, 2);
    }
}
