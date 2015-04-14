package com.devinschwab.eecs405.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportImdbDatasets {

    static final Pattern dataStartPattern = Pattern.compile("^Name[\\s]+Titles[\\s]*$");
    static final Pattern namePattern = Pattern.compile("^(.*,.*)\\t+");

    static class CommandLineArgs {
        @Parameter(names = "-in",
                required = true,
                converter = FileArgConverter.class,
                description = "IMDB Data file to import")
        public File imdbDataFile;

        @Parameter(names = "-db",
                required = true,
                converter = FileArgConverter.class,
                description = "SQLite Database Name")
        public File dbName;
    }

    public static void main(String rawArgs[]) {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        if (!args.imdbDataFile.isFile()) {
            throw new IllegalArgumentException(args.imdbDataFile.toString() + " is not a file.");
        }

        CharsetDecoder latin1Decoder=Charset.forName("latin1").newDecoder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(args.imdbDataFile), latin1Decoder)))
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

            PrintStream ps = new PrintStream(System.out, true, "UTF8");
            while((currLine = br.readLine()) != null) {
                Matcher nameMatch = namePattern.matcher(currLine);
                if(nameMatch.find()) {
                    ps.println(nameMatch.group(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
