package avalone.risk;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class MapGenerator 
{
	private Random rand;
	private AvaloneGLAPI glapi;
	Point[][] points;
	Point mapSize;
	ArrayList<Cell> cells;
	ArrayList<String> colors;
	
	public MapGenerator()
	{
		rand = new Random();
		mapSize = new Point(1600,1000);
		int avgHop = 20;
		points = new Point[mapSize.x/avgHop+1][mapSize.y/avgHop+1];
		cells = new ArrayList<Cell>();
		colors = new ArrayList<String>();
		colors.add("red");colors.add("blue");colors.add("yellow");colors.add("green");colors.add("cyan");colors.add("pink");
		colors.add("white");colors.add("brown");colors.add("beige");colors.add("dark_orchid");colors.add("medium_aqua_marine");
		colors.add("peach_puff");colors.add("salmon");colors.add("purple");colors.add("light_grey");colors.add("magenta");
		colors.add("fire_brick");colors.add("golden_rod");colors.add("burly_wood");colors.add("ivory");colors.add("khaki");
		generate(avgHop);
		
		loop();
	}
	
	public void generate(int avgHop)
	{
		for(int i = 0;i < points.length-1;i++)
		{
			for(int j = 0;j < points[i].length-1;j++)
			{
				points[i][j] = new Point(i*avgHop+rand.nextInt(avgHop/3)-(avgHop/6),j*avgHop+rand.nextInt(avgHop/3)-(avgHop/6));
			}
		}
		System.out.println(points.length-1);
		for(int i = 0;i < points.length-1;i++)
		{
			points[i][points[points.length-1].length-1] = new Point(points[i][0].x,points[i][0].y + mapSize.y);
		}
		for(int i = 0;i < points[points.length-1].length-1;i++)
		{
			points[points.length-1][i] = new Point(points[0][i].x + mapSize.x,points[0][i].y);
		}
		points[points.length-1][points[points.length-1].length-1] = new Point(points[0][0].x + mapSize.x,points[0][0].y + mapSize.y);
		int maxCellSize = 10;
		
		for(int i = 1;i < points.length;i++)
		{
			for(int j = 1;j < points[i].length;j++)
			{
				cells.add(new Cell(points,i,j,colors.get(rand.nextInt(colors.size()))));
			}
		}
	}
	
	public void loop()
	{
		glapi = new AvaloneGLAPI(1600,1000,"Map generation test");
		glapi.enableTextures();
		
		while (glapi.windowShouldNotClose()) 
    	{
    		glapi.glLoopBegin();
    		
    		for(int i = 1;i < cells.size();i++)
    		{
    			cells.get(i).drawBorder();
    		}
    		
    		glapi.drawFPS(new Point(5,960),new Point(40,995));
    		glapi.glLoopEnd(60);
    	}
    	glapi.destroyDisplay();
	}
	
	public static void main(String[] args)
    {	
    	new MapGenerator();
    }
}
