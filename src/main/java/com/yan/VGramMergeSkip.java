package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.VGramIndex;

import java.util.ArrayList;
import java.util.List;

public class VGramMergeSkip {
    ArrayList<Integer> heap = new ArrayList<Integer>();    //build heap
    ArrayList<Integer> R = new ArrayList<Integer>();//initialize output list
    public int listNum = 0;

    public ArrayList<Integer> vGramMergeSkip(VGramIndex v, ArrayList<List<Integer>> lists, int Tq, int qmin, int qmax, int edThreshold) {
        int T, Tc;//T=max(Tq,Tc)
        //push first element of each list to heap
        while (lists.size() > listNum) {

            int value = lists.get(listNum).get(1);
            heap = heappush(heap, value);
            //System.out.println("test");
            lists.get(listNum).remove(0);
            lists.get(listNum).add(0, 1);//pointer of the list
            listNum += 1;
        }
        listNum -= 1;

        java.util.List<QGram> vGram;

        while (heap.size() > 0) {
            int t = heap.get(0);//t is the top the heap

            //calculate T for t
            vGram = v.vgramList.get(t);
            List<Integer> nagVector = v.nagList.get(t);
            Tc = vGram.size() - v.nagList.get(t).get(edThreshold - 1);
            T = Math.max(Tq, Tc);
            //T=max(|VG(s1)|-NAG(s1, k),|VG(s2)|-NAG(s2, k))
            //pop from heap those records equals t
            int n = 0;//count the occurrence of t
            while (heap.get(n) == t) {
                n = n + 1;
                if (n >= heap.size() - 1) {
                    break;
                }
            }
            heap = heappop(heap, n);

            if (n >= T) {
                R.add(t);
                //push next record of poped list
                for (int i = 0; i <= listNum; i++) {
                    if (lists.get(i).get(lists.get(i).get(0)) == t) {
                        lists.get(i).add(0, lists.get(i).get(0) + 1);
                        lists.get(i).remove(1);
                        int value = lists.get(i).get(lists.get(i).get(0));
                        if (value > 0)
                            heap = heappush(heap, value);
                    }
                }
            } else {
                heap = heappop(heap, T - 1 - n);
                int t2 = heap.get(0);//t2 is the current top
                int m = 0;//count the occurence of t

                if (heap.size() <= 0) {
                    break;
                }
                while (heap.get(m) == t2) {
                    m = m + 1;
                    if (m >= heap.size() - 1) {
                        break;
                    }
                }
                heap = heappop(heap, m);
                //jump
                System.out.println("jump");
                for (int i = 0; i <= listNum; i++) {
                    int pointer = lists.get(i).get(0);
                    System.out.println("pointer=" + pointer);
                    if (pointer <= lists.get(i).size() - 1) {
                        if (lists.get(i).get(pointer) <= t2) {
                            while (lists.get(i).get(pointer) < t2) {
                                pointer += 1;
                                if (pointer > lists.get(i).size() - 1) {
                                    break;
                                }
                                lists.get(i).add(0, pointer);
                                lists.get(i).remove(1);
                            }
                            if (pointer <= lists.get(i).size() - 1) {
                                if (lists.get(i).get(pointer) >= t2)
                                    heap = heappush(heap, lists.get(i).get(pointer));
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

    public ArrayList<Integer> heappush(ArrayList<Integer> heap, int value) {
        System.out.println("push " + value);
        int i = heap.size();
        if (i > 0) {
            while (value < heap.get(i - 1)) {
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
