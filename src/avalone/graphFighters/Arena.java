package avalone.graphFighters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Arena 
{	
	public final String name;
	public ArrayList<ArenaObject> arenaObjects;
	
	public Arena(String name)
	{
		this.name = name;
		arenaObjects = new ArrayList<ArenaObject>();
		readObjects("textures" + File.separator + "graph_fighters" + File.separator + name + File.separator + "objects.txt");
	}
	
	private void readObjects(String path)
	{
		BufferedReader br;
		try 
		{
			br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) 
			{
				String[] infos = line.split(":");
			    arenaObjects.add(new ArenaObject(
			    		infos[4], new Point(Integer.parseInt(infos[0]),Integer.parseInt(infos[1])), new Point(Integer.parseInt(infos[0]),Integer.parseInt(infos[3]))
			    				, new Point(Integer.parseInt(infos[2]),Integer.parseInt(infos[3])), new Point(Integer.parseInt(infos[2]),Integer.parseInt(infos[1]))));
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void drawObjects()
	{
		for(int i = 0;i < arenaObjects.size();i++)
		{
			arenaObjects.get(i).draw();
		}
		AvaloneGLAPI.getInstance().unbindTexture();
	}
	
	public void collideWithObjects(Fighter fighter)
	{
		for(int i = 0;i < arenaObjects.size();i++)
		{
			fighter.collideWith(arenaObjects.get(i));
		}
	}
}
