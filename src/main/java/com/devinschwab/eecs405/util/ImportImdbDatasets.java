package com.devinschwab.eecs405.util;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class ImportImdbDatasets {

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

        System.out.println(args.imdbDataFile.toString());
        System.out.println(args.dbName.toString());
    }
}
