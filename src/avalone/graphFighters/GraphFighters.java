package avalone.graphFighters;

import java.util.HashMap;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import avalone.physics.Physics;

public class GraphFighters 
{
	private Arena arena;
	private HashMap<String,Fighter> fighters;
	private AvaloneGLAPI glapi;
	
	public GraphFighters()
	{
		arena = null;
		fighters = new HashMap<String,Fighter>();
		glapi = new AvaloneGLAPI(1200,900,"Graph Fighters","graph_fighters");
		//glapi = new AvaloneGLAPI(1600,1000,"Graph Fighters","graph_fighters");
		glapi.enableTextures();
		
		loadArena("arena1");
		while (glapi.windowShouldNotClose()) 
    	{
    		glapi.glLoopBegin();
    		
    		arena.drawObjects();
    		for(String key : fighters.keySet())
    		{
    			Fighter fighter = fighters.get(key);
    			Physics.applyGrav(fighter);
    			arena.collideWithObjects(fighter);
    			fighter.input();
    			fighter.draw();
    		}
    		
    		glapi.drawFPS(new Point(5,960),new Point(40,995));
    		glapi.glLoopEnd(60);
    	}
    	glapi.destroyDisplay();
	}
	
	public void loadArena(String newEnvName)
	{
		if(arena != null)
		{
			glapi.unloadTmpTextures(arena.name);
		}
		arena = new Arena(newEnvName);
		fighters.put("unnamed",new Player(150,300));
		glapi.loadTmpTextures(newEnvName);
	}
	
	public static void main(String[] args)
    {	
    	new GraphFighters();
    }
}
