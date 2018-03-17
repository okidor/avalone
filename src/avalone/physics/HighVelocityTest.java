package avalone.physics;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class HighVelocityTest 
{
	private AvaloneGLAPI glapi;

	public HighVelocityTest()
	{
		glapi = new AvaloneGLAPI(1200,400,"Collision Test");
		Solid solid1 = new Solid(10,new Point(10,45),new Point(10,55),new Point(20,55),new Point(20,45));
		Solid solid2 = new Solid(new Point(580,45),new Point(580,55),new Point(590,55),new Point(590,45));
		Solid borderG = new Solid(999999999,new Point(0,0),new Point(0,400),new Point(5,400),new Point(5,0));
	 	Solid borderD = new Solid(999999999,new Point(1195,0),new Point(1195,400),new Point(1200,400),new Point(1200,0));
		solid1.linearVelocity.x = 15;
		while (glapi.windowShouldNotClose()) 
		{
			glapi.glLoopBegin();
			
			solid1.update();
	    	solid2.update();
	    	Physics.resolveInterpolatedCollision(solid1,solid2);
	    	//Physics.resolveInterpolatedCollision(solid1,borderG);
	    	//Physics.resolveInterpolatedCollision(solid1,borderD);
	    	//Physics.resolveInterpolatedCollision(solid2,borderG);
	    	//Physics.resolveInterpolatedCollision(solid2,borderD);
			glapi.drawConvex(solid1.getVertices(), "red");
			glapi.drawConvex(solid2.getVertices(), "blue");
			glapi.drawConvex(borderG.getVertices(), "yellow");
			glapi.drawConvex(borderD.getVertices(), "yellow");
			
			glapi.drawFPS(new Point(15,860),new Point(50,895));
			glapi.glLoopEnd(1);
		}
		glapi.destroyDisplay();
	}
	
	public static void main(String[] args) 
	{
		new HighVelocityTest();
	}
}
