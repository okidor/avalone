package avalone.negend;

public class TileEntity extends Entity
{
	private Tile tile;
	private Inventory inv;
	private boolean open;
	
	public TileEntity(int posX,int posY,Chunk spawn)
	{
		super(posX,posY,spawn);
		this.tile = currentCaseLeft();
		posX = tile.coord.x;
		posY = tile.coord.y;
		inv = new Inventory(this,30);
		tile.setBlock(Block.chest);
		tile.link(this);
		open = false;
	}
	
	public void addItem(Item item,int amount)
	{
		inv.addItem(item, amount);
	}
	
	public void drawContent()
	{
		if(open)
		{
			inv.draw(oldPos, turned, false);
		}
	}
	
	public void destroy()
	{
		for(int i = 0;i < inv.getSize();i++)
		{
			inv.dropItemStack(i);
		}
	}

	@Override
	public void movements() 
	{
		
	}
}
