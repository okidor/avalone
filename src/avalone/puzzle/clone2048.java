package avalone.puzzle;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class clone2048 
{
	private AvaloneGLAPI glapi;
	private Point pOrig;
	private Point pEnd;
	private Point[][] points;
	private Point[][] points2;
	
	public clone2048(int nb,int screenSize)
	{
		glapi = new AvaloneGLAPI(screenSize,screenSize,"2048");
		glapi.enableTextures();
		pOrig = new Point(0,0);pEnd = new Point(799,799);
		int offset = (int) (screenSize/(nb * 7.5f));
		int tailleCase = (int) (screenSize/nb - (5.0f/4.0f) * offset);
		points = new Point[nb][nb];
		points2 = new Point[nb][nb];
		for(int i = 0;i < nb;i++)
		{
			for(int j = 0;j < nb;j++)
			{
				points[i][j] = new Point(offset*(i+1) + tailleCase*i,offset*(j+1) + tailleCase*j);
				points2[i][j] = (Point) points[i][j].clone(tailleCase);
			}
		}
		loop(nb,screenSize);
	}
	
	public void loop(int nb,int screenSize)
	{
		while (glapi.windowShouldNotClose()) 
        {
			glapi.glLoopBegin();
			drawTerrain(nb);
			glapi.drawFPS(new Point(5,screenSize - 45), new Point(45,screenSize - 5));
			glapi.glLoopEnd(100);
		}
		glapi.destroyDisplay();
	}
	
	public void drawTerrain(int nb)
	{
		glapi.drawRect(pOrig,pEnd,"gray");
		for(int i = 0;i < nb;i++)
		{
			for(int j = 0;j < nb;j++)
			{
				glapi.drawRect(points[i][j],points2[i][j],"ANTIQUE_WHITE");
			}
		}
	}
	
	public static void main(String[] argv)
    {
    	new clone2048(4,800);
    }
}