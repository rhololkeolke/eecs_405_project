package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Devin on 4/18/15.
 */
public class DataStatistics {

    static class CommandLineArgs implements IParameterValidator {
        @Parameter(names = "--in",
                required = true,
                converter = FileArgConverter.class,
                description = "Data file")
        public File inFile;

        @Parameter(names = "--outDir",
                required = true,
                converter = FileArgConverter.class,
                description = "Directory to store data statistics in")
        public File outDir;


        @Override
        public void validate(String name, String value) throws ParameterException {
            if(name.equals("--in")) {
                File inFile = new File(value);
                if (!inFile.isFile()) {
                    throw new ParameterException("Input file must exist");
                }
            } else if(name.equals("--outDir")) {
                File outFile = new File(value);
                outFile.mkdirs();
            }
        }
    }

    public static void main(String[] rawArgs) throws IOException {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        CSVParser parser = CSVParser.parse(args.inFile, Charset.forName("UTF-8"), CSVFormat.DEFAULT);
        int numStrings = 0;
        Map<Integer, Integer> lengthCounts = new HashMap<>();
        Set<Character> dataAlphabet = new HashSet<>();

        for (CSVRecord csvRecord : parser) {
            numStrings++;

            String s = csvRecord.get(0);
            Integer lengthCount = lengthCounts.getOrDefault(s.length(), 0) + 1;
            lengthCounts.put(s.length(), lengthCount);

            for (Character c : s.toCharArray()) {
                dataAlphabet.add(c);
            }
        }

        parser.close();

        // find the min, average and max string lengths
        int minLength = Integer.MAX_VALUE;
        int maxLength = Integer.MIN_VALUE;
        long totalLength = 0;
        for (Map.Entry<Integer, Integer> lengthEntry : lengthCounts.entrySet()) {
            if (lengthEntry.getKey() < minLength) {
                minLength = lengthEntry.getKey();
            }
            if (lengthEntry.getKey() > maxLength) {
                maxLength = lengthEntry.getKey();
            }
            totalLength += (long)lengthEntry.getKey()*(long)lengthEntry.getValue();
        }

        double avgLength = (double)totalLength/(double)numStrings;

        System.out.println("Data Statistics");
        System.out.println("------------------");
        System.out.println(String.format("Dataset Name: %s", args.inFile.getName()));
        System.out.println(String.format("Number of Strings: %d", numStrings));
        System.out.println(String.format("Minimum length: %d Maximum Length: %d Average Length: %.2f", minLength, maxLength, avgLength));
        System.out.println(String.format("Alphabet Size: %d", dataAlphabet.size()));


        List<Integer> stringLengths = new ArrayList<>(lengthCounts.keySet());
        Collections.sort(stringLengths);

        String filename = args.inFile.getName();
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(args.outDir, String.format("%s_length_distribution.csv", filename.substring(0, filename.lastIndexOf('.'))))));
        CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT);
        for (Integer length : stringLengths) {
            csvPrinter.print(length);
            csvPrinter.print(lengthCounts.get(length));
            csvPrinter.println();
        }

        csvPrinter.close();
    }
}
