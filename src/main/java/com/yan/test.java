package com.yan;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;

public class test {
	public static void main(String arg[]) throws IOException {
		
		int q=3;
		int T=4;//threshold
		int L;//optimal length of longlists
		int[][] lists;
		int[][] longLists;
		int[][] shortLists;
		int[] R;
		int listNum=0;
		Hashtable<String, ArrayList<Integer>> pairs = new Hashtable<>();
		String query="Le, Bruce";
		String[] qGram;
		ArrayList<Integer> record;
		qGram= new String[500];
		for(int i=1;i<=query.length()-q+1;i++){
			qGram[i]=query.substring(i-1, i-1+q);
		}
		
		//scan file and put into lists
		lists= new int[500][120000];//inverted lists
		GenerateInvertedList l= new GenerateInvertedList();
		pairs=l.generateInvertedList();
		
		for(int i=1;i<=query.length()-q+1;i++){

			listNum+=1;
			System.out.println("listNum="+i);
			record=pairs.get(qGram[i]);
			System.out.println("record.length="+record.size());
			for(int j =1;j<=record.size();j++){
				lists[i][j]=record.get(j-1);
				System.out.print(j+":"+lists[i][j]+",");
			}
			System.out.println();
			lists[i][0]=record.size();
		}
		
		/*
		String[] record;
		try{
			String pathname = "D:\\OneDrive\\Workspace\\EECS405\\data\\invertedList.txt";
			File filename = new File(pathname); 
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); 
			BufferedReader br = new BufferedReader(reader); 
			
			
			String line="";
			line = br.readLine();
			while (line!=null&&listNum!=query.length()-q+1) {
				record=line.split(":");
				int i=1;
				while(!record[0].equals(qGram[i])){
					i+=1;
					if(i>query.length()-q+1){
						break;
					}
				}
				//if gram found, put into list
				if(i<=query.length()-q+1){
					listNum+=1;
					System.out.println("listNum="+listNum);
					record=record[1].split(",");
					System.out.println("record.length="+record.length);
					for(int j =1;j<=record.length;j++){
						lists[listNum][j]=Integer.parseInt(record[j-1]);
						System.out.println(j+":"+lists[listNum][j]);
					}
					lists[listNum][0]=record.length;
				}
				
				line = br.readLine();
			}
			
			//out.flush();
			br.close();
			//out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//end scan
		
		*/
		
		MergeSkip a = new MergeSkip();
		R=a.mergeSkip(lists,T);
		
		//printout output
		System.out.print("output R:");
		int j=1;
		while(R[j]>0){
			System.out.print(R[j]+" ");
			j+=1;
		}
		j-=1;
		System.out.println("candidates Number="+j);
		
		
	}
}
