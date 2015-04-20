package com.yan;

import com.devinschwab.eecs405.VGramIndex;

import java.util.ArrayList;
import java.util.List;

public class VGramMergeSkip {
    ArrayList<Integer> heap = new ArrayList<Integer>();    //build heap
    ArrayList<Integer> R = new ArrayList<Integer>();//initialize output list
    ArrayList<Integer> Rt = new ArrayList<Integer>();
    public int listNum = 0;

    public ArrayList<Integer> vGramMergeSkip(ArrayList<List<Integer>> lists, int Tq, int qmin, int qmax, int edThreshold) {
        Integer temp;
        int T, Tc;//T=max(Tq,Tc)
        //push first element of each list to heap
        while (lists.size() > listNum) {

            Integer value = lists.get(listNum).get(1);
            heap = heappush(heap, value);
            //System.out.println("test");
            lists.get(listNum).remove(0);
            lists.get(listNum).add(0, 1);//pointer of the list
            listNum += 1;
        }
        listNum -= 1;

        while (heap.size() > 0) {
            Integer t = heap.get(0);//t is the top the heap

            //calculate T for t
            VGramIndex v = new VGramIndex(qmin, qmax);
            Tc = v.vgramList.get(t.intValue()).size() - v.nagList.get(t.intValue()).get(edThreshold).intValue();
            T = Math.max(Tq, Tc);
            //T=max(|VG(s1)|-NAG(s1, k),|VG(s2)|-NAG(s2, k))
            //pop from heap those records equals t
            Integer n = 0;//count the occurrence of t
            while (heap.get(n).intValue() == t.intValue()) {
                n = n.intValue() + 1;
                if (n >= heap.size() - 1) {
                    break;
                }
            }
            heap = heappop(heap, n);

            if (n.intValue() >= T) {
                R.add(t);
                Rt.add(n);
                //push next record of pope list
                for (int i = 0; i <= listNum; i++) {
                    if (lists.get(i).get(0).intValue() < lists.get(i).size()) {
                        if (lists.get(i).get(lists.get(i).get(0)).intValue() == t.intValue()) {
                            temp = lists.get(i).get(0).intValue() + 1;
                            lists.get(i).add(0, temp);
                            lists.get(i).remove(1);

                            if (lists.get(i).get(0).intValue() < lists.get(i).size()) {
                                Integer value = lists.get(i).get(lists.get(i).get(0).intValue()).intValue();
                                heap = heappush(heap, value);
                            }
                        }
                    }
                }
            } else {
                heap = heappop(heap, T - 1 - n.intValue());
                int t2 = heap.get(0).intValue();//t2 is the current top
                int m = 0;//count the occurrence of t

                if (heap.size() <= 0) {
                    break;
                }
                while (heap.get(m).intValue() == t2) {
                    m = m + 1;
                    if (m >= heap.size() - 1) {
                        break;
                    }
                }
                heap = heappop(heap, m);
                //jump
                System.out.println("jump");
                for (int i = 0; i <= listNum; i++) {
                    Integer pointer = lists.get(i).get(0);
                    System.out.println("pointer=" + pointer);
                    if (pointer.intValue() <= lists.get(i).size() - 1) {
                        if (lists.get(i).get(pointer.intValue()).intValue() <= t2) {
                            while (lists.get(i).get(pointer.intValue()).intValue() < t2) {
                                pointer = pointer.intValue() + 1;
                                if (pointer.intValue() > lists.get(i).size() - 1) {
                                    break;
                                }
                                lists.get(i).add(0, pointer);
                                lists.get(i).remove(1);
                            }
                            if (pointer.intValue() <= lists.get(i).size() - 1) {
                                if (lists.get(i).get(pointer.intValue()).intValue() >= t2)
                                    heap = heappush(heap, lists.get(i).get(pointer.intValue()));
                            }
                        }
                    }
                }

            }
        }
        return R;
    }

    public ArrayList<Integer> heappop(ArrayList<Integer> heap, int num) {
        System.out.println("pop " + num);
        for (int i = 1; i <= num; i++) {
            if (heap.size() > 0)
                heap.remove(0);
        }
        int j = 0;
        System.out.print("heap after pop: ");
        while (j < heap.size()) {
            System.out.print(heap.get(j) + " ");
            j = j + 1;
        }
        System.out.println();
        return heap;
    }

    public ArrayList<Integer> heappush(ArrayList<Integer> heap, Integer value) {
        System.out.println("push " + value);
        int i = heap.size();
        if (i > 0) {
            while (value.intValue() < heap.get(i - 1).intValue()) {
                i = i - 1;
                if (i == 0) {
                    break;
                }
            }
        }
        heap.add(i, value);

        int j = 0;
        System.out.print("heap after push: ");
        while (j < heap.size()) {
            System.out.print(heap.get(j) + " ");
            j = j + 1;
        }
        System.out.println();
        return heap;
    }

}
