// Tyler Carberry
// Stack
// Stores the history of the game board and score

import java.util.LinkedList;
public class Stack implements Cloneable
{
	LinkedList<Grid> stackBoard;
	LinkedList<Integer> stackScore;
	
	// A double stack that stores both the board and score
	public Stack()
	{
		stackBoard = new LinkedList<Grid>();
		stackScore = new LinkedList<Integer>();
	}
	
	// Both objects are pushed at the same time to avoid
	// the score and board getting out of sync
	public void push(Grid board, int score)
	{
		stackScore.add(score);
		stackBoard.add(board);
	}
	
	// Returns and deletes the most recent score
	public int popScore()
	{
		return stackScore.removeLast();
	}
	
	// Returns and deletes the most recent game board
	public Grid popBoard()
	{
		return stackBoard.removeLast();
	}
	
	// Returns the most recent score without removing it
	public int frontScore()
	{
		return stackScore.getLast();
	}
	
	// Returns the most recent score without removing it
	public Grid frontBoard()
	{
		return stackBoard.getLast();
	}
	
	// Creates a clone of the stack and all of its elements
	public Stack clone()
	{
		Stack result = new Stack();
		
		for(int i = stackBoard.size() - 1; i >= 0; i--)
			result.push(stackBoard.get(i), stackScore.get(i));
		
		return result;
	}
}
