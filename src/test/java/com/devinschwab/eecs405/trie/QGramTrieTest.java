package com.devinschwab.eecs405.trie;

import com.devinschwab.eecs405.QGram;
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

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWith0Qmin() throws Exception {
        trie = new QGramTrie(0, 10);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithQMinGreaterThanQMax() throws Exception {
        trie = new QGramTrie(11, 10);
    }

    @Test
    public void testConstructWithQGramList() throws Exception {
        List<QGram> qgrams = new LinkedList<>();
        qgrams.add(new QGram(0, "uni"));
        qgrams.add(new QGram(1, "niv"));

        trie = new QGramTrie(2, 4, qgrams);
        for(QGram qgram : qgrams) {
            assertTrue(trie.contains(qgram));
        }
    }

    @Test
    public void testConstructWithQGramListDuplicates() throws Exception {
        List<QGram> qgrams = new LinkedList<>();
        qgrams.add(new QGram(0, "nive"));
        qgrams.add(new QGram(1, "nive"));
        qgrams.add(new QGram(2, "nive"));

        trie = new QGramTrie(2, 4, qgrams);
        assertEquals(qgrams.size(), trie.getQGramFrequency("nive"));
        assertTrue(trie.contains("nive"));
        assertTrue(trie.contains("niv"));
        assertTrue(trie.contains("ni"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithListOfTooLongQGrams() throws Exception {
        List<QGram> strings = new LinkedList<>();
        strings.add(new QGram(0, "waytoolong"));
        strings.add(new QGram(1, "alsowaytoolong"));

        trie = new QGramTrie(2, 4, strings);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructWithListOfTooShortQGrams() throws Exception {
        List<QGram> strings = new LinkedList<>();
        strings.add(new QGram(0, "I"));
        strings.add(new QGram(0, "a"));

        trie = new QGramTrie(2, 4, strings);
    }

    @Test
    public void testInsert() throws Exception {
        assertFalse(trie.contains("nive"));
        trie.insert("nive");
        assertTrue(trie.contains("nive"));
        assertEquals(1, trie.getQGramFrequency("nive"));
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
    public void testInsertQGram() throws Exception {
        assertFalse(trie.contains("nive"));
        trie.insert(new QGram(0, "nive"));
        assertTrue(trie.contains("nive"));
        assertEquals(1, trie.getQGramFrequency("nive"));
        assertTrue(trie.contains("niv"));
        assertTrue(trie.contains("ni"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInsertTooLongQGram() throws Exception {
        trie.insert(new QGram(0, "waytoolong"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testInsertTooShortQGram() throws Exception {
        trie.insert(new QGram(0, "i"));
    }

    @Test
    public void testGetFrequencyExists() throws Exception {
        trie.insert("nive");
        assertEquals(1, trie.getQGramFrequency("nive"));
        assertEquals(0, trie.getQGramFrequency("niv"));
        assertEquals(0, trie.getQGramFrequency("ni"));
        trie.insert("nive");
        assertEquals(2, trie.getQGramFrequency("nive"));
        assertEquals(0, trie.getQGramFrequency("niv"));
        assertEquals(0, trie.getQGramFrequency("ni"));
    }

    @Test
    public void testGetFrequencyNonExistant() throws Exception {
        assertEquals(-1, trie.getQGramFrequency("nive"));
    }

    @Test
    public void testGetFrequencyExistsQGram() throws Exception {
        trie.insert(new QGram(0, "nive"));
        assertEquals(1, trie.getQGramFrequency(new QGram(0, "nive")));
        assertEquals(0, trie.getQGramFrequency(new QGram(1, "niv")));
        assertEquals(0, trie.getQGramFrequency(new QGram(2, "ni")));
        trie.insert(new QGram(1, "nive"));
        assertEquals(2, trie.getQGramFrequency(new QGram(0, "nive")));
        assertEquals(0, trie.getQGramFrequency(new QGram(1, "niv")));
        assertEquals(0, trie.getQGramFrequency(new QGram(2, "ni")));
    }

    @Test
    public void testGetFrequencyNonExistantGram() throws Exception {
        assertEquals(-1, trie.getQGramFrequency(new QGram(1, "nive")));
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
    public void testContainsQGram() throws Exception {
        assertFalse(trie.contains(new QGram(0, "nive")));
        trie.insert(new QGram(1, "nive"));
        assertTrue(trie.contains(new QGram(0, "nive")));
        assertTrue(trie.contains(new QGram(2, "niv")));
        assertTrue(trie.contains(new QGram(3, "ni")));
    }

    @Test
    public void testGetExtendedQGramStrings() throws Exception {
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

    @Test
    public void testGetExtendedQGrams() throws Exception {
        List<String> words = trie.getExtendedQGrams(new QGram(0, "n"));
        assertNotNull(words);
        assertEquals(0, words.size());
        trie.insert(new QGram(0, "nive"));
        words = trie.getExtendedQGrams(new QGram(1, "n"));
        assertNotNull(words);
        assertEquals(3, words.size());
        assertTrue(words.contains("nive"));
        assertTrue(words.contains("niv"));
        assertTrue(words.contains("ni"));
    }

    @Test
    public void testGetPrefixFrequency() throws Exception {
        assertEquals(-1, trie.getPrefixFrequency("a"));
        trie.insert("abc");
        trie.insert("abd");
        assertEquals(2, trie.getPrefixFrequency("a"));
        trie.insert("acd");
        assertEquals(3, trie.getPrefixFrequency("a"));
        trie.insert("bbb");
        assertEquals(3, trie.getPrefixFrequency("a"));
        assertEquals(4, trie.getPrefixFrequency(""));
    }

    @Test
    public void testTrieEquals() throws Exception {
        QGramTrie trie2 = new QGramTrie(trie.qmin, trie.qmax);
        assertTrue(trie.equals(trie2));

        trie2 = new QGramTrie(trie.qmin, trie.qmax+1);
        assertFalse(trie.equals(trie2));

        trie2 = new QGramTrie(trie.qmin+1, trie.qmax);
        assertFalse(trie.equals(trie2));

        trie2 = new QGramTrie(trie.qmin, trie.qmax);
        assertTrue(trie.equals(trie2));
        trie2.insert("uni");
        assertFalse(trie.equals(trie2));
        trie.insert("uni");
        assertTrue(trie.equals(trie2));
        trie2.insert("uni");
        assertFalse(trie.equals(trie2));
        trie.insert("uni");
        assertTrue(trie.equals(trie2));
        trie2.insert("un");
        assertFalse(trie.equals(trie2));
    }

    @Test
    public void testTrieHashCode() throws Exception {
        QGramTrie trie2 = new QGramTrie(trie.qmin, trie.qmax);
        assertEquals(trie.hashCode(), trie2.hashCode());

        trie2 = new QGramTrie(trie.qmin, trie.qmax+1);
        assertNotEquals(trie.hashCode(), trie2.hashCode());

        trie2 = new QGramTrie(trie.qmin+1, trie.qmax);
        assertNotEquals(trie.hashCode(), trie2.hashCode());

        trie2 = new QGramTrie(trie.qmin, trie.qmax);
        assertEquals(trie.hashCode(), trie2.hashCode());
        trie2.insert("uni");
        assertNotEquals(trie.hashCode(), trie2.hashCode());
        trie.insert("uni");
        assertEquals(trie.hashCode(), trie2.hashCode());
        trie2.insert("uni");
        assertNotEquals(trie.hashCode(), trie2.hashCode());
        trie.insert("uni");
        assertEquals(trie.hashCode(), trie2.hashCode());
        trie2.insert("un");
        assertNotEquals(trie.hashCode(), trie2.hashCode());
    }
}