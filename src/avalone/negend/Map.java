package avalone.negend;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.CustomIndex2DList;
import avalone.api.util.Point;
//import avalone.api.util.SoundSystem;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

import java.io.File;

public class Map 
{
	private CustomIndex2DList<Chunk> cl;
	//private HashSet<Entity> globalEntityList;
	private Point origin;
	private Point end;
	public Player play;
	public Ore[] ores;
	private AvaloneGLAPI glapi;
	private GameFile game;
	private boolean setLight = true;
	private Point center;
	public int time;
	private String[] sky = {"sky.png","sky_night.png"};
	public int indice;
	private int nextSpawn;
	
	public static boolean allowedToDraw = true;

	public Map(int tailleX,int tailleY,AvaloneGLAPI glapi,Ore[] ores)
	{
		initConfig();
		center = new Point(Const.tailleChunkX*Const.tailleCase/2,Const.tailleChunkY*Const.tailleCase/2);
		cl = new CustomIndex2DList<Chunk>();
		//globalEntityList = new HashSet<Entity>();
		origin = new Point(0,0);
		end = new Point(tailleX,tailleY);
		this.ores = ores;
		this.glapi = glapi;
		indice = 0;nextSpawn = 0;
		if(Var.newGame)
		{
			newGame();
		}
		else
		{
			SaveHandler save = new SaveHandler(this,glapi);
			cl = save.load();
			SpawnChunk spawnPoint = (SpawnChunk) cl.get(0,0);
			play = save.getLoadedPlayer();
			firstScroll(play);
			//play.currentChunk.playerList.add(play);
		}
		System.out.println("time = " + time);
	}
	
	public void newGame()
	{
		time = 0;
		SpawnChunk spawnPoint = new SpawnChunk(this,new Point(),setLight,true);
		generate(spawnPoint);
		spawnPlayer(spawnPoint);
		spawnPoint.spawnFirstChest();
	}
	
	public void initConfig()
	{
		game = new GameFile(Const.configPath);
		String s[] = game.al.get(0);
		setLight = Boolean.valueOf(s[2]);
		String nick[] = game.al.get(1);
		Var.nickname = nick[2];
	}
	
	public void generate(Chunk c)
	{
		cl.add(c.pos.x, c.pos.y, c);
		c.generate(ores);
	}
	
	public void actions()
	{
		dayNight();
		setChunkAround(play);
		spawnAroundPlayerChunk(play);
		//checkEntities();
		play.live();
		Var.rend.centerView(play.pos);
		draw();
		//System.out.println("player drawn at chunk coords: x = " + play.currentChunk.pos.x + " and y = " + play.currentChunk.pos.y);
	}
	
