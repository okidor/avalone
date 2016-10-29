package avalone.negend;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import avalone.negend.global.Var;

public class Inventory 
{
	private Item objectType[];
	private int objectNumber[];
	private int selectedItem;
	public int slotmax;
	private Item onMouseItem;
	private int onMouseNumber;
	private Entity owner;
	private int size;
	private int onMouseItemID = -1;
	
	public static final Item noItem = new ItemBlock(0,0);
	
	public Inventory(Entity owner,int size)
	{
		objectType = new Item[size];
		objectNumber = new int[size];
		selectedItem = 0;
		slotmax = 1;
		this.size = size;
		this.owner = owner;
		init();
	}
	
	public void init()
	{
		/*GameFile game = new GameFile("mod/startInventory.txt");
		ArrayList<String[]> slotList = new ArrayList<String[]>();
		slotList = game.al;*/
		for(int i = 0;i < size;i++)
		{
			/*String[] s = slotList.get(i);
			if(Integer.decode(s[3]) <= Const.unplacableOffset)
			{
				objectType[Integer.decode(s[1])] = new Item(Integer.decode(s[3]),Integer.decode(s[4]));
				objectNumber[Integer.decode(s[1])] = Integer.decode(s[5]);
			}*/
			objectType[i] = noItem;
			objectNumber[i] = 0;
		}
		onMouseItem = noItem;
		onMouseNumber = 0;
	}
	
	public Item getItem(int index)
	{
		return objectType[index];
	}
	
	public int getNumber(int index)
	{
		return objectNumber[index];
	}
	
