// 2048 Main
// Tyler Carberry

import java.util.InputMismatchException;
import java.util.Scanner;
public class Main
{
	// Used for the recursive autoplay method to determine
	// the total number of moves
	private static int autoMoveCount = 0;
	
	// All methods can use the scanner without declaring it each time
	private static Scanner scan = new Scanner (System.in);
	
	public static void main(String[] args)
	{	
		int input = 0;
			
		// Intro to game
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||  Welcome to 2048 ||  By Tyler Carberry  ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||               How To Play               ||");
		System.out.println("||  Combine similar numbers to reach 2048  ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||                Controls                 ||");
		System.out.println("||       Left, Right, Up, Down, Quit       ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||                Power Ups                ||");
		System.out.println("||              Shuffle, Undo              ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||                  Tips                   ||");
		System.out.println("||        l,r,u,d can also be used         ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println();
		
		
		System.out.println("Press enter to start playing the default mode. \n"
				+ "Type anything then press enter to customize the game");
		
		
		if(scan.nextLine().equals(""))
			defaultManualPlay();
		else
		{
			System.out.println("Manual or Autoplay? 1/2");
			
			// Error trapping for manual or autoplay
			while(input != 1 && input != 2)
			{
				try { input = scan.nextInt(); }
				
				catch(InputMismatchException e)
				{
					// Clears the invalid input
					scan.next();
				}
				
				if (input != 1 && input != 2)
					System.out.println("Incorrect input. Enter 1 or 2 with no punctuation");
			}
			
			if(input == 1)
				customManualPlay();
			else
			{
				input = -1;
				System.out.println("Recursive, Circle, Corner, or Random? 1/2/3/4");
				
				// Error trapping
				while(input > 4 || input < 1)
				{
					try
					{
						input = scan.nextInt();
					}
					catch(InputMismatchException e)
					{
						// Clear the invalid input
						scan.next();
					}
					
					if (input != 1 && input != 2)
						System.out.println("Incorrect input. Enter 1, 2, 3 or 4 with no punctuation");
				}
			
				Game game = new Game(4,4);
				
				switch(input)
				{
				
					case 1: recursiveHelper(game);
							break;
					case 2: circlePlay(game);
							break;
					case 3: cornerPlay(game);
							break;
					case 4: randomPlay(game);
							break;
					default:
						System.out.println("\nTesting the recursive play");
						for(int i = 0; i < 1000; i++)
						{
							recursivePlay(game.clone(), game.clone(), 2048, true);
							System.out.println(autoMoveCount);
							autoMoveCount = 0;
						}
				}
			}
		}
	}

	//---------------------------------------------------------
	// Create a normal game
	// 4x4 grid with no limits
	//---------------------------------------------------------
	public static void defaultManualPlay()
	{
		Game game = new Game();
		manualPlay(game);
	}
	
	//---------------------------------------------------------
	// Customize Manual Play
	// Move Limit, Undo Limit, Time Limit, Corner Mode
	//---------------------------------------------------------
	public static void customManualPlay()
	{
		Scanner scan = new Scanner(System.in);
		String limit;
		
		System.out.println("Enter the dimentions of the grid (Recommended 4 4)");
		int rows = -1;
		int cols = -1;
		
		// Error trapping for the dimensions of the grid
		while(cols < 1 || rows < 1)
		{
			try
			{
				rows = scan.nextInt();
				cols = scan.nextInt();
			}
			
			// If a string is entered instead
			catch(InputMismatchException e)
			{
				// Clears the invalid input
				scan.next();
			}
		
			if (cols < 1 || rows < 1)
				System.out.println("Incorrect input. Enter the number of rows and columns.");
		}
		
		Game game = new Game(rows,cols);
		
		scan.nextLine();
		
		System.out.println("Enter Move Limit (press enter for unlimited)");
		limit = scan.nextLine();
		
		if(limit.equals(""))
			game.setMoveLimit(-1);
		else
			game.setMoveLimit(Integer.parseInt(limit));
		
		System.out.println("Enter Undo Limit (press enter for unlimited)");
		limit = scan.nextLine();
		
		if(limit.equals(""))
			game.setUndoLimit(-1);
		else
			game.setUndoLimit(Integer.parseInt(limit));
			
		System.out.println("Enter Time Limit In Seconds (press enter for unlimited)");
		limit = scan.nextLine();
		
		if(! limit.equals(""))
			game.setTimeLimit(Integer.parseInt(limit));
		
		
		
		System.out.println("Corner Mode? (Immovable X's in corner)");
		System.out.println("Press enter for no, anything else for yes");
		limit = scan.nextLine();
		
		if(! limit.equals(""))
			game.cornerMode();
		
		// A game cannot be Corner Mode and X Mode at the same time
		else
		{	
			System.out.println("X Mode? (A movable X that cannot be combined)");
			System.out.println("Press enter for no, anything else for yes");
			limit = scan.nextLine();
			
			if(! limit.equals(""))
				game.XMode();
		
		}
		
		manualPlay(game);
			
	}
	
	
	//---------------------------------------------------------
	// Manual Play
	//---------------------------------------------------------
	public static void manualPlay(Game game)
	{
		String direction;
	
		System.out.println(game);
		
		while(!(game.lost()))
		{
			direction = scan.next();
			if(game.act(direction))
				System.out.println(game);
			else
			{
				if(game.lost())
					break;
				
				System.out.println("Invalid Command");
				System.out.println("Controls: Left, Right, Up, Down, Quit, Undo, Shuffle");
			}
		}

		int milliseconds = (int) (game.timePlayed() * 1000);
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) (milliseconds / (1000*60));
		milliseconds -= seconds * 60;
		
		// Round the time to the nearest millisecond
		// Prevent precision errors (0.89999999999 seconds)
		String milli = "" + milliseconds;
		milli = "." + milli.substring(0,3);
		
		System.out.println("Time Played: " + minutes + " minutes " + (seconds + milli) + " seconds");
	
		// This is needed to stop the game even if the time limit is not up
		// The 0 means that there were no errors
		System.exit(0);
	}
	
