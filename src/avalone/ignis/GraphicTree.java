package avalone.ignis;

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
	private Point leftDownCorner;
	private Point rightUpCorner;
	private Element ownElement;
	private Element enemyElement;
	private Node<Board> selected;
	private Node<Board> subSelected;
	private int indiceColor;
	
	public GraphicTree(AvaloneGLAPI glapi,Node<Board> node,Element ownElement,Element enemyElement)
	{
		this.glapi = glapi;
		this.ownElement = ownElement;
		this.enemyElement = enemyElement;
		this.node = node;
		hasAlreadyClicked = false;
		leftDownCorner = new Point(75,75);
		rightUpCorner = leftDownCorner.clone(468);
		selected = node.getChild(0);
		subSelected = selected.getChild(0);
		indiceColor = 0;
		
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
		Point p = new Point(490,600);
		clicDetection(p,p.clone(22,20),node,false,false,0);
		for(int i = 0;i < node.nbChildren();i++)
		{
			p = new Point(100+(node.getChild(i).indexIdentity%12)*66,500 - (node.getChild(i).indexIdentity/12) * 30);
			clicDetection(p,p.clone(33,20),node.getChild(i),true,false,i);
		}
		for(int j = 0;j < selected.nbChildren();j++)
		{
			p = new Point(50 + (selected.getChild(j).indexIdentity%12)*35,240 - (selected.getChild(j).indexIdentity/12)*35);
			clicDetection(p,p.clone(22,20),selected.getChild(j),false,true,j);
		}
		if(subSelected != null)
		{
			for(int k = 0;k < subSelected.nbChildren();k++)
			{
				p = new Point(500 + (subSelected.getChild(k).indexIdentity%12)*35,240 - (subSelected.getChild(k).indexIdentity/12)*35);
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
						glapi.addSubWindow(1000,550,"noeud");
						
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
		Element[][] elems = board.getElements();
		glapi.drawTexturedRect(leftDownCorner, rightUpCorner, "board.png");
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(elems[i][j] != Element.Empty)
				{
					Point pos = new Point(79 + i * 77,79 + j * 77);
					Point pos2 = pos.clone(75);
					glapi.drawTexturedRect(pos, pos2, elems[i][j].texture);
				}
			}
		}
		
		int number = Calculator.getPointsFromNumber(elems, ownElement);
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
		glapi.drawText(p1,p2,"balance is " + (total - enemyTotal) + " points","white");
	}
	
	private void draw()
	{
		Point p = new Point(490,600);
		glapi.drawText(p,p.clone(22,20),node.value,"red");
		
		for(int i = 0;i < node.nbChildren();i++)
		{
			p = new Point(100+(node.getChild(i).indexIdentity%12)*66,500 - (node.getChild(i).indexIdentity/12) * 30);
			String s = String.valueOf(node.getChild(i).value);
			if(s.length() == 1)
			{
				s = " " + s;
			}
			glapi.drawText(p,p.clone(33,20),s,colors[(i/2)%24]);
		}
		for(int j = 0;j < selected.nbChildren();j++)
		{
			p = new Point(50 + (selected.getChild(j).indexIdentity%12)*35,240 - (selected.getChild(j).indexIdentity/12)*35);
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
				p = new Point(500 + (subSelected.getChild(k).indexIdentity%12)*35,240 - (subSelected.getChild(k).indexIdentity/12)*35);
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
