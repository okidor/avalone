package avalone.ignis;

public enum Element 
{
	Empty(true,true,null,0),
	Fire(true,true,"fire.png",0),
	Water(true,true,"water.png",0),
	Earth(false,false,"earth.png",9),
	Wind(false,true,"wind.png",12);
	
	public final boolean canBePushedByWind;
	public final boolean canBePushedByEarth;
	public final String texture;
	public int amount;
	
	private Element(boolean canBePushedByWind,boolean canBePushedByEarth,String texture,int amount)
	{
		this.canBePushedByWind = canBePushedByWind;
		this.canBePushedByEarth = canBePushedByEarth;
		this.texture = texture;
		this.amount = amount;
	}
}
