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
	final double CHANCE_OF_2 = .90;
	
	private int score = 0;
	int turnNumber = 0;
	boolean quitGame = false;
	boolean newGame = true;
	
	// The time the game was started
	Date d1;
	
	// Input
	Scanner scan = new Scanner(System.in);
	
	public Game()
	{
		// The main board the game is played on
		board = new Grid(4,4);
		
		// Keeps track of the turn number
		turnNumber = 1;
		
		history = new Stack();
		
		// Adds 2 pieces to the board
		addRandomPiece();
		addRandomPiece();
	}
	
	public Game(int rows, int cols)
	{
		// The main board the game is played on
		board = new Grid(rows,cols);
		
		// Keeps track of the turn number
		turnNumber = 1;
		
		history = new Stack();
		
		// Adds 2 pieces to the board
		addRandomPiece();
		addRandomPiece();
	}
	
	public boolean act(String direction)
	{	
		// Keeps track of the start time
		if(newGame)
		{
			d1 = new Date();
			newGame = false;
		}
		
		// Used to determine if any pieces moved
		Grid lastBoard = board.clone();
		
		// Determines if the game is lost or quit
		if(lost())
			return false;
		
		
		// Moves the board
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
		else if(direction.equalsIgnoreCase("delete"))
		{
			
			delete(new Location(scan.nextInt(), scan.nextInt()));
			addRandomPiece();
			return true;
		}
		
		else
			// Invalid input
			return false;
		
		// If a piece moved then increment the turn number
		// and add a random piece to the board
		if(!board.equals(lastBoard))
		{
			turnNumber++;
			addRandomPiece();
			history.push(lastBoard, score);
		}
		
		return true;
	}
	
	// TODO: Combine the following act/move methods if possible
	
	// Moves all of the pieces. Calls moveRight
	private void actRight()
	{
		//System.out.println("Act Right");
		Location loc;
		for(int col = board.getNumCols()-1; col >=0; col--)
			for(int row = 0; row < board.getNumRows(); row++)
			{
				loc = new Location(row, col);
				if(!board.isEmpty(loc))
					moveRight(loc);
			}
	}
	
	// Moves a single piece
	private void moveRight(Location from)
	{
		//System.out.println("Move Right");
		Location to = from.getRight();
		while(board.isValid(to))
		{
			if(board.isEmpty(to))
			{
				board.move(from, to);
				from = to.clone();
				to = to.getRight();
			}
			else
			{
				if(board.get(from) == board.get(to))
					add(from, to);
				return;
			}
		}
	}
	
	private void actLeft()
	{
		//System.out.println("Act Left");
		Location loc;
		for(int col = 0; col < board.getNumCols(); col++)
		{
			for(int row = 0; row < board.getNumRows(); row++)
			{
				loc = new Location(row, col);
				if(!board.isEmpty(loc))
					moveLeft(loc);
			}
		}
	}
	private void moveLeft(Location from)
	{
		//System.out.println("Move Left");
		Location to = from.getLeft();
		while(board.isValid(to))
		{
			if(board.isEmpty(to))
			{
				board.move(from, to);
				from = to.clone();
				to = to.getLeft();
			}
			else
			{
				if(board.get(from) == board.get(to))
					add(from, to);
				return;
			}
		}
	}
	
	private void actUp()
	{
		//System.out.println("Act Up");
		Location loc;
		for(int row = 0; row < board.getNumRows(); row++)
		{
			for(int col = 0; col < board.getNumCols(); col++)
			{
				loc = new Location(row, col);
				if(!board.isEmpty(loc))
					moveUp(loc);
			}
		}
	}
	private void moveUp(Location from)
	{
		//System.out.println("Move Up");
		Location to = from.getUp();
		while(board.isValid(to))
		{
			if(board.isEmpty(to))
			{
				board.move(from, to);
				from = to.clone();
				to = to.getUp();
			}
			else
			{
				if(board.get(from) == board.get(to))
					add(from, to);
				return;
			}
		}
	}
	
	private void actDown()
	{
		//System.out.println("Act Down");
		Location loc;
		for(int row = board.getNumRows()-1; row >=0; row--)
		{
			for(int col = 0; col < board.getNumCols(); col++)
			{
				loc = new Location(row, col);
				if(!board.isEmpty(loc))
					moveDown(loc);
			}
		}
	}
	private void moveDown(Location from)
	{
		//System.out.println("Move Down");
		Location to = from.getDown();
		while(board.isValid(to))
		{
			if(board.isEmpty(to))
			{
				board.move(from, to);
				from = to.clone();
				to = to.getDown();
			}
			else
			{
				if(board.get(from) == board.get(to))
					add(from, to);
				return;
			}
		}
	}
	
	
	// Takes the sum of 2 pieces and adds it to the board
	private void add(Location from, Location to)
	{
		score += board.get(to) * 2;
		board.set(to, board.get(to) * 2);
		board.set(from, 0);
	}
	
	// Uses a stack to store previous moves
	public void undo()
	{
		if(turnNumber > 1)
		{
			score = history.popScore();
			board = history.popBoard();
			turnNumber--;
		}
	}
	
	// Shuffles the board
	public void shuffle()
	{
		LinkedList<Integer> pieces = new LinkedList<Integer>();
		
		for(int row = 0; row < board.getNumRows(); row++)
			for(int col = 0; col < board.getNumCols(); col++)
			{
				int num = board.get(new Location(row, col));
				if(num > 0)
					pieces.add(num);
			}
		
		board.clear();
		LinkedList<Location> empty;
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
		if(quitGame)
			return true;
		
		// If the board is not filled then the game is lost
		if(!board.getEmptyLocations().isEmpty())
			return false;
		
		int current = -1;
		int next;
		
		// Checks if two of the same number are next to each
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
		
		// Checks if two of the same number are next to each
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
	
	// Returns the number of seconds the game was played for
	public double timePlayed()
	{
		Date d2 = new Date();
		double seconds = ((d2.getTime() - d1.getTime()) / 1000.0);
		
		return seconds;
	}
	
	// Quits the game
	public void quit()
	{
		System.out.println("Game Quit");
		quitGame = true;
	}
	
	
	//
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
	
	public boolean equals(Game otherGame)
	{
		return board.equals(otherGame.getGrid());
	}
	
	public Game clone()
	{
		Game game = new Game();
		game.setGrid(board.clone());
		game.setScore(score);
		game.setHistory(history.clone());
		game.setTurn(turnNumber);
		return game;
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
	
	public String toString()
	{
		String output = "---------------------------------------------\n";
		output += "||  Turn #" + turnNumber + "   Score: " + score + "\n";
		output += "---------------------------------------------\n";
		output += board.toString();
		return output;
	}
}