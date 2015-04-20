package com.yan;

import java.util.ArrayList;
import java.util.List;

import com.devinschwab.eecs405.*;

public class test {
	
	public ArrayList<Integer> runQGram(String query,Integer T,int q){
		Integer temp;
		ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
		ArrayList<Integer> R= new ArrayList<Integer>();
		VGramIndex v=new VGramIndex(q,q);
		ArrayList<String> qGram = new ArrayList<String>();
		for(int i=1;i<=query.length()-q+1;i++){
			qGram.add(query.substring(i-1, i-1+q));
		}
		
		//scan hashtable and put into lists
		for(int i=0;i<=query.length()-q;i++){
				temp=v.invertedList.get(qGram.get(i)).size();
				lists.add(v.invertedList.get(qGram.get(i)));
				lists.get(i).add(0,temp);
		}
		
		MergeSkip a = new MergeSkip();
		R=a.mergeSkip(lists,T.intValue());
		
		//printout output
		System.out.print("output R:");
		int j=0;
		while(j<R.size()){
			System.out.print(R.get(j)+" ");
			j+=1;
		}
		j-=1;
		System.out.println("candidates Number="+j);
		
		return R;
	}
}
