package com.devinschwab.eecs405;

import junit.framework.TestCase;

/**
 * Created by Devin on 4/17/15.
 */
public class EditDistanceTest extends TestCase {

    public void testCalculate() throws Exception {
        assertEquals(3, EditDistance.calculate("kitten", "sitting"));
        assertEquals(3, EditDistance.calculate("sitting", "kitten"));
        assertEquals(1, EditDistance.calculate("hello", "jello"));
        assertEquals(3, EditDistance.calculate("good", "goodbye"));
        assertEquals(0, EditDistance.calculate("test", "test"));
        assertEquals(5, EditDistance.calculate("abcde", "zyxwv"));
        assertEquals(6, EditDistance.calculate("abcdef", ""));

    }
}