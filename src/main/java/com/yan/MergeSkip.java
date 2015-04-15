package com.yan;

public class MergeSkip {
	public int q=3;
	int[] heap;	//build heap
	//heap = new String[100];
	int[] R;//initialize output list
	int RNo=0;
	//R= new int[100];
	public int listNum=1;
	
	public int[] mergeSkip(int[][] lists,int T){
		
		heap = new int[100];
		R= new int[100];
		
		//push first element of each list to heap
		
		
		while(lists[listNum][1]>0){
			
			int value = lists[listNum][1];
			heap=heappush(heap,value);
			//System.out.println("test");
			lists[listNum][0]=1;//pointer of the list
			listNum+=1;
		}
		listNum-=1;
		
		while(heap[0]>0){
			int t = heap[0];//t is the top the heap
			//pop from heap those records equals t
			int n=1;//count the occurence of t
			
			while(heap[n]==t){
				n=n+1;
			}
			heap=heappop(heap,n);
			
			if(n>=T){
				RNo+=1;
				R[RNo]=t;
				//push next record of poped list
				for(int i=1;i<=listNum;i++){
					if(lists[i][lists[i][0]]==t){
						lists[i][0]+=1;
						int value=lists[i][lists[i][0]];
						if(value>0)
							heap=heappush(heap,value);
					}
				}
			}
			else{
				heap=heappop(heap,T-1-n);
				int t2=heap[0];//t2 is the current top
				int m=0;//count the occurence of t
				
				if(heap[0]<=0){
					break;
				}
				while(heap[m]==t2){
					m=m+1;
				}
				heap=heappop(heap,m);
				//jump
				for(int i =1;i<=listNum;i++){
					if(lists[i][lists[i][0]]<=t2){
						while(lists[i][lists[i][0]]<t2&&lists[i][lists[i][0]]>0){
							lists[i][0]+=1;
						}
						if(lists[i][lists[i][0]]>=t2)
							heap=heappush(heap,lists[i][lists[i][0]]);
					}
				}
				
			}
		}
		return R;
	}
	public int[] heappop(int[] heap,int num){
		System.out.println("pop "+num);
		for(int i=1;i<=listNum;i++){
			heap[i-1]=heap[i+num-1];
		}
		/*
		int i=0;
		while(heap[i+num]>0){
			heap[i]=heap[i+num];
			heap[i+num]=-1;
			i+=1;
		}
		*/
		int j=0;
		System.out.print(heap[0]+"-");
		System.out.print("heap after pop: ");
		while(heap[j]>0){
			System.out.print(heap[j]+" ");
			j=j+1;
		}
		System.out.println();
		return heap;
	}
	public int[] heappush(int[] heap,int value){
		System.out.println("push "+ value);
		int i=0;
		while(heap[i]>0){
			i+=1;
		}
		
		if(i>0){
			while(value<heap[i-1]){
				heap[i]=heap[i-1];
				i=i-1;
				if(i==0){
					break;
				}
			}
		}
		
		heap[i]=value;
		int j=0;
		System.out.print("heap after push: ");
		while(heap[j]>0){
			System.out.print(heap[j]+" ");
			j=j+1;
		}
		System.out.println();
		return heap;
	}

}
