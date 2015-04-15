package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportImdbDatasets {

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
            }
        }
    }

    static final Pattern dataStartPattern = Pattern.compile("^Name[\\s]+Titles[\\s]*$");
    static final Pattern namePattern = Pattern.compile("^(.*,.*)\\t+");

    public static void main(String rawArgs[]) throws ClassNotFoundException {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        CharsetDecoder latin1Decoder=Charset.forName("latin1").newDecoder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(args.inFile), latin1Decoder));
             BufferedWriter bw = new BufferedWriter(new FileWriter(args.outFile));
        )
        {

            String currLine;
            while ((currLine = br.readLine()) != null) {
                Matcher lineMatch = dataStartPattern.matcher(currLine);
                if (lineMatch.matches()) {
                    break;
                }
            }

            // skip the next line
            // it is just a horizontal break
            if (br.readLine() == null) {
                System.err.println("Expected separator line following data column names.");
                System.exit(2);
            }

            CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT);

            int numNamesInserted = 0;
            while((currLine = br.readLine()) != null) {
                Matcher nameMatch = namePattern.matcher(currLine);
                if(nameMatch.find()) {
                    csvPrinter.print(nameMatch.group(1));
                    csvPrinter.println();
                    numNamesInserted++;
                    if (numNamesInserted % 1000 == 0) {
                        System.out.println("Inserted " + numNamesInserted);
                    }
                    if (args.limit > 0 && numNamesInserted >= args.limit) {
                        System.out.println("Limit reached!");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
