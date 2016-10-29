package avalone.negend;

import avalone.negend.global.Const;

public class Physic 
{
	public static void verifyGrav(Entity ent)
	{
		applyGrav(ent);
		//secureScroll(glapi,ent,0,ent.vitY);
		if(ent.vitY <= 0)
		{
			//System.out.println("descente");
			checkCollisionFromGravity(ent,ent.cAround[7]);
		}
		else
		{
			//System.out.println("montee");
			checkCollisionFromJump(ent,ent.cAround[1]);
		}
	}
	
	private static void checkCollisionFromJump(Entity ent,Chunk cUp)
	{
		if(ent.overHeadLeft().getBlockSolidity().equals("solid") || ent.overHeadRight().getBlockSolidity().equals("solid"))
		{
			ent.vitY = 0;
			ent.pos.y = ent.overHeadLeft().yB - ent.tailleY;
		}
		else if(ent.overHeadLeft().getBlockSolidity().equals("liquid") || ent.overHeadRight().getBlockSolidity().equals("liquid"))
		{
			ent.pos.y = ent.pos.y + ent.vitY/3;
			//secureScroll(glapi,ent,0,-ent.vitY/2);
			if(ent.pos.y + ent.tailleY >= Const.tailleChunkY * Const.tailleCase)
			{
				ent.changeChunk(cUp, 4);
			}
		}
		else
		{
			if(ent.pos.y + ent.tailleY >= Const.tailleChunkY * Const.tailleCase)
			{
				ent.changeChunk(cUp, 4);
			}
		}
	}
	
	private static void checkCollisionFromGravity(Entity ent,Chunk cDown)
	{
		if(ent.currentCaseLeft().getBlockSolidity().equals("solid") || ent.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			ent.vitY = 0;
			ent.pos.y = ent.currentCaseLeft().yH;
			ent.nbJump = Const.totalJump;
		}
		else if(ent.currentCaseLeft().getBlockSolidity().equals("liquid") || ent.currentCaseRight().getBlockSolidity().equals("liquid"))
		{
			ent.pos.y = ent.pos.y - ent.vitY/2;
			if(ent.pos.y < 0)
			{
				ent.changeChunk(cDown, 3);
			}
		}
		else
		{
			if(ent.pos.y < 0)
			{
				ent.changeChunk(cDown, 3);
			}
		}
	}
	
	public static void checkCollisionFromLeft(Entity ent)
	{
		if(ent.currentCaseLeft().getBlockSolidity().equals("solid") || ent.headLeft().getBlockSolidity().equals("solid"))
		{
			ent.pos.x = ent.currentCaseLeft().xD;
		}
		else
		{
			if(ent.pos.x < 0)
			{
				ent.changeChunk(ent.cAround[3],1);
			}
		}
	}
	
	public static void checkCollisionFromRight(Entity ent)
	{
		if(ent.currentCaseRight().getBlockSolidity().equals("solid") || ent.headRight().getBlockSolidity().equals("solid"))
		{
			ent.pos.x = ent.currentCaseRight().xG - ent.tailleX;
		}
		else
		{
			if(ent.pos.x + ent.tailleX > Const.tailleChunkX * Const.tailleCase)
			{
				ent.changeChunk(ent.cAround[5],2);
			}
		}
	}
	
