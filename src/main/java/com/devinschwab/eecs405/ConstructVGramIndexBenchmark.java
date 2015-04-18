package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import com.devinschwab.eecs405.util.SimpleStopwatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * Created by Devin on 4/17/15.
 */
public class ConstructVGramIndexBenchmark {

    static class CommandLineArgs implements IParameterValidator {
        @Parameter(names = "--in",
                required = true,
                converter = FileArgConverter.class,
                description = "IMDB Data file to import")
        public File inFile;

        @Parameter(names = "--dataFile",
                   required = true,
                   converter = FileArgConverter.class,
                   description = "CSV file to save timing data to")
        public File dataFile;

        @Parameter(names = "--indexDir",
                required = true,
                converter = FileArgConverter.class,
                description = "Directory to save indices to")
        public File indexDir;

        @Parameter(names = "-l", description = "Insert up to this many names")
        public int limit;

        @Parameter(names = "--qmin",  required = true, description = "Minimum q-gram size for all tests")
        public int qmin = 2;

        @Parameter(names = "--qmax", required = true, description = "Maximum q-gram size for all tests")
        public int qmax = 4;

        @Parameter(names = "--qminstep", description = "Step size for qmin")
        public int qminstep = 1;

        @Parameter(names = "--qmaxstep", description = "Step size for qmax")
        public int qmaxstep = 1;

        @Parameter(names = "--minT", required = true, description = "Minimum pruning threshold for all tests")
        public int minT = 100;

        @Parameter(names = "--maxT", required = true, description = "Maximum pruning threshold for all tests")
        public int maxT = 1000;

        @Parameter(names = "-k", required = true, description = "Maximum NAG to calculate")
        public int k = 10;

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (name.equals("-l")) {
                int limit = Integer.parseInt(value);
                if (limit <= 0) {
                    throw new ParameterException("Limit must be at least 1");
                }
            } else if(name.equals("--in")) {
                File inFile = new File(value);
                if (!inFile.isFile()) {
                    throw new ParameterException("Input file must exist");
                }
            } else if (name.equals("--dataFile")) {
              File dataFileDir = new File(value).getParentFile();
                if (!dataFileDir.exists()) {
                    dataFileDir.mkdirs();
                }
                if (!dataFileDir.isDirectory()) {
                    throw new ParameterException("Failed to create data file folder");
                }
            } else if(name.equals("--indexDir")) {
                File directory = new File(value);
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
            } else if(name.equals("--maxT")) {
                if (Integer.parseInt(value) < 2) {
                    throw new ParameterException("maximum threshold must be at least 2");
                }
            } else if(name.equals("--minT")) {
                if (Integer.parseInt(value) < 1) {
                    throw new ParameterException("minimum threshold must be at least 1");
                }
            }
        }
    }

    public static void main(String rawArgs[]) {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch (ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        if (args.qmin >= args.qmax) {
            System.err.println("qmin must be less than qmax");
            System.exit(1);
        }

        if (args.minT >= args.maxT) {
            System.err.println("minT must be less than maxT");
            System.exit(1);
        }

        if (!args.indexDir.exists()) {
            args.indexDir.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(args.dataFile))) {
            CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT);

            for (int qmin = args.qmin; qmin < args.qmax; qmin += args.qminstep) {
                for (int qmax = qmin + 1; qmax <= args.qmax; qmax += args.qmaxstep) {
                    for (int threshold = args.minT; threshold < args.maxT; threshold += (int)((args.maxT - args.minT)/3.0)) {
                        csvPrinter.print(args.inFile.getName());
                        csvPrinter.print(qmin);
                        csvPrinter.print(qmax);
                        csvPrinter.print(args.k);
                        csvPrinter.print(threshold);

                        SimpleStopwatch stopwatch = new SimpleStopwatch();
                        System.out.println(String.format("Constructing vgram index for %s, qmin: %d, qmax: %d, k: %d, threshold: %d", args.inFile.toString(), qmin, qmax, args.k, threshold));
                        stopwatch.start();
                        List<Duration> durations = MakeVGramIndex.constructVGramIndex(args.inFile, args.indexDir, qmin, qmax, args.k, threshold);
                        stopwatch.stop();
                        System.out.println("Total time to construct: " + stopwatch.toString());

                        for (Duration duration : durations) {
                            csvPrinter.print(duration.getNano());
                        }
                        csvPrinter.print(stopwatch.getDuration().getNano());
                        csvPrinter.println();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
