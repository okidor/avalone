package avalone.api.util;

import java.util.ArrayList;

public class Node<T> 
{
	private ArrayList<Node<T>> child;
	public final boolean isMin;
	public int value;
	public int indexMove;
	public int indexIdentity;
	public boolean evaluated;
	public T data;
	
	public Node(boolean isMin, T data)
	{
		child = new ArrayList<Node<T>>();
		this.isMin = isMin;
		this.data = data;
		value = 100;
		indexMove = -1;
		evaluated = false;
	}
	
	public Node(boolean isMin, T data, int index)
	{
		this(isMin,data);
		indexIdentity = index;
	}
	
	public boolean isLeaf()
	{
		return child.size() == 0;
	}
	
	public void addChild(T data, int index)
	{
		child.add(new Node<T>(!isMin,data,index));
	}
	
	public int nbChildren()
	{
		return child.size();
	}
	
	public Node<T> getChild(int index)
	{
		if(index >= 0 && index < child.size())
		{
			return child.get(index);
		}
		return null;
	}
	
	public void destroyChild(int index)
	{
		if(index >= 0 && index < child.size())
		{
			child.get(index).destroyAllChildren();
			child.remove(index);
		}
		else
		{
			System.out.println("erreur de destruction d'un noeud");
		}
	}
	
	public void destroyAllChildren()
	{
		child.clear();
	}
}

