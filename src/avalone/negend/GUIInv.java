package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Var;

public class GUIInv extends GUIComponent
{
	private Inventory inv;
	private int currentIndex;
	
	public GUIInv(Inventory inv,Point localPosDownLeft,/* Point localPosUpRight,*/ String name) 
	{
		super(localPosDownLeft, new Point(localPosDownLeft.x + 35*9+30,localPosDownLeft.y + 35*2+30), name);
		for(int i = 0;i < inv.getSize();i++)
		{
			currentIndex = i;
			behaviors.add(new Behavior()
			{
				private int index = currentIndex;
				
				public void drawNumber(int number,Point p1)
				{
					String s = "" + number;
					while(s.length() < 3)
					{
						s = " " + s;
					}
					Var.rend.drawText(p1.clone(2,2), p1.clone(18,18), s, "red");
				}
				
				@Override
				public void determineDraw(Point renderPosDownLeft, Point renderPosUpRight, String componentName, PlayerInfo infos) 
				{
					if(index < inv.slotmax * 10)
					{
						int xSlotPos = index % 10;
						int ySlotPos = index / 10;
						Point p1 = new Point(renderPosDownLeft.x + (xSlotPos * 35),renderPosDownLeft.y -(ySlotPos * 35));
						
						Item item = inv.getItem(index);
						int number = inv.getNumber(index);
						if(item != Inventory.noItem && item.tier != 0)
						{
							Var.rend.drawTier(p1.clone(1), p1.clone(29),item.tier);
						}
						if(index == inv.getSelectedItemSlot())
						{
							Var.rend.draw(p1, p1.clone(30), "red.png");
						}
						else
						{
							Var.rend.draw(p1, p1.clone(30), "case.png");
						}
						if(item.id != 0)
						{
							item.draw(p1.clone(0));
							drawNumber(number,p1);
						}
					}
					if(index == 0)
					{
						inv.drawMouseItem();
					}
				}
				
				public void invInfos(Point p1)
				{
					Item item = inv.itemForSave(index);
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
				
				@Override
				public void reactionToClick(Point renderPosDownLeft, Point renderPosUpRight,Point mousePoint,String componentName,boolean leftClick)
				{
					if(inv.slotmax == 3)
					{
						int xSlotPos = index % 10;
						int ySlotPos = index / 10;
						Point p1 = new Point(renderPosDownLeft.x + (xSlotPos * 35),renderPosDownLeft.y -(ySlotPos * 35));
						Point p2 = p1.clone(30);
						
						if(mousePoint.x >= p1.x && mousePoint.x <= p2.x)
						{
							if(mousePoint.y >= p1.y && mousePoint.y <= p2.y)
							{
								System.out.println("clicked on slot " + index);
								invInfos(p1);
								if(leftClick)
								{
									inv.setMouseItem(index);
								}
								else
								{
									inv.setMouseItemRightClick(index);
								}
							}
						}
					}
				}
			});
		}
	}
}
