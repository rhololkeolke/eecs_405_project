package com.yan;

import com.devinschwab.eecs405.*;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class TestVGram {
	
	//call this func to run MergeSkip with vGram
	//output is a list of candidates' id
	public ArrayList<Integer> runVGram(String query,int edThreshold,int qmin,int qmax) throws IOException {
		Integer temp;
		int Tq;//query's occurence threshold
		ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
		ArrayList<Integer> R= new ArrayList<Integer>();//output	
		
	 	java.util.List<QGram> vGram;
		
		VGramIndex v=new VGramIndex(qmin,qmax);
		vGram=v.gramDict.generateVGrams(query);
		Tq=vGram.size()-v.nagVectorGenerator.generate(query, edThreshold).get(edThreshold-1).intValue();
		//scan file and put into lists
		
		for(int i=0;i<=vGram.size();i++){

			System.out.println("listNum="+i);
			temp=v.invertedList.get(vGram.get(i)).size();
			lists.add(v.invertedList.get(vGram.get(i)));
			lists.get(i).add(0,temp);
		}
		
		
		VGramMergeSkip a = new VGramMergeSkip();
		R=a.vGramMergeSkip(lists,Tq,qmin,qmax,edThreshold);
		
		//printout output
		System.out.print("output R:");
		int j=1;
		while(j<R.size()){
			System.out.print(R.get(j)+" ");
			j+=1;
		}
		j-=1;
		System.out.println();
		System.out.println("candidates Number="+j);
		return R;
		
		
	}
}
