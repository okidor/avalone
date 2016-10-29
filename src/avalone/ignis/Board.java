package avalone.ignis;

public class Board 
{
	protected Tile[][] tiles;
	public int[][] positionValues;
	
	private int borderXMin;
	private int borderXMax;
	private int borderYMin;
	private int borderYMax;
	
	protected Element currentElement;
	
	protected boolean cancelAction;
	private boolean loop;
	
	protected boolean block;
	
	public Board()
	{
		tiles = new Tile[6][6];
		
		borderXMin = 0;
		borderXMax = 5;
		borderYMin = 0;
		borderYMax = 5;
		
		currentElement = Element.Earth;
		block = false;
		initValues();
	}
	
	private Board(Board board)
	{
		tiles = new Tile[6][6];
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				tiles[i][j] = board.tiles[i][j].clone();
			}
		}
		positionValues = board.positionValues;
		
		borderXMax = board.borderXMax;
		borderXMin = board.borderXMin;
		borderYMax = board.borderYMax;
		borderYMin = board.borderYMin;
		
		currentElement = board.currentElement;
		
		cancelAction = board.cancelAction;
		loop = board.loop;
		
		block = board.block;
	}
	
	private void initValues()
	{
		positionValues = new int[6][6];
		positionValues[0][0] = 0;positionValues[0][1] = 2;positionValues[0][2] = 4;
		positionValues[1][0] = 2;positionValues[1][1] = 6;positionValues[1][2] = 8;
		positionValues[2][0] = 4;positionValues[2][1] = 8;positionValues[2][2] = 10;
		
		for(int i = 0;i < 3;i++)
		{
			for(int j = 0;j < 3;j++)
			{
				positionValues[5 - i][j] = positionValues[i][j];
				positionValues[i][5 - j] = positionValues[i][j];
				positionValues[5 - i][5 - j] = positionValues[i][j];
			}
		}
	}
	
	public void initBoard()
	{
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				tiles[i][j] = new Tile(79 + i * 77,79 + j * 77);
			}
		}
		
		tiles[0][0].elem = Element.Fire;
		tiles[0][1].elem = Element.Fire;
		tiles[1][0].elem = Element.Fire;
		
		tiles[2][2].elem = Element.Fire;
		tiles[3][3].elem = Element.Fire;
		
		tiles[5][5].elem = Element.Fire;
		tiles[5][4].elem = Element.Fire;
		tiles[4][5].elem = Element.Fire;
		
		tiles[0][4].elem = Element.Water;
		tiles[0][5].elem = Element.Water;
		tiles[1][5].elem = Element.Water;
		
		tiles[3][2].elem = Element.Water;
		tiles[2][3].elem = Element.Water;
		
		tiles[4][0].elem = Element.Water;
		tiles[5][0].elem = Element.Water;
		tiles[5][1].elem = Element.Water;
	}
	
	public Element[][] getElements()
	{
		Element[][] elem = new Element[6][6];
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				elem[i][j] = tiles[i][j].elem;
			}
		}
		return elem;
	}
	
	public Board clone()
	{
		return new Board(this);
	}
	
	public Element checkWin()
	{
		int fire = 0;
		int water = 0;
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(tiles[i][j].elem == Element.Fire)
				{
					fire++;
				}
				else if(tiles[i][j].elem == Element.Water)
				{
					water++;
				}
			}
		}
		if(fire == 0)
		{
			return Element.Water;
		}
		else if(water == 0)
		{
			return Element.Fire;
		}
		else
		{
			return null;
		}
	}
	
	public void pushColumnUp(int indice,boolean simul)
	{
		pushColumn(indice,borderYMin,borderYMax,1,simul,-75);
	}
	
	public void pushColumnDown(int indice,boolean simul)
	{
		pushColumn(indice,borderYMax,borderYMin,-1,simul,75);
	}
	
	private void pushColumn(int indiceX,int indiceY,int border,int incr,boolean simul,int animation)
	{
		cancelAction = false;
		int tmpEarthAmount = Element.Earth.amount;
		int tmpWindAmount = Element.Wind.amount;
		int tmpFireAmount = Element.Fire.amount;
		int tmpWaterAmount = Element.Water.amount;
		if(indiceX < borderXMin || indiceX > borderXMax)
		{
			return;
		}
		if(currentElement.amount > 0)
		{
			currentElement.amount--;
			recurColumn(indiceX,indiceY,currentElement,border,incr,animation);
			if(cancelAction)
			{
				currentElement.amount++;
			}
			else
			{
				if(!simul)
				{
					block = true;
				}
				destroyTerrain();
			}
		}
		else
		{
			cancelAction = true;
		}
		if(simul)
		{
			Element.Earth.amount = tmpEarthAmount;
			Element.Wind.amount = tmpWindAmount;
			Element.Fire.amount = tmpFireAmount;
			Element.Water.amount = tmpWaterAmount;
		}
	}
	
	private void recurColumn(int indiceX,int indiceY,Element elem,int border,int incr,int animation)
	{
		if(tiles[indiceX][indiceY].elem == Element.Empty)
		{
			tiles[indiceX][indiceY].elem = elem;
			tiles[indiceX][indiceY].animateY = tiles[indiceX][indiceY].animateY + animation;
		}
		else
		{
			if(indiceY == border)
			{
				if((currentElement == Element.Earth && tiles[indiceX][indiceY].elem.canBePushedByEarth)
				 ||(currentElement == Element.Wind	&& tiles[indiceX][indiceY].elem.canBePushedByWind))
				{
					Element.Earth.amount++;
					tiles[indiceX][indiceY].elem = elem;
					tiles[indiceX][indiceY].animateY = tiles[indiceX][indiceY].animateY + animation;
					return;
				}
				else
				{
					cancelAction = true;
				}
			}
			else
			{
				recurColumn(indiceX,indiceY+incr,tiles[indiceX][indiceY].elem,border,incr,animation);
				if(!cancelAction)
				{
					tiles[indiceX][indiceY].elem = elem;
					tiles[indiceX][indiceY].animateY = tiles[indiceX][indiceY].animateY + animation;
				}
			}
		}
	}
	
	public void pushRowLeft(int indice,boolean simul)
	{
		pushRow(borderXMax,indice,borderXMin,-1,simul,75);
	}
	
	public void pushRowRight(int indice,boolean simul)
	{
		pushRow(borderXMin,indice,borderXMax,1,simul,-75);
	}
	
	private void pushRow(int indiceX,int indiceY,int border,int incr,boolean simul,int animation)
	{
		cancelAction = false;
		int tmpEarthAmount = Element.Earth.amount;
		int tmpWindAmount = Element.Wind.amount;
		int tmpFireAmount = Element.Fire.amount;
		int tmpWaterAmount = Element.Water.amount;
		if(indiceY < borderYMin || indiceY > borderYMax)
		{
			return;
		}
		if(currentElement.amount > 0)
		{
			currentElement.amount--;
			recurRow(indiceX,indiceY,currentElement,border,incr,animation);
			if(cancelAction)
			{
				currentElement.amount++;
			}
			else
			{
				if(!simul)
				{
					block = true;
				}
				destroyTerrain();
			}
		}
		else
		{
			cancelAction = true;
		}
		if(simul)
		{
			Element.Earth.amount = tmpEarthAmount;
			Element.Wind.amount = tmpWindAmount;
			Element.Fire.amount = tmpFireAmount;
			Element.Water.amount = tmpWaterAmount;
		}
	}
	
	private void recurRow(int indiceX,int indiceY,Element elem,int border,int incr,int animation)
	{
		if(tiles[indiceX][indiceY].elem == Element.Empty)
		{
			tiles[indiceX][indiceY].elem = elem;
			tiles[indiceX][indiceY].animateX = tiles[indiceX][indiceY].animateX + animation;
		}
		else
		{
			if(indiceX == border)
			{
				
				if((currentElement == Element.Earth && tiles[indiceX][indiceY].elem.canBePushedByEarth)
				 ||(currentElement == Element.Wind  && tiles[indiceX][indiceY].elem.canBePushedByWind))
				{
					Element.Earth.amount++;
					tiles[indiceX][indiceY].elem = elem;
					tiles[indiceX][indiceY].animateX = tiles[indiceX][indiceY].animateX + animation;
					return;
				}
				else
				{
					cancelAction = true;
				}
			}
			else
			{
				recurRow(indiceX+incr,indiceY,tiles[indiceX][indiceY].elem,border,incr,animation);
				if(!cancelAction)
				{
					tiles[indiceX][indiceY].elem = elem;
					tiles[indiceX][indiceY].animateX = tiles[indiceX][indiceY].animateX + animation;
				}
			}
		}
	}
	
	public void destroyTerrain()
	{
		loop = true;
		while(loop)
		{
			loop = false;
			/* ligne du bas */
			borderYMin = destroyLine(borderYMin,1);
		
			/* ligne du haut */
			borderYMax = destroyLine(borderYMax,-1);
		
			/* colonne de gauche */
			borderXMin = destroyColumn(borderXMin,1);
		
			/* colonne de droite */
			borderXMax = destroyColumn(borderXMax,-1);
		}
	}
	
	private int destroyLine(int border,int incr)
	{
		boolean mustDestroy = true;
		for(int i = borderXMin;i < borderXMax;i++)
		{
			if(tiles[i][border].elem != tiles[i+1][border].elem)
			{
				mustDestroy = false;
				break;
			}
			else if(tiles[i][border].elem == Element.Empty)
			{
				mustDestroy = false;
				break;
			}
		}
		if(mustDestroy && borderXMin < borderXMax)
		{
			for(int i = borderXMin;i <= borderXMax;i++)
			{
				tiles[i][border].destroy();
				Element.Earth.amount++;
			}
			loop = true;
			border = border + incr;
			Calculator.updateValues(borderXMin,borderXMax,borderYMin,borderYMax,positionValues);
		}
		return border;
	}
	
	private int destroyColumn(int border,int incr)
	{
		boolean mustDestroy = true;
		for(int i = borderYMin;i < borderYMax;i++)
		{
			if(tiles[border][i].elem != tiles[border][i+1].elem)
			{
				mustDestroy = false;
				break;
			}
			else if(tiles[border][i].elem == Element.Empty)
			{
				mustDestroy = false;
				break;
			}
		}
		if(mustDestroy && borderYMin < borderYMax)
		{
			for(int i = borderYMin;i <= borderYMax;i++)
			{
				tiles[border][i].destroy();
				Element.Earth.amount++;
			}
			loop = true;
			border = border + incr;
			Calculator.updateValues(borderXMin,borderXMax,borderYMin,borderYMax,positionValues);
		}
		return border;
	}
	
	public int getBorderXMax()
	{
		return borderXMax;
	}
	
	public int getBorderXMin()
	{
		return borderXMin;
	}
	
	public int getBorderYMax()
	{
		return borderYMax;
	}
	
	public int getBorderYMin()
	{
		return borderYMin;
	}
}