package avalone.graphFighters;

import avalone.api.lwjgl3.AvaloneGLAPI;

public class Player extends Fighter
{
	public Player(int x, int y) 
	{
		super(x, y);
	}

	@Override
	public void input() 
	{
		char latestKey = AvaloneGLAPI.getInstance().lastPressedKeyBetween('q','d','\u2190','\u2192',' ');
		if(latestKey == 'q' || latestKey == '\u2190')
		{
			pos.x = pos.x - 5;
		}
		if(latestKey == 'd' || latestKey == '\u2192')
		{
			pos.x = pos.x + 5;
		}
		if(latestKey == ' ')
		{
			//jump();
		}
	}
}
