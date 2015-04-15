package com.devinschwab.eecs405.util;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.File;

/**
 * Created by Devin on 4/15/15.
 */
public class CommandLineArgs implements IParameterValidator {
    @Parameter(names = "-in",
            required = true,
            converter = FileArgConverter.class,
            description = "IMDB Data file to import")
    public File imdbDataFile;

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