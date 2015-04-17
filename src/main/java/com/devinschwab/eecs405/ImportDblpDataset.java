package com.devinschwab.eecs405;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.devinschwab.eecs405.util.FileArgConverter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Devin on 4/17/15.
 */
public class ImportDblpDataset {

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

    static final Pattern authorTag = Pattern.compile("<author>(.*)</author>");

    public static void main(String[] rawArgs) {

        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args.inFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(args.outFile));
        ) {

            CSVPrinter csvPrinter = new CSVPrinter(bw, CSVFormat.DEFAULT);

            String currLine;
            while ((currLine = br.readLine()) != null) {
                Matcher lineMatch = authorTag.matcher(currLine);
                if (lineMatch.matches()) {
                    csvPrinter.print(lineMatch.group(1));
                    csvPrinter.println();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
