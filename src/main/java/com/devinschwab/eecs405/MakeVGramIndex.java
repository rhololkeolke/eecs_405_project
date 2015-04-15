package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import com.devinschwab.eecs405.util.SimpleStopwatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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
                description = "SQLite Database Name")
        public File outFile;

        @Parameter(names = "-l", description = "Insert up to this many names")
        public int limit;

        @Parameter(names = "--qmin",  required = true, description = "Minimum q-gram size")
        public int qmin;

        @Parameter(names = "--qmax", required = true, description = "Maximum q-gram size")
        public int qmax;

        @Parameter(names = "--threshold", required = true, description = "Pruning threshold")
        public int threshold;

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

        System.out.println("Starting construction of Trie");
        SimpleStopwatch stopwatch = new SimpleStopwatch();
        stopwatch.start();

        GramDictionary gramDictionary = new GramDictionary(args.qmin, args.qmax);
        CSVParser parser = CSVParser.parse(args.inFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        int numAdded = 0;
        for (CSVRecord csvRecord : parser) {
            gramDictionary.addStringToFrequencyTrie(csvRecord.get(0));
            numAdded++;
            if (numAdded > args.limit && args.limit != 0) {
                break;
            }
        }
        stopwatch.stop();
        System.out.println(String.format("Added %d strings to Trie in %s", numAdded, stopwatch.toString()));

        List<String> allGrams = gramDictionary.dictionaryTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Pruning trie");
        stopwatch.start();
        gramDictionary.prune(5);
        stopwatch.stop();
        System.out.println("Pruned in " + stopwatch.toString());

        allGrams = gramDictionary.dictionaryTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in pruned trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Constructing inverted trie");
        stopwatch.start();
        gramDictionary.generateInverseTrie();
        stopwatch.stop();
        System.out.println("Constructed inverted trie in " + stopwatch.toString());

        allGrams = gramDictionary.inverseTrie.getExtendedQGrams("", true);
        System.out.println("Number of grams in inverse trie: " + allGrams.size());

        stopwatch.reset();
        System.out.println("Computing the VGRAMS");
        stopwatch.start();
        NagVectorGenerator nagVectorGenerator = new NagVectorGenerator(gramDictionary);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(args.outFile))) {
            CSVPrinter csvOut = new CSVPrinter(bw, CSVFormat.DEFAULT);
            parser = CSVParser.parse(args.inFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
            numAdded = 0;
            for (CSVRecord csvRecord : parser) {
                String s = csvRecord.get(0);
                List<QGram> grams = gramDictionary.generateVGrams(s);
                List<Integer> nagVector = nagVectorGenerator.generate(s, 5);

                csvOut.print(s);
                for (QGram gram : grams) {
                    csvOut.print(gram.position);
                    csvOut.print(gram.gram);
                }
                for (Integer nag : nagVector) {
                    csvOut.print(nag);
                }
                csvOut.println();

                numAdded++;
                if (numAdded > args.limit && args.limit != 0) {
                    break;
                }
            }
        }
        stopwatch.stop();
        System.out.println("Wrote VG and NAG for every string in " + stopwatch.toString());
    }
}
