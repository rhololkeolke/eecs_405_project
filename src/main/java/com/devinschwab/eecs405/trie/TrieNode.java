package com.devinschwab.eecs405.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devin on 4/8/15.
 */
public class TrieNode implements Comparable<TrieNode> {

    public int frequency;
    public boolean isQGram;
    public int qgramFrequency;

    private Map<Character, TrieNode> children;

    public TrieNode() {
        children = new HashMap<>();
        isQGram = false;
        qgramFrequency = 0;
    }

    public TrieNode getChild(char childKey) {
        return children.get(childKey);
    }

    public TrieNode addChild(char childKey) {
        if(children.containsKey(childKey))
            return getChild(childKey);

        TrieNode child = new TrieNode();
        children.put(childKey, child);
        return child;
    }

    public TrieNode removeChild(char childKey) {
        return children.remove(childKey);
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public int getNumChildren() {
        return children.size();
    }

    @Override
    public String toString() {
        return "TrieNode(freq=" + frequency + ",isQGram=" + isQGram + ",wordFreq=" + qgramFrequency + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrieNode trieNode = (TrieNode) o;

        if (frequency != trieNode.frequency) return false;
        if (isQGram != trieNode.isQGram) return false;
        if (children.size() != trieNode.children.size()) return false;
        for(Character key : children.keySet()) {
            if(trieNode.children.get(key) == null)
                return false;
        }
        return qgramFrequency == trieNode.qgramFrequency;

    }

    @Override
    public int hashCode() {
        int result = frequency;
        result = 31 * result + (isQGram ? 1 : 0);
        result = 31 * result + qgramFrequency;
        for(Character key : children.keySet()) {
            result = 31 * result + key.hashCode();
        }
        return result;
    }

    @Override
    public int compareTo(TrieNode o) {
        if(frequency > o.frequency) {
            return 1;
        } else if(frequency == o.frequency) {
            return 0;
        } else {
            return -1;
        }
    }
}
