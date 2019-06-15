package avalone.penguins;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.lwjgl3.IA;
import avalone.api.util.Node;
import avalone.api.util.Point;

public class Penguins_IA extends IA<Board>
{
	private int playerIndex;
	
	public Penguins_IA(Board rootData, int playerIndex, int depth) 
	{
		super(rootData, depth);
		this.playerIndex = playerIndex;
	}

	@Override
	public int evalSituationScore(Board board) 
	{
		int score = 0;
		if(board.penguinsSetOnBoard)
		{
			for(int i = 0;i < board.playersNumber;i++)
			{
				try
				{
					if(i == playerIndex)
					{
						score += board.getPinguinsPos(i).length;
					}
					else
					{
						score -= board.getPinguinsPos(i).length;
					}
				}
				catch(ArrayIndexOutOfBoundsException a)
				{
					System.out.println("out of bounds");
					AvaloneGLAPI glapi = AvaloneGLAPI.getInstance();
					glapi.addSubWindow(1000,650,"tree");
					
					glapi.enableTextures();
				    while (glapi.lastSubWindowShouldNotClose()) 
				    {
				    	glapi.glLoopBegin();
				    	 
				    	Tile tiles[][] = board.getTiles();
				 		for(int k = 0;k < 8;k++)
				 		{
				 			for(int j = 0;j < 8;j++)
				 			{
				 				if(tiles[k][j] != null)
				 				{
				 					tiles[k][j].draw(glapi);
				 				}
				 			}
				 		}
				    	glapi.glLoopEnd();
				    }
				    glapi.destroyLastSubWindow();
					a.printStackTrace();
				}
			}
			
			int bestOpponentScore = 0;
			for(int i = 0;i < board.playersNumber;i++)
			{
				if(i == board.getCurrentPlayer())
				{
					score += board.players[i].getScore();
				}
				else
				{
					int opponentScore = board.players[i].getScore();
					score -= opponentScore;
					bestOpponentScore = Math.max(bestOpponentScore,opponentScore);
				}
			}
			score -= bestOpponentScore;		// prioritize difference between ia and best opponent
			//attention a ce que l'ia n'evite pas d'emprisonner des pingouins car ca augmente le score
			//TODO: ne pas calculer que les points acquis mais aussi ceux securisés sous les pingouins
		}
		return score;
	}

	@Override
	public void generateChildren(Node<Board> n) 
	{
		if(n.data.penguinsSetOnBoard)
		{
			Point[] penguinTiles = n.data.getPinguinsPos(n.data.currentPlayer);
			for(int i = 0;i < penguinTiles.length;i++)
			{
				if(penguinTiles[i] != null)
				{
					for(int j = 0;j < 8;j++)
					{
						for(int k = 0;k < 8;k++)
						{
							Board virtualBoard = n.data.clone();
							boolean allowedMove = virtualBoard.movePenguin(penguinTiles[i].x, penguinTiles[i].y, j, k);
							if(allowedMove)
							{
								int sourceIndex = (penguinTiles[i].x << 3) + penguinTiles[i].y;
								int targetIndex = (j << 3) + k;
								int indexIdentity = (sourceIndex << 6) + targetIndex;
								n.addChild(virtualBoard, indexIdentity);
							}
						}
					}
				}
			}
		}
		else
		{
			for(int i = 0;i < 8;i++)
			{
				for(int j = 0;j < 8;j++)
				{
					Board virtualBoard = n.data.clone();
					boolean allowedPlace = virtualBoard.placePenguin(i, j);
					if(allowedPlace)
					{
						n.addChild(virtualBoard, (i << 3) + j);
					}
				}
			}
		}
	}

	@Override
	public void move(Node<Board> playNode) 
	{
		if(playNode.data.penguinsSetOnBoard)
		{
			int chosenMove = playNode.indexMove;
			int sourceIndex = chosenMove >> 6;
			int targetIndex = chosenMove - (sourceIndex << 6);
			int sourceX = sourceIndex >> 3;
			int sourceY = sourceIndex - (sourceX << 3);
			int targetX = targetIndex >> 3;
			int targetY = targetIndex - (targetX << 3);
			//System.out.println(chosenMove + ", " + sourceIndex);
			//System.out.println(sourceX + ", " + sourceY + ", " + targetX + ", " + targetY);
			//System.out.print(playNode.data.currentPlayer + " -> ");
			playNode.data.movePenguin(sourceX, sourceY, targetX, targetY);
			//System.out.println(playNode.data.currentPlayer);
		}
		else
		{
			int moveX = playNode.indexMove >> 3;
			int moveY = playNode.indexMove - (moveX << 3);
			playNode.data.placePenguin(moveX, moveY);
		}
	}

	@Override
	public void debug(Node<Board> playNode) 
	{
		new GraphicTree(AvaloneGLAPI.getInstance(),playNode);
	}

}
