import java.util.Scanner;
public class Main
{
	// Used for the recursive autoplay method to determine
	// the total number of moves
	public static int autoMoveCount = 0;
	
	static Scanner scan = new Scanner (System.in);
	
	
	public static void main(String[] args)
	{
		int input;
			
		// Intro to game
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||  Welcome to 2048 ||  By Tyler Carberry  ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||               How To Play               ||");
		System.out.println("||  Combine similar numbers to reach 2048  ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||                Controls                 ||");
		System.out.println("||    Left, Right, Up, Down, Undo, Quit    ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println("||                  Tips                   ||");
		System.out.println("||        l,r,u,d can also be used         ||");
		System.out.println("|| --------------------------------------- ||");
		System.out.println();
		
		System.out.println("Enter the dimentions of the grid (Default 4x4)");
		int rows = scan.nextInt();
		int cols = scan.nextInt();
		Game game = new Game(rows,cols);
		
		System.out.println("Manual or Autoplay? 1/2");
		input = scan.nextInt();
		
		if(input == 1)
			manualPlay(game);
		else
		{
			System.out.println("Recursive, Circle, Corner, or Random? 1/2/3/4");
			input = scan.nextInt();
			
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


	//---------------------------------------------------------
	// Manual Play
	//---------------------------------------------------------
	public static void manualPlay(Game game)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println(game);
		System.out.println("Move in which direction? u/d/l/r  (stop to quit)");
		String direction;

		while(!(game.lost()))
		{
			direction = scan.next();
			if(game.act(direction))
				System.out.println(game);
			else
			{
				System.out.println("Invalid Command");
				System.out.println("Controls: Left, Right, Up, Down, Quit, Undo, Shuffle");
			}
		}

		System.out.println(game);
		
		int milliseconds = (int) (game.timePlayed() * 1000);
		
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) (milliseconds / (1000*60));
		milliseconds -= seconds * 60;
		
		
		System.out.println("Time Played: " + minutes + " minutes " + (seconds + milliseconds/1000.0) + " seconds");
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
		if(autoMoveCount % 6000 == 0)
		{
			System.out.println("Undoing the game");
			game = original.clone();
			System.out.println(game);
		}
		
		
		
		// Stops automatically after 150000 moves because
		// most games take only 2000-3000
		if(autoMoveCount >= 15000)
		{
			//System.out.println(game);
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
