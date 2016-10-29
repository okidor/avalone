package avalone.api.lwjgl3;

import static org.lwjgl.opengl.GL11.*;
import avalone.api.util.Point;

public class APITest 
{
	 private GLAPI glapi;
	 
	 public APITest() 
	 {
		 glapi = new GLAPI(1000,500,"APITest");
	     loop();
	 }
	 
	 private void loop() 
	 {
		 glapi.enableTextures();
	     while (glapi.windowShouldNotClose()) 
	     {
	    	 glapi.glLoopBegin();
	    	 
	    	 /*String s = "abcdefghijklmnopqrstuvwxyz";
	    	 glapi.drawText(new Point(10,10), new Point(10 + s.length()*7,23), s,"red");
	    	 glapi.drawText(new Point(10,110), new Point(10 + s.length()*14,136), s,"red");
	    	 glapi.drawText(new Point(10,290), new Point(10 + s.length()*28,342), s,"red");
	    	 s = s.toUpperCase();
	    	 glapi.drawText(new Point(10,40), new Point(10 + s.length()*7,53), s,"blue");
	    	 glapi.drawText(new Point(10,140), new Point(10 + s.length()*14,166), s,"blue");
	    	 glapi.drawText(new Point(10,340), new Point(10 + s.length()*28,392), s,"blue");*/
	    	 String s = "0123456789";
	    	 /*glapi.drawText(new Point(10,70), new Point(10 + s.length()*7,83), s,"green");
	    	 glapi.drawText(new Point(10,170), new Point(10 + s.length()*14,196), s,"green");*/
	    	 glapi.drawText(new Point(10,390), new Point(10 + 10*28,442), s,"green");
	    	 glapi.drawEmptyRect(new Point(10,390), new Point(10 + 10*28,442), "red");
	    	 
	    	 glapi.glLoopEnd();
	     }
	     glapi.destroyDisplay();
	 }
	 
	 public static void main(String[] args) 
	 {
	     new APITest();
	 }
}
