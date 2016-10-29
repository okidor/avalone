package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Var;

public class InfoMouse 
{
	public static void draw(Player play)
	{
		Point p = Var.rend.getMousePos();
		if(p.x >= play.pos.x && p.x < play.pos.x + play.tailleX)
		{
			if(p.y >= play.pos.y && p.y <= play.pos.y + play.tailleY)
			{
				Var.rend.drawText(p.clone(22,-15), p.clone(100,1),String.valueOf(play.health) + "/" + String.valueOf(play.maxHealth),"red");
			}
		}
		
		for(int j = 0;j < play.inv.slotmax;j++)
		{
			Point p1 = new Point(play.pos.x - 141,270 + play.pos.y -(j * 35));
			for(int i = 0;i < 10;i++)
			{
				p1.moveCoords(35,0);
				if(p.x >= p1.x && p.x < p1.x + 32)
				{
					if(p.y >= p1.y && p.y < p1.y + 32)
					{
						Item item = play.inv.itemForSave(j * 10 + i);
						String name = item.getTexture();
						if(name.contains("."))
						{
							name = name.split("\\.")[0];
						}
						Var.rend.drawText(p1.clone(15 -(7 * name.length()/2),-23), p1.clone(15 + 7 * name.length()/2,-10), name, "red");
						if(item instanceof WeaponItem)
						{
							String s1 = "tier " + String.valueOf(item.tier);
							String s2 = "level " + String.valueOf(item.level);
							String s3 = "damage " + String.valueOf(item.damage);
							Var.rend.drawText(p1.clone(15 -(7 * s1.length()/2),-39), p1.clone(15 + 7 * s1.length()/2,-26), s1, "red");
							Var.rend.drawText(p1.clone(15 -(7 * s2.length()/2),-55), p1.clone(15 + 7 * s2.length()/2,-42), s2, "red");
							Var.rend.drawText(p1.clone(15 -(7 * s3.length()/2),-71), p1.clone(15 + 7 * s3.length()/2,-58), s3, "red");
						}
					}
				}
			}
		}
	}
}
