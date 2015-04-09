package com.devinschwab.eecs405.trie;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/8/15.
 */
public class TrieNodeTest {

    TrieNode node;

    @Before
    public void setUp() throws Exception {
        node = new TrieNode();
        assertEquals(0, node.frequency);
        assertFalse(node.isQGram);
        assertEquals(0, node.qgramFrequency);
    }

    @Test
    public void testGetNonExistentChild() throws Exception {
        assertNull(node.getChild('a'));
    }

    @Test
    public void testGetExistingChild() throws Exception {
        node.addChild('a');
        assertEquals(1, node.getNumChildren());
        assertNotNull(node.getChild('a'));
    }

    @Test
    public void testAddChild() throws Exception {
        TrieNode childNodeA = node.addChild('a');
        assertEquals(1, node.getNumChildren());
        assertNotNull(childNodeA);

        TrieNode childNodeB = node.addChild('b');
        assertEquals(2, node.getNumChildren());
        assertNotNull(childNodeB);

        assertNotEquals(System.identityHashCode(childNodeA), System.identityHashCode(childNodeB));
    }

    @Test
    public void testAddDuplicateChild() throws Exception {
        TrieNode childNode1 = node.addChild('a');
        assertEquals(1, node.getNumChildren());
        assertNotNull(childNode1);

        TrieNode childNode2 = node.addChild('a');
        assertEquals(1, node.getNumChildren());
        assertNotNull(childNode2);
        assertEquals(childNode1, childNode2);
    }

    @Test
    public void testRemoveExistingChild() throws Exception {
        node.addChild('a');
        assertEquals(1, node.getNumChildren());

        TrieNode child = node.removeChild('a');
        assertEquals(0, node.getNumChildren());
        assertNotNull(child);
    }

    @Test
    public void testRemoveNonExistentChild() throws Exception {
        assertEquals(0, node.getNumChildren());
        TrieNode child = node.removeChild('a');
        assertNull(child);
    }

    @Test
    public void testGetNumChildren() throws Exception {
        assertEquals(0, node.getNumChildren());
        node.addChild('a');
        assertEquals(1, node.getNumChildren());
        node.addChild('b');
        assertEquals(2, node.getNumChildren());
        node.removeChild('a');
        assertEquals(1, node.getNumChildren());
        node.removeChild('b');
        assertEquals(0, node.getNumChildren());
    }

    @Test
    public void testGetChildren() throws Exception {
        assertNotNull(node.getChildren());
        assertEquals(0, node.getChildren().size());
        node.addChild('a');
        node.addChild('b');
        Map<Character, TrieNode> children = node.getChildren();
        assertNotNull(children);
        assertEquals(2, children.size());
        assertTrue(children.containsKey('a'));
        assertTrue(children.containsKey('b'));
    }

    @Test
    public void testEquals() throws Exception {
        TrieNode node2 = new TrieNode();
        assertTrue(node.equals(node2));

        node2.frequency = 1;
        assertFalse(node.equals(node2));
        node2.frequency = node.frequency;
        assertTrue(node.equals(node2));

        node2.isQGram = !node2.isQGram;
        assertFalse(node.equals(node2));
        node2.isQGram = node.isQGram;
        assertTrue(node.equals(node2));

        node2.qgramFrequency = 10;
        assertFalse(node.equals(node2));
        node2.qgramFrequency = node.qgramFrequency;
        assertTrue(node.equals(node2));

        node2.addChild('a');
        assertFalse(node.equals(node2));
        node.addChild('a');
        assertTrue(node.equals(node2));
        node2.addChild('b');
        node.addChild('c');
        assertFalse(node.equals(node2));
    }

    @Test
    public void testHashCode() throws Exception {
        TrieNode node2 = new TrieNode();
        assertEquals(node.hashCode(), node2.hashCode());

        node2.frequency = 1;
        assertNotEquals(node.hashCode(), node2.hashCode());
        node2.frequency = node.frequency;
        assertEquals(node.hashCode(), node2.hashCode());

        node2.isQGram = !node2.isQGram;
        assertNotEquals(node.hashCode(), node2.hashCode());
        node2.isQGram = node.isQGram;
        assertEquals(node.hashCode(), node2.hashCode());

        node2.qgramFrequency = 10;
        assertNotEquals(node.hashCode(), node2.hashCode());
        node2.qgramFrequency = node.qgramFrequency;
        assertEquals(node.hashCode(), node2.hashCode());

        node2.addChild('a');
        assertNotEquals(node.hashCode(), node2.hashCode());
        node.addChild('a');
        assertEquals(node.hashCode(), node2.hashCode());
        node2.addChild('b');
        node.addChild('c');
        assertNotEquals(node.hashCode(), node2.hashCode());
    }
}