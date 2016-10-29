package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Const;

public class Tile 
{
	public Point coord;
	private Block block;
	public int taille;
	public int xG,xD,yB,yH;
	public Point num;
	private int tick;
	private int textureOffset;
	
	public Ore ore;
	public int subID;
	public int light;
	public boolean debug;
	public boolean debug2;
	private boolean locked;
	
	private TileEntity linked;
	
	//public static final Tile undefined_tile = new Tile(-1,-1,Block.undefined);
	
	public Tile(int x,int y)
	{
		this(x,y,Block.air);
	}
	
	public Tile(int x,int y,Block block)
	{
		coord = new Point(x,y);
		taille = Const.tailleCase;
		initBorders();
		num = new Point(coord.x/taille, coord.y/taille);
		this.block = block;
		ore = new Ore();
		subID = 0;
		initLight();
		locked = false;
		debug = false;debug2 = false;
		tick = 0;
		textureOffset = 0;
	}
	
	public void initLight()
	{
		if(!getBlockSolidity().equals("nonsolid"))
		{
			light = 0;
			return;
		}
		light = Const.maxLight;
	}
	
	public void initBorders()
	{
		if(coord.y == 0)
		{
			yB = Const.tailleCase*Const.tailleChunkY;
			yH = coord.y + taille;
		}
		else if(coord.y == Const.tailleCase * (Const.tailleChunkY - 1))
		{
			yB = coord.y;
			yH = 0;
		}
		else
		{
			yB = coord.y;
			yH = coord.y + taille;
		}
		
		if(coord.x == 0)
		{
			xG = Const.tailleChunkX * Const.tailleCase;
			xD = coord.x + taille;
		}
		else if(coord.x == Const.tailleCase * (Const.tailleChunkX - 1))
		{
			xG = coord.x;
			xD = 0;
		}
		else
		{
			xG = coord.x;
			xD = coord.x + taille;
		}
		   //a revoir pour bords de chunk
	}
	
	public boolean isEmpty()
	{
		return true;
	}
	
	public void lock()
	{
		locked = true;
	}
	
	public void link(TileEntity te)
	{
		linked = te;
	}
	
	public String getTexture()
	{
		return block.getTexture(subID);
	}
	
	public int getTextureOffset()
	{
		tick++;
		//Const.debug("(Tile:getTextureOffset): tick = " + tick);
		if(tick >= Const.maxFPS/2)
		{
			tick = 0;
			textureOffset++;
		}
		return textureOffset;
	}
	
	public void clearTextureOffset()
	{
		textureOffset = 0;
	}
	
	public int getBlockID()
	{
		return block.blockID;
	}
	
	public String getBlockSolidity()
	{
		return block.solidity;
	}
	
	public int getFlowPotential()
	{
		return block.flowPotential;
	}
	
	public boolean canBeBroken()
	{
		return block.breakable;
	}
	
	public void setBlock(Block block)
	{
		if(!locked)
		{
			this.block = block;
		}
		else
		{
			Const.debug("(Tile:setBlock): couldn't change block because tile is locked");
		}
	}
	
	public void setBlock(Item item)
	{
		if(!locked)
		{
			if(item.isPlacable())
			{
				block = Block.getBlock(item.id);
				subID = item.subID;
			}
			else
			{
				Const.debug("(Tile): tried to place non placable item");
			}
		}
		else
		{
			Const.debug("(Tile:setBlock): couldn't change block because tile is locked");
		}
	}
	
	public void destroyBlock()
	{
		block = Block.air;
		subID = 0;
		light = Const.maxLight;
		if(linked != null)
		{
			linked.destroy();
		}
	}
}
