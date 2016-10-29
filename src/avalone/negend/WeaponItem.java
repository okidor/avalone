package avalone.negend;

import avalone.negend.global.Const;

public class WeaponItem extends Item
{
	public WeaponItem(int subID,int tier,int level,String texture)
	{
		super(Const.unplacableOffset + 1,subID,EnumItem.weapon,tier,level,texture);
	}
	
	public void setType()
	{
		type = 3;
	}
}
