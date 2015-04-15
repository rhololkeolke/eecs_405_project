package com.devinschwab.eecs405;

import com.devinschwab.eecs405.trie.QGramTrie;
import com.devinschwab.eecs405.trie.TrieNode;
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

    public TestNagVectorGenerator nagGeneratorCheckTester;
    public List<QGram> vgrams;
    public final String testString = "universal";

    public NagVectorGenerator nagGenerator;

    @Before
    public void setUp() {
        GramDictionary gramDict = new GramDictionary(2, 4);
        gramDict.dictionaryTrie.insert("ni");
        gramDict.dictionaryTrie.insert("ivr");
        gramDict.dictionaryTrie.insert("sal");
        gramDict.dictionaryTrie.insert("uni");
        gramDict.dictionaryTrie.insert("vers");

        nagGeneratorCheckTester = new TestNagVectorGenerator(gramDict);
        String s = "universal";
        vgrams = new ArrayList<>(4);
        vgrams.add(new QGram(0, "uni"));
        vgrams.add(new QGram(2, "iv"));
        vgrams.add(new QGram(3, "vers"));
        vgrams.add(new QGram(6, "sal"));

        GramDictionary nagGramDict = new GramDictionary(2, 3);
        QGramTrie nagDictTrie = new QGramTrie(2, 3);
        TrieNode currNode = nagDictTrie.root;
        currNode.addChild('c');
        currNode.addChild('i');
        currNode.addChild('s');
        currNode.addChild('t');
        currNode.addChild('u');
        currNode = currNode.getChild('c');
        currNode.addChild('h');
        currNode = currNode.addChild('k');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('c').getChild('h');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('i');
        currNode = currNode.addChild('c');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('s');
        currNode.addChild('t');
        currNode.addChild('u');
        currNode = currNode.getChild('t');
        currNode.isQGram = true;
        currNode = currNode.addChild('i');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('s').getChild('u');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('t');
        currNode = currNode.addChild('u');
        currNode.isQGram = true;
        currNode = nagDictTrie.root.getChild('u');
        currNode = currNode.addChild('c');
        currNode.isQGram = true;
        /*currNode.addChild('t').addChild('i').isQGram = true;
        currNode.addChild('o').addChild('i').isQGram = true;
        currNode.addChild('n').addChild('g').isQGram = true;
        currNode.getChild('n').addChild('n').isQGram = true;
        currNode.addChild('b').addChild('i').isQGram = true;
        currNode.getChild('b').addChild('o').isQGram = true;
        currNode.addChild('g').addChild('i').isQGram = true;
        currNode.getChild('g').addChild('o').isQGram = true;
        currNode.addChild('i').addChild('n').isQGram = true;
        currNode.getChild('i').getChild('n').addChild('g').isQGram = true;
        currNode.getChild('i').addChild('o').isQGram = true;
        currNode.getChild('i').addChild('t').isQGram = true;*/

        nagGramDict.dictionaryTrie = nagDictTrie;
        nagGramDict.generateInverseTrie();

        nagGenerator = new NagVectorGenerator(nagGramDict);
    }

    @Test
    public void testNumPotentiallyAffectedGrams() throws Exception {

        int numPotentiallyAffected = nagGeneratorCheckTester.numPotentiallyAffectedGrams(testString, vgrams, 4, false);
        assertEquals(2, numPotentiallyAffected);

        numPotentiallyAffected = nagGeneratorCheckTester.numPotentiallyAffectedGrams(testString, vgrams, 5, false);
        assertEquals(1, numPotentiallyAffected);
    }

    @Test
    public void testCheckCategory1() throws Exception {
        assertTrue(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(0), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(1), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(2), 4, false));
        assertTrue(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(3), 4, false));

        assertTrue(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(0), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(1), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(2), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(1, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testCheckCategory2() throws Exception {
        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(0), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(1), 4, false));
        assertTrue(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(2), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(3), 4, false));

        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(0), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(1), 5, false));
        assertTrue(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(2), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(2, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testCheckCategory3() throws Exception {
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(0), 4, false));
        assertTrue(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(1), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(2), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(3), 4, false));

        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(0), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(1), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(2), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(3, testString, vgrams.get(3), 5, false));
    }

    // This is not a great test
    @Test
    public void testCheckCategory4() throws Exception {
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(0), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(1), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(2), 4, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(3), 4, false));

        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(0), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(1), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(2), 5, false));
        assertFalse(nagGeneratorCheckTester.checkCategory(4, testString, vgrams.get(3), 5, false));
    }

    @Test
    public void testGenerate() throws Exception {
        List<Integer> expectedVector = new ArrayList<>(2);
        expectedVector.add(2);
        expectedVector.add(3);

        List<Integer> nagVector = nagGenerator.generate("stick", 2);
        assertEquals(expectedVector.size(), nagVector.size());

        for (int i = 0; i < expectedVector.size(); i++) {
            assertEquals(expectedVector.get(i), nagVector.get(i));
        }
    }
}