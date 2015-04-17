package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.VGramIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestVGram {

    //call this func to run MergeSkip with vGram
    //output is a list of candidates' id
    public ArrayList<Integer> runVGram(String query, int edThreshold, int qmin, int qmax) throws IOException {

        int Tq = 4;//query's occurence threshold
        ArrayList<List<Integer>> lists = new ArrayList<>();
        ArrayList<Integer> R;//output

        java.util.List<QGram> vGram;

        VGramIndex v = new VGramIndex(qmin, qmax);
        vGram = v.gramDict.generateVGrams(query);
        Tq = vGram.size() - v.nagVectorGenerator.generate(query, edThreshold).get(edThreshold - 1);
        //scan file and put into lists

        // TODO: Load this from serialized class or CSV file
        for (int i = 0; i <= vGram.size(); i++) {

            System.out.println("listNum=" + i);

            lists.add(v.invertedList.get(vGram.get(i)));

            lists.get(i).add(0, v.invertedList.get(vGram.get(i)).size());
        }


        VGramMergeSkip a = new VGramMergeSkip();
        R = a.vGramMergeSkip(v, lists, Tq, qmin, qmax, edThreshold);

        //printout output
        System.out.print("output R:");
        int j = 1;
        while (j < R.size()) {
            System.out.print(R.get(j) + " ");
            j += 1;
        }
        j -= 1;
        System.out.println("candidates Number=" + j);
        return R;


    }
}
