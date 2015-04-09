package com.devinschwab.eecs405;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/8/15.
 */
public class QGramTest {

    @Test
    public void testGenerateQGrams() throws Exception {
        List<QGram> grams = QGram.generateQGrams(3, "university");
        assertEquals(8, grams.size());

        List<QGram> expectedGrams = new ArrayList<>(8);
        expectedGrams.add(new QGram(0, "uni"));
        expectedGrams.add(new QGram(1, "niv"));
        expectedGrams.add(new QGram(2, "ive"));
        expectedGrams.add(new QGram(3, "ver"));
        expectedGrams.add(new QGram(4, "ers"));
        expectedGrams.add(new QGram(5, "rsi"));
        expectedGrams.add(new QGram(6, "sit"));
        expectedGrams.add(new QGram(7, "ity"));

        for(int i=0; i<expectedGrams.size(); i++) {
            assertEquals(expectedGrams.get(i), grams.get(i));
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGenerateQGramsZeroSize() throws Exception {
        QGram.generateQGrams(0, "university");
    }

    @Test
    public void testGenerateQGramsStringSize() throws Exception {
        List<QGram> grams = QGram.generateQGrams(3, "the");
        assertEquals(1, grams.size());
    }

    @Test
    public void testGenerateQGramsLargerThanString() throws Exception {
        List<QGram> grams = QGram.generateQGrams(4, "the");
        assertEquals(0, grams.size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNegativePosition() throws Exception {
        new QGram(-1, "testing");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNullGram() throws Exception {
        new QGram(0, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEmptyGram() throws Exception {
        new QGram(0, "");
    }

    @Test
    public void testQGramSize() throws Exception {
        QGram gram = new QGram(0, "test");
        assertEquals(4, gram.size());
    }

    @Test
    public void testQGramEquals() throws Exception {
        QGram gram0A = new QGram(0, "gramA");
        QGram gram0B = new QGram(0, "gramB");
        QGram gram1A = new QGram(1, "gramA");
        QGram gram0ADup = new QGram(0, "gramA");

        assertTrue(gram0A.equals(gram0ADup));
        assertFalse(gram0A.equals(gram0B));
        assertFalse(gram0A.equals(gram1A));
    }

    @Test
    public void testIsSubsumed() throws Exception {
        QGram gramA = new QGram(0, "univ");
        QGram gramB = new QGram(0, "uni");
        QGram gramC = new QGram(1, "niv");
        QGram gramD = new QGram(1, "nive");
        QGram gramE = new QGram(2, "ive");

        assertTrue(gramA.subsumes(gramB));
        assertTrue(gramA.subsumes(gramC));
        assertFalse(gramA.subsumes(gramD));
        assertFalse(gramA.subsumes(gramE));
        assertFalse(gramC.subsumes(gramA));
    }
}