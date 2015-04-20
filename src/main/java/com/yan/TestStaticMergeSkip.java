package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.VGramIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestStaticMergeSkip {

    public static List<Integer> runQGram(Map<String, List<Integer>> invertedLists, String query, Integer T, int q) {
        Integer temp;
        List<List<Integer>> lists = new ArrayList<>();
        List<Integer> R;
        List<QGram> qGram = GenerateInvertedList.generateQGrams(query, q);

        //scan hashtable and put into lists
        for (int i = 0; i <= query.length() - q; i++) {
            temp = invertedLists.get(qGram.get(i).gram).size();
            lists.add(new ArrayList<>(invertedLists.get(qGram.get(i).gram)));
            lists.get(i).add(0, temp);
        }

        MergeSkip a = new MergeSkip();
        R = a.mergeSkip(lists, T);

        //printout output
        System.out.print("output R:");
        int j = 0;
        while (j < R.size()) {
            System.out.print(R.get(j) + " ");
            j += 1;
        }
        j -= 1;
        System.out.println("candidates Number=" + j);

        return R;
    }
}
