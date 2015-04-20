package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.mergeskip.MergeSkip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestStaticMergeSkip {

    public static List<Integer> runQGram(Map<String, List<Integer>> invertedLists, String query, int k, int q) {

        MergeSkip mergeSkip = new MergeSkip(invertedLists);
        return mergeSkip.mergeLists(query, q, k);

    }
}
