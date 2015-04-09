package com.devinschwab.eecs405;

import com.devinschwab.eecs405.trie.QGramTrie;
import com.devinschwab.eecs405.trie.TrieNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/8/15.
 */
public class GramDictionaryTest {

    GramDictionary gramDict;

    @Before
    public void setUp() throws Exception {
        gramDict = new GramDictionary(2, 3);
    }

    @Test
    public void testAddStringToFrequencyTrie() throws Exception {
        gramDict.addStringToFrequencyTrie("stick");
        gramDict.addStringToFrequencyTrie("stich");
        gramDict.addStringToFrequencyTrie("such");
        gramDict.addStringToFrequencyTrie("stuck");

        QGramTrie expectedTrie = new QGramTrie(2, 3);
        TrieNode currNode = expectedTrie.root;
        currNode.frequency = 11;
        currNode.addChild('s');
        currNode.addChild('t');
        currNode.addChild('i');
        currNode.addChild('u');
        currNode = currNode.getChild('s');
        currNode.frequency = 4;
        currNode.addChild('t');
        currNode.addChild('u');
        currNode = currNode.getChild('t');
        currNode.frequency = 3;
        currNode.isQGram = true;
        currNode.addChild('i');
        currNode.addChild('u');
        currNode = currNode.getChild('i');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;
        currNode = expectedTrie.root.getChild('s').getChild('t').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('s').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.addChild('c');
        currNode = currNode.getChild('c');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('t');
        currNode.frequency = 3;
        currNode.addChild('i');
        currNode.addChild('u');
        currNode = currNode.getChild('i');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode = currNode.addChild('c');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;
        currNode = expectedTrie.root.getChild('t').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode = currNode.addChild('c');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('i');
        currNode.frequency = 2;
        currNode = currNode.addChild('c');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.addChild('k');
        currNode.addChild('h');
        currNode = currNode.getChild('k');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('i').getChild('c').getChild('h');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('u');
        currNode.frequency = 2;
        currNode = currNode.addChild('c');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.addChild('h');
        currNode.addChild('k');
        currNode = currNode.getChild('h');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedTrie.root.getChild('u').getChild('c').getChild('k');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;

        assertTrue(expectedTrie.equals(gramDict.freqTrie));
    }

    @Test
    public void testGetQMin() throws Exception {
        assertEquals(2, gramDict.getQMin());
    }

    @Test
    public void testGetQMax() throws Exception {
        assertEquals(3, gramDict.getQMax());
    }
}