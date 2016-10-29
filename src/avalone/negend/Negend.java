package avalone.negend;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;
//import avalone.api.util.SoundSystem;

import org.lwjgl.openal.AL;

public class Negend 
{
	public Ore[] ores;
	public Map map;
	public AvaloneGLAPI glapi;
	
	public void startGame()
	{
		glapi = new AvaloneGLAPI(Const.tailleFenX,Const.tailleFenY,"Negend","negend");
		glapi.enableTextures();
		
		if(Var.newGame)
		{
			ores = new Ore[Var.oresNumber];
			for(int i = 0;i < Var.oresNumber;i++)
			{
				ores[i] = createOre(i);
				System.out.println(ores[i].name);
			}
		}
		Renderer rend = new Renderer(glapi);
		map = new Map(Const.tailleChunkX,Const.tailleChunkY,glapi,ores);
		mainLoop();
	}
	
	public void mainLoop()
	{
		//SoundSystem soundSys = new SoundSystem("8bit_cover");
		while (glapi.windowShouldNotClose()) 
    	{
    		glapi.glLoopBegin();
    		
    		map.actions();
    		
    		glapi.drawFPS(new Point(5,Const.tailleFenY-40),new Point(40,Const.tailleFenY-5));
    		glapi.glLoopEnd(Const.maxFPS);
    	}
		//soundSys.stop();
		//soundSys.killALData();
		//AL.destroy();
		map.save();
    	glapi.destroyDisplay();
	}
	
	public Ore createOre(int i)
	{
		Ore ore = new Ore(i,glapi);
		return ore;
	}
}
