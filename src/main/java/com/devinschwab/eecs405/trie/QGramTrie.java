package com.devinschwab.eecs405.trie;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Devin on 4/8/15.
 */
public class QGramTrie {
    public final TrieNode root;

    private int numQGrams = 0;
    public final int qmin;
    public final int qmax;

    /**
     * Construct an empty QGram Trie
     *
     * @param qmin The minimum qgram size
     * @param qmax The maximum qgram size
     */
    public QGramTrie(int qmin, int qmax) {
        this.root = new TrieNode();
        this.qmax = qmax;
        this.qmin = qmin;
    }

    /**
     * Construct a QGram Trie and fill it with all of the specified qgrams
     * and their prefix qgrams. The prefix qgrams have lengths from qmin to
     * the length of the string being inserted.
     *
     * All qgrams must have lengths in range [qmin, qmax].
     *
     * @param qmin The minimum qgram size
     * @param qmax The maximum qgram size
     * @param qgrams The qgrams to insert
     */
    public QGramTrie(int qmin, int qmax, List<String> qgrams) {
        this(qmin, qmax);

        for(String s : qgrams) {
            this.insert(s);
        }
    }

    /**
     * Insert the qgram and all prefixes of size qmin or greater.
     *
     * @param qgram The qgram to insert
     */
    public void insert(String qgram) {
        if(qgram.length() < qmin || qgram.length() > qmax) {
            throw new IllegalArgumentException("qgram length(" + qgram.length() + ") must be between Trie qmin(" + qmin + ") and qmax(" + qmax + ").");
        }

        TrieNode currNode = root;
        int numChars = 0;
        for(char c : qgram.toCharArray()) {
            numChars++;
            currNode.frequency++;
            currNode = currNode.addChild(c); // if child exists will return that child
            if(numChars >= qmin) {
                if (!currNode.isQGram) {
                    currNode.isQGram = true;
                    numQGrams++;
                }
            }
        }
        currNode.frequency++;
        if(!currNode.isQGram) {
            numQGrams++;
        }
        currNode.isQGram = true;
        currNode.qgramFrequency++;
    }

    /**
     * Get the frequency of the q-gram.
     *
     * If the qgram is not in the Trie then this will return -1.
     *
     * Please note that automatically added prefix q-grams default to a frequency of 0.
     *
     * @param qgram The qgram to lookup the frequency of
     * @return The frequency of the qgram.
     */
    public int getFrequency(String qgram) {
        if(qgram.length() < qmin || qgram.length() > qmax) {
            return -1;
        }
        TrieNode currNode = root;
        for(char c : qgram.toCharArray()) {
            currNode = currNode.getChild(c);
            if(currNode == null) {
                return -1;
            }
        }
        if(!currNode.isQGram)
            return -1;
        return currNode.qgramFrequency;
    }

    /**
     * Check if the qgram is in the Trie.
     *
     * @param qgram The qgram to check for
     * @return True if in the Trie, false otherwise
     */
    public boolean contains(String qgram) {
        if(qgram.length() < qmin || qgram.length() > qmax) {
            return false;
        }
        TrieNode currNode = root;
        for(char c : qgram.toCharArray()) {
            currNode = currNode.getChild(c);
            if(currNode == null) {
                return false;
            }
        }
        return currNode.isQGram;
    }

    public int getNumQGrams() {
        return numQGrams;
    }

    /**
     * Get a list of this qgram and any qgrams this qgram is a prefix for.
     *
     * If the prefix is not a qgram in the trie then an empty list will be returned.
     *
     * @param prefix The prefix qgram to find extended qgrams for.
     * @return The list of extended qgrams in the trie
     */
    public List<String> getExtendedQGrams(String prefix) {
        if(prefix.length() > qmax) {
            return new LinkedList<>();
        }
        TrieNode currNode = root;
        for(char c : prefix.toCharArray()) {
            currNode = currNode.getChild(c);
            if(currNode == null) {
                return new LinkedList<>();
            }
        }

        return getQGrams(currNode, prefix);
    }

    private List<String> getQGrams(TrieNode node, String prefix) {
        List<String> words = new LinkedList<>();
        if(node.isQGram) {
            words.add(prefix);
        }
        for(Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            for(String subTreeWord : getQGrams(entry.getValue(), prefix + entry.getKey())) {
                words.add(subTreeWord);
            }
        }
        return words;
    }
}