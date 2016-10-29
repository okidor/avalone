package avalone.negend;

import java.util.ArrayList;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class SkyChunk extends Chunk
{
	public SkyChunk(Map map,Point pos,boolean setLight)
	{
		super(map,pos,setLight);
		luckFactor = 300;
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		surfaceModifier = chunkBuffer[7].surfaceModifRegister[posX]-Const.tailleChunkY;
		surfaceModifRegister[posX] = chunkBuffer[7].surfaceModifRegister[posX]-Const.tailleChunkY;
		if(posY == surfaceModifier)
		{
			return Block.snow;
		}
		if(posY == surfaceModifier-1 || posY == surfaceModifier-2)
		{
			return Block.dirt;
		}
		if(posY <= surfaceModifier-3)
		{
			return Block.stone;
		}
		if(cases[posX][posY].getBlockID() != Block.air.blockID)
		{
			return Block.getBlock(cases[posX][posY].getBlockID());//ca marche mais faut trouver un code moins moche
		}
		return Block.air;
	}

	public void generateBlocks(Ore[] ores) 
	{
		chunkBuffer[7] = map.checkChunk(pos.x,pos.y-1);
		initGen(ores);
	}

	public void extendGen() 
	{
		//islands
		buildTrees();
	}
	
	public void buildTrees()
	{
		int taille;
		int nbArbres = Var.rand.nextInt(10);
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i = 0;i < nbArbres;i++)
		{
			taille = Var.rand.nextInt(4);
			taille = taille + 3;
			int place = Var.rand.nextInt(72) + 1;
			int trylimit = 0;
			for(int k = 0;k < al.size();k++)
			{
				if(trylimit == 100)
				{
					break;
				}
				if(al.get(k) == place+2 || al.get(k) == place+1 || al.get(k) == place-1 || al.get(k) == place-2 || al.get(k) == place)
				{
					place = Var.rand.nextInt(72) + 1;
					k = 0;
					trylimit++;
				}
			}
			
			int y = -1;
			if(trylimit != 100)
			{
				al.add(place);
				for(int j = 0;j < Const.tailleChunkY-1;j++)
				{
					if(cases[place][j].getBlockID() == Block.snow.blockID)
					{				
						y = j;
						break;
					}
					
				}
			}
			if(y != -1)
			{
				Chunk cUp = map.checkChunk(pos.x, pos.y + 1,true);
				int modifier = 0;
				if(pos.y > 1)
				{
					modifier = 14;
				}
				else
				{
					modifier = 7;
				}
				for(int j = 1;j <= taille;j++)
				{
					if(y + j < Const.tailleChunkY)
					{
						behind[place][y+j].setBlock(Block.wood);
						behind[place][y+j].light = Const.maxLight;
						behind[place][y+j].subID = 1;
						//cases[place][y+j].block.layer = 1;
						if(j > 2)
						{
							if(behind[place+1][y+j].getBlockID() == 0)
							{
								behind[place+1][y+j].setBlock(Block.leaves);
								behind[place+1][y+j].subID = modifier + j%2;
								//cases[place+1][y+j].block.layer = 1;
							}
							if(behind[place-1][y+j].getBlockID() == 0)
							{
								behind[place-1][y+j].setBlock(Block.leaves);
								behind[place-1][y+j].subID = modifier + 2 + j%2;
								//cases[place-1][y+j].block.layer = 1;
							}
						}
					}
					else
					{
						cUp.behind[place][y+j-Const.tailleChunkY].setBlock(Block.wood);
						cUp.behind[place][y+j-Const.tailleChunkY].light = Const.maxLight;
						cUp.behind[place][y+j-Const.tailleChunkY].subID = 1;
						cUp.behind[place][y+j-Const.tailleChunkY].lock();
						if(j > 2)
						{
							if(cUp.behind[place+1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.behind[place+1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.behind[place+1][y+j-Const.tailleChunkY].subID = modifier+j%2;
								cUp.behind[place+1][y+j-Const.tailleChunkY].lock();
							}
							if(cUp.behind[place-1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.behind[place-1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.behind[place-1][y+j-Const.tailleChunkY].subID = modifier + 2 + j%2;
								cUp.behind[place-1][y+j-Const.tailleChunkY].lock();
							}
						}
					}
				}
				if(y+taille+1 < Const.tailleChunkY)
				{
					behind[place][y+taille+1].setBlock(Block.leaves);
					behind[place][y+taille+1].subID = modifier + 4;
					//cases[place][y+taille+1].block.layer = 1;
				}
				else
				{
					cUp.behind[place][y+taille+1-Const.tailleChunkY].setBlock(Block.leaves);
					cUp.behind[place][y+taille+1-Const.tailleChunkY].subID = modifier + 4;
					cUp.behind[place][y+taille+1-Const.tailleChunkY].lock();
				}
				if(y+taille < Const.tailleChunkY)
				{
					if(taille%2 == 1)
					{
						behind[place+1][y+taille].subID = modifier + 5;
						behind[place-1][y+taille].subID = modifier + 6;
					}
				}
				else
				{
					if(taille%2 == 1)
					{
						cUp.behind[place+1][y+taille-Const.tailleChunkY].subID = modifier + 5;
						cUp.behind[place+1][y+taille-Const.tailleChunkY].lock();
						cUp.behind[place-1][y+taille-Const.tailleChunkY].subID = modifier + 6;
						cUp.behind[place+1][y+taille-Const.tailleChunkY].lock();
					}
				}
			}
		}
	}
}
