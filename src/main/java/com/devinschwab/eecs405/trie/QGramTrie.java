package com.devinschwab.eecs405.trie;

import com.devinschwab.eecs405.QGram;

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
        if (qmin < 1) {
            throw new IllegalArgumentException("qmin must be > 0");
        }
        if (qmin > qmax) {
            throw new IllegalArgumentException("qmax must be >= qmin");
        }
        this.root = new TrieNode();
        this.qmax = qmax;
        this.qmin = qmin;
    }

    /**
     * Construct a QGram Trie and fill it with all of the specified qgrams
     * and their prefix qgrams. The prefix qgrams have lengths from qmin to
     * the lenght of the string being inserted.
     *
     * All qgrams must have lengths in range [qmin, qmax].
     *
     * @param qmin The minimum qgram size
     * @param qmax The maximum qgram size
     * @param qgrams The qgrams to insert
     */
    public QGramTrie(int qmin, int qmax, List<QGram> qgrams) {
        this(qmin, qmax);

        for(QGram gram : qgrams) {
            this.insert(gram.gram);
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
     * Insert the qgram and all prefixes of size qmin or greater.
     *
     * @param qgram The qgram to insert
     */
    public void insert(QGram qgram) {
        insert(qgram.gram);
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
    public int getQGramFrequency(String qgram) {
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
     * Get the frequency of the q-gram.
     *
     * If the qgram is not in the Trie then this will return -1.
     *
     * Please note that automatically added prefix q-grams default to a frequency of 0.
     *
     * @param qgram The qgram to lookup the frequency of
     * @return The frequency of the qgram.
     */
    public int getQGramFrequency(QGram qgram) {
        return getQGramFrequency(qgram.gram);
    }

    /**
     * Get the frequency of this qgram prefix. Essentially this gets the frequency
     * of the sum of the frequency of the children of the specified prefix.
     *
     * @param prefix Subtree to get frequency of
     * @return Frequency of prefix or -1 if prefix is not in Trie
     */
    public int getPrefixFrequency(String prefix) {
        TrieNode currNode = root;
        for(char c : prefix.toCharArray()) {
            currNode = currNode.getChild(c);
            if(currNode == null) {
                return -1;
            }
        }
        return  currNode.frequency;
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

    /**
     * Check if the qgram is in the Trie.
     *
     * @param qgram The qgram to check for
     * @return True if in the Trie, false otherwise
     */
    public boolean contains(QGram qgram) {
        return contains(qgram.gram);
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

    /**
     * Get a list of this qgram and any qgrams this qgram is a prefix for.
     *
     * If the prefix is not a qgram in the trie then an empty list will be returned.
     *
     * @param prefix The prefix qgram to find extended qgrams for.
     * @return The list of extended qgrams in the trie
     */
    public List<String> getExtendedQGrams(QGram prefix) {
        return getExtendedQGrams(prefix.gram);
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

    public String getLongestQGram(String string) {
        TrieNode currNode = root;
        int i = 0;
        for(Character c : string.toCharArray()) {
            currNode = currNode.getChild(c);
            if(currNode == null)
                break;
            i++;
        }
        if(i >= qmin) {
            return string.substring(0, i);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QGramTrie trie = (QGramTrie) o;

        if (this.qmin != trie.qmin) return false;
        if (this.qmax != trie.qmax) return false;
        return equalsHelper(root, trie.root);

    }

    private boolean equalsHelper(TrieNode nodeA, TrieNode nodeB) {
        if(!nodeA.equals(nodeB)) {
            return false;
        }

        for(Character key : nodeA.getChildren().keySet()) {
            if (!equalsHelper(nodeA.getChild(key), nodeB.getChild(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = qmin;
        result = 31 * result + qmax;
        return hashCodeHelper(result, root);
    }

    private int hashCodeHelper(int result, TrieNode node) {
        result = 31 * result + node.hashCode();
        for(TrieNode child : node.getChildren().values()) {
            result = hashCodeHelper(result, child);
        }
        return result;
    }
}