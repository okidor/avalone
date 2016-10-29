package avalone.negend;

import java.util.ArrayList;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class SurfaceChunk extends Chunk
{
	public int biome;
	public int sideBiome;
	
	public SurfaceChunk(Map map,Point pos,boolean setLight,boolean init)
	{
		super(map,pos,setLight);
		luckFactor = 200;
	}
	
	protected void setSurfaceModifier()
	{
		if(pos.x > 0)
		{
			surfaceModifier = chunkBuffer[3].surfaceModifRegister[Const.tailleChunkX-1];
		}
		else if(pos.x < 0)
		{
			surfaceModifier = chunkBuffer[5].surfaceModifRegister[0];
		}
	}
	
	public boolean positiveX()
	{
		return pos.x >= 0;
	}
	
	protected int chooseBiome()
	{
		if(sideBiome == Const.plainsBiome)
		{
			int a = Var.rand.nextInt(4);
			if(a == 0)
			{
				return Const.hillsBiome;
			}
			else if(a == 1)
			{
				return Const.beachBiome;
			}
			else
			{
				return sideBiome;
			}
		}
		else if(sideBiome == Const.hillsBiome)
		{
			int a = Var.rand.nextInt(4);
			if(a == 0)
			{
				return Const.plainsBiome;
			}
			else if(a == 1)
			{
				return sideBiome;
			}
			else
			{
				return Const.mountainsBiome;
			}
		}
		else if(sideBiome == Const.beachBiome)
		{
			int a = Var.rand.nextInt(5);
			if(a == 0 || a == 1)
			{
				return sideBiome;
			}
			else if(a == 2)
			{
				return Const.plainsBiome;
			}
			else
			{
				return Const.oceanBiome;
			}
		}
		else if(sideBiome == Const.oceanBiome)
		{
			int a = Var.rand.nextInt(5);
			if(a == 0)
			{
				return sideBiome;
			}
			else if(a == 3 || a == 4)
			{
				return Const.deepOceanBiome;
			}
			else
			{
				return Const.beachBiome;
			}
		}
		else if(sideBiome == Const.mountainsBiome)
		{
			int a = Var.rand.nextInt(5);
			if(a == 0)
			{
				return sideBiome;
			}
			else if(a == 3 || a == 4)
			{
				return Const.skyMountainsBiome;
			}
			else
			{
				return Const.hillsBiome;
			}
		}
		else if(sideBiome == Const.deepOceanBiome)
		{
			int a = Var.rand.nextInt(3);
			if(a == 0)
			{
				return sideBiome;
			}
			else
			{
				return Const.oceanBiome;
			}
		}
		else if(sideBiome == Const.skyMountainsBiome)
		{
			int a = Var.rand.nextInt(3);
			if(a == 0)
			{
				return sideBiome;
			}
			else
			{
				return Const.mountainsBiome;
			}
		}
		else
		{
			Const.debug("(SurfaceChunk:chooseBiome): warning, sideBiome is not valid on chunk: " + pos.x + ", " + pos.y + " (has value:" + sideBiome + ")");
			return -1;
		}
	}
	
	private void surfaceBiome()
	{
		int a;
		if(biome == Const.plainsBiome)
		{
			a = Var.rand.nextInt(3);
			a--;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 15)
			{
				surfaceModifier--;
			}
			if(surfaceModifier < 3)
			{
				surfaceModifier++;
			}
		}
		else if(biome == Const.hillsBiome)
		{
			a = Var.rand.nextInt(5);
			a = a - 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 20)
			{
				surfaceModifier--;
			}
			else if(surfaceModifier < 7)
			{
				if(a != 2)
				{
					surfaceModifier = surfaceModifier + 2;
				}
			}
		}
		else if(biome == Const.beachBiome)
		{
			a = Var.rand.nextInt(7);
			a = a - 3; a = a / 3;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > 2)
			{
				surfaceModifier--;
			}
			if(surfaceModifier < 0)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
		else if(biome == Const.oceanBiome)
		{
			a = Var.rand.nextInt(4);
			a = a - 2; a = a / 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > -1)
			{
				surfaceModifier = surfaceModifier - 2;
			}
			if(surfaceModifier < -15)
			{
				surfaceModifier++;
			}
		}
		else if(biome == Const.mountainsBiome)
		{
			a = Var.rand.nextInt(3);
			a--;a = a * 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier < 25)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
		else if(biome == Const.deepOceanBiome)
		{
			a = Var.rand.nextInt(4);
			a = a - 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier > -15)
			{
				surfaceModifier = surfaceModifier - 2;
			}
		}
		else if(biome == Const.skyMountainsBiome)
		{
			a = Var.rand.nextInt(4);
			a--;a = a * 2;
			surfaceModifier = surfaceModifier + a;
			if(surfaceModifier < 40)
			{
				surfaceModifier = surfaceModifier + 2;
			}
		}
	}
	
	public void generateBlocks(Ore[] ores)
	{
		if(pos.x > 0)
		{
			chunkBuffer[3] = map.checkChunk(pos.x-1,pos.y);
			sideBiome = ((SurfaceChunk)chunkBuffer[3]).biome;
		}
		else if(pos.x < 0)
		{
			chunkBuffer[5] = map.checkChunk(pos.x+1,pos.y);
			sideBiome = ((SurfaceChunk)chunkBuffer[5]).biome;
		}
		else
		{
			sideBiome = -1;
		}
		biome = chooseBiome();
		setSurfaceModifier();
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			surfaceBiome();
			if(positiveX())
			{
				surfaceModifRegister[i] = surfaceModifier;
			}
			else
			{
				surfaceModifRegister[Const.tailleChunkX-1 - i] = surfaceModifier;
			}
		}
		initGen(ores);
	}
	
	public Block chooseBlock(int posX,int posY)
	{
		/*if(posX == Const.tailleChunkX/2 && posY == surfaceModifRegister[posX] + 1)
		{
			return Block.chest;
		}*/
		if(posY == surfaceModifRegister[posX])
		{
			if(surfaceModifRegister[posX] > 2)
			{
				return Block.grass;
			}
			else
			{
				return Block.sand;
			}
		}
		
		if(posY == surfaceModifRegister[posX]-1 || posY == surfaceModifRegister[posX]-2)
		{
			if(surfaceModifRegister[posX] > 2)
			{
				return Block.dirt;
			}
			else
			{
				if(posY == surfaceModifRegister[posX])
				{
					return Block.stone;
				}
				return Block.sand;
			}
		}
		
		if(posY <= surfaceModifRegister[posX]-3)
		{
			return Block.stone;
		}
		return Block.air;
	}
	
	public void extendGen()
	{
		smooth();
		buildTrees();
	}
	
	public void buildTrees()
	{
		Chunk cUp = map.checkChunk(pos.x, pos.y + 1);
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
					if(cases[place][j].getBlockID() == Block.grass.blockID)
					{				
						y = j;
						break;
					}
					
				}
			}
			if(y != -1)
			{
				for(int j = 1;j <= taille;j++)
				{
					if(y + j < Const.tailleChunkY)
					{
						behind[place][y+j].setBlock(Block.wood);
						behind[place][y+j].light = Const.maxLight;
						behind[place][y+j].subID = 0;
						//cases[place][y+j].block.layer = 1;
						if(j > 2)
						{
							if(behind[place+1][y+j].getBlockID() == 0)
							{
								behind[place+1][y+j].setBlock(Block.leaves);
								behind[place+1][y+j].subID = j%2;
								//cases[place+1][y+j].block.layer = 1;
							}
							if(behind[place-1][y+j].getBlockID() == 0)
							{
								behind[place-1][y+j].setBlock(Block.leaves);
								behind[place-1][y+j].subID = 2 + j%2;
								//cases[place-1][y+j].block.layer = 1;
							}
						}
					}
					else
					{
						cUp.behind[place][y+j-Const.tailleChunkY].setBlock(Block.wood);
						cUp.behind[place][y+j-Const.tailleChunkY].light = Const.maxLight;
						cUp.behind[place][y+j-Const.tailleChunkY].subID = 0;
						cUp.behind[place][y+j-Const.tailleChunkY].lock();
						if(j > 2)
						{
							if(cUp.behind[place+1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.behind[place+1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.behind[place+1][y+j-Const.tailleChunkY].subID = j%2;
								cUp.behind[place+1][y+j-Const.tailleChunkY].lock();
							}
							if(cUp.behind[place-1][y+j-Const.tailleChunkY].getBlockID() == 0)
							{
								cUp.behind[place-1][y+j-Const.tailleChunkY].setBlock(Block.leaves);
								cUp.behind[place-1][y+j-Const.tailleChunkY].subID = 2 + j%2;
								cUp.behind[place-1][y+j-Const.tailleChunkY].lock();
							}
						}
					}
				}
				if(y+taille+1 < Const.tailleChunkY)
				{
					behind[place][y+taille+1].setBlock(Block.leaves);
					behind[place][y+taille+1].subID = 4;
					//cases[place][y+taille+1].block.layer = 1;
				}
				else
				{
					cUp.behind[place][y+taille+1-Const.tailleChunkY].setBlock(Block.leaves);
					cUp.behind[place][y+taille+1-Const.tailleChunkY].subID = 4;
					cUp.behind[place][y+taille+1-Const.tailleChunkY].lock();
				}
				if(y+taille < Const.tailleChunkY)
				{
					if(taille%2 == 1)
					{
						if(behind[place+1][y+taille].getBlockID() == Block.leaves.blockID)
						{
							behind[place+1][y+taille].subID = 5;
						}
						if(behind[place-1][y+taille].getBlockID() == Block.leaves.blockID)
						{
							behind[place-1][y+taille].subID = 6;
						}
					}
				}
				else
				{
					if(taille%2 == 1)
					{
						if(cUp.behind[place+1][y+taille-Const.tailleChunkY].getBlockID() == Block.leaves.blockID)
						{
							cUp.behind[place+1][y+taille-Const.tailleChunkY].subID = 5;
						}
						//cUp.behind[place+1][y+taille-Const.tailleChunkY].lock();
						if(cUp.behind[place-1][y+taille-Const.tailleChunkY].getBlockID() == Block.leaves.blockID)
						{
							cUp.behind[place-1][y+taille-Const.tailleChunkY].subID = 6;
						}
						//cUp.behind[place+1][y+taille-Const.tailleChunkY].lock();
					}
				}
			}
		}
	}
}
