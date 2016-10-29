package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Const;

public class SpawnChunk extends SurfaceChunk
{
	public SpawnChunk(Map map,Point pos,boolean setLight,boolean init)
	{
		super(map,pos,setLight,init);
		//new Error().printStackTrace();
	}
	
	public void spawnFirstChest()
	{
		Point p = genSpawnCoords();
		tileEntityList.add(new TileEntity(p.x,p.y,this));
	}
	
	public void setSurfaceModifier()
	{
		surfaceModifier = 10;
	}
	
	protected int chooseBiome()
	{
		return Const.plainsBiome;
	}
}
