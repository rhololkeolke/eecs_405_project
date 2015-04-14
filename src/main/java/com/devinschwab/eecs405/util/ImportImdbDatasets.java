package com.devinschwab.eecs405.util;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportImdbDatasets {

    static final Pattern dataStartPattern = Pattern.compile("^Name[\\s]+Titles[\\s]*$");
    static final Pattern namePattern = Pattern.compile("^(.*,.*)\\t+");

    static class CommandLineArgs implements IParameterValidator {
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
            }
        }
    }

    public static void main(String rawArgs[]) throws ClassNotFoundException {
        CommandLineArgs args = new CommandLineArgs();

        try {
            new JCommander(args, rawArgs);
        } catch(ParameterException e) {
            System.err.println("Failed to parse args. Reason: " + e.getMessage());
            new JCommander(args).usage();
            System.exit(1);
        }

        // load xerial driver
        Class.forName("org.sqlite.JDBC");

        /*try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }*/

        String dbUrl = "jdbc:sqlite:" + args.dbName.toString();

        try (Connection conn = DriverManager.getConnection(dbUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(ImportImdbDatasets.class.getResourceAsStream("imdb_schema.sql")));
        ) {

            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1000];
            while(br.read(buffer) != -1) {
                sb.append(buffer);
            }

            String sqlFileContents = sb.toString();

            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            statement.execute(sqlFileContents);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        CharsetDecoder latin1Decoder=Charset.forName("latin1").newDecoder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(args.imdbDataFile), latin1Decoder));
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + args.dbName.toString());
             PreparedStatement insertStringStmt = conn.prepareStatement("INSERT INTO imdb_actors(name) VALUES (?)");
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

            int numNamesInserted = 0;
            while((currLine = br.readLine()) != null) {
                Matcher nameMatch = namePattern.matcher(currLine);
                if(nameMatch.find()) {
                    insertStringStmt.setString(1, nameMatch.group(1));
                    insertStringStmt.addBatch();
                    if (numNamesInserted++ % 1000 == 0) {
                        System.out.println("Inserted " + numNamesInserted + " values");
                        insertStringStmt.executeBatch();
                    }
                    if (numNamesInserted >= args.limit) {
                        insertStringStmt.executeBatch();
                        System.out.println("Limit reached!");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
