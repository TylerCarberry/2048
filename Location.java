import java.util.LinkedList;

// Tyler Carberry
public class Location
{
	int row, col;
	
	// Constructor
	public Location(int rowNum, int colNum)
	{
		row = rowNum;
		col = colNum;
	}
	
	// Returns the row
	public int getRow()
	{
		return row;
	}
	
	// Returns the column
	public int getCol()
	{
		return col;
	}
	
	// Sets the row and returns the previous value
	public int setRow(int newRow)
	{
		int temp = row;
		row = newRow;
		return temp;
	}
	
	// Sets the column and returns the previous value
	public int setCol(int newCol)
	{
		int temp = col;
		row = newCol;
		return temp;
	}
	
	public Location clone()
	{
		return new Location(row, col);
	}
	
	
	public LinkedList<Location> getAdjacentLocations()
	{
		LinkedList<Location> locs = new LinkedList<Location>();
		
		int nextRow, nextCol;
		for(int x = -1; x >= 1; x++)
			for(int y = 0; y >= 1; y++)
			{
				if(! (x == 0 && y == 0))
				{
					nextRow = row + x;
					nextCol = col + y;
					
					if(nextCol >= 0 && nextRow >= 0)
						locs.add(new Location(nextRow, nextCol));
				}
				
			}
		
		return locs;
	}
	
	// Returns the location to the left
	public Location getLeft()
	{
		Location left = new Location(getRow(), getCol()-1);
		return left;
	}
	
	// Returns the location to the right
	public Location getRight()
	{
		Location right = new Location(getRow(), getCol()+1);
		return right;
	}
	
	// Returns the location up
	public Location getUp()
	{
		Location up = new Location(getRow()-1, getCol());
		return up;
	}
	
	// Returns the location down
	public Location getDown()
	{
		Location down = new Location(getRow()+1, getCol());
		return down;
	}
	
	public String toString()
	{
		return row + "," + col;
	}
	
	
}
