package com.yan;

import java.util.ArrayList;
import java.util.List;

public class MergeSkip {
    ArrayList<Integer> heap = new ArrayList<>();    //build heap
    ArrayList<Integer> R = new ArrayList<>();//initialize output list
    public int listNum = 0;
    ArrayList<Integer> Rt = new ArrayList<>();

    public ArrayList<Integer> mergeSkip(List<List<Integer>> lists, Integer T) {

        //push first element of each list to heap
        Integer temp;

        while (lists.size() > listNum) {

            Integer value = lists.get(listNum).get(1);
            heap = heappush(heap, value);
            //System.out.println("test");
            lists.get(listNum).remove(0);
            lists.get(listNum).add(0, 1);//pointer of the list
            listNum += 1;
        }
        listNum -= 1;

        while (heap.size() >= T) {
            System.out.println("new step");
            Integer t = heap.get(0);//t is the top the heap
            //pop from heap those records equals t
            Integer n = 1;//count the occurrence of t

            while (heap.get(n).intValue() == t.intValue()) {
                n = n + 1;
                if (n >= heap.size() - 1) {
                    break;
                }
            }
            heap = heappop(heap, n);

            if (n >= T) {
                R.add(t);
                Rt.add(n);
                //push next record of popped list
                for (int i = 0; i <= listNum; i++) {
                    if (lists.get(i).get(0) < lists.get(i).size()) {
                        if (lists.get(i).get(lists.get(i).get(0)).intValue() == t.intValue()) {
                            temp = lists.get(i).get(0) + 1;
                            lists.get(i).add(0, temp);
                            lists.get(i).remove(1);

                            if (lists.get(i).get(0) < lists.get(i).size()) {
                                Integer value = lists.get(i).get(lists.get(i).get(0));
                                heap = heappush(heap, value);
                            }
                        }
                    }
                }
            } else {
                heap = heappop(heap, T - 1 - n);
                Integer t2 = heap.get(0);//t2 is the current top
                Integer m = 0;//count the occurrence of t

                if (heap.size() <= 0) {
                    break;
                }
                while (heap.get(m).intValue() == t2.intValue()) {
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
                    if (pointer <= lists.get(i).size() - 1) {
                        if (lists.get(i).get(pointer) <= t2) {
                            while (lists.get(i).get(pointer) < t2) {
                                pointer = pointer + 1;
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

    //heap functions
    public ArrayList<Integer> heappop(ArrayList<Integer> heap, int num) {
        System.out.println("pop " + num);
        for (int i = 1; i <= num; i++) {
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
