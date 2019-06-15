package avalone.ignis;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Ignis 
{
	 private AvaloneGLAPI glapi;
	 private GraphicBoard board;
	 private Legacy_IA ia;
	 private Ignis_IA ia2;
	 public static int player = 1;
	 private Element playerElement;
	 private Element IAElement;
	  
	 public Ignis() 
	 {
		 glapi = new AvaloneGLAPI(1103,618,"Ignis: choose an element","ignis");
		 board = new GraphicBoard();

		 chooseElement();
		 ia = new Legacy_IA(glapi,board,IAElement,3);
		 ia2 = new Ignis_IA(board,playerElement,3);
		 glapi.setWindowTitle("Ignis: player " + player);
	     loop();
	 }
	 
	 private void chooseElement()
	 {
		 Point[] p = new Point[4];
		 p[0] = new Point(239,276);p[1] = p[0].clone(75);
		 p[2] = new Point(791,276);p[3] = p[2].clone(75);
		 
		 glapi.enableTextures();
		 boolean loop = true;
	     while (loop) 
	     {
	    	 glapi.glLoopBegin();
	    	 if(glapi.windowShouldClose())
	    	 {
	    		 loop = false;
	    		 glapi.glLoopEnd(40);
		    	 glapi.destroyDisplay();
		    	 System.exit(0);
	    	 }
	    	 
	    	 if(glapi.hasLeftClicked())
	    	 {
		    	 Point pMouse = glapi.getMouse();
	    		 if(pMouse.x < 552)
	    		 {
	    			 playerElement = Element.Fire;
	    			 IAElement = Element.Water;
	    			 loop = false;
	    		 }
	    		 else
	    		 {
	    			 playerElement = Element.Water;
	    			 IAElement = Element.Fire;
	    			 loop = false;
	    		 }
	    	 }
	    	 glapi.drawText(new Point(380,540), new Point(720,690),"Choose Your Element Please", "white");

	    	 glapi.drawTexturedRect(p[0],p[1],Element.Fire.texture);
	    	 glapi.drawTexturedRect(p[2],p[3],Element.Water.texture);
	    	 
	    	 glapi.glLoopEnd(40);
	     }
	     /*if(glapi.windowShouldClose())
	     {
	    	 glapi.glLoopEnd(40);
	    	 glapi.destroyDisplay();
	    	 System.exit(0);
	     }*/
	 }
	 
	 private void loop()
	 {
		 glapi.enableTextures();
	     while (glapi.windowShouldNotClose()) 
	     {
	    	 glapi.glLoopBegin();
	    	 
	    	 if(player == 2 && !board.block)
	    	 {
	    		 ia.evaluateCurrentPos();
	    		 //board.clic(glapi);
	    	 }
	    	 if(player == 1 && !board.block)
	    	 {
	    		 //ia2.evaluateCurrentPos();
	    		 //ia2.evalCurrentSituation();
	    		 board.clic(glapi);
	    	 }
	    	 
	    	 board.animate(glapi);
	    	 board.draw(glapi);
	    	 
	    	 //glapi.drawFPS(new Point(5,570), new Point(45,610));
	    	 glapi.setWindowTitle("Ignis: player " + player);
	    	 
	    	 glapi.glLoopEnd(40);
	     }
	     glapi.destroyDisplay();
	     ia.closeFile();
	 }
	 
	 public static void main(String[] args) 
	 {
	     new Ignis();
	 }
}
