package com.devinschwab.eecs405;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/14/15.
 */
public class NagVectorGeneratorTest {

    /**
     * This class is used to access the protected category check methods for testing purposes.
     */
    public class TestNagVectorGenerator extends NagVectorGenerator {
        public TestNagVectorGenerator(GramDictionary gramDict) {
            super(gramDict);
        }

        public boolean checkCategory(int category, String s, QGram gram, int charIndex, boolean isInsertion) {
            return super.checkCategory(category, s, gram, charIndex, isInsertion);
        }
    }

    public TestNagVectorGenerator nagGenerator;
    public List<QGram> vgrams;
    public final String testString = "universal";

    @Before
    public void setUp() {
        GramDictionary gramDict = new GramDictionary(2, 4);
        gramDict.addStringToFrequencyTrie("ni");
        gramDict.addStringToFrequencyTrie("ivr");
        gramDict.addStringToFrequencyTrie("sal");
        gramDict.addStringToFrequencyTrie("uni");
        gramDict.addStringToFrequencyTrie("vers");

        nagGenerator = new TestNagVectorGenerator(gramDict);
        String s = "universal";
        vgrams = new ArrayList<>(4);
        vgrams.add(new QGram(0, "uni"));
        vgrams.add(new QGram(2, "iv"));
        vgrams.add(new QGram(3, "vers"));
        vgrams.add(new QGram(6, "sal"));
    }

    @Test
    public void testNumPotentiallyAffectedGrams() throws Exception {

        int numPotentiallyAffected = nagGenerator.numPotentiallyAffectedGrams(testString, vgrams, 4, false);
        assertEquals(2, numPotentiallyAffected);

        numPotentiallyAffected = nagGenerator.numPotentiallyAffectedGrams(testString, vgrams, 5, false);
        assertEquals(1, numPotentiallyAffected);
    }

    @Test
    public void testCheckCategory1() throws Exception {
        assertFalse(nagGenerator.checkCategory(1, testString, vgrams.get(0), 4, false));
        assertTrue(nagGenerator.checkCategory(1, testString, vgrams.get(1), 4, false));
        assertTrue(nagGenerator.checkCategory(1, testString, vgrams.get(2), 4, false));
        assertFalse(nagGenerator.checkCategory(1, testString, vgrams.get(3), 4, false));

        assertFalse(nagGenerator.checkCategory(1, testString, vgrams.get(0), 5, false));
        assertTrue(nagGenerator.checkCategory(1, testString, vgrams.get(1), 5, false));
        assertTrue(nagGenerator.checkCategory(1, testString, vgrams.get(2), 5, false));
        assertFalse(nagGenerator.checkCategory(1, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testCheckCategory2() throws Exception {
        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(0), 4, false));
        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(1), 4, false));
        assertTrue(nagGenerator.checkCategory(2, testString, vgrams.get(2), 4, false));
        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(3), 4, false));

        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(0), 5, false));
        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(1), 5, false));
        assertTrue(nagGenerator.checkCategory(2, testString, vgrams.get(2), 5, false));
        assertFalse(nagGenerator.checkCategory(2, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testCheckCategory3() throws Exception {
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(0), 4, false));
        assertTrue(nagGenerator.checkCategory(3, testString, vgrams.get(1), 4, false));
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(2), 4, false));
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(3), 4, false));

        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(0), 5, false));
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(1), 5, false));
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(2), 5, false));
        assertFalse(nagGenerator.checkCategory(3, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testCheckCategory4() throws Exception {
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(0), 4, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(1), 4, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(2), 4, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(3), 4, false));

        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(0), 5, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(1), 5, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(2), 5, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(3), 5, false));

        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(0), 1, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(1), 1, false));
        assertTrue(nagGenerator.checkCategory(4, testString, vgrams.get(2), 1, false));
        assertFalse(nagGenerator.checkCategory(4, testString, vgrams.get(3), 1, false));
    }

    @Test
    public void testGenerate() throws Exception {
        fail();
    }
}