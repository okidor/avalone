package avalone.penguins;

import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Node;
import avalone.api.util.Point;

public class GraphicTree 
{
	private Node<Board> node;
	private AvaloneGLAPI glapi;
	private static final String[] colors = {"red","blue","green","yellow","cyan","orange","chocolate",
									"gray","violet_light","steel_blue","ghost_white","magenta",
									"pink","beige","coral","indian_red","green_yellow","golden_rod",
									"light_green","peach_puff","rosy_brown","royal_blue","sliver",
									"medium_violet_red"};
	private boolean hasAlreadyClicked;
	private Node<Board> selected;
	private Node<Board> subSelected;
	private int indiceColor;
	private int maxChildren;
	
	private Point firstLayerPos;
	private ArrayList<Point> secondLayerPos;
	private ArrayList<Point> thirdLayerPos;
	private ArrayList<Point> fourthLayerPos;
	
	public GraphicTree(AvaloneGLAPI glapi,Node<Board> node)
	{
		this.glapi = glapi;
		this.node = node;
		hasAlreadyClicked = false;
		selected = node.getChild(0);
		subSelected = selected.getChild(0);
		indiceColor = 0;
		maxChildren = 84;
		firstLayerPos = new Point(490,600);
		secondLayerPos = new ArrayList<Point>();
		thirdLayerPos = new ArrayList<Point>();
		fourthLayerPos = new ArrayList<Point>();
		for(int i = 0;i < maxChildren; i++)
		{
			secondLayerPos.add(new Point(100 + (i%12) * 66, 500 - (i/12) * 30));
			thirdLayerPos.add(new Point(50 + (i%12) * 35, 240 - (i/12) * 35));
			fourthLayerPos.add(new Point(500 + (i%12) * 35, 240 - (i/12) * 35));
		}
		
		glapi.addSubWindow(1000,650,"tree");
		
		glapi.enableTextures();
	    while (glapi.lastSubWindowShouldNotClose()) 
	    {
	    	 glapi.glLoopBegin();
	    	 
	    	 actions();
	    	 
	    	 glapi.glLoopEnd();
	     }
	    glapi.destroyLastSubWindow();
	}
	
	private void actions()
	{
		clic();
		draw();
	}
	
	private void clic()
	{
		clicDetection(firstLayerPos,firstLayerPos.clone(22,20),node,false,false,0);
		for(int i = 0;i < node.nbChildren();i++)
		{
			//Point p = new Point(100+(node.getChild(i).indexIdentity%12)*66,500 - (node.getChild(i).indexIdentity/12) * 30);
			Point p = secondLayerPos.get(i);
			clicDetection(p,p.clone(33,20),node.getChild(i),true,false,i);
		}
		for(int j = 0;j < selected.nbChildren();j++)
		{
			//Point p = new Point(50 + (selected.getChild(j).indexIdentity%12)*35,240 - (selected.getChild(j).indexIdentity/12)*35);
			Point p = thirdLayerPos.get(j);
			clicDetection(p,p.clone(22,20),selected.getChild(j),false,true,j);
		}
		if(subSelected != null)
		{
			for(int k = 0;k < subSelected.nbChildren();k++)
			{
				//Point p = new Point(500 + (subSelected.getChild(k).indexIdentity%12)*35,240 - (subSelected.getChild(k).indexIdentity/12)*35);
				Point p = fourthLayerPos.get(k);
				clicDetection(p,p.clone(22,20),subSelected.getChild(k),false,false,k);
			}
		}
	}
	
