package com.devinschwab.eecs405;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Devin on 4/15/15.
 */
public class VGramIndex {

    public Map<String, List<Integer>> invertedList;
    public Map<Integer, List<String>> vgramList;
    public Map<Integer, List<Integer>> nagList;

    public GramDictionary gramDict;
    public NagVectorGenerator nagVectorGenerator;

    public VGramIndex(int qmin, int qmax) {
        invertedList = new HashMap<>();
        vgramList = new HashMap<>();
        nagList = new HashMap<>();

        gramDict = new GramDictionary(qmin, qmax);
        nagVectorGenerator = new NagVectorGenerator(gramDict);
    }
}
