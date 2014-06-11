import java.util.*;

// Tyler Carberry
// 2048
public class Game
{
	// The main board the game is played on
	private Grid board;
	
	// Stores the previous boards and scores
	private Stack history;
	
	// The % of a 2 appearing
	public final double CHANCE_OF_2 = .90;
	
	private int score = 0;
	private int turnNumber = 0;
	private boolean quitGame = false;
	
	// Used to start the time limit on the first move
	// instead of when the game is created
	private boolean newGame = true;
	
	// The time the game was started
	private Date d1;
	
	// Limited number of moves
	// -1 = unlimited
	private int movesRemaining = -1;
	
	// Limited number of undos
	// -1 = unlimited
	private int undosRemaining  = -1;
	
	// The time limit in seconds before the game automatically quits
	// The timer starts immediately after the first move
	private int timeLimit = -1;
	
	// The default game size is 4x4
	public Game()
	{
		this(4,4);
	}
	
	public Game(int rows, int cols)
	{
		// The main board the game is played on
		board = new Grid(rows,cols);
		
		// Keeps track of the turn number
		turnNumber = 1;
		
		// Store the move history
		history = new Stack();
		
		// Adds 2 pieces to the board
		addRandomPiece();
		addRandomPiece();
	}
	
	// All moves are called through this method
	public boolean act(String direction)
	{	
		// Keep track of the start time and activates the time limit
		if(newGame)
		{
			d1 = new Date();
			if(timeLimit > 0)
				activateTimeLimit();
			
			newGame = false;
		}
		
		// Used to determine if any pieces moved
		Grid lastBoard = board.clone();
		
		// Determine if the game is lost or quit
		if(lost())
			return false;
		
		
		// Move the board
		if(direction.equalsIgnoreCase("l") || direction.equalsIgnoreCase("left"))
			actLeft();
		else if(direction.equalsIgnoreCase("r") || direction.equalsIgnoreCase("right"))
			actRight();
		else if(direction.equalsIgnoreCase("u") || direction.equalsIgnoreCase("up"))
			actUp();
		else if(direction.equalsIgnoreCase("d") || direction.equalsIgnoreCase("down"))
			actDown();
		else if(direction.equalsIgnoreCase("x") || direction.equalsIgnoreCase("exit")
			|| direction.equalsIgnoreCase("stop") || direction.equalsIgnoreCase("quit"))
			quit();
		else if(direction.equalsIgnoreCase("undo") || direction.equalsIgnoreCase("back"))
		{
			undo();
			return true;
		}
		else if(direction.equalsIgnoreCase("shuffle"))
		{
			shuffle();
			turnNumber++;
			return true;
		}
		else
			// Invalid input
			return false;
		
		// If a piece moved then increment the turn number
		// and add a random piece to the board
		if(! board.equals(lastBoard))
		{
			turnNumber++;
			addRandomPiece();
			history.push(lastBoard, score);
			movesRemaining--;
		}
		
		return true;
	}
	
	// TODO: Combine the following act methods if possible
	
	// Moves all of the pieces to the right
	private void actRight()
	{
		Location loc;
		
		// Start at the right side of the board and move left
		for(int col = board.getNumCols()-1; col >=0; col--)
			for(int row = 0; row < board.getNumRows(); row++)
			{
				loc = new Location(row, col);
				
				// Do not move X's or 0's
				if(board.get(loc) > 0)
					move(loc, Location.RIGHT);
			}
	}
	
	// Moves all of the pieces to the left
	private void actLeft()
	{
		Location loc;
		
		// Start at the left side of the board and move right
		for(int col = 0; col < board.getNumCols(); col++)
		{
			for(int row = 0; row < board.getNumRows(); row++)
			{
				loc = new Location(row, col);
				
				// Do not move X's or 0's
				if(board.get(loc) > 0)
					move(loc, Location.LEFT);
			}
		}
	}
	
	// Moves all of the pieces up
	private void actUp()
	{
		Location loc;
		
		// Start at the bottom of the board and move up
		for(int row = 0; row < board.getNumRows(); row++)
		{
			for(int col = 0; col < board.getNumCols(); col++)
			{
				loc = new Location(row, col);

				// Do not move X's or 0's
				if(board.get(loc) > 0)
					move(loc, Location.UP);
			}
		}
	}
	
	// Moves all of the pieces down
	private void actDown()
	{
		Location loc;
		
		// Start at the top side of the board and move down
		for(int row = board.getNumRows()-1; row >=0; row--)
		{
			for(int col = 0; col < board.getNumCols(); col++)
			{
				loc = new Location(row, col);

				// Do not move X's or 0's
				if(board.get(loc) > 0)
					move(loc, Location.DOWN);
			}
		}
	}
	
