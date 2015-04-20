package com.yan;

import com.devinschwab.eecs405.QGram;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Devin on 4/19/15.
 */
public class GenerateInvertedListTest {

    @Test
    public void testGenerateQGrams() throws Exception {
        String test = "hello";
        List<QGram> grams = GenerateInvertedList.generateQGrams(test, 2);
        assertEquals(4, grams.size());

        List<QGram> expectedGrams = new ArrayList<>(4);
        expectedGrams.add(new QGram(0, "he"));
        expectedGrams.add(new QGram(1, "el"));
        expectedGrams.add(new QGram(2, "ll"));
        expectedGrams.add(new QGram(3, "lo"));

        for (int i = 0; i < grams.size(); i++) {
            assertEquals(expectedGrams.get(i), grams.get(i));
        }

        grams = GenerateInvertedList.generateQGrams(test, test.length()+1);
        assertEquals(0, grams.size());
    }

    @Test
    public void testGenerateInvertedList() throws Exception {
        Map<Integer, String> strings = new HashMap<>();
        strings.put(10, "hello");
        strings.put(11, "jello");

        Map<String, List<Integer>> invertedLists = GenerateInvertedList.generateInvertedList(strings, 2);
        assertEquals(5, invertedLists.size());

        List<Integer> invertedList = invertedLists.get("he");
        assertNotNull(invertedList);
        assertEquals(1, invertedList.size());
        assertEquals(10, invertedList.get(0).intValue());

        invertedList = invertedLists.get("je");
        assertNotNull(invertedList);
        assertEquals(1, invertedList.size());
        assertEquals(11, invertedList.get(0).intValue());

        invertedList = invertedLists.get("el");
        assertNotNull(invertedList);
        assertEquals(2, invertedList.size());
        assertTrue(invertedList.contains(10));
        assertTrue(invertedList.contains(11));

        invertedList = invertedLists.get("ll");
        assertNotNull(invertedList);
        assertEquals(2, invertedList.size());
        assertTrue(invertedList.contains(10));
        assertTrue(invertedList.contains(11));

        invertedList = invertedLists.get("lo");
        assertNotNull(invertedList);
        assertEquals(2, invertedList.size());
        assertTrue(invertedList.contains(10));
        assertTrue(invertedList.contains(11));
    }
}
