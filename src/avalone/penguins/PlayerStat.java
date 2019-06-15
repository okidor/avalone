package avalone.penguins;

public class PlayerStat 
{
	private int number;
	private int nbPenguins;
	private int score;
	private boolean hasFinished;
	
	public PlayerStat(int number, int nbPenguins)
	{
		this.number = number;
		this.nbPenguins = nbPenguins;
		score = 0;
		hasFinished = false;
	}
	
	private PlayerStat(PlayerStat playerStat)
	{
		number = playerStat.number;
		nbPenguins = playerStat.nbPenguins;
		score = playerStat.score;
		hasFinished = playerStat.hasFinished;
	}
	
	public PlayerStat clone()
	{
		return new PlayerStat(this);
	}
	
	public void decreasePenguin(Tile tile)
	{
		nbPenguins--;
		score += tile.value;
	}
	
	public void checkEnd()
	{
		if(nbPenguins == 0)
		{
			hasFinished = true;
		}
	}
	
	public void addScore(int value)
	{
		score += value;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public boolean getFinished()
	{
		return hasFinished;
	}
	
	public int getNbPenguins()
	{
		return nbPenguins;
	}
}
