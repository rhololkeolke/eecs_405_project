package com.yan;

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
 * Created by Devin on 4/19/15.
 */
public class ConstructStaticIndexBenchmark {
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

        @Parameter(names = "--qmin", description = "start of q size range")
        public int qmin = 2;

        @Parameter(names = "--qmax", description = "end of q size range")
        public int qmax = 2;

        @Parameter(names = "--qstep", description = "step size of q")
        public int qstep = 1;

        @Override
        public void validate(String name, String value) throws ParameterException {
            if(name.equals("--in")) {
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
            } else if(name.equals("--qmax") || name.equals("--qmin")) {
                if (Integer.parseInt(value) < 2) {
                    throw new ParameterException("q must be at least 2");
                }
            } else if (name.equals("--qstep")) {
                if (Integer.parseInt(value) < 1) {
                    throw new ParameterException("qstep must be at least 1");
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

        if (args.qmin > args.qmax) {
            System.err.println("qmin must be <= qmax");
            System.exit(1);
        }

        if (!args.indexDir.exists()) {
            args.indexDir.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(args.dataFile))) {

            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.DEFAULT);

            for (int q = args.qmin; q <= args.qmax; q += args.qstep) {
                String datasetName = args.inFile.getName();
                datasetName = datasetName.substring(0, datasetName.lastIndexOf('.'));
                printer.print(datasetName);
                printer.print(q);

                SimpleStopwatch stopwatch = new SimpleStopwatch();
                System.out.println(String.format("Constructing inverted lists for %s, q: %d", datasetName, q));
                stopwatch.start();
                List<Duration> durations = GenerateInvertedList.generateAndSaveInvertedLists(args.inFile, args.indexDir, q);
                stopwatch.stop();
                System.out.println("Total time to construct: " + stopwatch.toString());

                for (Duration duration : durations) {
                    printer.print(duration.getNano());
                }
                printer.print(stopwatch.getDuration().getNano());
                printer.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
