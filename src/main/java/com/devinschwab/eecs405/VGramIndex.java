package com.devinschwab.eecs405;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

/**
 * Created by Devin on 4/15/15.
 */
public class VGramIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    public Map<String, List<Integer>> invertedList;
    public Map<Integer, Integer> numVGrams;
    public Map<Integer, List<Integer>> nagList;

    public GramDictionary gramDict;
    public NagVectorGenerator nagVectorGenerator;

    public VGramIndex(int qmin, int qmax) {
        invertedList = new HashMap<>();
        numVGrams = new HashMap<>();
        nagList = new HashMap<>();

        gramDict = new GramDictionary(qmin, qmax);
        nagVectorGenerator = new NagVectorGenerator(gramDict);
    }

    public void saveToDisk(File outputDir) {
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File vgramFile = new File(outputDir, "vgram.index");
        File invertedListsFile = new File(outputDir, "inverted_lists.index");
        File gramDictFile = new File(outputDir, "gram_dict.index");

        try (BufferedWriter vgramWriter = new BufferedWriter(new FileWriter(vgramFile));
             BufferedWriter invertedListWriter = new BufferedWriter(new FileWriter(invertedListsFile));
             ObjectOutputStream gramDictOut = new ObjectOutputStream(new FileOutputStream(gramDictFile, false))
        ) {
            CSVPrinter vgramPrinter = new CSVPrinter(vgramWriter, CSVFormat.DEFAULT);
            List<Integer> sortedKeys = new ArrayList<>(numVGrams.keySet());
            Collections.sort(sortedKeys);
            for (Integer stringID : sortedKeys) {
                vgramPrinter.print(numVGrams.get(stringID));
                List<Integer> nagVec = nagList.get(stringID);
                for (Integer nag : nagVec) {
                    vgramPrinter.print(nag);
                }
                vgramPrinter.println();
            }

            CSVPrinter invertedListPrinter = new CSVPrinter(invertedListWriter, CSVFormat.DEFAULT);
            for (Map.Entry<String, List<Integer>> entry : invertedList.entrySet()) {
                invertedListPrinter.print(entry.getKey());
                for (Integer stringID : entry.getValue()) {
                    invertedListPrinter.print(stringID);
                }
                invertedListPrinter.println();
            }

            gramDictOut.writeObject(gramDict);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
