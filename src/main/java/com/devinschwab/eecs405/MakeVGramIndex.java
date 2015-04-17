package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import com.devinschwab.eecs405.util.SimpleStopwatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Devin on 4/15/15.
 */
public class MakeVGramIndex {

    static class CommandLineArgs implements IParameterValidator {
        @Parameter(names = "-in",
                required = true,
                converter = FileArgConverter.class,
                description = "IMDB Data file to import")
        public File inFile;

        @Parameter(names = "-out",
                required = true,
                converter = FileArgConverter.class,
                description = "Index output directory")
        public File outDir;

        @Parameter(names = "-l", description = "Insert up to this many names")
        public int limit;

        @Parameter(names = "--qmin",  required = true, description = "Minimum q-gram size")
        public int qmin = 2;

        @Parameter(names = "--qmax", required = true, description = "Maximum q-gram size")
        public int qmax = 4;

        @Parameter(names = "--threshold", required = true, description = "Pruning threshold")
        public int threshold = 500;

        @Parameter(names = "-k", required = true, description = "Maximum NAG to calculate")
        public int k = 10;

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (name.equals("-l")) {
                int limit = Integer.parseInt(value);
                if (limit <= 0) {
                    throw new ParameterException("Limit must be at least 1");
                }
            } else if(name.equals("-in")) {
                File inFile = new File(value);
                if (!inFile.isFile()) {
                    throw new ParameterException("Input file must exist");
                }
            } else if(name.equals("-out")) {
                File directory = new File(value).getParentFile();
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                if (!directory.isDirectory()) {
                    throw new ParameterException("Failed to create output folder");
                }
            } else if(name.equals("--qmin")) {
                if (Integer.parseInt(value) < 1) {
                    throw new ParameterException("qmin must be at least 1");
                }
            } else if(name.equals("--qmax")) {
                if (Integer.parseInt(value) < 2) {
                    throw new ParameterException("qmax must be at least 2");
                }
            } else if(name.equals("--threshold")) {
                if (Integer.parseInt(value) < 1) {
                    throw new ParameterException("threshold must be at least 1");
                }
            }
        }
    }

    public static void main(String rawArgs[]) throws ClassNotFoundException, IOException {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        if (args.qmin >= args.qmax) {
            System.err.println("qmin must be less than qmax");
            System.exit(1);
        }

        constructVGramIndex(args.inFile, args.outDir, args.qmin, args.qmax, args.k, args.threshold);
    }

    public static List<Duration> constructVGramIndex(File inFile, File outDir, int qmin, int qmax, int k, int threshold) throws IOException {
        List<Duration> durations = new LinkedList<>();

        VGramIndex vGramIndex = new VGramIndex(qmin, qmax);

        System.out.println("Starting construction of Trie");
        SimpleStopwatch stopwatch = new SimpleStopwatch();
        stopwatch.start();

        CSVParser parser = CSVParser.parse(inFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        int numAdded = 0;
        for (CSVRecord csvRecord : parser) {
            vGramIndex.gramDict.addStringToFrequencyTrie(csvRecord.get(0));
        }
        stopwatch.stop();
        System.out.println(String.format("Added %d strings to Trie in %s", numAdded, stopwatch.toString()));
        durations.add(stopwatch.getDuration());

        List<String> allGrams = vGramIndex.gramDict.dictionaryTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Pruning trie");
        stopwatch.start();
        vGramIndex.gramDict.prune(threshold);
        stopwatch.stop();
        System.out.println("Pruned in " + stopwatch.toString());
        durations.add(stopwatch.getDuration());

        allGrams = vGramIndex.gramDict.dictionaryTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in pruned trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Constructing inverted trie");
        stopwatch.start();
        vGramIndex.gramDict.generateInverseTrie();
        stopwatch.stop();
        System.out.println("Constructed inverted trie in " + stopwatch.toString());
        durations.add(stopwatch.getDuration());

        allGrams = vGramIndex.gramDict.inverseTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in inverse trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Computing the VGRAMS");
        stopwatch.start();

        try {
            parser = CSVParser.parse(inFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                int index = (int)csvRecord.getRecordNumber();
                String s = csvRecord.get(0);
                List<QGram> grams = vGramIndex.gramDict.generateVGrams(s);
                List<Integer> nagVector = vGramIndex.nagVectorGenerator.generate(s, k);

                // save the grams and the nag to index
                vGramIndex.numVGrams.put(index, grams.size());
                vGramIndex.nagList.put(index, nagVector);

                // add index to inverted list for each gram
                for (QGram qgram : grams) {
                    List<Integer> invertedList = vGramIndex.invertedList.get(qgram.gram);
                    if (invertedList == null) {
                        invertedList = new ArrayList<>();
                        vGramIndex.invertedList.put(qgram.gram, invertedList);
                    }
                    invertedList.add(index);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopwatch.stop();
        System.out.println("Finished constructing index in " + stopwatch.toString());
        durations.add(stopwatch.getDuration());

        stopwatch.reset();
        System.out.println("Saving serialized index to disk");
        stopwatch.start();

        File indexFile = new File(outDir, String.format("%s_%d_%d_%d_%d_vgram.index", inFile.getName(), qmin, qmax, k, threshold));
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(indexFile, false))) {
            out.writeObject(vGramIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopwatch.stop();
        System.out.println("Finished writing index to " + indexFile.toString() + " in " + stopwatch.toString());
        durations.add(stopwatch.getDuration());

        return durations;
    }
}
