package yenigun.io.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphSearchConfigurator;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;

public class Sudoku extends GraphStateImpl //Her þey bu 2 parantez arasýnda olmalý
{
	//public static final int n=2; 1.Test
	public static final int n=3;
	public static final int n2=n*n;
	
	private byte[][] board = null;
	public int zeros=n2*n2;
	
	private void refreshZeros() 
	{
		zeros=0;
		for(int i=0;i<n2;i++) 
			for(int j=0;j<n2;j++) 
				if(board[i][j]==0)
					zeros++;
		
	}
	@Override
	public int hashCode() {
		
		//return toString().hashCode(); // 1. Way
		
		byte[] copy=new byte[n2*n2];
		int k=0;
		for(int i = 0; i<n2;i++)
			 for(int j = 0; j<n2;j++)
				 copy[k++]=board[i][j];
		return Arrays.hashCode(copy);
	}
	public Sudoku() {
		
		board = new byte [n2][n2];
		for(int i = 0; i<n2;i++)
		 for(int j = 0; j<n2;j++)
			 board[i][j]=0;
		
		 	
	}
	public Sudoku(Sudoku parent) {
		
		board = new byte [n2][n2];
		for(int i = 0; i<n2;i++)
		 for(int j = 0; j<n2;j++)
			 board[i][j]=parent.board[i][j];
		zeros=parent.zeros;
	}
	
	@Override
	public String toString() {
		/*String temp="";
		for(int i=0;i<n2;i++) {
			for(int j=0;j<n2;j++) 
				temp += board[i][j]+",";	
			
			temp += "\n";
		}*/
		
		StringBuilder temp=new StringBuilder();
		for(int i=0;i<n2;i++) {
			for(int j=0;j<n2;j++) 
				temp.append(board[i][j]+",");	
			temp.append("\n");
		}		
		// TODO Auto-generated method stub
	   
		return temp.toString();
	}
	
	public void fromString(String txt)
	{
		int k=0;
		for(int i=0;i<n2;i++)
			for(int j=0;j<n2;j++) {
				board[i][j]=Byte.valueOf(txt.substring(k, k+1));
				k++;
			}
		
		refreshZeros();
		
	}
	
	public boolean isLegal() 
	{
		byte[] group=new byte[n2];
		for(int i=0;i<n2;i++)
		{
			for(int j=0;j<n2;j++)
				group[j]=board[i][j];
			if(!isGroupLegal(group))
				return false;
		}
		
		for(int i=0;i<n2;i++)
		{
			for(int j=0;j<n2;j++)
				group[j]=board[j][i];
			if(!isGroupLegal(group))
				return false;
		}
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				int q=0;
				for(int k=0;k<n;k++)
					for(int l=0;l<n;l++)
						group[q++]=board[i*n+k][j*n+l];
				if(!isGroupLegal(group))
					return false;
			}
		}
		
		
		return true;
		
	}
	
	private boolean isGroupLegal(byte[] group) 
	{
		boolean[] visited=new boolean[n2+1];
		for(int i=1;i<n2;i++) //if u want u can skip this block
			visited[i]=false;
		for(int i=0;i<group.length;i++)
		{
			if(group[i]==0)
				continue;
			if(visited[group[i]])
				return false;
			
			visited[group[i]]=true;
		}
		
		return true;
		
	}

	public static void main(String[] args) {
		
		Sudoku s=new Sudoku();
		System.out.println("Hello");
		//System.out.println(s); // string gönderilmeli java da otamatik çevirme yapar. s.toString() 
		
		String sudokuAsTxt="003020600000005001001806400008102900000000008006708200002609500800203009005010300";
		//String sudokuAsTxt="003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        
		
		//s.fromString(sudokuAsTxt); 1.test
		System.out.println(s);
		System.out.println(s.isLegal());
		System.out.println(s.isLegal());
		System.out.println(s.zeros);
		GraphSearchConfigurator conf=new GraphSearchConfigurator();
		conf.setWantedNumberOfSolutions(Integer.MAX_VALUE);
		GraphSearchAlgorithm a= new BestFirstSearch(s,conf);
		a.execute();
		//System.out.println(a.getSolutions().get(0));
		for(GraphState solution : a.getSolutions())
			System.out.println(solution);
		
		System.out.println("Time [ms]: " +a.getDurationTime());
		System.out.println("Closed States: "+a.getClosedStatesCount());		//how many iteration 
		System.out.println("Open States: "+a.getOpenSet().size());
		
		System.out.println("Solutions: " +a.getSolutions().size());
		
		/* Until 10.December Sliding puzzle + A* (n^2-1)-puzzle 
		   [ 0 1 2						[ 7 0 3
		   	 3 4 5		then shuffle      1 8 5    and try to solve minimum path with A* 
		   	 5 6 7 ]					  2 4 6 ]
		  
		  A* gives minumum path
		  BestFirst giving any solution
		  
		  create SlidingPuzzle Class and in this class
		  	1.n constant (n=3).
		  	2.board(byte[][])
		  	3.constant/enumaration {L,R,U,D}
		  	4.constructors
		  	5.toString(),hashCode()
		  	6.makeMove(3.,) *Move R.. return boolean.
		  	7.Shuffle() pick one piece and do some random movements and iteration 100 time to shuffle puzlle. // int number of moves
		  		sp.mix(100)
		  	(int) (Math.Random)() * 4)			java.util.Random
		  	
		  	Random r=new Random(); // Random r= new Random(123); seed
		  	r.nextInteger() %4
		  	r.nextDouble()
		  */
	}
	
	
	@Override
	public List<GraphState> generateChildren() {
		int i=0,j=0; //coordinates of some '0'
		zeroFinder:
		for(i=0;i<n2;i++)
			for(j=0;j<n2;j++)
				if(board[i][j]==0)
					break zeroFinder;
		List<GraphState> children=new ArrayList<GraphState>();
		if(i==n2)
			return children;
		for(int k=0;k<n2;k++)
		{
			Sudoku child=new Sudoku(this);
			child.board[i][j]=(byte)(k+1);
			if(child.isLegal()) 
			{
				children.add(child);
				child.zeros--;
				
			}
		}
		return children;
	}

	@Override
	public boolean isSolution() {
		// TODO Auto-generated method stub
		return ((zeros == 0) && (isLegal()));
	}
	

}
