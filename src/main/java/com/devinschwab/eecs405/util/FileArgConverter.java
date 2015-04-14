package com.devinschwab.eecs405.util;

import com.beust.jcommander.IStringConverter;

import java.io.File;

/**
 * Created by Devin on 4/14/15.
 */
public class FileArgConverter implements IStringConverter<File> {

    public File convert(String value) {
        return new File(value);
    }
}
