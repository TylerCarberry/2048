import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.security.sasl.AuthorizeCallback;
public class Main
{
	// Used for the recursive autoplay method to determine
	// the total number of moves
	public static int num = 0;
	
	static Scanner scan = new Scanner (System.in);
	
	// public native int find_best_move(int[][] board);
	
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
			
			if(input == 1)
				recursiveHelper(game);
			else
				if(input == 2)
					circlePlay(game);
				else
					if(input == 3)
						cornerPlay(game);
					else
						if(input == 4)
							randomPlay(game);
						else
						{
							System.out.println("\nTesting the recursive play");
							
							for(int i = 0; i < 100; i++)
							{
								recursivePlayTest(game, 2048, true);
								System.out.println(num);
								num = 0;
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
		recursivePlayTest(game, tile, true);
		System.out.println("**** GAME WON ****");
		System.out.println("Total Number of Moves: " + num);
		
	}
	
	// OLD
	public static boolean recursivePlay(Game game, int tile)
	{
		//System.out.println(game);
		Game lastTurn = game.clone();
		num++;
		
		// Stops automatically after 5000 moves because most games take only 2000
		if(num >= 5000)
		{
			return true;
		}
		
		
		if(game.won(tile))
			return true;

		game.act("up");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), tile))
				return true;
		
		game.act("left");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), tile))
				return true;
		
		game.act("right");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), tile))
				return true;
		
		game.act("down");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlay(game.clone(), tile))
				return true;
		
		//System.out.println("***  Undo  ***");
		return false;
	}
	
	// NEW
	public static boolean recursivePlayTest(Game game, int tile, boolean upFirst)
	{
		// System.out.println(game);
		
		Game lastTurn = game.clone();
		num++;
		
		
		if(num % 6000 == 0)
		{
			//System.out.println("Current turns: " + game.getTurns());
			//System.out.println(game);
			
			System.out.println("Undoing the entire game");
			System.out.println(game.getTurns());
			while(game.getTurns() > 1)
				game.undo();
			System.out.println(game.getTurns());
			System.out.println(game);
		}
		
		
		
		// Stops automatically after 100000 moves because most games take only 2000
		if(num >= 15000)
		{
			System.out.println(game);
			System.out.println("***** Time Limit Reached *****");
			return true;
		}
		
		/*
		
		if(num == 7000)
		{
			//System.out.println("Current turns: " + game.getTurns());
			//System.out.println(game);
			//System.out.println("Undoing the entire game");
			for(int i = 0; i < game.getTurns(); i++)
				game.undo();
			//System.out.println(game);
		}
		
		
		
		if(num == 3500)
		{
			//System.out.println("Current turns: " + game.getTurns());
			//System.out.println(game);
			//System.out.println("Undoing 1/3 the turns");
			for(int i = 0; i < game.getTurns() * 2 / 3; i++)
				game.undo();
			//System.out.println(game);
		}
		
		*/
		
		
		if(game.won(tile))
			return true;
		
		if(upFirst)
		{
			game.act("up");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlayTest(game.clone(), tile, !upFirst))
					return true;
			
			game.act("left");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlayTest(game.clone(), tile, !upFirst))
					return true;
			
		}
		else
		{
			game.act("left");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlayTest(game.clone(), tile, !upFirst))
					return true;
			
			game.act("up");
			if(! (game.lost() || game.equals(lastTurn)))
				if(recursivePlayTest(game.clone(), tile, !upFirst))
					return true;
		}
		
		
		game.act("right");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlayTest(game.clone(), tile, false))
				return true;
		
		game.act("down");
		if(! (game.lost() || game.equals(lastTurn)))
			if(recursivePlayTest(game.clone(), tile, false))
				return true;
		
		//System.out.println("**** Undo ****");
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
