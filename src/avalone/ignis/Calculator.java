package avalone.ignis;

public class Calculator 
{		
	public static int getPointsFromNumber(Element[][] elems,Element elem)
	{
		int points = 0;
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(elems[i][j] == elem)
				{
					points = points + 10;
				}
			}
		}
		if(points == 0)
		{
			points = points - 500;
		}
		return points;
	}
	
	public static int getPointsFromPositionValues(Element[][] elems,Element elem,int[][] positionValues)
	{
		int points = 0;
		
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(elems[i][j] == elem)
				{
					points = points + positionValues[i][j];
				}
			}
		}
		return points;
	}
	
	public static int getPointsFromNbCoups(Element[][] elems,Element elem,int borderXMin,int borderXMax,int borderYMin,int borderYMax)
	{
		int points = 0;
		
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(elems[i][j] == elem)
				{
					points = points + computeNbCoup(i,j,elems,borderXMin,borderXMax,borderYMin,borderYMax);
				}
			}
		}
		
		return points;
	}
	
	public static int getTotalPoints(Board board,Element elem)
	{
		//return getPointsFromNumber(board.getElements(),elem) + getPointsFromPositionValues(board.getElements(),elem,board.positionValues) + getPointsFromNbCoups(board.getElements(),elem,board.getBorderXMin(),board.getBorderXMax(),board.getBorderYMin(),board.getBorderYMax());
		Element[][] elems = board.getElements();
		int points = 0;
		
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				if(elems[i][j] == elem)
				{
					points = points + 10 + board.positionValues[i][j] 
					+ computeNbCoup(i,j,elems,board.getBorderXMin(),board.getBorderXMax(),board.getBorderYMin(),board.getBorderYMax());
				}
			}
		}
		
		return points;
	}
	
	public static void updateValues(int xMin,int xMax,int yMin,int yMax,int[][] positionValues)
	{
		int xRange = xMax-xMin;
		int yRange = yMax-yMin;
		for(int i = 0;i < 6;i++)
		{
			for(int j = 0;j < 6;j++)
			{
				positionValues[i][j] = -1;
			}
		}
		for(int i = xMin;i <= xRange/2 + xMin;i++)
		{
			int iValue = i - xMin;
			for(int j = yMin;j <= yRange/2 + yMin;j++)
			{
				int jValue = j - yMin;
				positionValues[i][j] = iValue*2 + jValue*2 + (iValue/2 + iValue%2)*(jValue/2 + jValue%2)*2;
			}
			if(yRange % 2 == 1)
			{
				int mid = yRange/2 + 1;
				int midValue = mid - yMin;
				positionValues[i][mid] = iValue*2 + (midValue)*2 + (iValue/2 + iValue%2) * (midValue/2 + midValue%2)*2;
			}
		}
		if(xRange % 2 == 1)
		{
			int xMid = xRange/2 + 1;
			int xMidValue = xMid - xMin;
			for(int j = yMin;j <= yRange/2 + yMin;j++)
			{
				int jValue = j - yMin;
				positionValues[xMid][j] = xMidValue*2 + jValue*2 + (xMidValue/2 + xMidValue%2)*(jValue/2 + jValue%2)*2;
			}
			if(yRange % 2 == 1)
			{
				int yMid = yRange/2 + 1;
				int yMidValue = yMid - yMin;
				positionValues[xMid][yMid] = xMidValue*2 + (yMidValue)*2 + (xMidValue/2 + xMidValue%2) * (yMidValue/2 + yMidValue%2)*2;
			}
		}
		
		for(int i = xMin;i <= xRange/2 + xMin;i++)
		{
			for(int j = yMin;j <= yRange/2 + yMin;j++)
			{
				positionValues[xMax - i][j] = positionValues[i][j];
				positionValues[i][yMax - j] = positionValues[i][j];
				positionValues[xMax - i][yMax - j] = positionValues[i][j];
			}
		}
	}
	
	private static int computeNbCoup(int x,int y,Element[][] elems,int borderXMin,int borderXMax,int borderYMin,int borderYMax)
	{
		int nb = 10;int nbDerriere;
		boolean direct = true;
		
		for(int k = borderXMin;k < x;k++)
		{
			if(elems[k][y] == Element.Earth)
			{
				direct = false;
			}
		}
		
		if(direct)
		{
			nbDerriere = 0;
			for(int k = x+1;k <= borderXMax;k++)
			{
				if(elems[k][y] != Element.Empty)
				{
					nbDerriere++;
				}
			}
			int xRange = borderXMax - borderXMin + 1;
			nb = min(nb,xRange - nbDerriere);
		}
		direct = true;
		
		for(int k = x+1;k <= borderXMax;k++)
		{
			if(elems[k][y] == Element.Earth)
			{
				direct = false;
			}
		}
		
		if(direct)
		{
			nbDerriere = 0;
			for(int k = borderXMin;k < x;k++)
			{
				if(elems[k][y] != Element.Empty)
				{
					nbDerriere++;
				}
			}
			int xRange = borderXMax - borderXMin + 1;
			nb = min(nb,xRange - nbDerriere);
		}
		direct = true;
		
		for(int k = 0;k < y;k++)
		{
			if(elems[x][k] == Element.Earth)
			{
				direct = false;
			}
		}
		
		if(direct)
		{
			nbDerriere = 0;
			for(int k = y+1;k < 6;k++)
			{
				if(elems[x][k] != Element.Empty)
				{
					nbDerriere++;
				}
			}
			int yRange = borderYMax - borderYMin + 1;
			nb = min(nb,yRange - nbDerriere);
		}
		direct = true;
		
		for(int k = y+1;k < 6;k++)
		{
			if(elems[x][k] == Element.Earth)
			{
				direct = false;
			}
		}
		
		if(direct)
		{
			nbDerriere = 0;
			
			for(int k = 0;k < y;k++)
			{
				if(elems[x][k] != Element.Empty)
				{
					nbDerriere++;
				}
			}
			int yRange = borderYMax - borderYMin + 1;
			nb = min(nb,yRange - nbDerriere);
		}
		
		return nb;
	}
	
	public static int min(int a,int b)
	{
		if(a <= b)
		{
			return a;
		}
		else
		{
			return b;
		}
	}
	
	public static int max(int a,int b)
	{
		if(a >= b)
		{
			return a;
		}
		else
		{
			return b;
		}
	}
}
