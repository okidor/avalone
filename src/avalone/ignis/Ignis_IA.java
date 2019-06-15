package avalone.ignis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.lwjgl3.IA;
import avalone.api.util.Node;

public class Ignis_IA extends IA<Board>
{
	private Element ownElement;
	private Element enemyElement;

	//private BufferedWriter out;
	private FileWriter out;

	public Ignis_IA(Board board,Element elem,int depth)
	{
		super(board, depth);
		ownElement = elem;
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
	
	public void writeFile(Node<Board> root)
	{
		if(out == null)
		{
			System.out.println("can't write, because no file is opened");
			return;
		}
		try 
		{
			out.write(root.indexMove + " ");
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
	

	@Override
	public void move(Node<Board> playNode)
	{
		for(int i = 0;i < playNode.nbChildren();i++)
		{
			int nb = Calculator.getPointsFromNumber(playNode.getChild(i).data.getElements(),enemyElement);
			if(nb == -500)
			{
				System.out.println("coup direct pour gagner");
				playNode.indexMove = playNode.getChild(i).indexIdentity;
			}
		}
		writeFile(playNode);
		if(playNode.indexMove % 2 == 0)
		{
			playNode.data.currentElement = Element.Earth;
		}
		else
		{
			playNode.data.currentElement = Element.Wind;
		}
		if(playNode.indexMove < 12)
		{
			playNode.data.pushColumnDown((playNode.indexMove/2)%6,false);
		}
		else if(playNode.indexMove < 24)
		{
			playNode.data.pushColumnUp((playNode.indexMove/2)%6,false);
		}
		else if(playNode.indexMove < 36)
		{
			playNode.data.pushRowLeft((playNode.indexMove/2)%6,false);
		}
		else if(playNode.indexMove < 48)
		{
			playNode.data.pushRowRight((playNode.indexMove/2)%6,false);
		}
		else if(playNode.indexMove == -1)
		{
			System.out.println("aucun coup trouv� pour la situation actuelle");
		}
	}

	@Override
	public int evalSituationScore(Board situationBoard) {
		return Calculator.getTotalPoints(situationBoard, ownElement) - Calculator.getTotalPoints(situationBoard, enemyElement);
	}

	@Override
	public void generateChildren(Node<Board> n)
	{
		//In this function, data contains the board representing the situation for each node
		for(int i = n.data.getBorderXMin();i <= n.data.getBorderXMax();i++)
		{
			Board virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Earth;
			virtualBoard.pushColumnDown(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,i*2);
			}
			
			virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Wind;
			virtualBoard.pushColumnDown(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,i*2+1);
			}
		}
		for(int i = n.data.getBorderXMin();i <= n.data.getBorderXMax();i++)
		{
			Board virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Earth;
			virtualBoard.pushColumnUp(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,12 + i*2);
			}
			
			virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Wind;
			virtualBoard.pushColumnUp(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,13 + i*2);
			}
		}
		for(int i = n.data.getBorderYMin();i <= n.data.getBorderYMax();i++)
		{
			Board virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Earth;
			virtualBoard.pushRowLeft(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,24 + i*2);
			}
			
			virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Wind;
			virtualBoard.pushRowLeft(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,25 + i*2);
			}
		}
		for(int i = n.data.getBorderYMin();i <= n.data.getBorderYMax();i++)
		{
			Board virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Earth;
			virtualBoard.pushRowRight(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,36 + i*2);
			}
			
			virtualBoard = n.data.clone();
			virtualBoard.currentElement = Element.Wind;
			virtualBoard.pushRowRight(i,true);
			if(!virtualBoard.cancelAction)
			{
				n.addChild(virtualBoard,37 + i*2);
			}
		}
	}

	@Override
	public void debug(Node<Board> playNode) 
	{
		new GraphicTree(AvaloneGLAPI.getInstance(),playNode,ownElement,enemyElement);
	}
}
