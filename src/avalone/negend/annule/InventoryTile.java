package avalone.negend.annule;

import avalone.api.util.Point;
import avalone.negend.Block;
import avalone.negend.Chunk;
import avalone.negend.Inventory;
import avalone.negend.Tile;

public class InventoryTile extends Tile implements Storage
{
	public Inventory inv;
	private Chunk currentChunk;
	
	private InventoryTile(int x,int y,Chunk currentChunk)
	{
		super(x,y,Block.chest);
		//inv = new Inventory(this,30);
		this.currentChunk = currentChunk;
	}

	@Override
	public Point getPos() 
	{
		return coord;
	}

	@Override
	public Chunk getCurrentChunk() 
	{
		return currentChunk;
	}
}
