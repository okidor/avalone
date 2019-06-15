package avalone.ignis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import avalone.api.lwjgl3.AvaloneGLAPI;

public class Legacy_IA 
{
	private AvaloneGLAPI glapi;
	private Noeud racine;
	private Board board;
	private Element ownElement;
	private Element enemyElement;
	private int prof;
	private int counter;
	//private BufferedWriter out;
	private FileWriter out;
	private ArrayList<Noeud> leaves;
	private boolean addLeaves;
	private boolean debugMode;
	
	private Random rand;
	
	public Legacy_IA(AvaloneGLAPI glapi,Board board,Element elem,int prof)
	{
		this.glapi = glapi;
		this.board = board;
		counter = 0;
		this.prof = prof;
		ownElement = elem;
		leaves = new ArrayList<Noeud>();
		addLeaves = true;
		debugMode = false;
		rand = new Random();
		if(elem == Element.Fire)
		{
			enemyElement = Element.Water;
		}
		else if(elem == Element.Water)
		{
			enemyElement = Element.Fire;
		}
		openFile("IA" + File.separator + "coups.txt");
	}
	
	public void evaluateCurrentPos()
	{
		racine = new Noeud(false,board.clone());
		double time = glapi.getTime();
		leaves.add(racine);
		addLayers();
		//minimax(racine);
		double time2 = glapi.getTime();
		if(debugMode)
		{
			//new GraphicTree(glapi,racine,ownElement,enemyElement);
		}
		move();
		System.out.println("AI took " + (time2 - time) + " seconds to play");
		counter++;
		System.out.println("ia a jou� son tour numero " + counter);
	}
	
	private void addLayers()
	{
		for(int i = 0;i < prof-1;i++)
		{
			addLayer();
		}
		addLeaves = false;
		addLayer();
		addLeaves = true;
	}
	
