package avalone.negend;

import java.util.ArrayList;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public abstract class Chunk 
{
	public Tile[][] cases;
	public Tile[][] behind;
	public Point pos;
	protected int surfaceModifier;
	protected int[] surfaceModifRegister;
	protected boolean setLight;
	public ArrayList<Mob> mobList;
	public ArrayList<Player> playerList;
	protected ArrayList<WorldItem> itemList;
	protected ArrayList<TileEntity> tileEntityList;
	protected Chunk cHorizontal;
	protected Chunk cVertical;
	protected int luckFactor;
	protected boolean genFlag;
	public Chunk[] chunkBuffer;
	public Map map;
	protected boolean hasGenerated;
	
	protected Chunk(Map map,Point pos,boolean setLight)
	{
		this.pos = pos;
		cases = new Tile[Const.tailleChunkX][Const.tailleChunkY];
		behind = new Tile[Const.tailleChunkX][Const.tailleChunkY];
		initCases();
		this.setLight = setLight;
		surfaceModifRegister = new int[Const.tailleChunkX];
		mobList = new ArrayList<Mob>();
		playerList = new ArrayList<Player>();
		itemList = new ArrayList<WorldItem>();
		tileEntityList = new ArrayList<TileEntity>();
		chunkBuffer = new Chunk[9];
		this.map = map;
		chunkBuffer[4] = this;
		hasGenerated = false;
	}
	
	public void generate(Ore[] ores)
	{
		if(!hasGenerated)
		{
			generateBlocks(ores);
			hasGenerated = true;
		}
		else
		{
			Const.debug("(Chunk:generate): tried to generate chunk " + pos.x + ", " + pos.y + " twice");
		}
	}
	
	public abstract Block chooseBlock(int posX,int posY);
	
	public abstract void extendGen();
	
	protected abstract void generateBlocks(Ore[] ores);
	
	public Point genSpawnCoords()
	{
		Point p = new Point();
		p.y = -1;
		int i = 0;
		while(p.y == -1 && i <= 100)
		{
			p.x = Var.rand.nextInt(Const.tailleChunkX*Const.tailleCase);
			p.y = getFirstAirBlockHeight(p.x);
			i++;
		}
		return p;
	}
	
	public void light()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(cases[i][j].getBlockSolidity().equals("solid"))
				{
					cases[i][j].light = sunLight(i,j,2);
				}
				else if(cases[i][j].getBlockSolidity().equals("liquid"))
				{
					cases[i][j].light = sunLight(i,j,1);
				}
			}
		}
	}
	
	public int sunLight(int x,int y,int decr)
	{
		if(y < Const.tailleChunkY-1)
		{
			if(cases[x][y+1].getBlockSolidity().equals("nonsolid"))
			{
				return Const.max(cases[x][y+1].light - decr,0);
			}
			else
			{
				if(cases[x][y+1].getBlockSolidity().equals("liquid"))
				{
					return Const.max(sunLight(x,y+1,1) - decr,0);
				}
				else
				{
					return Const.max(sunLight(x,y+1,2) - decr,0);
				}
				
			}
		}
		return cases[x][y].light;
	}
	
	public void updateLight()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(!cases[i][j].getBlockSolidity().equals("nonsolid"))
				{
					sideLight(i,j);
				}
			}
		}
	}
	
	public void sideLight(int x,int y)
	{
		int leftLight = getLeftLight(x,y,chunkBuffer[3]);
		int rightLight = getRightLight(x,y,chunkBuffer[5]);
		int downLight = getDownLight(x,y,chunkBuffer[7]);
		int upLight = getUpLight(x,y,chunkBuffer[1]);
		cases[x][y].light = Const.max(leftLight,rightLight,downLight,upLight);
		cases[x][y].light = Const.max(cases[x][y].light,0);
	}
	
	private int getLeftLight(int x,int y,Chunk cLeft)
	{
		if(x > 0)
		{
			if(cases[x-1][y].getBlockSolidity().equals("solid"))
			{
				return cases[x-1][y].light - 2;
			}
			else
			{
				if(cases[x-1][y].light > 2)
				{
					return cases[x-1][y].light - 1;
				}
				return cases[x-1][y].light;
			}
		}
		else
		{
			if(cLeft.cases[Const.tailleChunkX-1][y].getBlockSolidity().equals("solid"))
			{
				return cLeft.cases[Const.tailleChunkX-1][y].light - 2;
			}
			else
			{
				if(cLeft.cases[Const.tailleChunkX-1][y].light > 2)
				{
					return cLeft.cases[Const.tailleChunkX-1][y].light - 1;
				}
				return cLeft.cases[Const.tailleChunkX-1][y].light;
			}
		}
	}
	
	private int getRightLight(int x,int y,Chunk cRight)
	{
		if(x < Const.tailleChunkX-1)
		{
			if(cases[x+1][y].getBlockSolidity().equals("solid"))
			{
				return cases[x+1][y].light - 2;
			}
			else
			{
				if(cases[x+1][y].light > 2)
				{
					return cases[x+1][y].light - 1;
				}
				return cases[x+1][y].light;
			}
		}
		else
		{
			if(cRight.cases[0][y].getBlockSolidity().equals("solid"))
			{
				return cRight.cases[0][y].light - 2;
			}
			else
			{
				if(cRight.cases[0][y].light > 2)
				{
					return cRight.cases[0][y].light - 1;
				}
				return cRight.cases[0][y].light;
			}
		}
	}
	
	private int getDownLight(int x,int y,Chunk cDown)
	{
		if(y > 0)
		{
			if(cases[x][y-1].getBlockSolidity().equals("solid"))
			{
				return cases[x][y-1].light - 2;
			}
			else
			{
				if(cases[x][y-1].light > 2)
				{
					return cases[x][y-1].light - 1;
				}
				return cases[x][y-1].light;
			}
		}
		else
		{
			if(cDown.cases[x][Const.tailleChunkY-1].getBlockSolidity().equals("solid"))
			{
				return cDown.cases[x][Const.tailleChunkY-1].light - 2;
			}
			else
			{
				if(cDown.cases[x][Const.tailleChunkY-1].light > 2)
				{
					return cDown.cases[x][Const.tailleChunkY-1].light - 1;
				}
				return cDown.cases[x][Const.tailleChunkY-1].light;
			}
		}
	}
	
	private int getUpLight(int x,int y,Chunk cUp)
	{
		if(y < Const.tailleChunkY-1)
		{
			if(cases[x][y+1].getBlockSolidity().equals("solid"))
			{
				return cases[x][y+1].light - 2;
			}
			else
			{
				if(cases[x][y+1].light > 2)
				{
					return cases[x][y+1].light - 1;
				}
				return cases[x][y+1].light;
			}
		}
		else
		{
			if(cUp.cases[x][0].getBlockSolidity().equals("solid"))
			{
				return cUp.cases[x][0].light - 2;
			}
			else
			{
				if(cUp.cases[x][0].light > 2)
				{
					return cUp.cases[x][0].light - 1;
				}
				return cUp.cases[x][0].light;
			}
		}
	}
	
	public void smooth()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(i > 0 && i < Const.tailleChunkX - 1)
				{
					if(j > 0 && j < Const.tailleChunkY - 1)
					{
						if(cases[i][j].getBlockID() == Block.grass.blockID)
						{
							if(cases[i-1][j].getBlockID() == Block.air.blockID && cases[i+1][j].getBlockID() == Block.air.blockID) 
							{
								cases[i][j].destroyBlock();
								cases[i][j-1].setBlock(Block.grass);
								cases[i][j-1].subID = 0;
							}
						}
						else if(cases[i][j].getBlockID() == Block.air.blockID)
						{
							if(cases[i-1][j].getBlockID() == Block.grass.blockID && cases[i+1][j].getBlockID() == Block.grass.blockID) 
							{
								if(cases[i][j-1].getBlockID() == Block.grass.blockID)
								{
									cases[i][j].setBlock(Block.grass);
									cases[i][j-1].setBlock(Block.dirt);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void grassGrow()
	{
		for(int i = 0;i < cases.length;i++)
		{
			for(int j = 0;j < cases[0].length;j++)
			{
				if(j+1 < Const.tailleChunkY)
				{
					if(cases[i][j].getBlockID() == Block.dirt.blockID && cases[i][j+1].getBlockID() == Block.air.blockID)
					{
						if(cases[i][j].light > 7)
						{
							cases[i][j].setBlock(Block.grass);
						}
					}
				}
				if(cases[i][j].getBlockID() == Block.grass.blockID)
				{
					if(j < Const.tailleChunkY-1 && cases[i][j+1].getBlockID() != Block.air.blockID)
					{
						cases[i][j].setBlock(Block.dirt);
						cases[i][j].subID = Const.noMetaData;
					}
					else if(i > 0 && cases[i-1][j].getBlockID() == Block.air.blockID)
					{
						if(i < Const.tailleChunkX-1 && cases[i+1][j].getBlockID() == Block.air.blockID)
						{
							cases[i][j].subID = Const.upSubID;
						}
						else
						{
							cases[i][j].subID = Const.leftSubID;
						}
					}
					else if(i < Const.tailleChunkX-1 && cases[i+1][j].getBlockID() == Block.air.blockID)
					{
						cases[i][j].subID = Const.rightSubID;
					}
					else
					{
						cases[i][j].subID = Const.defaultSubID;
					}
				}
			}
		}
	}
	
	public void draw()
	{
		if(setLight)
		{
			updateLight();
		}
		grassGrow();
		for(int i = 0;i < cases.length;i++)
		{
			for(int j = 0;j < cases[0].length;j++)
			{
				drawTile(behind[i][j]);
				drawTile(cases[i][j]);
			}
		}
		drawShadows();
		AI();
		drawAlive();
		items();
		drawLimit();
	}
	
	private void drawTile(Tile tile)
	{
		Point p1 = tile.coord.clone(0);
		Point p2 = p1.clone(Const.tailleCase);
		
		if(tile.getBlockID() != 0)
		{
			/*Point p1 = tile.coord.clone(0);
			Point p2 = p1.clone(Const.tailleCase);*/
			
			if(tile.light > 0)
			{
				if(tile.getBlockSolidity().equals("liquid"))
				{
					Var.rend.drawLiquid(p1,p2,tile);
				}
				else
				{
					Var.rend.draw(p1,p2,tile);
				}
			}
		}
		if(tile.debug)
		{
			Var.rend.drawDebug(p1,p2,tile,"red");
			//Const.debug("(Chunk:drawTile): " + pos.x + ", " + pos.y);
		}
		if(tile.debug2)
		{
			Var.rend.drawDebug(p1,p2,tile,"blue");
		}
	}
	
	protected void items()
	{
		for(int i = 0;i < itemList.size();i++)
		{
			WorldItem item = itemList.get(i);
			ArrayList<AliveEntity> tmp = new ArrayList<AliveEntity>();
			for(int j = 0;j < mobList.size();j++)
			{
				if(mobList.get(j).health > 0)
				{
					tmp.add(mobList.get(j));
				}
			}
			for(int j = 0;j < playerList.size();j++)
			{
				if(playerList.get(j).health > 0)
				{
					tmp.add(playerList.get(j));
				}
			}
			item.setTargetToNearest(tmp);
			Physic.verifyGrav(item);
			item.movements();
			item.draw();
		}
	}
	
	public void addWorldItem(WorldItem item)
	{
		itemList.add(item);
	}
		
	public void AI()
	{
		for(int i = 0;i < mobList.size();i++)
		{
			Mob mob = mobList.get(i);
			mob.live();
		}
	}
	
	public void drawAlive()
	{
		for(int i = 0;i < mobList.size();i++)
		{
			mobList.get(i).draw();
		}
		for(int i = 0;i < playerList.size();i++)
		{
			//rend.setOffset(0, 0);
			playerList.get(i).draw();
		}
	}
	
	public void getDamagedMob(Player play)
	{
		for(int i = 0;i < mobList.size();i++)
		{
			Mob mob = mobList.get(i);
			if(Mob.distance(play.pos, mob.pos) < 21)
			{
				Const.debug("(Chunk:getDamagedMob): distance = " + Mob.distance(play.pos, mob.pos));
				if(play.turned == 0 || play.turned == 1)
				{
					if(mob.pos.x <= play.pos.x)
					{
						mob.takeDamage(play,play.inv.getSelectedWeaponDamageAmount(),-15);
					}
				}
				else if(play.turned == 2)
				{
					if(mob.pos.x >= play.pos.x)
					{
						mob.takeDamage(play,play.inv.getSelectedWeaponDamageAmount(),15);
					}
				}
			}
		}
	}
	
	public void drawShadows()
	{
		//ArrayList<Point>[] als = new ArrayList[Const.maxLight];
		Var.rend.unbindTexture();
		if(setLight)
		{
			for(int k = 0;k < Const.maxLight;k++)
			{
				ArrayList<Point> p1s = new ArrayList<Point>();
				ArrayList<Point> p2s = new ArrayList<Point>();
				for(int i = 0;i < cases.length;i++)
				{
					for(int j = 0;j < cases[0].length;j++)
					{
						if(cases[i][j].light == k)
						{
							Point p1 = cases[i][j].coord.clone(0);
							p1s.add(p1);
							p2s.add(p1.clone(Const.tailleCase));
						}
					}
				}
				Var.rend.drawShadowGroup(p1s, p2s, k);
			}
		}
		Var.rend.clearFilter();
	}
	
	public void drawLimit()
	{
		Var.rend.drawChunkLimit();
		/*for(int i = 0;i < itemList.size();i++)
		{
			rend.drawRadiusDebug(itemList.get(i));
		}*/
	}
	
	protected void initCases()
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				cases[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase);
				behind[i][j] = new Tile(i*Const.tailleCase,j*Const.tailleCase);
			}
		}
	}
	
	public void initGen(Ore[] ores)
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				cases[i][j].setBlock(chooseBlock(i,j));
				int b = Var.rand.nextInt(ores.length);
				int c = Var.rand.nextInt(luckFactor);
				if(c == 0 && cases[i][j].getBlockSolidity().equals("solid"))
				{
					cases[i][j].ore = ores[b];
					cases[i][j].subID = (cases[i][j].subID + 1 + b) * (-1);
				}
			}
		}
		extendGen();
		if(setLight)
		{
			light();
		}
	}
	
	public int getFirstAirBlockHeight(int posX)
	{
		int gridX = posX/Const.tailleCase;
		for(int i = 0;i < Const.tailleChunkY-2;i++)
		{
			if(cases[gridX][i].getBlockID() == Block.grass.blockID && cases[gridX][i+1].getBlockID() == Block.air.blockID && cases[gridX][i+2].getBlockID() == Block.air.blockID)
			{
				return cases[gridX][i+1].coord.y;
			}
		}
		return -1;
	}
	
	public void choose(int x,int y,Inventory inv,boolean add)
	{
		if(add)
		{
			addBlock(x,y,inv);
		}
		else
		{
			destroyBlock(x,y,inv);
		}
		//cases[x][y].block.setLayer(rend);
	}
	
	public void destroyBlock(int x,int y,Inventory inv)
	{
		if(cases[x][y].getBlockID() != Block.air.blockID)
		{
			doDestroy(cases[x][y],inv);
		}
		else
		{
			doDestroy(behind[x][y],inv);
		}
	}
	
	private void doDestroy(Tile tile,Inventory inv)
	{
		if(tile.canBeBroken())
		{
			boolean mineral = false;
			if(tile.subID < 0)
			{
				mineral = true;
				tile.subID = (tile.subID + 1 + tile.ore.id) * (-1);
				//inv.addMineral(tile.ore.id,tile.ore.color);
			}
			//Const.debug("(Chunk:destroyBlock): id of worlditem spawned = " + cases[x][y].getBlockID());
			if(mineral)
			{
				addWorldItem(new WorldItem(tile.coord.x,tile.coord.y,this,new MineralItem(tile.subID,tile.ore.color,tile.ore),1));
			}
			addWorldItem(new WorldItem(tile.coord.x,tile.coord.y,this,new ItemBlock(tile.getBlockID(),tile.subID),1));
			tile.destroyBlock();
		}
	}
	
	public void addBlock(int x,int y,Inventory inv)
	{
		if(cases[x][y].getBlockSolidity().equals("nonsolid"))
		{
			for(int i = 0; i < playerList.size();i++)
			{
				Player tmpPlay = playerList.get(i);
				if(tmpPlay.currentCaseLeft().num.x == x && tmpPlay.currentCaseLeft().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (currentCaseLeft)");
					return;
				}
				else if(tmpPlay.currentCaseRight().num.x == x && tmpPlay.currentCaseRight().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (currentCaseRight)");
					return;
				}
				else if(tmpPlay.headLeft().num.x == x && tmpPlay.headLeft().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (headLeft)");
					return;
				}
				else if(tmpPlay.headRight().num.x == x && tmpPlay.headRight().num.y == y)
				{
					Const.debug("(Chunk:addBlock): tried to place a block on player (headRight)");
					return;
				}
			}
			Item tmp = inv.useItem();
			cases[x][y].setBlock(tmp);
			cases[x][y].light = 6;
		}
	}
	
	/*public Renderer getRenderer()
	{
		return rend;
	}*/
}
