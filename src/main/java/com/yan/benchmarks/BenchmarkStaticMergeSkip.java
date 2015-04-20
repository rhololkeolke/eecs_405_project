package com.yan.benchmarks;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.EditDistance;
import com.devinschwab.eecs405.mergeskip.MergeSkip;
import com.devinschwab.eecs405.util.FileArgConverter;
import com.devinschwab.eecs405.util.SimpleStopwatch;
import com.yan.GenerateInvertedList;
import com.yan.TestStaticMergeSkip;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Devin on 4/19/15.
 */
public class BenchmarkStaticMergeSkip {

    static class CommandLineArgs implements IParameterValidator {
        @Parameter(names = "--datasetFile",
                required = true,
                converter = FileArgConverter.class,
                description = "inverted lists index")
        public File datasetFile;

        @Parameter(names = "--queryFile",
                required = true,
                converter = FileArgConverter.class,
                description = "List of query strings")
        public File queryFile;

        @Parameter(names = "--benchmarkFile",
                required = true,
                converter = FileArgConverter.class,
                description = "CSV file to save timing data to")
        public File benchmarkFile;

        @Parameter(names = "--indexDir",
                required = true,
                converter = FileArgConverter.class,
                description = "Directory to load indices from")
        public File indexDir;

        @Parameter(names = "--kmin", required = true, description = "Minimum edit distance threshold to test")
        public int kmin;

        @Parameter(names = "--kmax", required = true, description = "Maximum edit distance threshold to test")
        public int kmax;

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (name.equals("--datasetFile")) {
                if (!new File(value).isFile()) {
                    throw new ParameterException(String.format("Dataset %s does not exist", value));
                }
            } else if (name.equals("--queryFile")) {
                if (!new File(value).isFile()) {
                    throw new ParameterException(String.format("Query file %s does not exist", value));
                }
            } else if (name.equals("--benchmarkFile")) {
                File benchmarkDir = new File(value).getParentFile();
                if (!benchmarkDir.exists()) {
                    benchmarkDir.mkdirs();
                }
                if (!benchmarkDir.isDirectory()) {
                    throw new ParameterException(String.format("Failed to create benchmark file directory %s",
                            benchmarkDir.getName()));
                }
            } else if (name.equals("--indexDir")) {
                if (!new File(value).isDirectory()) {
                    throw new ParameterException(String.format("%s is not a directory", value));
                }
            } else if (name.equals("--kmin")) {
                if (Integer.parseInt(value) < 0) {
                    throw new ParameterException("kmin must be at least 0");
                }
            } else if (name.equals("--kmax")) {
                if (Integer.parseInt(value) < 0) {
                    throw new ParameterException("kmax must be at least 0");
                }
            }
        }
    }

    public static Pattern indexPattern = Pattern.compile(".*_([\\d]+)_.*");

    public static void main(String rawArgs[]) {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch (ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        if (args.kmin > args.kmax) {
            System.err.println("kmin must be <= kmax");
            System.exit(1);
        }

        // get list of all index files
        List<File> staticIndexFiles = new LinkedList<>();
        for (File file : args.indexDir.listFiles()) {
            if (!file.isFile()) {
                continue;
            }
            String extension = file.getName().substring(file.getName().lastIndexOf('.'), file.getName().length());
            if (!extension.equals(".index")) {
                continue;
            }

            staticIndexFiles.add(file);
        }

        SimpleStopwatch stopwatch = new SimpleStopwatch();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(args.benchmarkFile))) {

            CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT);

            for (File indexFile : staticIndexFiles) {

                Matcher indexFileMatcher = indexPattern.matcher(indexFile.getName());
                if (!indexFileMatcher.matches()) {
                    System.err.println("Unknown qgram length!");
                    continue;
                }

                int q = Integer.parseInt(indexFileMatcher.group(1));

                System.out.println("Loading index " + indexFile.getName() + " from file");
                Map<String, List<Integer>> invertedLists = GenerateInvertedList.loadFromFile(indexFile);
                System.out.println("Finished loading");

                MergeSkip mergeSkip = new MergeSkip(invertedLists);

                for (int k = args.kmin; k < args.kmax; k++) {

                    CharsetDecoder latin1Decoder= Charset.forName("latin1").newDecoder();
                    try (BufferedReader queryReader = new BufferedReader(
                            new InputStreamReader(new FileInputStream(args.queryFile), latin1Decoder))) {

                        CSVParser queryParser = new CSVParser(queryReader, CSVFormat.DEFAULT);

                        for (CSVRecord record : queryParser) {
                            System.out.println(String.format("Mergeskip with index %s, k: %d, query: %s",
                                    indexFile.getName(), k, record.get(0)));
                            csvPrinter.print(indexFile.getName());
                            csvPrinter.print(q);
                            csvPrinter.print(k);
                            String queryString = record.get(0);
                            csvPrinter.print(queryString);

                            int T = record.get(0).length() + q - 1 - k*q;
                            csvPrinter.print(T);

                            stopwatch.reset();
                            System.out.println("Finding candidates with mergeskip");
                            stopwatch.start();
                            List<Integer> candidates = mergeSkip.mergeLists(queryString, q, k);
                            stopwatch.stop();
                            System.out.println(String.format("Found %d candidates in %s",
                                    candidates.size(), stopwatch.toString()));
                            csvPrinter.print(candidates.size());
                            csvPrinter.print(stopwatch.getDuration().getNano());
                            stopwatch.reset();

                            Set<Integer> candidateSet = new HashSet<>(candidates);

                            System.out.println("Checking candidate edit distance");
                            List<String> similarStrings = new ArrayList<>(candidates.size());
                            stopwatch.start();
                            try (BufferedReader dataReader = new BufferedReader(new InputStreamReader(new FileInputStream(args.datasetFile), latin1Decoder))) {
                                CSVParser dataParser = new CSVParser(dataReader, CSVFormat.DEFAULT);
                                int stringId = 0;
                                for (CSVRecord dataRecord : dataParser) {
                                    if (candidateSet.contains(stringId)) {

                                        int ed = EditDistance.calculate(queryString, dataRecord.get(0));
                                        if (ed <= k) {
                                            similarStrings.add(dataRecord.get(0));
                                        } else {
                                            System.out.println(String.format("ed(%s, %s) = %d > %d", queryString, dataRecord.get(0), ed, k));
                                        }
                                    }
                                    stringId++;
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stopwatch.stop();
                            System.out.println(String.format("Found %d similar strings in %s",
                                    similarStrings.size(), stopwatch.toString()));
                            csvPrinter.print(similarStrings.size());
                            csvPrinter.print(stopwatch.getDuration().getNano());

                            csvPrinter.println();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
