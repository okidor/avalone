package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class MineralItem extends Item
{
	public float[] color;
	private Ore ore;
	
	MineralItem(int subID,float[] color,Ore ore)
	{
		super(Const.unplacableOffset,subID,EnumItem.mineralBlock,0,0,ore.name);
		this.color = color;
		this.ore = ore;
	}
	
	public void draw(Point p)
	{
		p.moveCoords(7, 7);
		Var.rend.drawMineral(p,p.clone(16),color);
	}
	
	public void drawHand(Point posPlayer,int turned)
	{
		if(turned == 0)
		{
			Point p = posPlayer.clone(-7,5);
			Var.rend.drawMineral(p,p.clone(16),color);
		}
		else if(turned == 1)
		{
			Point p = posPlayer.clone(-15,5);
			Var.rend.drawMineral(p,p.clone(16),color);
		}
		else if(turned == 2)
		{
			Point p = posPlayer.clone(7,5);
			Point p2 = p.clone(16);
			p.moveCoords(16, 0);
			p2.moveCoords(-16,0);
			Var.rend.drawMineral(p,p2,color);
		}
	}
	
	public void setType()
	{
		type = 2;
	}
	
	public Ore getOre()
	{
		return ore;
	}
}
