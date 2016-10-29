package avalone.ignis;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class GraphicBoard extends Board
{
	private Point leftDownCorner;
	private Point rightUpCorner;
	private boolean hasAlreadyClicked;
	
	private ArrayList<Point> upArrows;
	private ArrayList<Point> leftArrows;
	private ArrayList<Point> downArrows;
	private ArrayList<Point> rightArrows;
	
	private Point redLineP1;
	private Point redLineP2;
	private Point redLineP3;
	private Point redLineP4;
	
	private Point current;
	private Random rand;
	private double timer;
	private int x;
	private int y;
	private Point pEnd;
	
	public GraphicBoard()
	{
		leftDownCorner = new Point(75,75);
		rightUpCorner = leftDownCorner.clone(468);
		hasAlreadyClicked = false;
		
		upArrows = new ArrayList<Point>();
		leftArrows = new ArrayList<Point>();
		downArrows = new ArrayList<Point>();
		rightArrows = new ArrayList<Point>();
		
		redLineP1 = new Point(618,0);
		redLineP2 = new Point(618,618);
		redLineP3 = new Point(618,133);
		redLineP4 = new Point(1103,133);
		
		current = new Point(824,27);
		rand = new Random();
		timer = 0;
		x = rand.nextInt(1028);
		y = 40 + rand.nextInt(503);
		pEnd = new Point(x,y);
		
		initBoard();
	}
	
	public void initBoard()
	{
		super.initBoard();
		for(int i = 0; i < 6;i++)
		{
			upArrows.add(new Point(79 + i * 77,0));
			leftArrows.add(new Point(543,79 + i * 77));
			downArrows.add(new Point(79 + i * 77,543));
			rightArrows.add(new Point(0,79 + i * 77));
		}
	}
	
	public void animate(AvaloneGLAPI glapi)
	{
		if(block)
		{
			for(int i = 0;(i < 6) && block;i++)
			{
				for(int j = 0;(j < 6) && block;j++)
				{
					block = block && (tiles[i][j].animateX == 0) && (tiles[i][j].animateY == 0);
				}
			}
			if(block)
			{
				Ignis.player = 3 - Ignis.player;
			}
			block = !block;
		}
	}
	
	public void clic(AvaloneGLAPI glapi)
	{
		if (glapi.hasLeftClicked())
		{
			if(!hasAlreadyClicked)
			{
				Point p = glapi.getMouse();
				for(int i = 0; i < Element.Earth.amount;i++)
				{
					Point p1 = new Point(623 + (i % 6) * 80,538 - (i / 6) * 80);
					checkSelection(p,p1,Element.Earth);
				}
			
				for(int i = 0; i < Element.Wind.amount;i++)
				{
					Point p1 = new Point(1023 - (i % 6) * 80,138 + (i / 6) * 80);
					checkSelection(p,p1,Element.Wind);
				}
			
				for(int i = 0; i < 6;i++)
				{
					Point p1 = upArrows.get(i);
					checkSelection(p,p1,0,i);
					p1 = leftArrows.get(i);
					checkSelection(p,p1,1,i);
					p1 = downArrows.get(i);
					checkSelection(p,p1,2,i);
					p1 = rightArrows.get(i);
					checkSelection(p,p1,3,i);
				}
				hasAlreadyClicked = true;
			}
		}
		else
		{
			hasAlreadyClicked = false;
		}
	}
	
	private void checkSelection(Point mousePoint,Point arrowPoint,int id,int indice)
	{
		if(mousePoint.x > arrowPoint.x && mousePoint.x < arrowPoint.x + 75)
		{
			if(mousePoint.y > arrowPoint.y && mousePoint.y < arrowPoint.y + 75)
			{
				if(id == 0)
				{
					pushColumnUp(indice,false);
				}
				else if(id == 1)
				{
					pushRowLeft(indice,false);
				}
				else if(id == 2)
				{
					pushColumnDown(indice,false);
				}
				else if(id == 3)
				{
					pushRowRight(indice,false);
				}
			}
		}
	}
	
	private void checkSelection(Point mousePoint,Point arrowPoint,Element elem)
	{
		if(mousePoint.x > arrowPoint.x && mousePoint.x < arrowPoint.x + 75)
		{
			if(mousePoint.y > arrowPoint.y && mousePoint.y < arrowPoint.y + 75)
			{
				currentElement = elem;
			}
		}
	}
	
	public void draw(AvaloneGLAPI glapi)
	{
		if(Element.Earth.amount == 0)
		{
			currentElement = Element.Wind;
		}
		if(Element.Wind.amount == 0)
		{
			currentElement = Element.Earth;
		}
		Element elem = checkWin();
		if(elem == null)
		{
			drawBoard(glapi);
		}
		else
		{
			timer++;
			
			if(timer >= 10)
			{
				x = rand.nextInt(1028);
				y = 40 + rand.nextInt(503);
				pEnd = new Point(x,y);
				timer = 0;
			}
			glapi.drawTexturedRect(pEnd, pEnd.clone(75), elem.texture);
			Point p2 = new Point(451,20);
			String[] elemName = elem.texture.split("\\.");
			glapi.drawText(p2,p2.clone(200,20),elemName[0] + " won","white");
		}
	}
	
	private void drawBoard(AvaloneGLAPI glapi)
	{
		glapi.drawTexturedRect(leftDownCorner, rightUpCorner, "board.png");
		
		Point p = glapi.getMouse();
		for(int i = 0; i < 6;i++)
		{
			Point p1 = upArrows.get(i);
			glapi.drawTexturedRect(p1,p1.clone(75),"up_arrow" + chooseArrowTexture(p,p1) + ".png");
			p1 = leftArrows.get(i);
			glapi.drawTexturedRect(p1,p1.clone(75),"left_arrow" + chooseArrowTexture(p,p1) + ".png");
			p1 = downArrows.get(i);
			glapi.drawTexturedRect(p1,p1.clone(75),"down_arrow" + chooseArrowTexture(p,p1) + ".png");
			p1 = rightArrows.get(i);
			glapi.drawTexturedRect(p1,p1.clone(75),"right_arrow" + chooseArrowTexture(p,p1) + ".png");
		}
			for(int i = 0;i < 6;i++)
			{
				for(int j = 0;j < 6;j++)
				{
					tiles[i][j].draw(glapi);
				}
			}
		
		
		for(int i = 0; i < Element.Earth.amount;i++)
		{
			Point p1 = new Point(623 + (i % 6) * 80,538 - (i / 6) * 80);
			glapi.drawTexturedRect(p1, p1.clone(75), "earth.png");
		}
		
		for(int i = 0; i < Element.Wind.amount;i++)
		{
			Point p1 = new Point(1023 - (i % 6) * 80,138 + (i / 6) * 80);
			glapi.drawTexturedRect(p1, p1.clone(75), "wind.png");
		}
		
		glapi.drawTexturedRect(current, current.clone(75), currentElement.texture);
		
		glapi.drawLine(redLineP1, redLineP2, "red");
		glapi.drawLine(redLineP3, redLineP4, "red");
		glapi.clearFilter();
	}
	
	private String chooseArrowTexture(Point mousePoint,Point arrowPoint)
	{
		if(block || Ignis.player == 2)
		{
			return "_red";
		}
		else
		{
			if(mousePoint.x > arrowPoint.x && mousePoint.x < arrowPoint.x + 75)
			{
				if(mousePoint.y > arrowPoint.y && mousePoint.y < arrowPoint.y + 75)
				{
					return "_blue";
				}
				else
				{
					return "";
				}
			}
			else
			{
				return "";
			}
		}
	}
}
