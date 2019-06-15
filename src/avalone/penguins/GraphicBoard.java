package avalone.penguins;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class GraphicBoard extends Board
{
	public static final String playerColors[] = new String[] {"red", "blue", "yellow", "green"};
	public static final Point scorePos[] = new Point[] {new Point(800,400), new Point(800,300), new Point(800,200), new Point(800,100)};
	public static final Point scoreTextPos[] = new Point[] {new Point(840,390), new Point(860,410), new Point(840,290), new Point(860,310), new Point(840,190), new Point(860,210), new Point(840,90), new Point(860,110)};
	
	private AvaloneGLAPI glapi;
	private Penguins_IA ia;
	private Penguins_IA ia2;
	
	public GraphicBoard()
	{
		super();
		glapi = new AvaloneGLAPI(1000,800,"Penguins","penguins");
		ia = new Penguins_IA(this,1,3);
		ia2 = new Penguins_IA(this,0,3);
		loop();
	}
	
	private void loop()
	 {
		 glapi.enableTextures();
	     while (glapi.windowShouldNotClose()) 
	     {
	    	 glapi.glLoopBegin();
	    	 
	    	 if(hasWinner)
	    	 {
	    		 winner();
	    	 }
	    	 else
	    	 {
	    		 if(currentPlayer == 1)
	    		 {
	    			 //play();
	    			 ia.evalCurrentSituation();
	    		 }
	    		 else
	    		 {
	    			 play();
	    			 //ia2.evalCurrentSituation();
	    		 }
	    		 draw();
	    	 }
	    	 //glapi.drawFPS(new Point(5,70), new Point(45,110));
	    	 
	    	 glapi.glLoopEnd(60);
	     }
	     glapi.destroyDisplay();
	 }
	
	public void draw()
	{
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(tiles[i][j] != null)
				{
					tiles[i][j].draw(glapi);
				}
			}
		}
		for(int i = 0;i < playersNumber;i++)
		{
			glapi.drawCircle(scorePos[i], 40, playerColors[i]);
			glapi.drawText(scoreTextPos[i*2], scoreTextPos[i*2+1], players[i].getScore(), "white");
		}
	}
	
	public void play()
	{
		boolean next = false;
		if (glapi.hasLeftClicked())
		{
			if(!hasAlreadyClicked)
			{
				Tile clickedTile = Util.clickOnTile(tiles, glapi.getMouse());
				if(!penguinsSetOnBoard)
				{
					next = placePenguin(clickedTile);
				}
				else
				{
					next = movePenguin(glapi.getMouse());
				}
				hasAlreadyClicked = true;
			}
		}
		else
		{
			hasAlreadyClicked = false;
		}
		if(!next)
		{
			//TODO effet graphique
		}
	}
	
	public void winner()
	{
		glapi.drawRect(new Point(0,0), new Point(1000,800), playerColors[winner]);
		glapi.drawText(new Point(200,200), new Point(800,600), playerColors[winner] + " wins", "white");
	}
	
	public static void main (String[] args)
	{
		new GraphicBoard();
	}
}
