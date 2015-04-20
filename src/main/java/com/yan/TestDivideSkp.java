package com.yan;

import java.util.ArrayList;
import java.util.List;

import com.devinschwab.eecs405.*;

public class TestDivideSkp {
	
	public ArrayList<Integer> runQGram(String query,Integer T,Integer q,Integer L){

		ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
		ArrayList<List<Integer>> longLists = new ArrayList<List<Integer>>();
		ArrayList<List<Integer>> shortLists = new ArrayList<List<Integer>>();
		ArrayList<Integer> R= new ArrayList<Integer>();
		ArrayList<Integer> Rt= new ArrayList<Integer>();
		//L=T.intValue()-1;//optimal length of long lists
		VGramIndex v=new VGramIndex(q,q);
		ArrayList<String> qGram = new ArrayList<String>();
		for(int i=1;i<=query.length()-q+1;i++){
			qGram.add(query.substring(i-1, i-1+q));
		}
		
		//scan hashtable and put into lists
		for(int i=0;i<=query.length()-q;i++){

				lists.add((v.invertedList.get(qGram.get(i))));
				lists.get(i).add(0,v.invertedList.get(qGram.get(i)).size());
		}
		
		//sort the lists with lengths
		for(int i=0;i<lists.size()-1;i++){
			int maxNo=i;
			for(int j=i;j<lists.size()-1;j++){
						
				if(lists.get(maxNo).get(0).intValue()<lists.get(j+1).get(0).intValue()){
					maxNo=j+1;
				}
			}
			lists.add(i,lists.get(maxNo));
			lists.remove(maxNo+1);
		}
		
		for(int i=0;i<L;i++){
			longLists.add(lists.get(i));
		}
		for(int i=L;i<lists.size();i++){
			shortLists.add(lists.get(i));
		}
		
		MergeSkip a = new MergeSkip();
		R=a.mergeSkip(shortLists,T.intValue()-L.intValue());
		Rt=a.Rt;
		System.out.print("end of short list");
		
		//use binary search on long lists
		BinarySearch b = new BinarySearch();
		R=b.binarySearch(longLists,T,R,Rt);
				
		//printout output
		System.out.print("output R:");
		int j=0;
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
