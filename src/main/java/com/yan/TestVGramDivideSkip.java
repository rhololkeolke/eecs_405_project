package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.VGramIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestVGramDivideSkip {
    public ArrayList<Integer> runVGram(String query, int edThreshold, int qmin, int qmax, Integer L) throws IOException {
        Integer temp;
        int Tq;
        ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
        ArrayList<List<Integer>> longLists = new ArrayList<List<Integer>>();
        ArrayList<List<Integer>> shortLists = new ArrayList<List<Integer>>();
        ArrayList<Integer> R = new ArrayList<Integer>();
        ArrayList<Integer> Rt = new ArrayList<Integer>();
        java.util.List<QGram> vGram;
        //L=T.intValue()-1;//optimal length of long lists
        VGramIndex v = new VGramIndex(qmin, qmax);
        vGram = v.gramDict.generateVGrams(query);
        Tq = vGram.size() - v.nagVectorGenerator.generate(query, edThreshold).get(edThreshold - 1).intValue();

        for (int i = 0; i <= vGram.size(); i++) {

            System.out.println("listNum=" + i);
            temp = v.invertedList.get(vGram.get(i)).size();
            lists.add(v.invertedList.get(vGram.get(i)));
            lists.get(i).add(0, temp);
        }

        //sort the lists with lengths
        for (int i = 0; i < lists.size() - 1; i++) {
            int maxNo = i;
            for (int j = i; j < lists.size() - 1; j++) {

                if (lists.get(maxNo).get(0).intValue() < lists.get(j + 1).get(0).intValue()) {
                    maxNo = j + 1;
                }
            }
            lists.add(i, lists.get(maxNo));
            lists.remove(maxNo + 1);
        }

        for (int i = 0; i < L; i++) {
            longLists.add(lists.get(i));
        }
        for (int i = L; i < lists.size(); i++) {
            shortLists.add(lists.get(i));
        }
        VGramMergeSkip a = new VGramMergeSkip();
        R = a.vGramMergeSkip(shortLists, Tq, qmin, qmax, edThreshold);
        Rt = a.Rt;
        System.out.print("end of short list");

        //use binary search on long lists
        VGramBinarySearch b = new VGramBinarySearch();
        R = b.binarySearch(longLists, Tq, R, Rt, qmin, qmax, edThreshold);

        //printout output
        System.out.print("output R:");
        int j = 0;
        while (j < R.size()) {
            System.out.print(R.get(j) + " ");
            j += 1;
        }
        j -= 1;
        System.out.println();
        System.out.println("candidates Number=" + j);

        return R;
    }
}

