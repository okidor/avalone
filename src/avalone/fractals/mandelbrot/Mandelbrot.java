package avalone.fractals.mandelbrot;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import java.lang.Math;

public class Mandelbrot 
{
	private byte stepIter;
	private byte maxStepIter;
	private float stepX;
	private float stepY;
	private AvaloneGLAPI glapi;
	private Point p1;
	private Point p2;
	private int xMin = -2;
	private int xMax = 1;
	private int yMin = -1;
	private int yMax = 1;
	private String[] colors = {"black","purple","blue","red","orange","yellow","green","white","light_blue"};
	private byte[][] num;
	
	public Mandelbrot()
	{
		stepIter = 1;
		maxStepIter = 100;
		stepX = 3.0f/1500.0f;
		stepY = 2.0f/1000.0f;
		num = new byte[1501][1001];
		glapi = new AvaloneGLAPI(1500,1000,"Mandelbrot");
		p1 = new Point(0,0);
		p2 = new Point(1500,1000);
		//long time1 = 0, time2 = 0, time3 = 0, time4 = 0, time5 = 0;
		while(stepIter < maxStepIter)
		{
			//time1 = System.currentTimeMillis();
			computeMandelbrot();
			//time2 = System.currentTimeMillis();
			glapi.glLoopBegin();
			//time3 = System.currentTimeMillis();
			draw();
			//time4 = System.currentTimeMillis();
			glapi.glLoopEnd(60);
			//time5 = System.currentTimeMillis();
			stepIter++;
			//long compTime = time2 - time1;
			//long drawTime = time4 - time3;
			//System.out.println("computation time: " + compTime + ", " + drawTime);
		}
		loop();
	}
	
	private void computeMandelbrot()
	{
		int xCounter = 0;
		//long time1 = 0,time2 = 0,time3 = 0,time4 = 0,time5 = 0;
		for(double i = -2;i < 1;i = i + stepX)
		{
			int yCounter = 0;
			for(double j = -1;j < 1;j = j + stepY)
			{
				Complex complex = new Complex(0.0,0.0);
				//time2 = System.nanoTime();
				for(int k = 0; k <= stepIter;k++)
				{
					complex = complex.square();
					complex.addSelf(i,j);
				}
				//time3 = System.nanoTime();
				double result = complex.abs();
				//time4 = System.nanoTime();
				if(result <= 2)
				{
					num[xCounter][yCounter] = (byte) (stepIter % colors.length);
				}
				/*else
				{
					num[xCounter][yCounter] = 0;
				}*/
				//long suiteTime = time3 - time2;
				//long absTime = time4 - time3;
				//System.out.println("mandel time: " + suiteTime + ", " + absTime);
				yCounter++;
			}
			xCounter++;
		}
		//System.out.println("pixelColor = " + pixelColor);
	}
	
	private void draw()
	{
		/*int pixelCounter = 0;
		for(int i = 0; i < 1500;i++)
		{
			for(int j = 0;j < 1000;j++)
			{
				if(num[i][j] != 0)
				{
					pixelCounter++;
				}
			}
		}*/
		glapi.drawPixelScreen(p1,p2,num,colors);
	}
	
	private void loop()
	{
	    while (glapi.windowShouldNotClose()) 
	    {
	    	 glapi.glLoopBegin();

	    	 draw();
	    	 
	    	 glapi.glLoopEnd(60);
	    }
	    glapi.destroyDisplay();
	}
	 
	 public static void main(String[] args) 
	 {
	     new Mandelbrot();
	 }
}
