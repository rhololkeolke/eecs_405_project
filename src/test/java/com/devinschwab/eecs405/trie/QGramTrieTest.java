package com.devinschwab.eecs405.trie;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/8/15.
 */
public class QGramTrieTest {

    QGramTrie trie;

    @Before
    public void setUp() throws Exception {
        trie = new QGramTrie(2, 4);
    }

    @Test
    public void testConstructWithList() throws Exception {
        List<String> qgrams = new LinkedList<>();
        qgrams.add("uni");
        qgrams.add("niv");
        qgrams.add("ive");
        qgrams.add("ver");
        qgrams.add("ers");

        trie = new QGramTrie(2, 4, qgrams);
        assertEquals(qgrams.size() * 2, trie.getNumQGrams());
        for(String s : qgrams) {
            assertTrue(trie.contains(s));
        }
    }

    @Test
    public void testConstructWithListDuplicates() throws Exception {
        List<String> qgrams = new LinkedList<>();
        qgrams.add("nive");
        qgrams.add("nive");
        qgrams.add("nive");

        trie = new QGramTrie(2, 4, qgrams);
        assertEquals(qgrams.size(), trie.getFrequency("nive"));
        assertTrue(trie.contains("nive"));
        assertTrue(trie.contains("niv"));
        assertTrue(trie.contains("ni"));
        assertEquals(3, trie.getNumQGrams());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithListOfTooLongStrings() throws Exception {
        List<String> strings = new LinkedList<>();
        strings.add("waytoolong");
        strings.add("alsowaytoolong");

        trie = new QGramTrie(2, 4, strings);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithListOfTooShortStrings() throws Exception {
        List<String> strings = new LinkedList<>();
        strings.add("I");
        strings.add("a");

        trie = new QGramTrie(2, 4, strings);
    }

    @Test
    public void testInsert() throws Exception {
        assertFalse(trie.contains("nive"));
        trie.insert("nive");
        assertTrue(trie.contains("nive"));
        assertEquals(1, trie.getFrequency("nive"));
        assertTrue(trie.contains("niv"));
        assertTrue(trie.contains("ni"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInsertTooLong() throws Exception {
        trie.insert("waytoolong");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInsertTooShort() throws Exception {
        trie.insert("i");
    }

    @Test
    public void testGetFrequencyExists() throws Exception {
        trie.insert("nive");
        assertEquals(1, trie.getFrequency("nive"));
        assertEquals(0, trie.getFrequency("niv"));
        assertEquals(0, trie.getFrequency("ni"));
        trie.insert("nive");
        assertEquals(2, trie.getFrequency("nive"));
        assertEquals(0, trie.getFrequency("niv"));
        assertEquals(0, trie.getFrequency("ni"));
    }

    @Test
    public void testGetFrequencyNonExistant() throws Exception {
        assertEquals(-1, trie.getFrequency("nive"));
    }

    @Test
    public void testContains() throws Exception {
        assertFalse(trie.contains("nive"));
        trie.insert("nive");
        assertTrue(trie.contains("nive"));
        assertTrue(trie.contains("niv"));
        assertTrue(trie.contains("ni"));
    }

    @Test
    public void testGetNumQGrams() throws Exception {
        assertEquals(0, trie.getNumQGrams());
        trie.insert("nive");
        assertEquals(3, trie.getNumQGrams());
        trie.insert("nive");
        assertEquals(3, trie.getNumQGrams()); // don't count duplicates
        trie.insert("fox");
        assertEquals(5, trie.getNumQGrams());
    }

    @Test
    public void testGetWordsStartingWith() throws Exception {
        List<String> words = trie.getExtendedQGrams("qu");
        assertNotNull(words);
        assertEquals(0, words.size());
        trie.insert("nive");
        words = trie.getExtendedQGrams("n");
        assertNotNull(words);
        assertEquals(3, words.size());
        assertTrue(words.contains("nive"));
        assertTrue(words.contains("niv"));
        assertTrue(words.contains("ni"));
    }
}