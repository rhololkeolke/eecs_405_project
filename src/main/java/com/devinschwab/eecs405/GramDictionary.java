package com.devinschwab.eecs405;

import com.devinschwab.eecs405.trie.QGramTrie;

import java.util.List;

/**
 * Created by Devin on 4/8/15.
 */
public class GramDictionary {

    public QGramTrie freqTrie;

    public GramDictionary(int qmin, int qmax) {
        freqTrie = new QGramTrie(qmin, qmax);
    }

    public void addStringToFrequencyTrie(String s) {
        List<QGram> qgrams = QGram.generateQGrams(getQMax(), s);
        for(QGram qgram : qgrams) {
            freqTrie.insert(qgram);
        }
    }

    public int getQMin() {
        return freqTrie.qmin;
    }

    public int getQMax() {
        return freqTrie.qmax;
    }
}