	// Move a single piece all of the way in a given direction
	// Will combine with a piece of the same value
	
	// Precondition: direction is called using the final
	// variables in the location class
	private void move(Location from, int direction)
	{
		// Do not move X spaces
		if(board.get(from) < 0) return;
				
		Location to = from.getAdjacent(direction);
		while(board.isValid(to))
		{
			if(board.isEmpty(to))
			{
				board.move(from, to);
				from = to.clone();
				to = to.getAdjacent(direction);
			}
			else
			{
				if(board.get(from) == board.get(to))
					add(from, to);
				return;
			}
		}
	}
		
		
	
	// Precondition: from and to are valid locations with equal values
	// Adds piece "from" into piece "to"
	// 4 4 -> 0 8
	private void add(Location from, Location to)
	{
		score += board.get(to) * 2;
		board.set(to, board.get(to) * 2);
		board.set(from, 0);
	}
	
	// Undo the game 1 turn
	// Uses a stack to store previous moves
	public void undo()
	{
		if(turnNumber > 1 && undosRemaining != 0)
		{
			// Undo the score, board, and turn #
			score = history.popScore();
			board = history.popBoard();
			turnNumber--;
			
			// Use up one of the undos allowed
			if(undosRemaining > 0)
				undosRemaining--;
			
			// Print the number of undos remaining
			if(undosRemaining >= 0)
				System.out.println("Undos remaining: " + undosRemaining);
		}
	}
	
	// Shuffles the board
	public void shuffle()
	{
		// Adds every piece > 0 to a linked list
		LinkedList<Integer> pieces = new LinkedList<Integer>();
		
		for(int row = 0; row < board.getNumRows(); row++)
			for(int col = 0; col < board.getNumCols(); col++)
			{
				int num = board.get(new Location(row, col));
				if(num > 0)
				{
					pieces.add(num);
					
					// Removes the piece from the board
					// This is used instead of board.clear() to prevent
					// the X's from disappearing in corner mode
					board.set(new Location(row,col), 0);
				}
			}
		
		LinkedList<Location> empty;
		
		// Adds every piece to a random empty location
		for(int num : pieces)
		{
			empty = board.getEmptyLocations();
			board.set(empty.get((int) (Math.random() * empty.size())), num);
		}
	}
	
	// Removes the piece from the given location
	public void delete(Location loc)
	{
		board.set(loc, 0);
	}
	
	// Stop the game automatically after x seconds
	public void setTimeLimit(int seconds)
	{
		if(seconds > 0)
			timeLimit = seconds;
	}
	
	// Precondition: timeLimit > 0
	// Is called after the first move
	private void activateTimeLimit()
	{	
		// Create a new thread to quit the game
		final Thread t = new Thread() {
		    public void run()
		    {
		    	try
		    	{
		    		// Pause the thread for x milliseconds
		    		// The game continues to run
		            Thread.sleep(getTimeLimit() * 1000);
		    	}
		    	catch (Exception e)
		    	{
		    		System.out.println(Thread.currentThread().getStackTrace());
		    	}
		    	
		    	// After the time limit is up, quit the game
		    	System.out.println("Time Limit Reached");
		    	quitGame = true;   
		    }
		};
		
		t.start();
	}
	
	// Returns the time limit in seconds
	// This is NOT the amount of time currently left in the game,
	// it is the total time limit.
	public int getTimeLimit()
	{
		return timeLimit;
	}
	
	// Places immovable X's in the corners of the board
	// This will clear any existing pieces on the board
	public void cornerMode()
	{
		board.clear();
		board.set(new Location(0,0), -1);
		board.set(new Location(0,board.getNumCols() - 1), -1);
		board.set(new Location(board.getNumRows() - 1,0), -1);
		board.set(new Location(board.getNumRows() - 1 ,board.getNumCols() - 1), -1);
		addRandomPiece();
		addRandomPiece();
	}
	
	// Limit the number of undos
	// -1 = unlimited
	public void setUndoLimit(int limit)
	{
		undosRemaining = limit;
	}
	
	// Returns the number of undos left
	// -1 = unlimited
	public int getUndosRemaining()
	{
		return undosRemaining;
	}
	
	// Limit the number of move
	// -1 = unlimited
	public void setMoveLimit(int limit)
	{
		movesRemaining = limit;
	}
	
	// Returns the number of moves left
	// -1 = unlimited
	public int getMovesRemaining()
	{
		return movesRemaining;
	}
	
	// Randomly adds a new piece to an empty space
	// 90% add 2, 10% add 4
	// CHANCE_OF_2 is a final variable declared at the top
	public void addRandomPiece()
	{
		LinkedList<Location> empty = board.getEmptyLocations();
		
		if(empty.isEmpty())
			return;
		
		int randomLoc = (int) (Math.random() * empty.size());
		if(Math.random() < CHANCE_OF_2)
			board.set(empty.get(randomLoc), 2);
		else
			board.set(empty.get(randomLoc), 4);
	}
	