	//---------------------------------------------------------
	// Calls the recursive play method
	//---------------------------------------------------------
	public static void recursiveHelper(Game game)
	{
		System.out.println("Play until which tile is reached?");
		System.out.println("(Values above 2048 are not recommended)");
		int tile = scan.nextInt();
		recursivePlay(game, game, tile, true);
		System.out.println("**** GAME WON ****");
		System.out.println("Total Number of Moves: " + autoMoveCount);
		
	}
	
	
	// I ran the game over 100 times with a 10,000 move limit
	// The number of moves it took to reach 2048:
	// Min:932   |  Q1: 1707   |  Median: 2759
	// Q3: 5822  |  Max: 10000 |  Average: 4165
	// When the move gets above Q3 (6000 moves) the game resets to the original
	// The game should now take less than 12,000 moves, 94% of games
	
	public static boolean recursivePlay(Game game, Game original, int tile, boolean upFirst)
	{
		System.out.println(game);
		
		if(game.won(tile))
			return true;
		
		Game lastTurn = game.clone();
		autoMoveCount++;
		
		// Undos the the entire game every 6000 moves
		if(tile <= 2048 && autoMoveCount % 6000 == 0)
		{
			System.out.println("Reseting the game");
			game = original.clone();
			System.out.println(game);
		}
		
		// Stops automatically after 150000 moves because
		// most games take only 2000-3000
		if(autoMoveCount >= 15000)
		{
			System.out.println("***** Time Limit Reached *****");
			return true;
		}
		
		if(upFirst)
		{
			game.act("up");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlay(game.clone(), original, tile, !upFirst))
					return true;
			
			game.act("left");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlay(game.clone(), original, tile, !upFirst))
					return true;
			
		}
		else
		{
			game.act("left");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlay(game.clone(), original, tile, !upFirst))
					return true;
			
			game.act("up");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlay(game.clone(), original, tile, !upFirst))
					return true;
		}
		
		
		game.act("right");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), original, tile, false))
				return true;
		
		game.act("down");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), original, tile, false))
				return true;
		
		System.out.println("**** Undo ****");
		return false;
	}
	
	// Moves up, left, down, right until it loses and returns the final score
	public static int circlePlay(Game game)
	{
		while(!(game.lost()))
		{
			System.out.println(game);
			System.out.println("Moving up");
			game.act("up");
			System.out.println(game);
			System.out.println("Moving left");
			game.act("left");
			System.out.println(game);
			System.out.println("Moving down");
			game.act("down");
			System.out.println(game);
			System.out.println("Moving right");
			game.act("right");
		}
		System.out.println(game);
		return game.getScore();
	}

	// Moves randomly and returns the final score
	public static int randomPlay(Game game)
	{
		double num;
		while(!(game.lost()))
		{
			System.out.println(game);
			num = Math.random();

			if(num > .5)
				if(num > .75)
				{
					System.out.println("Acting up");
					game.act("up");
				}
				else
				{
					System.out.println("Acting left");
					game.act("left");
				}
			else
				if(num > .25)
				{
					System.out.println("Acting down");
					game.act("down");
				}
				else
				{
					System.out.println("Acting right");
					game.act("right");
				}
		}
		return game.getScore();
	}


	// Moves up, left, up, left until it can't move
	// then goes right, if still can't move goes down
	public static int cornerPlay(Game game)
	{
		while(!(game.lost()))
		{
			while(game.canMoveRight() || game.canMoveUp() || game.canMoveLeft())
			{
				while(game.canMoveUp() || game.canMoveLeft())
				{
					System.out.println("Acting up");
					game.act("u");
					System.out.println(game);
					
					System.out.println("Acting left");
					game.act("l");
					System.out.println(game);
				}
				System.out.println("Acting right");
				game.act("r");
				System.out.println(game);
			}
			System.out.println("Acting down");
			game.act("d");
			System.out.println(game);

		}

		return game.getScore();
	}
}
