package com.devinschwab.eecs405.mergeskip;

import com.devinschwab.eecs405.QGram;
import com.yan.GenerateInvertedList;

import java.util.*;

/**
 * Created by Devin on 4/19/15.
 */
public class MergeSkip {

    public Map<String, List<Integer>> invertedLists;

    public class HeapItem implements Comparable<HeapItem> {
        public int listId;
        public int stringId;

        public HeapItem(int listId, int stringId) {
            this.listId = listId;
            this.stringId = stringId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HeapItem heapItem = (HeapItem) o;

            if (listId != heapItem.listId) return false;
            return stringId == heapItem.stringId;

        }

        @Override
        public int hashCode() {
            int result = listId;
            result = 31 * result + stringId;
            return result;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", listId, stringId);
        }

        @Override
        public int compareTo(HeapItem o) {
            if (this.stringId > o.stringId) {
                return 1;
            } else if (this.stringId == o.stringId) {
                return 0;
            }
            return -1;
        }
    }

    public MergeSkip(Map<String, List<Integer>> invertedLists) {
        this.invertedLists = invertedLists;
    }

    public List<Integer> mergeLists(String s, int q, int k) {
        List<QGram> qgrams = GenerateInvertedList.generateQGrams(s, q);

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

        // the actual algorithm
        List<Integer> R = new LinkedList<>();

        // add fronteirs to heap
        PriorityQueue<HeapItem> frontierHeap = new PriorityQueue<>();
        for (int i = 0; i < ridLists.size(); i++) {
            List<Integer> ridList = ridLists.get(i);
            if (!ridList.isEmpty()) {
                Integer stringId = ridLists.get(i).remove(0);
                frontierHeap.add(new HeapItem(i, stringId));
            }
        }

        while (!frontierHeap.isEmpty()) {
            HeapItem topID = frontierHeap.peek();
            List<HeapItem> poppedItems = new ArrayList<>();
            while (!frontierHeap.isEmpty() && topID.stringId == frontierHeap.peek().stringId) {
                poppedItems.add(frontierHeap.poll());
            }

            // if last ID made it into R then
            // for each popped list add the next element to the frontier
            if (poppedItems.size() >= T) {
                R.add(topID.stringId);
                for (HeapItem item : poppedItems) {
                    List<Integer> ridList = ridLists.get(item.listId);
                    if (!ridList.isEmpty()) {
                        Integer stringId = ridList.remove(0);
                        frontierHeap.add(new HeapItem(item.listId, stringId));
                    }
                }
            } else { // otherwise pop T - 1 - n smallest records from H

                int remainingToPop = T - 1 - poppedItems.size();
                for (int i = 0; i < remainingToPop; i++) {
                    if (frontierHeap.isEmpty()) {
                        break;
                    }
                    poppedItems.add(frontierHeap.poll());
                }

                if (frontierHeap.isEmpty()) {
                    return R;
                }

                topID = frontierHeap.peek();
                for (HeapItem item : poppedItems) {
                    List<Integer> ridList = ridLists.get(item.listId);
                    if (!ridList.isEmpty()) {
                        int index = Collections.binarySearch(ridList, topID.stringId);
                        for (int i = 0; i < index; i++) {
                            ridList.remove(0);
                        }
                        if (!ridList.isEmpty()) {
                            frontierHeap.add(new HeapItem(item.listId, ridList.remove(0)));
                        }
                    }
                }
            }
        }

        return R;
    }
}
