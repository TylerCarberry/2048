// Tyler Carberry
// Stack

import java.util.LinkedList;
public class Stack
{
	LinkedList<Grid> stackBoard;
	LinkedList<Integer> stackScore;
	
	public Stack()
	{
		stackBoard = new LinkedList<Grid>();
		stackScore = new LinkedList<Integer>();
	}
	
	public void push( Grid board, int score)
	{
		stackScore.add(score);
		stackBoard.add(board);
	}
	
	public int popScore()
	{
		return stackScore.removeLast();
	}
	
	public Grid popBoard()
	{
		return stackBoard.removeLast();
	}
	
	public Stack clone()
	{
		Stack result = new Stack();
		
		for(int i = stackBoard.size() - 1; i >= 0; i--)
		{
			result.push(stackBoard.get(i), stackScore.get(i));
		}
		
		return result;
	}

	


}
