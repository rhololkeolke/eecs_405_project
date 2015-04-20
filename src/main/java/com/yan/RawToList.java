package com.yan;

import java.io.*;

public class RawToList {
    public static void main(String args[]) {

        try {

            //read raw imdb file
            String pathname = "D:\\OneDrive\\Workspace\\EECS405\\data\\actors.list";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String[] record;


            //write to output file
            File writename = new File("D:\\OneDrive\\Workspace\\EECS405\\data\\actorsList.txt");
            writename.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));

            int lineNo = 0;
            for (int i = 1; i <= 240; i++) {
                line = br.readLine();
            }
            while (lineNo < 2141116) {
                if (!line.equals("")) {
                    record = line.split("	");


                    if (!record[0].equals("")) {
                        lineNo += 1;
                        System.out.println(lineNo);
                        out.write(lineNo + ":" + record[0] + "\r\n");
                    }
                    //out.write(line+"\r\n");

                    //if(lineNo==2000){
                    //	break;
                    //}

                }
                line = br.readLine();

                out.flush();

            }

            out.flush();
            br.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}