	private void clicDetection(Point p1,Point p2,Node<Board> n,boolean showFils,boolean showPetitFils,int indice)
	{
		Point p = glapi.getMouse();
		if(p.x >= p1.x && p.x <= p2.x)
		{
			if(p.y >= p1.y && p.y <= p2.y)
			{
				if(showFils)
				{
					selected = n;
					if(n.getChild(0) != null)
					{
						subSelected = n.getChild(0);
					}
					indiceColor = indice;
				}
				else if(showPetitFils)
				{
					subSelected = n;
				}
				if (glapi.hasLeftClicked())
				{
					if(!hasAlreadyClicked)
					{
						hasAlreadyClicked = true;
						glapi.addSubWindow(750,700,"node");
						
						glapi.enableTextures();
					    while (glapi.lastSubWindowShouldNotClose()) 
					    {
					    	 glapi.glLoopBegin();
					    	 
					    	 drawSituation(n.data);
					    	 
					    	 glapi.glLoopEnd();
					     }
					    glapi.destroyLastSubWindow();
					}
				}
				else
				{
					hasAlreadyClicked = false;
					glapi.drawRect(p1, p2, "white");
					glapi.clearFilter();
				}
			}
		}
	}
	
	private void drawSituation(Board board)
	{
		Tile tiles[][] = board.getTiles();
		for(int i = 0;i < 8;i++)
		{
			for(int j = 0;j < 8;j++)
			{
				if(tiles[i][j] != null)
				{
					tiles[i][j].draw(glapi);
				}
			}
		}
		
	/*	int number = Calculator.getPointsFromNumber(elems, ownElement);
		int position = Calculator.getPointsFromPositionValues(elems, ownElement,board.positionValues);
		int nbCoups = Calculator.getPointsFromNbCoups(elems, ownElement,board.getBorderXMin(),board.getBorderXMax(),board.getBorderYMin(),board.getBorderYMax());
		
		int enemyNumber = Calculator.getPointsFromNumber(elems, enemyElement);
		int enemyPosition = Calculator.getPointsFromPositionValues(elems, enemyElement,board.positionValues);
		int enemyNbCoups = Calculator.getPointsFromNbCoups(elems, enemyElement,board.getBorderXMin(),board.getBorderXMax(),board.getBorderYMin(),board.getBorderYMax());
		
		int total = number + position + nbCoups;
		int enemyTotal = enemyNumber + enemyPosition + enemyNbCoups;
		
		Point p1 = new Point(580,500);
		Point p2 = p1.clone(500,20);
		glapi.drawText(p1,p2,"AI elements give " + number + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"AI positions give " + position + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"AI number turns give " + nbCoups + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"AI total give " + total + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"player elements substract " + enemyNumber + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"player positions substract " + enemyPosition + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"player number turns substract " + enemyNbCoups + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"player total substract " + enemyTotal + " points","white");
		
		p1.y = p1.y - 30;
		p2.y = p2.y - 30;
		glapi.drawText(p1,p2,"balance is " + (total - enemyTotal) + " points","white");*/
	}
	
	private void draw()
	{
		glapi.drawText(firstLayerPos,firstLayerPos.clone(22,20),node.value,"red");
		for(int i = 0;i < node.nbChildren();i++)
		{
			//Point p = new Point(100+(i%12)*66,500 - (i/12) * 30);
			Point p = secondLayerPos.get(i);
			String s = String.valueOf(node.getChild(i).value);
			if(s.length() == 1)
			{
				s = " " + s;
			}
			glapi.drawText(p,p.clone(33,20),s,colors[(i/2)%24]);
		}
		for(int j = 0;j < selected.nbChildren();j++)
		{
			//Point p = new Point(50 + (j%12)*35,240 - (j/12)*35);
			Point p = thirdLayerPos.get(j);
			String s = String.valueOf(selected.getChild(j).value);
			if(s.length() == 1)
			{
				s = " " + s;
			}
			glapi.drawText(p,p.clone(22,20),s,colors[(indiceColor/2)%24]);
		}
		if(subSelected != null)
		{
			for(int k = 0;k < subSelected.nbChildren();k++)
			{
				//Point p = new Point(500 + (k%12)*35,240 - (k/12)*35);
				Point p = fourthLayerPos.get(k);
				String s = String.valueOf(subSelected.getChild(k).value);
				if(s.length() == 1)
				{
					s = " " + s;
				}
				glapi.drawText(p,p.clone(22,20),s,colors[(indiceColor/2)%24]);
			}
		}
	}
}