	public int getSelectedItemSlot()
	{
		return selectedItem;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getSelectedID()
	{
		return objectType[selectedItem].id;
	}
	
	public boolean isWeaponSelected()
	{
		return objectType[selectedItem].isWeapon();
	}
	
	public int getSelectedWeaponDamageAmount()
	{
		if(isWeaponSelected())
		{
			return objectType[selectedItem].damage;
		}
		return 0;
	}
	
	public boolean isEmptySlot(int ind)
	{
		if(ind >= 0 && ind < size)
		{
			if(objectType[ind] == noItem)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	public void dropItem(int ind)
	{
		dropItem(ind,1);
	}
	
	public void dropItem(int ind,int amount)
	{
		if(ind == onMouseItemID)
		{
			if(onMouseNumber >= amount)
			{
				WorldItem item = new WorldItem(owner.pos.x,owner.pos.y,owner.currentChunk,onMouseItem,amount,300);
				onMouseNumber -= amount;
				if(onMouseNumber == 0)
				{
					onMouseItem = noItem;
				}
				owner.currentChunk.itemList.add(item);
			}
			else
			{
				System.out.println("cannot drop more than number in inventory");
			}
		}
		else
		{
			if(objectNumber[ind] >= amount)
			{
				WorldItem item = new WorldItem(owner.pos.x,owner.pos.y,owner.currentChunk,objectType[ind],amount,300);
				objectNumber[ind] -= amount;
				if(objectNumber[ind] == 0)
				{
					objectType[ind] = noItem;
				}
				owner.currentChunk.itemList.add(item);
			}
			else
			{
				System.out.println("cannot drop more than number in inventory");
			}
		}
	}
	
	public void dropItemStack(int ind)
	{
		if(objectNumber[ind] >= 1)
		{
			WorldItem item = new WorldItem(owner.pos.x,owner.pos.y,owner.currentChunk,objectType[ind],objectNumber[ind]);
			objectNumber[ind] = 0;
			objectType[ind] = noItem;
			owner.currentChunk.itemList.add(item);
		}
	}
	
	public void changeSelectedItem()
	{
		selectedItem = Var.rend.changeValueWithWheel(selectedItem,-1);
		if(selectedItem < 0)
		{
			selectedItem = 0;
		}
		else if(selectedItem > 9)
		{
			selectedItem = 9;
		}
	}
	
	public Item useItem()
	{
		if(objectType[selectedItem].isPlacable())
		{
			if(objectNumber[selectedItem] >= 1)
			{
				objectNumber[selectedItem] --;
				if(objectNumber[selectedItem] == 0)
				{
					Item tmp = objectType[selectedItem];
					objectType[selectedItem] = noItem;
					return tmp;
				}
			}
			return objectType[selectedItem];
		}
		return noItem;
	}
	
	public boolean addItem(Item item,int amount)
	{
		//System.out.println("added " + amount + " of " + item + " to " + owner);
		if(item.isStackable())
		{
			for(int i = 0;i < size;i++)
			{
				if(objectType[i].id == item.id && objectType[i].subID == item.subID && objectNumber[i] < 999)
				{
					objectNumber[i] = objectNumber[i] + amount;
					return true;
				}
			}
		}
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == 0)
			{
				objectType[i] = item;
				objectNumber[i] = amount;
				return true;
			}
		}
		return false;
		//System.out.println("warning, inventory is full");
	}
	
	/*public void addMineral(int subID,float[] color,Ore ore)
	{
		MineralItem invItem = new MineralItem(subID,color,ore); 
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == invItem.id && objectType[i].subID == invItem.subID && objectNumber[i] < 999)
			{
				objectNumber[i]++;
				return;
			}
		}
		for(int i = 0;i < size;i++)
		{
			if(objectType[i].id == 0)
			{
				objectType[i] = invItem;
				objectNumber[i] = 1;
				return;
			}
		}
	}*/
	
	public void setMouseItemRightClick(Point p,Point pos)
	{
		int invSlot = getSlotFromPoint(p,pos);
		if(invSlot != -1)
		{
			if(onMouseItem != noItem && onMouseItem == objectType[invSlot])
			{
				if(objectNumber[invSlot] < 999)
				{
					onMouseNumber--;
					objectNumber[invSlot]++;
					if(onMouseNumber == 0)
					{
						onMouseItem = noItem;
					}
				}
			}
			else if(onMouseItem == noItem)
			{
				if(objectType[invSlot] != noItem)
				{
					onMouseItem = objectType[invSlot];
					onMouseNumber = objectNumber[invSlot]/2;
					objectNumber[invSlot] = objectNumber[invSlot] - onMouseNumber;
				}
			}
		}
	}
	
	public void setMouseItemRightClick(int invSlot)
	{
		if(onMouseItem != noItem && onMouseItem == objectType[invSlot])
		{
			if(objectNumber[invSlot] < 999)
			{
				onMouseNumber--;
				objectNumber[invSlot]++;
				if(onMouseNumber == 0)
				{
					onMouseItem = noItem;
				}
			}
		}
		else if(onMouseItem == noItem)
		{
			if(objectType[invSlot] != noItem)
			{
				onMouseItem = objectType[invSlot];
				onMouseNumber = objectNumber[invSlot]/2;
				objectNumber[invSlot] = objectNumber[invSlot] - onMouseNumber;
			}
		}
	}
	
	public void setMouseItem(Point p,Point pos)
	{
		int invSlot = getSlotFromPoint(p,pos);
		if(invSlot != -1)
		{
			if(onMouseItem != noItem && onMouseItem == objectType[invSlot])
			{
				objectNumber[invSlot] = objectNumber[invSlot] + onMouseNumber;
				if(objectNumber[invSlot] > 999)
				{
					onMouseNumber = objectNumber[invSlot] - 999;
					objectNumber[invSlot] = 999;
				}
				else
				{
					onMouseNumber = 0;
					onMouseItem = noItem;
				}
			}
			else
			{
				switchItem(invSlot);
			}
		}
		else if(invSlot == -1 && onMouseItem != noItem)
		{
			dropItem(onMouseItemID);
			onMouseItem = noItem;
			onMouseNumber = 0;
		}
	}
	
	public void setMouseItem(int invSlot)
	{
		if(onMouseItem != noItem && onMouseItem == objectType[invSlot])
		{
			objectNumber[invSlot] = objectNumber[invSlot] + onMouseNumber;
			if(objectNumber[invSlot] > 999)
			{
				onMouseNumber = objectNumber[invSlot] - 999;
				objectNumber[invSlot] = 999;
			}
			else
			{
				onMouseNumber = 0;
				onMouseItem = noItem;
			}
		}
		else
		{
			switchItem(invSlot);
		}
	}
	
	public void switchItem(int invSlot)
	{
		Item tmpItem = onMouseItem;
		int tmpNumber = onMouseNumber;
		onMouseItem = objectType[invSlot];
		onMouseNumber = objectNumber[invSlot];
		objectType[invSlot] = tmpItem;
		objectNumber[invSlot] = tmpNumber;
	}
	
	public int getSlotFromPoint(Point p,Point pos)
	{
		for(int j = 0;j < slotmax;j++)
		{
			Point p1 = new Point(pos.x - 141,270 + pos.y -(j * 35));
			for(int i = 0;i < 10;i++)
			{
				p1.moveCoords(35,0);
				Point p2 = p1.clone(30);
				if(p.x >= p1.x && p.x <= p2.x && p.y >= p1.y && p.y <= p2.y)
				{
					return j*10+i;
				}
			}
		}
		return -1;
	}
	
	public boolean draw(Point pos,int turned,boolean usingItemInHand)
	{
		for(int j = 0;j < slotmax;j++)
		{
			Point p1 = new Point(pos.x - 141,270 + pos.y -(j * 35));
			for(int i = 0;i < 10;i++)
			{
				p1.moveCoords(35,0);
				if(objectType[j*10+i] != noItem && objectType[j*10+i].tier != 0)
				{
					Var.rend.drawTier(p1.clone(1), p1.clone(29),objectType[j*10+i].tier);
				}
				if(j*10+i == selectedItem)
				{
					Var.rend.draw(p1, p1.clone(30), "red.png");
					if(objectType[j*10+i].id != 0)
					{
						usingItemInHand = objectType[j*10+i].drawHand(pos,turned,usingItemInHand);
					}
				}
				else
				{
					Var.rend.draw(p1, p1.clone(30), "case.png");
				}
				if(objectType[j*10+i].id != 0)
				{
					objectType[j*10+i].draw(p1.clone(0));
					drawNumber(objectNumber[j*10+i],p1);
				}
			}
		}
		if(onMouseItem.id != 0)
		{
			Point mPoint = Var.rend.getMousePos().clone(-15);
			onMouseItem.draw(mPoint);
			drawNumber(onMouseNumber,mPoint);
		}
		return usingItemInHand;
	}
	
	public void drawMouseItem()
	{
		if(onMouseItem.id != 0)
		{
			Point mPoint = Var.rend.getMousePos().clone(-15);
			onMouseItem.draw(mPoint);
			drawNumber(onMouseNumber,mPoint);
		}
	}
	
	public void drawNumber(int number,Point p1)
	{
		String s = "" + number;
		while(s.length() < 3)
		{
			s = " " + s;
		}
		Var.rend.drawText(p1.clone(2,2), p1.clone(18,18), s, "red");
	}
	
	public Item itemForSave(int ind)
	{
		return objectType[ind];
	}
	
	public int numberForSave(int ind)
	{
		return objectNumber[ind];
	}
	
	public float[] getMineralColor(int ind)
	{
		return ((MineralItem)objectType[ind]).color;
	}
}
