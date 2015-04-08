package com.devinschwab.eecs405.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devin on 4/8/15.
 */
public class TrieNode {

    public int frequency;
    public boolean isWord;
    public int wordFrequency;

    private Map<Character, TrieNode> children;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
        wordFrequency = 0;
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
}
