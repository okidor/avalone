package avalone.penguins;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Board 
{
	protected Tile tiles[][];
	protected int currentPlayer;
	protected int playersNumber;
	protected PlayerStat players[];
	protected boolean penguinsSetOnBoard;
	protected int penguinsPerPlayer;
	protected boolean hasAlreadyClicked;
	protected int placedPenguins;
	protected boolean hasWinner;
	protected int winner;
	private boolean firstClick;
	private Tile selectedTile;
	
	public Board()
	{
		tiles = new Tile[8][8];
		currentPlayer = 0;
		playersNumber = 2;
		players = new PlayerStat[playersNumber];
		penguinsSetOnBoard = false;
		penguinsPerPlayer = 4 - (playersNumber - 2);
		hasAlreadyClicked = false;
		placedPenguins = 0;
		hasWinner = false;
		winner = -1;
		firstClick = true;
		selectedTile = null;
		initBoard();
	}
	
	private Board(Board board)
	{
		tiles = new Tile[8][8];
		for(int i = 0;i < tiles.length;i++)
		{
			for(int j = 0;j < tiles[0].length;j++)
			{
				tiles[i][j] = board.tiles[i][j] == null ? null : board.tiles[i][j].clone();
			}
		}
		currentPlayer = board.currentPlayer;
		playersNumber = board.playersNumber;
		players = new PlayerStat[playersNumber];
		for(int i = 0;i < players.length;i++)
		{
			players[i] = board.players[i].clone();
		}
		penguinsSetOnBoard = board.penguinsSetOnBoard;
		penguinsPerPlayer = board.penguinsPerPlayer;
		hasAlreadyClicked = board.hasAlreadyClicked;
		placedPenguins = board.placedPenguins;
		hasWinner = board.hasWinner;
		winner = board.winner;
		firstClick = board.firstClick;
		selectedTile = board.selectedTile;
	}
	
	public Board clone()
	{
		return new Board(this);
	}
	
	public void initBoard()
	{
		int distribFish[] = new int[] {30,20,10};
		Random rand = new Random();
		for(int i = 0;i < tiles.length;i++)
		{
			for(int j = 0;j < tiles[0].length;j++)
			{
				if(i == tiles.length - 1)
				{
					if(j % 2 == 1)
					{
						tiles[i][j] = null;
					}
					else
					{
						tiles[i][j] = new Tile(i,j,genTileValue(rand,distribFish));
					}
				}
				else
				{
					tiles[i][j] = new Tile(i,j,genTileValue(rand,distribFish));
				}
			}
		}
		for(int i = 0;i < playersNumber; i++)
		{
			players[i] = new PlayerStat(i,penguinsPerPlayer);
		}
	}
	
	private int genTileValue(Random rand, int distribFish[])
	{
		while(true)
		{
			int randVal = rand.nextInt(6);
			if(randVal < 3 && distribFish[0] > 0)
			{
				distribFish[0]--;
				return 1;
			}
			if(randVal < 5 && distribFish[1] > 0)
			{
				distribFish[1]--;
				return 2;
			}
			if(randVal < 6 && distribFish[2] > 0)
			{
				distribFish[2]--;
				return 3;
			}
		}
	}
	
	public boolean placePenguin(int tileX, int tileY)
	{
		return placePenguin(tiles[tileX][tileY]);
	}
	
	protected boolean placePenguin(Tile clickedTile)
	{
		if(clickedTile != null && clickedTile.value == 1 && clickedTile.penguin == -1)
		{
			clickedTile.penguin = currentPlayer;
			placedPenguins++;
			if(placedPenguins == penguinsPerPlayer * playersNumber)
			{
				penguinsSetOnBoard = true;
				scanFinish();
			}
			nextPlayer(0);
			return true;
		}
		return false;
	}
	
	public boolean movePenguin(int oldTileX, int oldTileY, int newTileX, int newTileY)
	{
		return movePenguin(tiles[oldTileX][oldTileY],tiles[newTileX][newTileY]) == 0;
	}
	
	protected int movePenguin(Tile oldTile, Tile newTile)
	{
		if(oldTile != null && newTile != null && oldTile != newTile)
		{
			if(newTile.penguin == oldTile.penguin)
			{
				newTile.selected = true;
				oldTile.selected = false;
				return 1;
			}
			else if(Util.isValidPath(tiles, newTile, oldTile))
			{
				newTile.penguin = currentPlayer;
				tiles[oldTile.x][oldTile.y] = null;
				oldTile.selected = false;
				players[currentPlayer].addScore(oldTile.value);
				scanFinish();
				nextPlayer(0);
				return 0;
			}
		}
		return 2;
	}
	
	public boolean movePenguin(Point mouse)
	{
		//Tile oldSelectedTile = selectedTile;
		Tile clickedTile = Util.clickOnTile(tiles, mouse);
		if(firstClick)
		{
			if(clickedTile != null && clickedTile.penguin == currentPlayer)
			{
				firstClick = false;
				selectedTile = clickedTile;
				selectedTile.selected = true;
				//System.out.println("first click: " + selectedTile.x + ", " + selectedTile.y);
			}
		}
		else
		{
			int moveResult = movePenguin(selectedTile, clickedTile);
			if(moveResult == 0)
			{
				firstClick = true;
				selectedTile = null;
				return true;
			}
			else if(moveResult == 1)
			{
				selectedTile = clickedTile;
			}
		}
		return false;
	}
	
	public void scanFinish()
	{
		for(int k = 0;k < playersNumber;k++)
		{
			for(int i = 0;i < tiles.length;i++)
			{
				for(int j = 0;j < tiles[0].length;j++)
				{
					if(tiles[i][j] != null && tiles[i][j].penguin == k)
					{
						int c1 = tiles[i][j].x - (8 - tiles[i][j].y)/2;
						int c2 = tiles[i][j].x - tiles[i][j].y/2;
						int uptileX1 = c1 + (8 - (tiles[i][j].y + 1))/2;
						int uptileX2 = c2 + (tiles[i][j].y + 1)/2;
						int downtileX1 = c1 + (8 - (tiles[i][j].y - 1))/2;
						int downtileX2 = c2 + (tiles[i][j].y - 1)/2;
						/*System.out.println("checking tile " + i + ", " + j + " for player number " + number);
						System.out.println("c1 = " + c1 + ", c2 = " + c2);
						System.out.println("uptileX1 = " + uptileX1 + ", uptileX2 = " + uptileX2);
						System.out.println("downtileX1 = " + downtileX1 + ", downtileX2 = " + downtileX2);*/
						if(i > 0)
						{
							if(tiles[i-1][j] != null && tiles[i-1][j].penguin == -1)
							{
								//System.out.println("tile " + (i-1) + ", " + j + " is accessible");
								continue;
							}
						}
						if(i < tiles.length-1)
						{
							if(tiles[i+1][j] != null && tiles[i+1][j].penguin == -1)
							{
								//System.out.println("tile " + (i+1) + ", " + j + " is accessible");
								continue;
							}
						}
						if(j < tiles[0].length-1 && uptileX1 >= 0 && uptileX1 < tiles.length)
						{
							if(tiles[uptileX1][j+1] != null && tiles[uptileX1][j+1].penguin == -1)
							{
								//System.out.println("tile " + uptileX1 + ", " + (j+1) + " is accessible");
								continue;
							}
						}
						if(j < tiles[0].length-1 && uptileX2 >= 0 && uptileX2 < tiles.length)
						{
							if(tiles[uptileX2][j+1] != null && tiles[uptileX2][j+1].penguin == -1)
							{
								//System.out.println("tile " + uptileX2 + ", " + (j+1) + " is accessible");
								continue;
							}
						}
						if(j > 0 && downtileX1 >= 0 && downtileX1 < tiles.length)
						{
							if(tiles[downtileX1][j-1] != null && tiles[downtileX1][j-1].penguin == -1)
							{
								//System.out.println("tile " + downtileX1 + ", " + (j-1) + " is accessible");
								continue;
							}
						}
						if(j > 0 && downtileX2 >= 0 && downtileX2 < tiles.length)
						{
							if(tiles[downtileX2][j-1] != null && tiles[downtileX2][j-1].penguin == -1)
							{
								//System.out.println("tile " + downtileX2 + ", " + (j-1) + " is accessible");
								continue;
							}
						}
						players[k].decreasePenguin(tiles[i][j]);
						tiles[i][j] = null;
					}
				}
			}
			players[k].checkEnd();
		}
	}
	
	protected void nextPlayer(int depth)
	{
		currentPlayer = (currentPlayer + 1) % playersNumber;
		if(players[currentPlayer].getFinished())
		{
			if(depth < playersNumber)
			{
				nextPlayer(depth + 1);
			}
			else
			{
				int winner = 0;
				int nbWinner = 1;
				for(int i = 1;i < playersNumber;i++)
				{
					if(players[winner].getScore() < players[i].getScore())
					{
						winner = i;
						nbWinner = 1;
					}
					else if(players[winner].getScore() == players[i].getScore())
					{
						nbWinner++;
					}
				}
				if(nbWinner > 1)
				{
					//TODO: multi winner
				}
				else
				{
					hasWinner = true;
					this.winner = winner;
				}
			}
		}
	}
	
	public Tile[][] getTiles()
	{
		Tile tiles[][] = new Tile[8][8];
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(this.tiles[i][j] == null)
				{
					tiles[i][j] = null;
				}
				else
				{
					tiles[i][j] = this.tiles[i][j].clone();
				}
			}
		}
		return tiles;
	}
	
	public Point[] getPinguinsPos(int player)
	{
		Point[] penguinsPos = new Point[players[player].getNbPenguins()];
		int penguinIndex = 0;
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(tiles[i][j] != null && tiles[i][j].penguin == player)
				{
					penguinsPos[penguinIndex] = new Point(i,j);
					penguinIndex++;
				}
			}
		}
		return penguinsPos;
	}
	
	public int[][] getPenguins()
	{
		int[][] penguins = new int[8][8];
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(tiles[i][j] == null)
				{
					penguins[i][j] = -1;
				}
				else
				{
					penguins[i][j] = tiles[i][j].penguin;
				}
			}
		}
		return penguins;
	}
	
	public int getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	/*public int[][] getValues()
	{
		int[][] values = new int[8][8];
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(tiles[i][j] == null)
				{
					values[i][j] = 0;
				}
				else
				{
					values[i][j] = tiles[i][j].value;
				}
			}
		}
		return values;
	}*/
}

