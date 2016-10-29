package avalone.negend;

import avalone.api.util.Point;

public class PlayerInfo 
{
	public final Point pos;
	public final int health;
	public final int maxHealth;
	public final int tailleX;
	public final int tailleY;
	
	public PlayerInfo(Player play)
	{
		pos = play.pos;
		health = play.health;
		maxHealth = play.maxHealth;
		tailleX = play.tailleX;
		tailleY = play.tailleY;
	}
}
