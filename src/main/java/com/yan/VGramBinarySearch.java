package com.yan;

import com.devinschwab.eecs405.VGramIndex;

import java.util.ArrayList;
import java.util.List;

public class VGramBinarySearch {
    ArrayList<Integer> R2 = new ArrayList<Integer>();//initialize output list

    public ArrayList<Integer> binarySearch(ArrayList<List<Integer>> longLists, int Tq, ArrayList<Integer> R, ArrayList<Integer> Rt, int qmin, int qmax, int edThreshold) {
        int occurence, T, Tc;
        for (int i = 0; i < R.size(); i++) {
            System.out.println("candidate " + i + ":" + R.get(i));
            //calculate T for candidate
            Integer t = R.get(i);
            VGramIndex v = new VGramIndex(qmin, qmax);
            Tc = v.vgramList.get(t.intValue()).size() - v.nagList.get(t.intValue()).get(edThreshold).intValue();
            T = Math.max(Tq, Tc);
            for (int n = 0; n < longLists.size(); n++) {
                //System.out.println("longLists No. "+n+":"+longLists.get(n));
                for (int m = 1; m < longLists.get(n).size(); m++) {
                    //System.out.println(longLists.get(n).get(m)+" ");
                    if (R.get(i).equals(longLists.get(n).get(m))) {
                        Integer x = Integer.valueOf(Rt.get(i).intValue() + 1);
                        System.out.println(R.get(i) + " plus one:" + x);
                        Rt.add(i, x);
                        Rt.remove(i + 1);
                        occurence = Rt.get(i);
                        if (occurence >= T) {
                            R2.add(R.get(i));
                            break;
                        }

                    }
                }
                System.out.println();
                occurence = Rt.get(i);
                if (occurence >= T) {
                    break;
                }

            }
        }

        return R2;

    }
}