class Tile
{
	private ArrayList<Point> pos;
	public final int x,y;
	public final int value;
	public int penguin;
	private Point center; 
	public boolean selected;
	
	public Tile(int x, int y, int value)
	{
		penguin = -1;
		pos = new ArrayList<Point>();
		this.x = x;
		this.y = y;
		int altY = y/2;
		int spacingX = 92;
		int spacingY = 157;
		if(y % 2 == 0)
		{
			pos.add(new Point(spacingX * x,80 + spacingY * altY));
			pos.add(new Point(45 + spacingX * x,105 + spacingY * altY));
			pos.add(new Point(90 + spacingX * x,80 + spacingY * altY));
			pos.add(new Point(90 + spacingX * x,25 + spacingY * altY));
			pos.add(new Point(45 + spacingX * x,spacingY * altY));
			pos.add(new Point(spacingX * x,25 + spacingY * altY));
		}
		else
		{
			pos.add(new Point(46 + spacingX * x,156 + spacingY * altY));
			pos.add(new Point(91 + spacingX * x,181 + spacingY * altY));
			pos.add(new Point(136 + spacingX * x,156 + spacingY * altY));
			pos.add(new Point(136 + spacingX * x,106 + spacingY * altY));
			pos.add(new Point(91 + spacingX * x,81 + spacingY * altY));
			pos.add(new Point(46 + spacingX * x,106 + spacingY * altY));
		}
		this.value = value;
		center = new Point(pos.get(1).x, (pos.get(2).y + pos.get(3).y)/2);
		selected = false;
		//System.out.println(value + "_fish.jpg");
	}
	
	private Tile(Tile tile)
	{
		pos = new ArrayList<Point>();
		for(int i = 0;i < tile.pos.size();i++)
		{
			pos.add(tile.pos.get(i).clone());
		}
		x = tile.x; y = tile.y;
		value = tile.value;
		penguin = tile.penguin;
		center = tile.center;
		selected = tile.selected;
	}
	
	public Tile clone()
	{
		return new Tile(this);
	}
	
	public void draw(AvaloneGLAPI glapi)
	{
		glapi.drawConvex(pos, "blue");
		if(selected)
		{
			glapi.drawFramedConvex(pos, "red");
		}
		glapi.clearFilter();
		glapi.drawTexturedRect(pos.get(5).clone(20, 0), pos.get(5).clone(70,50), value + "_fish.jpg");
		glapi.unbindTexture();
		if(penguin > -1)
		{
			glapi.drawCircle(center, 40, GraphicBoard.playerColors[penguin]);
		}
	}
	
	public ArrayList<Point> getPos()
	{
		return pos;
	}
}
