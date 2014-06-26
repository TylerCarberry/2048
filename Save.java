import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Save
{
	public static final String SAVELOCATION = "savegame.txt";
	
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
	
	public static Game loadGame() throws IOException, ClassNotFoundException
	{
		
		FileInputStream fi = new FileInputStream(SAVELOCATION);
		ObjectInputStream input = new ObjectInputStream(fi);
		
		Game game = (Game) input.readObject();
		
		fi.close();
		input.close();
		
		return game;
	}
}
