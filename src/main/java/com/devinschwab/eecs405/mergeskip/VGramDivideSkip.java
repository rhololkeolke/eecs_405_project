package com.devinschwab.eecs405.mergeskip;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.VGramIndex;

import java.util.*;

/**
 * Created by Devin on 4/20/15.
 */
public class VGramDivideSkip {

    public VGramIndex vGramIndex;
    public VGramMergeSkip mergeSkip;

    public VGramDivideSkip(VGramIndex vGramIndex) {
        this.vGramIndex = vGramIndex;
    }

    public List<Integer> mergeLists(String s, int k, double mu) {
        List<QGram> qgrams = vGramIndex.gramDict.generateVGrams(s);

        int M = Integer.MIN_VALUE;
        for (QGram qgram : qgrams) {
            List<Integer> invertedList = vGramIndex.invertedList.get(qgram.gram);
            if (invertedList != null && invertedList.size() > M) {
                M = invertedList.size();
            }
        }

        // copy the relevant lists
        List<List<Integer>> ridLists = new ArrayList<>(qgrams.size());

        for (QGram qgram : qgrams) {
            List<Integer> invertedList = vGramIndex.invertedList.get(qgram.gram);
            if (invertedList != null) {
                List<Integer> ridList = new ArrayList<>(invertedList);
                Collections.sort(ridList);
                ridLists.add(ridList);
            }
        }

        List<Integer> nagVector = vGramIndex.nagVectorGenerator.generate(s, k);

        int Tq = Math.min(qgrams.size(), qgrams.size() - nagVector.get(k-1));
        if (Tq <= 0) {
            // when negative every id in the list is a candidate
            System.out.println("T <= 0. Returning all candidates");
            Set<Integer> candidateSet = new HashSet<>();
            for (List<Integer> ridList : ridLists) {
                candidateSet.addAll(ridList);
            }
            return new ArrayList<>(candidateSet);
        }

        double L = Tq/(mu*Math.log(M) + 1);

        System.out.println("L: " + L + " T - L: " + (Tq - (int)L));

        Collections.sort(ridLists, (o1, o2) -> o2.size() - o1.size());

        List<List<Integer>> longLists = new ArrayList<>();
        for (int i = 0; i < (int)L; i++) {
            longLists.add(ridLists.get(i));
        }

        List<List<Integer>> shortLists = new ArrayList<>();
        for (int i = (int)L; i < ridLists.size(); i++) {
            shortLists.add(ridLists.get(i));
        }

        List<Integer> shortCandidates = mergeSkip.mergeLists(shortLists, k, Tq - (int)L);

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
