import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Save
{
	public static final String SAVELOCATION = "savegame.txt";
	
	/**
	 * Saves the current game to a file. Overwrites any previous saves.
	 * @param game The game to save
	 * @throws IOException Save file can not be accessed
	 */
	public static void saveGame(Game game) throws IOException
	{
		File file = new File(SAVELOCATION);
		
		// Serialize the game
		FileOutputStream fop = new FileOutputStream(file);
		ObjectOutputStream output = new ObjectOutputStream(fop);
		
		// Write the game to the file
		output.writeObject(game);
	
		output.close();
		fop.close();
	}
	
	/**
	 * Loads the saved game from a file
	 * @return The saved game
	 * @throws IOException Save file can not be accessed
	 * @throws ClassNotFoundException Class of a serialized object cannot be found
	 */
	public static Game loadGame() throws IOException, ClassNotFoundException
	{
		
		FileInputStream fi = new FileInputStream(SAVELOCATION);
		ObjectInputStream input = new ObjectInputStream(fi);
		
		Game game = (Game) input.readObject();
		
		fi.close();
		input.close();
		
		return game;
	}
	
	/**
	 * Clears the save file
	 * @throws IOException Save file can not be accessed
	 */
	public static void clearSave() throws IOException
	{
		File file = new File(SAVELOCATION);
		
		// Serialize the game
		FileOutputStream fop = new FileOutputStream(file);
		ObjectOutputStream output = new ObjectOutputStream(fop);
		
		// Write an empty String over the game
		output.writeObject("");
	
		output.close();
		fop.close();
	}
	
}
