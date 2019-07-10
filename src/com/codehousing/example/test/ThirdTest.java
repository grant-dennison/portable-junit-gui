package com.codehousing.example.test;

import com.codehousing.junitgui.gui.TestDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@TestDescription("A test that does something (not so) fancy")
public class ThirdTest {

    @Test
    public void testExample1() {
        assertEquals(1, 1);
    }

    @Test
    public void testExample2() {
        assertEquals(1, 2);
    }
}