	public void setChunkAround(Entity ent)
	{
		ent.cAround[0] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y+1);
		ent.cAround[1] = checkChunk(ent.currentChunk.pos.x, ent.currentChunk.pos.y+1);
		ent.cAround[2] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y+1);
		ent.cAround[3] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y);
		ent.cAround[4] = ent.currentChunk;
		ent.cAround[5] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y);
		ent.cAround[6] = checkChunk(ent.currentChunk.pos.x-1, ent.currentChunk.pos.y-1);
		ent.cAround[7] = checkChunk(ent.currentChunk.pos.x, ent.currentChunk.pos.y-1);
		ent.cAround[8] = checkChunk(ent.currentChunk.pos.x+1, ent.currentChunk.pos.y-1);
	}
	
	public void setChunkAround(Chunk center)
	{
		center.chunkBuffer[0] = checkChunk(center.pos.x-1, center.pos.y+1);
		center.chunkBuffer[1] = checkChunk(center.pos.x, center.pos.y+1);
		center.chunkBuffer[2] = checkChunk(center.pos.x+1, center.pos.y+1);
		center.chunkBuffer[3] = checkChunk(center.pos.x-1, center.pos.y);
		center.chunkBuffer[5] = checkChunk(center.pos.x+1, center.pos.y);
		center.chunkBuffer[6] = checkChunk(center.pos.x-1, center.pos.y-1);
		center.chunkBuffer[7] = checkChunk(center.pos.x, center.pos.y-1);
		center.chunkBuffer[8] = checkChunk(center.pos.x+1, center.pos.y-1);
	}
	
	/*private void checkEntities()
	{
		
		Iterator<Entity> it = globalEntityList.iterator();
		while (it.hasNext()) 
		{
			Entity tmp = it.next();
			if(tmp.updateChunk)
			{
				setChunkAround(tmp);
				tmp.updateChunk = false;
			}
			if(tmp.isDestroyed())
			{
				globalEntityList.remove(tmp);
				Const.debug("(Map:checkEntities): entity removed");
			}
		}
	}*/
	
	public void spawnMob(Chunk c)
	{
		Point p = c.genSpawnCoords();
		if(p.y != -1)
		{
			Mob mob1 = new Mob(p.x,p.y,c,play);
			setChunkAround(mob1);
			//globalEntityList.add(mob1);
			c.mobList.add(mob1);
		}
	}
	
	public void attemptGroupSpawn(Chunk c)
	{
		int a = Var.rand.nextInt(16);
		int sqrt = 1 + (int)Math.sqrt(a);
		//System.out.println(a + " -> " + sqrt);
		//System.out.print(sqrt);
		sqrt = 5 - sqrt;
		//System.out.println(" -> " + sqrt);
		
		if(sqrt + c.mobList.size() <= 4)
		{
			for(int i = 0; i <= sqrt;i++)
			{
				spawnMob(c);
			}
		}
	}
	
	public void spawnAroundPlayerChunk(Player play)
	{
		int a = Var.rand.nextInt(2);
		if(a == 0)
		{
			randomlySpawnMob(play.cAround[3]);
		}
		else
		{
			randomlySpawnMob(play.cAround[5]);
		}
	}
	
	public void randomlySpawnMob(Chunk c)
	{
		if(sky[indice].equals("sky_night.png") && c.mobList.size() < 5)
		{
			nextSpawn = nextSpawn + Var.rand.nextInt(60);
			if(nextSpawn >= 10800)
			{
				attemptGroupSpawn(c);
				nextSpawn = 0;
			}
		}
	}
	
	public void spawnPlayer(Chunk spawnPoint)
	{
		Point p = spawnPoint.genSpawnCoords();
		play = new Player(p.x,p.y,spawnPoint,glapi);
		setChunkAround(play);
		if(play.currentCaseLeft().getBlockSolidity().equals("solid") || play.currentCaseRight().getBlockSolidity().equals("solid"))
		{
			play.pos.y = play.pos.y + Const.tailleCase;
		}
		play.inv.addItem(new WeaponItem(/*0,*/0,10,10,"epee.png"), 1);
		play.inv.addItem(new WeaponItem(/*0,*/0,2000,10,"epee.png"), 1);
		play.inv.addItem(new WeaponItem(/*0,*/0,4500,10,"epee.png"), 1);
		play.inv.addItem(new WeaponItem(/*0,*/0,6000,10,"epee.png"), 1);
		play.inv.addItem(new WeaponItem(/*0,*/0,8500,10,"epee.png"), 1);
		firstScroll(play);
		
		//globalEntityList.add(play);
		//spawnPoint.playerList.add(play);
	}
	
	public void firstScroll(Player play)
	{
		int centerX = Const.tailleFenX/2;
		int centerY = Const.tailleFenY/2;
		Point pos = play.pos;
		glapi.scroll(pos.x-centerX,pos.y-centerY);
	}
	
	public void dayNight()
	{
		time++;
		if(time > 20000)
		{
			time = 0;
			indice = (indice + 1) % sky.length;
		}
	}
	
	public Chunk checkChunk(int posX,int posY)
	{
		return checkChunk(posX,posY,false);
	}
	
	public Chunk checkChunk(int posX,int posY,boolean debug)
	{
		if(debug)
		{
			Const.debug("(Map:checkChunk): pos = " + posX + ", " + posY);
			Const.debug("(Map:checkChunk): " + cl.get(posX,posY));
		}
		Chunk cFound = cl.get(posX,posY);
		if(cFound == null)
		{
			Chunk c;
			if(posY < 0)
			{				
				c = new UndergroundChunk(this,new Point(posX,posY),setLight);
				//System.out.println("new underGroundchunk: posX = " + posX + ", posY " + posY);
				//c.chunkBuffer[1] = checkChunk(posX,posY+1);
			}
			else if(posY > 0)
			{
				c = new SkyChunk(this,new Point(posX,posY),setLight);
				//c.chunkBuffer[7] = checkChunk(posX,posY-1);
			}
			else
			{
				if(posX > 0)
				{
					c = new SurfaceChunk(this,new Point(posX,posY),setLight,true);
					//c.chunkBuffer[3] = checkChunk(posX-1,posY);
				}
				else if(posX < 0)
				{
					c = new SurfaceChunk(this,new Point(posX,posY),setLight,true);
					//c.chunkBuffer[5] = checkChunk(posX+1,posY);
				}
				else
				{
					c = new SurfaceChunk(this,new Point(0,0),setLight,true);
					System.err.println("error while getting chunk,spawn point should exist");
					System.exit(1);
				}
			}
			generate(c);
			return c;
		}
		else
		{
			return cFound;
		}
	}
	
	public void draw()
	{
		int left = -1;
		int right = 1;
		int up = -1;
		int down = 1;
		if(play.pos.x > Const.tailleFenX/2 + Const.tailleCase*Const.maxLight - 1)
		{
			left = 0;
		}
		else if(play.pos.x < Const.tailleFenX/2 - Const.tailleCase*Const.maxLight - 1)
		{
			right = 0;
		}
		if(play.pos.y > Const.tailleFenY/2 + Const.tailleCase*Const.maxLight - 1)
		{
			down = 0;
		}
		else if(play.pos.y < Const.tailleFenY/2 - Const.tailleCase*Const.maxLight - 1)
		{
			up = 0;
		}
		origin.setCoords(play.pos.x-center.x, play.pos.y-center.y);
		end.setCoords(play.pos.x+center.x, play.pos.y+center.y);
		Var.rend.draw(origin, end, sky[indice]);
		for(int i = left;i <= right;i++)
		{
			for(int j = up;j <= down;j++)
			{
				Var.rend.setOffset(i*1200,(j*(-608)));
				setChunkAround(play.cAround[(i+1)+(j+1)*3]);
				Physic.flow(play.cAround[(i+1)+(j+1)*3]);
				play.cAround[(i+1)+(j+1)*3].draw();
			}
		}
		Var.rend.setOffset(0, 0);
		//play.draw(rend);
	}
	
	public void save()
	{
		SaveHandler save = new SaveHandler(this,glapi);
		save.save(cl);
	}
	
	public CustomIndex2DList<Chunk> getCustomList()
	{
		return cl;
	}
}
