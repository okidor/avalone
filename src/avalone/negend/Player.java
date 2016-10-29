package avalone.negend;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class Player extends AliveEntity
{
	public Point mouse;
	public int zoom;
	private boolean transiting;
	private boolean wasButtonDown;
	public int xp; public int level;
	private boolean usingItemInHand;
	private char currentKey;
	private AvaloneGLAPI glapi;
	private GUI gui;
	
	public Player(int posX,int posY,Chunk spawn,AvaloneGLAPI glapi)
	{
		super(posX,posY,spawn);
		this.glapi = glapi;
		vitY = 0;//dx = 0;
		tailleX = Const.tailleCase;
		tailleY = Const.tailleCase*2;
		nbJump = Const.totalJump;
		zoom = 1;
		mouse = new Point();
		wasButtonDown = false;
		health = 100;maxHealth = 100;
		xp = 0;level = 0;
		usingItemInHand = false;
		spawn.playerList.add(this);
		currentKey = 0;
		initGUI();
	}
	
	public void initGUI()
	{
		gui = new GUI(new PlayerInfo(this));
		GUIComponent healthBar = new GUIComponent(new Point(1080,580),new Point(1180,600),"health bar");
		GUIComponent nickname = new GUIComponent(new Point(600-Var.nickname.length()*3,340), new Point(600+Var.nickname.length()*3,356),"nickname");
		GUIInv guiInv = new GUIInv(inv,new Point(440,570),"inventory");
		healthBar.addBehavior(new Behavior()
		{
			@Override
			public void determineDraw(Point renderPosDownLeft,Point renderPosUpRight,String componentName,PlayerInfo infos)
			{
				Var.rend.drawLife(renderPosDownLeft,renderPosUpRight,infos.health,infos.maxHealth);
				//System.out.println(renderPosDownLeft.x + ", " +renderPosDownLeft.y + " -> " + renderPosUpRight.x + ", " + renderPosUpRight.y);
			}
			
			@Override
			public void reactionToClick(Point renderPosDownLeft, Point renderPosUpRight,Point mousePoint,String componentName,boolean leftClick)
			{
				System.out.println("clicked on " + componentName);
			}
		});
		nickname.addBehavior(new Behavior()
		{
			@Override
			public void determineDraw(Point renderPosDownLeft,Point renderPosUpRight,String componentName,PlayerInfo infos)
			{
				Var.rend.drawText(renderPosDownLeft, renderPosUpRight, Var.nickname, "red");
			}
			
			@Override
			public void reactionToClick(Point renderPosDownLeft, Point renderPosUpRight,Point mousePoint,String componentName,boolean leftClick)
			{
				System.out.println("clicked on " + componentName);
			}
		});
		gui.addComponent(healthBar);
		gui.addComponent(nickname);
		gui.addComponent(guiInv);
	}
	
	public void draw()
	{
		Var.rend.draw(pos, pos.clone(tailleX,tailleY), "player" + turned + ".png");
		//usingItemInHand = inv.draw(pos,turned,usingItemInHand);
		//rend.drawText(pos.clone(0,100), pos.clone(132,+132), "123456","white");
		//Var.rend.drawLife(pos,health,maxHealth);
		//Var.rend.drawText(pos.clone(-Var.nickname.length()*3,40), pos.clone(Var.nickname.length()*3,56), Var.nickname, "red");
		gui.draw();
		//InfoMouse.draw(this);
	}
	
	private boolean keyPressedOnce(char key)
	{
		return glapi.isKeyDown(key) && currentKey != key;
	}
	
	public void movements()
	{
		if(usingItemInHand)
		{
			currentChunk.getDamagedMob(this);
		}
		turned = 1;
		if(keyPressedOnce('p'))
		{
			zoom = zoom + 1;
 			glapi.zoom(zoom);
		}
		if(keyPressedOnce('m'))
		{
			zoom = 1;
 			glapi.clearZoom();
		}
		if(keyPressedOnce('i'))
		{
			inv.slotmax = 4 - inv.slotmax;
		}
		if(keyPressedOnce(' '))
		{
			jump();
		}
		currentKey = glapi.lastPressedKey();
		char latestKey = glapi.lastPressedKeyBetween('q','d','←','→');
		/*if(latestKey != 0)
		{
			System.out.println(latestKey);
		}*/
		if(latestKey == 'q' || latestKey == '←')
		{
			pos.x = pos.x - Const.depl;
			turned = 0;
			Physic.checkCollisionFromLeft(this);
		}
		if(latestKey == 'd' || latestKey == '→')
		{
			pos.x = pos.x + Const.depl;
			turned = 2;
			Physic.checkCollisionFromRight(this);
		}
		//System.out.println("left click: " + glapi.hasLeftClicked());
		if (glapi.hasLeftClicked())
		{
			if(!wasButtonDown)
			{
				wasButtonDown = true;
				//inv.setMouseItem(glapi.getMouse(),pos);
				System.out.println("player pos: " + pos.x + ", " + pos.y);
				gui.clickAction(true);
			}
			worldClickActions(false);
		}
		else if(glapi.hasRightClicked())
		{
			if(!wasButtonDown)
			{
				wasButtonDown = true;
				//inv.setMouseItemRightClick(glapi.getMouse(),pos);
				gui.clickAction(false);
			}
			if(inv.getSelectedID() != 0)
			{
				worldClickActions(true);
			}
		}
		else if(wasButtonDown && !glapi.hasLeftClicked() && !glapi.hasRightClicked())
		{
			wasButtonDown = false;
		}
		else
		{
			inv.changeSelectedItem();
		}
	}
	
	public void moveAndScroll(int move)
	{
		pos.x = pos.x + move;
		glapi.scroll(move,0);
	}
	
	public void worldClickActions(boolean add)
	{
		mouse = glapi.getMouse();
		if(inv.isWeaponSelected())
		{
			usingItemInHand = true;
		}
		else
		{
			boolean left = false;boolean right = false;boolean current = false;
			if(mouse.x < 0)
			{
				left = true;
			}
			else if(mouse.x/tailleCase >= Const.tailleChunkX)
			{
				right = true;
			}
			else
			{
				current = true;
			}
			yVerif(left,right,current,add);
		}
	}
	
	public void yVerif(boolean left,boolean right,boolean current,boolean add)
	{
		if(mouse.y < 0)
		{
			if(current)
			{
				cAround[7].choose(mouse.x/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[6].choose((mouse.x+1200)/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[8].choose((mouse.x-1200)/tailleCase,(mouse.y+608)/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
		else if(mouse.y/tailleCase >= Const.tailleChunkY)
		{
			if(current)
			{
				cAround[1].choose(mouse.x/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[0].choose((mouse.x+1200)/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[2].choose((mouse.x-1200)/tailleCase,(mouse.y-608)/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
		else
		{
			if(current)
			{
				currentChunk.choose(mouse.x/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else if(left)
			{
				cAround[3].choose((mouse.x+1200)/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else if(right)
			{
				cAround[5].choose((mouse.x-1200)/tailleCase,mouse.y/tailleCase,inv,add);
			}
			else
			{
				System.out.println("error when verifying height");
			}
		}
	}
	
	public void respawn()
	{
		health = maxHealth;
		//currentChunk = currentChunk.map.getCustomList().get(0,0);
		nbJump = Const.totalJump;
		zoom = 1;
		Point p = currentChunk.genSpawnCoords();
		currentChunk.map.setChunkAround(this);
		if(currentCaseLeft().getBlockSolidity().equals("solid") || currentCaseRight().getBlockSolidity().equals("solid"))
		{
			pos.y = pos.y + Const.tailleCase;
		}
		pos = p.clone(0);
		changeChunk(currentChunk.map.getCustomList().get(0,0),-1);
		//int centerX = Const.tailleFenX/2;
		//int centerY = Const.tailleFenY/2;
		//glapi.setView(pos.x-centerX,pos.y-centerY);
	}
	
	public void onDeath()
	{
		//currentChunk.playerList.remove(this);
		respawn();
	}
			
	public void changeChunk(Chunk newC,int n)
	{
		Const.debug("(Player:changeChunk): oldChunkX = " + currentChunk.pos.x);
		Const.debug("(Player:changeChunk): oldChunkY = " + currentChunk.pos.y);
		currentChunk.playerList.remove(this);
		super.changeChunk(newC,n);
		currentChunk.playerList.add(this);
		Const.debug("(Player:changeChunk): newChunkX = " + currentChunk.pos.x);
		Const.debug("(Player:changeChunk): newChunkY = " + currentChunk.pos.y);
		if(newC instanceof SurfaceChunk)
		{
			Const.debug("(Player:changeChunk): biome id = " +((SurfaceChunk) newC).biome);
		}
		int centerX = Const.tailleFenX/2;
		int centerY = Const.tailleFenY/2;
		if(n == 1)
		{
			//glapi.scroll((Const.tailleChunkX-1)*tailleCase+1,0);
			glapi.setView(centerX-Const.tailleCase,pos.y-centerY);
		}
		else if(n == 2)
		{
			//glapi.scroll(-(Const.tailleChunkX-1)*tailleCase-1,0);
			glapi.setView(-centerX,pos.y-centerY);
		}
		else if(n == 3)
		{
			glapi.setView(pos.x-centerX, centerY-Const.tailleCase);
		}
		else if(n == 4)
		{
			glapi.setView(pos.x-centerX, -centerY);
		}
		else if(n == -1)
		{
			glapi.setView(pos.x-centerX,pos.y-centerY);
		}
	}

	@Override
	public int chooseInventorySize() 
	{
		return 30;
	}

}
