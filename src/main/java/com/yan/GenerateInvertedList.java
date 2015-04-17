package com.yan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


public class GenerateInvertedList {
	public static int q=3;
	//public static void main(String[] args) throws IOException {
	public Hashtable<String, ArrayList<Integer>> generateInvertedList() throws IOException {
		Hashtable<String, ArrayList<Integer>> pairs = new Hashtable<>();
		
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\OneDrive\\Workspace\\EECS405\\data\\actorsList.txt"));
	    try {
	        
	        String line = br.readLine();
	        String[] record = null;
	        int id;
	        String[] qGram;
	        
	        while (line != null) {
	        	System.out.println(line);
	        	
	        	//do with it 
	        	record = line.split(":");
	        	id=Integer.parseInt(record[0]);
	        	qGram= new String[500];
	    		for(int i=1;i<=record[1].length()-q+1;i++){
	    			qGram[i]=record[1].substring(i-1, i-1+q);
	    		}
	    		for(int i=1;i<=record[1].length()-q+1;i++){
	    			if (pairs.containsKey(qGram[i])) {
		        		pairs.get(qGram[i]).add(id);
		        	} else {
		        		ArrayList<Integer> list = new ArrayList<Integer>();
		        		list.add(id);
		        		pairs.put(qGram[i], list);
		        		
		        	}
	    		}
	        	
	        	line = br.readLine();
	        }
	      
	    } finally {
	        br.close();
	    }
		/*write
	    PrintWriter printWriter = new PrintWriter(new File("D:\\OneDrive\\Workspace\\EECS405\\data\\invertedLists.txt"));

	    Iterator<String> iterator = pairs.keySet().iterator();
	    while (iterator.hasNext()) {
	    	
	    	String key = (String) iterator.next();
	    	
	    	ArrayList<Integer> arrayList = pairs.get(key);

	    	String string = "";
	    	for (int i = 0; i < arrayList.size() - 1; i++) {
	    		string += arrayList.get(i) + ",";
	    	}
	    	
	    	string += arrayList.get(arrayList.size() - 1);
	    	System.out.println(key);
	    	printWriter.println(key + ":" + string);
	    	
	    }
	    
	    printWriter.flush();
	    printWriter.close();
	    
	    */
	    
	    
	    
	    
	    
	    
	    System.out.print(0);
	    return pairs;
	}
}
