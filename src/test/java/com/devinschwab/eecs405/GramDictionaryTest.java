package com.devinschwab.eecs405;

import com.devinschwab.eecs405.trie.QGramTrie;
import com.devinschwab.eecs405.trie.TrieNode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/8/15.
 */
public class GramDictionaryTest {

    GramDictionary gramDict;
    QGramTrie expectedUnprunedTrie;
    QGramTrie expectedPrunedTrie;
    QGramTrie expectedInverseTrie;
    QGramTrie vgramTrie;

    @Before
    public void setUp() throws Exception {
        gramDict = new GramDictionary(2, 3);
        gramDict.addStringToFrequencyTrie("stick");
        gramDict.addStringToFrequencyTrie("stich");
        gramDict.addStringToFrequencyTrie("such");
        gramDict.addStringToFrequencyTrie("stuck");

        expectedUnprunedTrie = new QGramTrie(2, 3);
        TrieNode currNode = expectedUnprunedTrie.root;
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
        currNode = expectedUnprunedTrie.root.getChild('s').getChild('t').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedUnprunedTrie.root.getChild('s').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.addChild('c');
        currNode = currNode.getChild('c');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedUnprunedTrie.root.getChild('t');
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
        currNode = expectedUnprunedTrie.root.getChild('t').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode = currNode.addChild('c');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedUnprunedTrie.root.getChild('i');
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
        currNode = expectedUnprunedTrie.root.getChild('i').getChild('c').getChild('h');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedUnprunedTrie.root.getChild('u');
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
        currNode = expectedUnprunedTrie.root.getChild('u').getChild('c').getChild('k');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;

        expectedPrunedTrie = new QGramTrie(2, 3);
        currNode = expectedPrunedTrie.root;
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
        currNode.qgramFrequency = 1;
        currNode.addChild('i');
        currNode = currNode.getChild('i');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;
        currNode = expectedPrunedTrie.root.getChild('s').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedPrunedTrie.root.getChild('t');
        currNode.frequency = 3;
        currNode.addChild('i');
        currNode.addChild('u');
        currNode = currNode.getChild('i');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;
        currNode = expectedPrunedTrie.root.getChild('t').getChild('u');
        currNode.frequency = 1;
        currNode.isQGram = true;
        currNode.qgramFrequency = 1;
        currNode = expectedPrunedTrie.root.getChild('i');
        currNode.frequency = 2;
        currNode = currNode.addChild('c');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;
        currNode = expectedPrunedTrie.root.getChild('u');
        currNode.frequency = 2;
        currNode = currNode.addChild('c');
        currNode.frequency = 2;
        currNode.isQGram = true;
        currNode.qgramFrequency = 2;

        expectedInverseTrie = new QGramTrie(2, 3);
        currNode = expectedInverseTrie.root;
        currNode.addChild('i');
        currNode.addChild('t');
        currNode.addChild('u');
        currNode.addChild('c');
        currNode = currNode.getChild('i');
        currNode = currNode.addChild('t');
        currNode.isQGram = true;
        currNode = currNode.addChild('s');
        currNode.isQGram = true;
        currNode = expectedInverseTrie.root.getChild('t');
        currNode = currNode.addChild('s');
        currNode.isQGram = true;
        currNode = expectedInverseTrie.root.getChild('u');
        currNode.addChild('s');
        currNode.addChild('t');
        currNode = currNode.getChild('s');
        currNode.isQGram = true;
        currNode = expectedInverseTrie.root.getChild('u').getChild('t');
        currNode.isQGram = true;
        currNode = expectedInverseTrie.root.getChild('c');
        currNode.addChild('i');
        currNode.addChild('u');
        currNode = currNode.getChild('i');
        currNode.isQGram = true;
        currNode = expectedInverseTrie.root.getChild('c').getChild('u');
        currNode.isQGram = true;

        vgramTrie = new QGramTrie(2, 4);
        vgramTrie.insert("ni");
        vgramTrie.insert("ivr");
        vgramTrie.insert("sal");
        vgramTrie.insert("uni");
        vgramTrie.insert("vers");
    }

    @Test
    public void testAddStringToFrequencyTrie() throws Exception {
        assertTrue(expectedUnprunedTrie.equals(gramDict.dictionaryTrie));
    }

    @Test
    public void testPrune() throws Exception {
        gramDict.prune(2);
        assertTrue(expectedPrunedTrie.equals(gramDict.dictionaryTrie));
    }

    @Test
    public void testGenerateInverseTrie() throws Exception {
        gramDict.dictionaryTrie = expectedPrunedTrie;
        gramDict.generateInverseTrie();
        assertTrue(expectedInverseTrie.equals(gramDict.inverseTrie));
    }

    @Test
    public void testGetQMin() throws Exception {
        assertEquals(2, gramDict.getQMin());
    }

    @Test
    public void testGetQMax() throws Exception {
        assertEquals(3, gramDict.getQMax());
    }

    @Test
    public void testGenerateVGrams() throws Exception {
        gramDict = new GramDictionary(2, 4);
        gramDict.dictionaryTrie = vgramTrie;
        List<QGram> vgrams = gramDict.generateVGrams("universal");
        assertEquals(4, vgrams.size());
        assertEquals(new QGram(0, "uni"), vgrams.get(0));
        assertEquals(new QGram(2, "iv"), vgrams.get(1));
        assertEquals(new QGram(3, "vers"), vgrams.get(2));
        assertEquals(new QGram(6, "sal"), vgrams.get(3));
    }
}