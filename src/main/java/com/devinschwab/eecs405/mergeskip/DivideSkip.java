package com.devinschwab.eecs405.mergeskip;

import com.devinschwab.eecs405.QGram;
import com.yan.GenerateInvertedList;

import java.util.*;

/**
 * Created by Devin on 4/20/15.
 */
public class DivideSkip {

    public Map<String, List<Integer>> invertedLists;
    public MergeSkip mergeSkip;

    public DivideSkip(Map<String, List<Integer>> invertedLists) {
        this.invertedLists = invertedLists;
        this.mergeSkip = new MergeSkip(invertedLists);
    }

    public List<Integer> mergeLists(String s, int q, int k, double mu) {
        List<QGram> qgrams = GenerateInvertedList.generateQGrams(s, q);

        int M = Integer.MIN_VALUE;
        for (QGram qgram : qgrams) {
            List<Integer> invertedList = invertedLists.get(qgram.gram);
            if (invertedList != null && invertedList.size() > M) {
                M = invertedList.size();
            }
        }

        // copy the relevant lists
        List<List<Integer>> ridLists = new ArrayList<>(qgrams.size());

        for (QGram qgram : qgrams) {
            List<Integer> invertedList = invertedLists.get(qgram.gram);
            if (invertedList != null) {
                List<Integer> ridList = new ArrayList<>(invertedList);
                Collections.sort(ridList);
                ridLists.add(ridList);
            }
        }

        int T = Math.min(qgrams.size(), s.length() + q - 1 - k*q);
        if (T <= 0) {
            // when negative every id in the list is a candidate
            System.out.println("T <= 0. Returning all candidates");
            Set<Integer> candidateSet = new HashSet<>();
            for (List<Integer> ridList : ridLists) {
                candidateSet.addAll(ridList);
            }
            return new ArrayList<>(candidateSet);
        }

        double L = T/(mu*Math.log(M) + 1);

        System.out.println("L: " + L + " T - L: " + (T - (int)L));

        Collections.sort(ridLists, (o1, o2) -> o2.size() - o1.size());

        List<List<Integer>> longLists = new ArrayList<>();
        for (int i = 0; i < (int)L; i++) {
            longLists.add(ridLists.get(i));
        }

        List<List<Integer>> shortLists = new ArrayList<>();
        for (int i = (int)L; i < ridLists.size(); i++) {
            shortLists.add(ridLists.get(i));
        }

        List<Integer> shortCandidates = mergeSkip.mergeLists(shortLists, T - (int)L);

        List<Integer> R = new LinkedList<>();
        for (Integer candidate : shortCandidates) {
            int numOccurrences = 0;
            for (List<Integer> longList : longLists) {
                int index = Collections.binarySearch(longList, candidate.intValue());
                if (index >= 0 && index != longList.size() && candidate.equals(longList.get(index))) {
                    numOccurrences++;
                }
            }

            if (numOccurrences >= (int)L) {
                R.add(candidate);
            }
        }

        return R;
    }
}
