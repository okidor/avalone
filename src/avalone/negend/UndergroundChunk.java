package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class UndergroundChunk extends Chunk
{
	//private Chunk cUp;
	
	public UndergroundChunk(Map map,Point pos/*,Chunk cUp*/,boolean setLight) 
	{
		super(map,pos/*,cUp*/,setLight);
		luckFactor = 200/(1-pos.y);
		if(luckFactor == 0)
		{
			luckFactor = 1;
		}
		//this.cUp = cUp;
		genFlag = false;
	}

	public Block chooseBlock(int posX,int posY)
	{
		surfaceModifier = chunkBuffer[1].surfaceModifRegister[posX]+Const.tailleChunkY;
		surfaceModifRegister[posX] = chunkBuffer[1].surfaceModifRegister[posX]+Const.tailleChunkY;
		int a = Var.rand.nextInt(2);
		if(posY == surfaceModifier || posY == surfaceModifier-a)
		{
			return Block.sand;
		}
		else if(posY > surfaceModifier)
		{
			return Block.water;
		}
		return Block.stone;
	}

	public void generateBlocks(Ore[] ores) 
	{
		chunkBuffer[1] = map.checkChunk(pos.x,pos.y+1);
		initGen(ores);
	}

	public void extendGen() 
	{
		//grottes
	}
}
