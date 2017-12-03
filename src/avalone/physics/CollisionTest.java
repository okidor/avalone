package avalone.physics;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class CollisionTest 
{
	private AvaloneGLAPI glapi;
	private Random rand;
	private ArrayList<Solid> objs;
	private ArrayList<String> colors;
	
	public CollisionTest()
	{
		glapi = new AvaloneGLAPI(900,900,"APITest");
		objs = new ArrayList<Solid>();
		colors = new ArrayList<String>();
		//Solid solid1 = new Solid(new Point(10,10),new Point(10,20),new Point(20,20),new Point(20,10));
   	 	Solid solid1 = new Solid(new Point(350,450),new Point(255,519),new Point(291,631),new Point(409,631),new Point(445,519));
   	 	Solid borderB = new Solid(999999999,new Point(0,0),new Point(0,5),new Point(900,5),new Point(900,0));
	 	Solid borderG = new Solid(999999999,new Point(0,6),new Point(0,894),new Point(5,894),new Point(5,6));
	 	Solid borderD = new Solid(999999999,new Point(895,6),new Point(895,894),new Point(900,894),new Point(900,6));
   	 	Solid borderH = new Solid(999999999,new Point(0,895),new Point(0,900),new Point(900,900),new Point(900,895));
   	 	objs.add(solid1);/*objs.add(solid2);*/objs.add(borderB);objs.add(borderG);objs.add(borderD);objs.add(borderH);
   	 	colors.add("red");colors.add("blue");colors.add("yellow");colors.add("yellow");colors.add("yellow");colors.add("yellow");
   	 	rand = new Random();
   	 	solid1.linearVelocity.x = rand.nextInt(20) - 10;
   	 	solid1.linearVelocity.y = rand.nextInt(20) - 10;
   	 	//solid2.linearVelocity.x = rand.nextInt(10);
	 	//solid2.linearVelocity.y = rand.nextInt(10);
		loop(solid1/*,solid2*/);
	 }

	 private void loop(Solid solid1/*,Solid solid2*/) 
	 {
		 int angle = 0;
		 glapi.enableTextures();
		 Physics.debug = false;
	     while (glapi.windowShouldNotClose()) 
	     {
	    	 glapi.glLoopBegin();
	    	 angle += 1;
	    	 glapi.beginRotate(new Point(700,700),angle);
	    	 glapi.drawArrow(new Point(700,700),new Point(650,650),"red");
	    	 glapi.drawArrow(new Point(700,700),new Point(650,750),"red");
	    	 glapi.drawArrow(new Point(700,700),new Point(750,650),"red");
	    	 glapi.drawArrow(new Point(700,700),new Point(750,750),"red");
	    	 glapi.endRotate();
	    	 Point mouse = glapi.getMouse();
	    	 //solid2.setAllPoints(mouse);
	    	 solid1.update();
	    	 //solid2.update();
	    	 Physics.resolveAllCollisions(objs);
	    	 if(!Physics.debug)
	    	 {
	    		 drawAll();
	    	 }
	    	 else
	    	 {
	    		 drawAllFramed();
	    	 }
	    	 
	    	 glapi.glLoopEnd();
	    	 if(Physics.debug)
	    	 {
	    		 AvaloneGLAPI.getInstance().mustsleep(10000);
	    		 Physics.debug = false;
	    	 }
	     }
	     glapi.destroyDisplay();
	 }
	 
	 public void drawAll()
	 {
		 for(int i = 0;i < objs.size();i++)
		 {
			 glapi.drawConvex(objs.get(i).vertices, colors.get(i));
		 }
	 }
	 
	 public void drawAllFramed()
	 {
		 for(int i = 0;i < objs.size();i++)
		 {
			 glapi.drawFramedConvex(objs.get(i).vertices, colors.get(i));
		 }
	 }

	 public static void main(String[] args) 
	 {
	     new CollisionTest();
	 }
}
