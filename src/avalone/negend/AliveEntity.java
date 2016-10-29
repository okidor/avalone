package avalone.negend;

public abstract class AliveEntity extends Entity
{
	public Inventory inv;
	public int health; public int maxHealth;
	protected boolean damaged;
	protected int damageCounter;
	protected int knockback;
	protected int antiJumpSpamm;
	protected boolean lastJump;
	private int invSize;
	
	protected AliveEntity(int posX, int posY, Chunk spawn) 
	{
		super(posX, posY, spawn);
		damaged = false;
		damageCounter = 90;
		knockback = 0;
		antiJumpSpamm = 0;
		lastJump = true;
		invSize = chooseInventorySize();
		inv = new Inventory(this,invSize);
	}
	
	public abstract int chooseInventorySize();
	
	public abstract void draw();
	
	public void takeDamage(AliveEntity ent,int amount,int knockback)
	{
		if(damageCounter <= 30)
		{
			if(health > 0)
			{
				System.out.println("damaged " + health + "/" + maxHealth);
				damaged = true;
				damageCounter = 90;
				health = health - amount;
				this.knockback = knockback;
				Integer i = tagTable.get(ent);
				if(i == null)
				{
					tagTable.put(ent,amount);
				}
				else
				{
					tagTable.put(ent, tagTable.get(ent) + amount);
				}
			}
		}
	}
	
	protected void jump()
	{
		if(nbJump > 0)
    	{
			if(antiJumpSpamm == 0)
			{
				antiJumpSpamm = 10;
				vitY = vitY + 10;
				nbJump--;
			}
    	}
	}
	
	public void live()
	{
		Physic.verifyGrav(this);
		if(antiJumpSpamm > 0)
		{
			antiJumpSpamm--;
		}
		Physic.applyKnockback(this);
		if(health <= 0)
		{
			death();
		}
		else
		{
			movements();
		}
		if(damageCounter > 0)
		{
			damageCounter--;
		}
		else if(damageCounter == 0)
		{
			damaged = false;
		}
	}
	
	public void death()
	{
		if(damageCounter > 0 && lastJump)
		{
			for(int i = 0; i < invSize;i++)
			{
				inv.dropItemStack(i);
			}
			jump();
			antiJumpSpamm = 0;
			jump();
			antiJumpSpamm = 0;
			jump();
			antiJumpSpamm = 0;
			jump();
			lastJump = false;
			layer = -1;
		}
		else if(damageCounter == 0)
		{
			onDeath();
		}
	}
	
	public abstract void onDeath();
}
