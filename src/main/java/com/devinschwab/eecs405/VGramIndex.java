package com.devinschwab.eecs405;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

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

    public static VGramIndex loadFromDisk(File inputDir) {
        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException(inputDir.toString() + " is not a valid vgram index directory");
        }

        File gramDictFile = new File(inputDir, "gram_dict.index");

        VGramIndex vgramIndex = null;
        try (ObjectInputStream gramDictIn = new ObjectInputStream(new FileInputStream(gramDictFile))) {
            GramDictionary gramDict = (GramDictionary)gramDictIn.readObject();
            vgramIndex = new VGramIndex(gramDict.getQMin(), gramDict.getQMax());
            vgramIndex.gramDict = gramDict;
            vgramIndex.nagVectorGenerator = new NagVectorGenerator(vgramIndex.gramDict);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        File vgramFile = new File(inputDir, "vgram.index");
        try (BufferedReader vgramReader = new BufferedReader(new FileReader(vgramFile))) {
            CSVParser vgramParser = new CSVParser(vgramReader, CSVFormat.DEFAULT);
            int stringId = 0;
            for (CSVRecord record : vgramParser) {
                vgramIndex.numVGrams.put(stringId, Integer.parseInt(record.get(0)));

                List<Integer> nagVector = new ArrayList<>();
                for (int i = 1; i < record.size(); i++) {
                    nagVector.add(Integer.parseInt(record.get(i)));
                }
                vgramIndex.nagList.put(stringId, nagVector);

                stringId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        File invertedListFile = new File(inputDir, "inverted_lists.index");
        try (BufferedReader invertedListReader = new BufferedReader(new FileReader(invertedListFile))) {

            CSVParser invertedListParser = new CSVParser(invertedListReader, CSVFormat.DEFAULT);
            for (CSVRecord record : invertedListParser) {
                String gram = record.get(0);
                List<Integer> invertedList = new ArrayList<>(record.size()-1);
                for (int i = 1; i < record.size(); i++) {
                    invertedList.add(Integer.parseInt(record.get(i)));
                }

                vgramIndex.invertedList.put(gram, invertedList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return vgramIndex;
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
