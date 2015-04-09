package com.devinschwab.eecs405;

import com.devinschwab.eecs405.trie.QGramTrie;
import com.devinschwab.eecs405.trie.TrieNode;

import java.util.*;

/**
 * Created by Devin on 4/8/15.
 */
public class GramDictionary {

    public QGramTrie dictionaryTrie;
    public QGramTrie inverseTrie;

    public GramDictionary(int qmin, int qmax) {
        dictionaryTrie = new QGramTrie(qmin, qmax);
        inverseTrie = new QGramTrie(qmin, qmax);
    }

    public void addStringToFrequencyTrie(String s) {
        List<QGram> qgrams = QGram.generateQGrams(getQMax(), s);
        for(QGram qgram : qgrams) {
            dictionaryTrie.insert(qgram);
        }
    }

    public void prune(int threshold) {
        prune(dictionaryTrie.root, threshold);
    }

    private void prune(TrieNode n, int threshold) {
        // the root -> n path is shorter than qmin
        if(!n.isQGram) {
            for(TrieNode c : n.getChildren().values()) {
                prune(c, threshold);
            }
            return;
        }

        // a gram corresponds to a leaf node of n
        if (n.frequency <= threshold) {
            n.getChildren().clear();
            n.isQGram = true;
            n.qgramFrequency = n.frequency;
        } else {
            List<Map.Entry<Character, TrieNode>> sortedNodes = sortNodeSet(n.getChildren().entrySet());
            int i = 0;
            for(;i<sortedNodes.size(); i++) {
                TrieNode currNode = sortedNodes.get(i).getValue();
                if(currNode.frequency + n.qgramFrequency > threshold) {
                    break;
                }
                n.qgramFrequency += currNode.frequency;
            }
            // Convert to list of keys to delete
            List<Character> keysToDelete = new LinkedList<>();
            for(int j=0; j<i; j++) {
                keysToDelete.add(sortedNodes.get(j).getKey());
            }
            // delete those keys
            for(Character key : keysToDelete) {
                n.getChildren().remove(key);
            }

            // prune remaining children
            for(TrieNode child : n.getChildren().values()) {
                prune(child, threshold);
            }
        }
    }

    public void generateInverseTrie() {
        List<String> qgrams = dictionaryTrie.getExtendedQGrams("");
        inverseTrie = new QGramTrie(getQMin(), getQMax());
        for(String qgram : qgrams) {
            inverseTrie.insert(new StringBuilder(qgram).reverse().toString());
        }
    }

    public int getQMin() {
        return dictionaryTrie.qmin;
    }

    public int getQMax() {
        return dictionaryTrie.qmax;
    }

    private List<Map.Entry<Character, TrieNode>> sortNodeSet(Collection<Map.Entry<Character, TrieNode>> c) {
        List<Map.Entry<Character, TrieNode>> list = new ArrayList<>(c);
        java.util.Collections.sort(list, new Comparator<Map.Entry<Character, TrieNode>>() {
            @Override
            public int compare(Map.Entry<Character, TrieNode> o1, Map.Entry<Character, TrieNode> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return list;
    }
}
