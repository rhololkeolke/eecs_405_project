package com.devinschwab.eecs405.mergeskip;

import com.sun.scenario.effect.Merge;
import com.yan.GenerateInvertedList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/19/15.
 */
public class MergeSkipTest {

    @Test
    public void testMergeSkip() throws Exception {

        List<String> strings = new ArrayList<>(3);
        strings.add("hello");
        strings.add("jello");
        strings.add("yellow");

        Map<String, List<Integer>> invertedLists = new HashMap<>(7);
        for (int i = 0; i < strings.size(); i++) {
            GenerateInvertedList.addStringToInvertedList(i, strings.get(i), 2, invertedLists);
        }

        MergeSkip mergeSkip = new MergeSkip(invertedLists);

        List<Integer> candidates = mergeSkip.mergeLists("hello", 2, 0);
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(0));

        candidates = mergeSkip.mergeLists("bellow", 2, 0);
        assertEquals(0, candidates.size());

        candidates = mergeSkip.mergeLists("hello", 2, 2);
        assertEquals(3, candidates.size());
        assertTrue(candidates.contains(0));
        assertTrue(candidates.contains(1));
        assertTrue(candidates.contains(2));

        candidates = mergeSkip.mergeLists("hello", 2, 3);
        assertEquals(3, candidates.size());
        assertTrue(candidates.contains(0));
        assertTrue(candidates.contains(1));
        assertTrue(candidates.contains(2));
    }

}