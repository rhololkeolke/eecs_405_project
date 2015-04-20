package com.yan;

import com.devinschwab.eecs405.QGram;
import com.devinschwab.eecs405.util.SimpleStopwatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.Duration;
import java.util.*;


public class GenerateInvertedList {

    public static List<Duration> generateInvertedList(File dataFile, File indexDir, int q) {

        List<Duration> durations = new LinkedList<>();

        SimpleStopwatch stopwatch = new SimpleStopwatch();
        System.out.println("Constructing inverted lists");
        stopwatch.start();

        Map<String, List<Integer>> invertedLists = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {

            CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT);

            int stringId = 0;
            for (CSVRecord record : parser) {

                String s = record.get(0);

                addStringToInvertedList(stringId, s, q, invertedLists);

                stringId++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        stopwatch.stop();
        System.out.println("Constructed inverted lists in " + stopwatch.toString());
        durations.add(stopwatch.getDuration());
        stopwatch.reset();

        System.out.println("Saving lists to file");
        stopwatch.start();

        String datasetName = dataFile.getName();
        datasetName = datasetName.substring(0, datasetName.lastIndexOf('.'));

        File invertedListFile = new File(indexDir, String.format("%s_%d_inverted_lists.index", datasetName, q));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(invertedListFile))) {

            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.DEFAULT);

            for (Map.Entry<String, List<Integer>> entry : invertedLists.entrySet()) {
                printer.print(entry.getKey());
                for (Integer id : entry.getValue()) {
                    printer.print(id);
                }
                printer.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        stopwatch.stop();
        System.out.println(String.format("Saved to %s in %s", invertedListFile.getName(), stopwatch.toString()));
        durations.add(stopwatch.getDuration());

        return durations;
    }

    public static Map<String, List<Integer>> loadFromFile(File listsFile) {
        Map<String, List<Integer>> invertedLists = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(listsFile))) {

            CSVParser parser = new CSVParser(br, CSVFormat.DEFAULT);

            for (CSVRecord record : parser) {
                String key = record.get(0);
                List<Integer> invertedList = new ArrayList<>(record.size()-1);
                for (int i = 1 ; i < record.size(); i++) {
                    invertedList.add(Integer.parseInt(record.get(i)));
                }
                invertedLists.put(key, invertedList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return invertedLists;
    }

    public static List<QGram> generateQGrams(String s, int q) {
        List<QGram> qgrams = new LinkedList<>();
        for (int i = 0; i <= s.length() - q; i++) {
            qgrams.add(new QGram(i, s.substring(i, i + q)));
        }
        return qgrams;
    }

    public static void addStringToInvertedList(int id, String s, int q, Map<String, List<Integer>> invertedLists) {
        List<QGram> qgrams = generateQGrams(s, q);

        for (QGram qgram : qgrams) {
            List<Integer> invertedList = invertedLists.get(qgram.gram);
            if (invertedList == null) {
                invertedList = new LinkedList<>();
                invertedLists.put(qgram.gram, invertedList);
            }
            invertedList.add(id);
        }
    }

    public static Map<String, List<Integer>> generateInvertedList(Map<Integer, String> strings, int q) {
        Map<String, List<Integer>> invertedLists = new HashMap<>();

        for (Map.Entry<Integer, String> stringEntry : strings.entrySet()) {
            addStringToInvertedList(stringEntry.getKey(), stringEntry.getValue(), q, invertedLists);
        }

        return invertedLists;
    }
}
