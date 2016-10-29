package avalone.ignis;

import java.util.ArrayList;

public class Noeud 
{
	private ArrayList<Noeud> fils;
	public final boolean estUnMin;
	public int value;
	public Board board;
	public int indexMove;
	public int indexIdentity;
	public boolean evaluated;
	
	public Noeud(boolean estUnMin,Board board)
	{
		fils = new ArrayList<Noeud>();
		this.estUnMin = estUnMin;
		this.board = board;
		value = 100;
		indexMove = -1;
		evaluated = false;
	}
	
	public Noeud(boolean estUnMin,Board board,int index)
	{
		this(estUnMin,board);
		indexIdentity = index;
	}
	
	public boolean estUneFeuille()
	{
		return fils.size() == 0;
	}
	
	public void addFils(Board board,int index)
	{
		fils.add(new Noeud(!estUnMin,board,index));
	}
	
	public int nbFils()
	{
		return fils.size();
	}
	
	public Noeud getFils(int index)
	{
		if(index >= 0 && index < fils.size())
		{
			return fils.get(index);
		}
		return null;
	}
	
	public void destroyFils(int index)
	{
		if(index >= 0 && index < fils.size())
		{
			fils.get(index).destroyAllChild();
			fils.remove(index);
		}
		else
		{
			System.out.println("erreur de destruction d'un noeud");
		}
	}
	
	public void destroyAllChild()
	{
		fils.clear();
	}
}