	public static void unstuck(WorldItem item)
	{
		if(!item.currentCaseLeft().getBlockSolidity().equals("solid"))
		{
			if(item.currentCaseLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
		}
		else if(!item.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			if(item.currentCaseRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
		}
		else if(!item.underFeetLeft().getBlockSolidity().equals("solid"))
		{
			if(item.underFeetLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
			if(item.underFeetLeft().coord.y + Const.tailleCase/2 < item.pos.y)
			{
				item.pos.y = item.pos.y - 2;
			}
		}
		else if(!item.underFeetRight().getBlockSolidity().equals("solid"))
		{
			if(item.underFeetRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
			if(item.underFeetRight().coord.y + Const.tailleCase/2 < item.pos.y)
			{
				item.pos.y = item.pos.y - 2;
			}
		}
		else if(!item.leftOfFeet().getBlockSolidity().equals("solid"))
		{
			if(item.leftOfFeet().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
		}
		else if(!item.rightOfFeet().getBlockSolidity().equals("solid"))
		{
			if(item.rightOfFeet().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
		}
		else if(!item.headLeft().getBlockSolidity().equals("solid"))
		{
			if(item.headLeft().coord.x + Const.tailleCase/2 < item.pos.x)
			{
				item.pos.x = item.pos.x - 2;
			}
			if(item.headLeft().coord.y + Const.tailleCase/2 > item.pos.y)
			{
				item.pos.y = item.pos.y + 2;
			}
		}
		else if(!item.headRight().getBlockSolidity().equals("solid"))
		{
			if(item.headRight().coord.x + Const.tailleCase/2 > item.pos.x)
			{
				item.pos.x = item.pos.x + 2;
			}
			if(item.headRight().coord.y + Const.tailleCase/2 > item.pos.y)
			{
				item.pos.y = item.pos.y + 2;
			}
		}
	}
	
	public static void applyKnockback(AliveEntity ent)
	{
		ent.pos.x = ent.pos.x + ent.knockback;
		if(ent.knockback < 0)
		{
			checkCollisionFromLeft(ent);
			ent.pos.y = ent.pos.y - ent.knockback;
			ent.knockback++;
			//secureScroll(glapi,ent,ent.knockback,-ent.knockback);
		}
		else if(ent.knockback > 0)
		{
			checkCollisionFromRight(ent);
			ent.pos.y = ent.pos.y + ent.knockback;
			ent.knockback--;
			//secureScroll(glapi,ent,ent.knockback,ent.knockback);
		}
		//checkCollisionFromJump(ent,glapi,ent.cAround[1]);
		//secureCenter(glapi,ent);
	}
	
	public static void flow(Chunk c)
	{
		waterFall(c);
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(c.cases[i][j].getBlockSolidity().equals("liquid"))
				{
					if(getSecureCase(c,i+1,j).getBlockSolidity().equals("nonsolid") && c.cases[i][j].getFlowPotential() > c.cases[i][j].subID)
					{
						if(getSecureCase(c,i,j-1).getBlockSolidity().equals("solid") && !getSecureCase(c,i+1,j-1).getBlockSolidity().equals("liquid"))
						{
							getSecureCase(c,i+1,j).setBlock(Block.getBlock(c.cases[i][j].getBlockID()));
							getSecureCase(c,i+1,j).subID = c.cases[i][j].subID + 1;
						}
						else if(getSecureCase(c,i+1,j-1).getBlockSolidity().equals("liquid") && getSecureCase(c,i+1,j-1).subID == 0)
						{
							getSecureCase(c,i+1,j).setBlock(Block.getBlock(c.cases[i][j].getBlockID()));
							getSecureCase(c,i+1,j).subID = c.cases[i][j].subID + 1;
						}
					}
					else if(c.cases[i][j].getBlockID() == getSecureCase(c,i+1,j).getBlockID())
					{
						if(getSecureCase(c,i+1,j).subID > c.cases[i][j].subID + 1)
						{
							getSecureCase(c,i+1,j).subID = c.cases[i][j].subID + 1;
						}
					}
					if(getSecureCase(c,i-1,j).getBlockSolidity().equals("nonsolid") && c.cases[i][j].getFlowPotential() > c.cases[i][j].subID)
					{
						if(getSecureCase(c,i,j-1).getBlockSolidity().equals("solid") && !getSecureCase(c,i-1,j-1).getBlockSolidity().equals("liquid"))
						{
							getSecureCase(c,i-1,j).setBlock(Block.getBlock(c.cases[i][j].getBlockID()));
							getSecureCase(c,i-1,j).subID = c.cases[i][j].subID + 1;
						}
						else if(getSecureCase(c,i-1,j-1).getBlockSolidity().equals("liquid") && getSecureCase(c,i-1,j-1).subID == 0)
						{
							getSecureCase(c,i-1,j).setBlock(Block.getBlock(c.cases[i][j].getBlockID()));
							getSecureCase(c,i-1,j).subID = c.cases[i][j].subID + 1;
						}
					}
					else if(c.cases[i][j].getBlockID() == getSecureCase(c,i-1,j).getBlockID())
					{
						if(getSecureCase(c,i-1,j).subID > c.cases[i][j].subID + 1)
						{
							getSecureCase(c,i-1,j).subID = c.cases[i][j].subID + 1;
						}
					}
				}
			}
		}
	}
	
	private static Tile getSecureCase(Chunk c,int i,int j)
	{
		if(i < 0)
		{
			return c.chunkBuffer[3].cases[i+Const.tailleChunkX][j];
		}
		if (i >= Const.tailleChunkX)
		{
			return c.chunkBuffer[5].cases[i-Const.tailleChunkX][j];
		}
		
		if(j < 0)
		{
			return c.chunkBuffer[7].cases[i][j+Const.tailleChunkY];
		}
		if (j >= Const.tailleChunkY)
		{
			return c.chunkBuffer[1].cases[i][j-Const.tailleChunkY];
		}
		return c.cases[i][j];
	}
	
	private static void waterFall(Chunk c)
	{
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				if(c.cases[i][j].getBlockSolidity().equals("liquid"))
				{
					if(getSecureCase(c,i,j-1).getBlockSolidity().equals("nonsolid"))
					{
						getSecureCase(c,i,j-1).setBlock(Block.getBlock(c.cases[i][j].getBlockID()));
						
						getSecureCase(c,i,j-1).subID = c.cases[i][j].subID;
						c.cases[i][j].destroyBlock();
						/*getSecureCase(c,i,j-1).subID = 0;
						if(c.cases[i][j].subID == 0)
						{
							c.[i][j].destroyBlock();
						}*/
					}
					else if(c.cases[i][j].getBlockID() == getSecureCase(c,i,j-1).getBlockID())
					{
						if(c.cases[i][j].subID == 0)
						{
							if(getSecureCase(c,i,j-1).subID != 0)
							{
								c.cases[i][j].subID = getSecureCase(c,i,j-1).subID;
								getSecureCase(c,i,j-1).subID = 0;
							}
							else
							{
								//columnfall
								int det = 0;
								if(getSecureCase(c,i+1,j).getBlockSolidity().equals("nonsolid"))
								{
									det++;
								}
								if(getSecureCase(c,i-1,j).getBlockSolidity().equals("nonsolid"))
								{
									det = det + 2;
								}
								if(det == 1)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i+1,j).setBlock(Block.water);
									getSecureCase(c,i+1,j).subID = 0;
								}
								else if(det == 2)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i-1,j).setBlock(Block.water);
									getSecureCase(c,i-1,j).subID = 0;
								}
								else if(det == 3)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i+1,j).setBlock(Block.water);
									getSecureCase(c,i+1,j).subID = getSecureCase(c,i+1,j).getFlowPotential()/2;
									getSecureCase(c,i-1,j).setBlock(Block.water);
									getSecureCase(c,i-1,j).subID = getSecureCase(c,i-1,j).getFlowPotential()/2;
								}
							}
						}
						else 
						{
							if(getSecureCase(c,i,j-1).subID != 0)
							{
								getSecureCase(c,i,j-1).subID = getSecureCase(c,i,j-1).subID - c.cases[i][j].subID;
								if(getSecureCase(c,i,j-1).subID < 0)
								{
									c.cases[i][j].subID = c.cases[i][j].getFlowPotential() + getSecureCase(c,i,j-1).subID;
									getSecureCase(c,i,j-1).subID = 0;
								}
							}
						}
						/*if(c.cases[i][j].subID == 0)
						{
							if(getSecureCase(c,i,j-1).subID != 0)
							{
								c.cases[i][j].destroyBlock();
								getSecureCase(c,i,j-1).subID = 0;
							}
							else
							{
								int det = 0;
								//columnfall
								if(getSecureCase(c,i+1,j).getBlockSolidity().equals("nonsolid"))
								{
									det++;
								}
								if(getSecureCase(c,i-1,j).getBlockSolidity().equals("nonsolid"))
								{
									det = det + 2;
								}
								if(det == 1)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i+1,j).setBlock(Block.water);
									getSecureCase(c,i+1,j).subID = 0;
								}
								else if(det == 2)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i-1,j).setBlock(Block.water);
									getSecureCase(c,i-1,j).subID = 0;
								}
								else if(det == 3)
								{
									c.cases[i][j].destroyBlock();
									getSecureCase(c,i+1,j).setBlock(Block.water);
									getSecureCase(c,i+1,j).subID = getSecureCase(c,i+1,j).getFlowPotential()/2;
									getSecureCase(c,i-1,j).setBlock(Block.water);
									getSecureCase(c,i-1,j).subID = getSecureCase(c,i-1,j).getFlowPotential()/2;
								}
							}
						}
						else
						{
							getSecureCase(c,i,j-1).subID = 0;
						}*/
					}
				}
			}
		}
	}
	
	public static void airFriction()
	{
		
	}
	
	/*private static void secureScroll(AvaloneGLAPI glapi, Entity ent,int dx,int dy)
	{
		if(ent instanceof Player)
		{
			glapi.scroll(dx,dy);
		}
	}*/
	
	private static void applyGrav(Entity ent)
	{
		if(ent.vitY > -Const.tailleCase + 1)
		{
			ent.vitY = ent.vitY + Const.gravity;
		}
		ent.pos.y = ent.pos.y + ent.vitY;
	}
}