	public void openFile(String name)
	{
		try 
		{
			out = new FileWriter(name);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void writeFile(Noeud n)
	{
		if(out == null)
		{
			System.out.println("can't write, because no file is opened");
			return;
		}
		try 
		{
			out.write(n.indexMove + " ");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void closeFile()
	{
		try 
		{
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		out = null;
	}
	
	public void generateFils(Noeud n)
	{
		if(n.nbFils() == 0)
		{
			for(int i = n.board.getBorderXMin();i <= n.board.getBorderXMax();i++)
			{
				Board virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Earth;
				virtualBoard.pushColumnDown(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,i*2);
				}
				
				virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Wind;
				virtualBoard.pushColumnDown(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,i*2+1);
				}
			}
			for(int i = n.board.getBorderXMin();i <= n.board.getBorderXMax();i++)
			{
				Board virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Earth;
				virtualBoard.pushColumnUp(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,12 + i*2);
				}
				
				virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Wind;
				virtualBoard.pushColumnUp(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,13 + i*2);
				}
			}
			for(int i = n.board.getBorderYMin();i <= n.board.getBorderYMax();i++)
			{
				Board virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Earth;
				virtualBoard.pushRowLeft(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,24 + i*2);
				}
				
				virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Wind;
				virtualBoard.pushRowLeft(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,25+ i*2);
				}
			}
			for(int i = n.board.getBorderYMin();i <= n.board.getBorderYMax();i++)
			{
				Board virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Earth;
				virtualBoard.pushRowRight(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,36 + i*2);
				}
				
				virtualBoard = n.board.clone();
				virtualBoard.currentElement = Element.Wind;
				virtualBoard.pushRowRight(i,true);
				if(!virtualBoard.cancelAction)
				{
					n.addFils(virtualBoard,37 + i*2);
				}
			}
		}
	}
	
	public void addLayer()
	{
		System.out.println(leaves.size() + " feuilles sont valides pour avoir des fils");
		System.out.println("generation de " + (leaves.size()*48) + " fils");
		for(int i = 0;i < leaves.size();i++)
		{
			generateFils(leaves.get(i));
			if(i % 5000 == 0)
			{
				long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				System.out.println("using " + usedMem + " on " + Runtime.getRuntime().totalMemory() + " allocated (" + Runtime.getRuntime().maxMemory()+ ")");
			}
		}
		leaves.clear();
		alphabeta(racine,-2000000,2000000);
	}
	
	public void move()
	{
		for(int i = 0;i < racine.nbFils();i++)
		{
			int nb = Calculator.getPointsFromNumber(racine.getFils(i).board.getElements(),enemyElement);
			if(nb == -500)
			{
				System.out.println("coup direct pour gagner");
				racine.indexMove = racine.getFils(i).indexIdentity;
			}
		}
		writeFile(racine);
		if(racine.indexMove % 2 == 0)
		{
			board.currentElement = Element.Earth;
		}
		else
		{
			board.currentElement = Element.Wind;
		}
		if(racine.indexMove < 12)
		{
			board.pushColumnDown((racine.indexMove/2)%6,false);
		}
		else if(racine.indexMove < 24)
		{
			board.pushColumnUp((racine.indexMove/2)%6,false);
		}
		else if(racine.indexMove < 36)
		{
			board.pushRowLeft((racine.indexMove/2)%6,false);
		}
		else if(racine.indexMove < 48)
		{
			board.pushRowRight((racine.indexMove/2)%6,false);
		}
		else if(racine.indexMove == -1)
		{
			System.out.println("aucun coup trouv� pour la situation actuelle");
		}
	}
	
	public void minimax(Noeud n)
	{
		if(n.estUneFeuille())
		{
			n.value = Calculator.getTotalPoints(n.board, ownElement) - Calculator.getTotalPoints(n.board, enemyElement);
			return;
		}
		else
		{
			if(n.estUnMin)
			{
				int val = 2000000;
				for(int i = 0;i < n.nbFils();i++)
				{
					minimax(n.getFils(i));
					//val = Calculator.min(val,n.getFils(i).value);
					if(n.getFils(i).value < val)
					{
						n.indexMove = n.getFils(i).indexIdentity;
						val = n.getFils(i).value;
					}
				}
				n.value = val;
				return;
			}
			else
			{
				int val = -2000000;
				for(int i = 0;i < n.nbFils();i++)
				{
					minimax(n.getFils(i));
					//val = Calculator.max(val,n.getFils(i).value);
					if(n.getFils(i).value > val)
					{
						n.indexMove = n.getFils(i).indexIdentity;
						val = n.getFils(i).value;
					}
				}
				n.value = val;
				return;
			}
		}
	}
	
	public void alphabeta(Noeud n,int alpha,int beta)
	{
		if(n.estUneFeuille())
		{
			n.evaluated = true;
			n.value = Calculator.getTotalPoints(n.board, ownElement) - Calculator.getTotalPoints(n.board, enemyElement);
			if(addLeaves)
				leaves.add(n);
			return;
		}
		else
		{
			int val;
			ArrayList<Integer> indexVals = new ArrayList<Integer>();
			if(n.estUnMin)
			{
				val = 2000000;
				for(int i = 0;i < n.nbFils();i++)
				{
					alphabeta(n.getFils(i),alpha,beta);
					if(n.getFils(i).value < val)
					{
						val = n.getFils(i).value;
						indexVals.clear();
						indexVals.add(n.getFils(i).indexIdentity);
					}
					else if(n.getFils(i).value == val)
					{
						indexVals.add(n.getFils(i).indexIdentity);
					}
					if(alpha > val)
					{
						n.evaluated = true;
						n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
						indexVals.clear();
						n.value = val;
						return;
					}
					beta = Calculator.min(beta,val);
				}
			}
			else
			{
				val = -2000000;
				for(int i = 0;i < n.nbFils();i++)
				{
					alphabeta(n.getFils(i),alpha,beta);
					if(n.getFils(i).value > val)
					{
						val = n.getFils(i).value;
						indexVals.clear();
						indexVals.add(n.getFils(i).indexIdentity);
					}
					else if(n.getFils(i).value == val)
					{
						indexVals.add(n.getFils(i).indexIdentity);
					}
					if(val > beta)
					{
						n.evaluated = true;
						n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
						indexVals.clear();
						n.value = val;
						return;
					}
					alpha = Calculator.max(alpha,val);
				}
			}
			n.evaluated = true;
			n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
			indexVals.clear();
			n.value = val;
		}
	}
}
