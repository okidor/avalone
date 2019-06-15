package avalone.api.lwjgl3;

import java.util.ArrayList;
import java.util.Random;

import avalone.api.util.Node;

public abstract class IA<T> 
{
	private Node<T> root;
	private ArrayList<Node<T>> leaves;
	private int depth;
	private boolean debugMode;
	private Random rand;
	private boolean optimize;
	
	public IA(T rootData, int depth)
	{
		this.depth = depth;
		leaves = new ArrayList<Node<T>>();
		debugMode = false;
		optimize = true;
		rand = new Random();
		root = new Node<T>(false, rootData);
	}
	
	public abstract void generateChildren(Node<T> n);
	public abstract int evalSituationScore(T n);
	public abstract void move(Node<T> n);
	public abstract void debug(Node<T> n);
	
	public void evalCurrentSituation()
	{
		root.destroyAllChildren();	//TODO: optimize, find leaf with same rootData instead of recreating all the tree
		leaves.add(root);
		addLayers();
		//minimax(racine);
		if(debugMode)
		{
			debug(root);
		}
		move(root);
	}
	
	private void addLayers()
	{
		for(int i = 0;i < depth-1;i++)
		{
			addLayer(true);
		}
		addLayer(false);
	}
	
	public void addLayer(boolean addLeaves)
	{
		//System.out.println(leaves.size() + " leaves are valids to have children");
		//System.out.println("generating a total of " + (leaves.size()*48) + " children");
		for(int i = 0;i < leaves.size();i++)
		{
			Node<T> leaf = leaves.get(i);
			if(leaf.nbChildren() == 0)
			{
				generateChildren(leaf);
			}
			else
			{
				System.out.println("Skipped children generation for node already having children");
			}
			if(debugMode && (i % 5000 == 0))
			{
				long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				System.out.println("using " + usedMem + " on " + Runtime.getRuntime().totalMemory() + " allocated (" + Runtime.getRuntime().maxMemory()+ ")");
			}
		}
		leaves.clear();
		alphabeta(root,addLeaves,-2000000,2000000);
	}
	
	public void alphabeta(Node<T> n, boolean addLeaves, int alpha, int beta)
	{
		if(n.isLeaf())
		{
			n.evaluated = true;
			n.value = evalSituationScore(n.data);
			if(addLeaves)
				leaves.add(n);
			return;
		}
		else
		{
			int val;
			ArrayList<Integer> indexVals = new ArrayList<Integer>();
			if(n.isMin)
			{
				val = 2000000;
				for(int i = 0;i < n.nbChildren();i++)
				{
					alphabeta(n.getChild(i),addLeaves,alpha,beta);
					if(n.getChild(i).value < val)
					{
						val = n.getChild(i).value;
						indexVals.clear();
						indexVals.add(n.getChild(i).indexIdentity);
					}
					else if(n.getChild(i).value == val)
					{
						indexVals.add(n.getChild(i).indexIdentity);
					}
					//TODO option to skip alphabeta optimization
					if(optimize)
					{
						if(alpha > val)
						{
							n.evaluated = true;
							n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
							indexVals.clear();
							n.value = val;
							return;
						}
						beta = Math.min(beta,val);
					}
				}
			}
			else
			{
				val = -2000000;
				for(int i = 0;i < n.nbChildren();i++)
				{
					alphabeta(n.getChild(i),addLeaves,alpha,beta);
					if(n.getChild(i).value > val)
					{
						val = n.getChild(i).value;
						indexVals.clear();
						indexVals.add(n.getChild(i).indexIdentity);
					}
					else if(n.getChild(i).value == val)
					{
						indexVals.add(n.getChild(i).indexIdentity);
					}
					if(optimize)
					{
						if(val > beta)
						{
							n.evaluated = true;
							n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
							indexVals.clear();
							n.value = val;
							return;
						}
						alpha = Math.max(alpha,val);
					}
				}
			}
			n.evaluated = true;
			n.indexMove = indexVals.get(rand.nextInt(indexVals.size()));
			indexVals.clear();
			n.value = val;
		}
	}
}