	// A game is won if there is a 2048 tile or greater
	public boolean won()
	{
		return won(2048);
	}
	
	// A game is won if a tile is >= winningTile
	public boolean won(int winningTile)
	{
		Location loc;
		for(int col = 0; col < board.getNumCols(); col++)
		{
			for(int row = 0; row < board.getNumRows(); row++)
			{
				loc = new Location(row, col);
				if(board.get(loc) >= winningTile)
					return true;
			}
		}
		return false;
	}
	
	// Determines if the game is lost
	public boolean lost()
	{
		// If the game is quit then the game is lost
		if(quitGame || movesRemaining == 0)
			return true;
		
		// If the board is not filled then the game is lost
		if(!board.getEmptyLocations().isEmpty())
			return false;
		
		int current = -1;
		int next;
		
		// Check if two of the same number are next to each
		// other in a row.
		for(int row = 0; row < board.getNumRows(); row++)
		{
			for(int col = 0; col < board.getNumCols(); col++)
			{
				next = current;
				current = board.get(new Location(row,col));
				
				if(current == next)
					return false;
			}
			current = -1;
		}
		
		// Check if two of the same number are next to each
		// other in a column.
		for(int col = 0; col < board.getNumCols(); col++)
		{
			for(int row = 0; row < board.getNumRows(); row++)
			{
				next = current;
				current = board.get(new Location(row,col));
				
				if(current == next)
					return false;
			}
			current = -1;
		}
		return true;
	}
	
	// Return the number of seconds the game was played for
	public double timePlayed()
	{
		Date d2 = new Date();
		double seconds = ((d2.getTime() - d1.getTime()) / 1000.0);
		
		return seconds;
	}
	
	// Quit the game
	public void quit()
	{
		quitGame = true;
	}
	
	
	// The highest piece on the board
	public int highestPiece()
	{
		int highest = 0;
		for(int col = 0; col < board.getNumCols(); col++)
		{
			for(int row = 0; row < board.getNumRows(); row++)
			{
				if(board.get(new Location(row, col)) > highest)
					highest = board.get(new Location(row, col));
			}
		}
		return highest;
	}
	
	// Games are equal if they have the same board and score.
	// Even if their history is different.
	public boolean equals(Game otherGame)
	{
		return board.equals(otherGame.getGrid()) && score == otherGame.getScore();
	}
	
	// Used to avoid creating aliases
	public Game clone()
	{
		Game game = new Game();
		game.setGrid(board.clone());
		game.setScore(score);
		game.setHistory(history.clone());
		game.setTurn(turnNumber);
		return game;
	}
	

	// Determines if the board can move right
	public boolean canMoveRight()
	{
		Game nextMove = clone();
		nextMove.actRight();
		return !(nextMove.equals(this));
	}
	
	// Determines if the board can move left
	public boolean canMoveLeft()
	{
		Game nextMove = clone();
		nextMove.actLeft();
		return !(nextMove.equals(this));
	}
	
	// Determines if the board can move up
	public boolean canMoveUp()
	{
		Game nextMove = clone();
		nextMove.actUp();	
		return !(nextMove.equals(this));
	}
		
	// Determines if the board can move down
	public boolean canMoveDown()	
	{
		Game nextMove = clone();
		nextMove.actDown();
		return !(nextMove.equals(this));
	}
	
	
	
	public int getScore()
	{
		return score;
	}
	
	public int getTurns()
	{
		return turnNumber;
	}
	
	public Grid getGrid()
	{
		return board;
	}
	
	private void setTurn(int newTurn)
	{
		turnNumber = newTurn;
	}
	
	private void setGrid(Grid newGrid)
	{
		board = newGrid.clone();
	}
	
	private void setScore(int newScore)
	{
		score = newScore;
	}
	
	private void setHistory(Stack newHistory)
	{
		history = newHistory.clone();
	}
	
	/* Print the board in the form:
	---------------------------------------------
	||  Turn #8  Score: 20  Moves Left: 3
	---------------------------------------------
	| 8  |    | 2  |    |
	| 4  |    |    |    |
	| 2  |    |    | 2  |
	|    |    |    |    |		*/
	public String toString()
	{
		String output = "---------------------------------------------\n";
		output += "||  Turn #" + turnNumber + "  Score: " + score;
		
		if(movesRemaining >= 0)
			output += "  Moves Left: " + movesRemaining;
		
		output += "\n---------------------------------------------\n";
		output += board.toString() + "\n";
		
		return output;
	}
}