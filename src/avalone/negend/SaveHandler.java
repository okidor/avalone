package avalone.negend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.CustomIndex2DList;
import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class SaveHandler 
{
	private CustomIndex2DList<Chunk> cl;
	//private ArrayList<Point> loaded;
	private int[] sizes;
	private BufferedWriter out;
	//private PrintWriter out;
	private BufferedReader in;
	private Map map;
	private int mode;
	private Player play;
	private AvaloneGLAPI glapi;
	
	public SaveHandler(Map map,AvaloneGLAPI glapi)
	{
		this.map = map;
		mode = 0;
		//loaded = new ArrayList<Point>();
		this.glapi = glapi;
	}
	
	public void save(CustomIndex2DList<Chunk> cl)
	{
		this.cl = cl;
		sizes = cl.size();
		File f = FileUtils.createFile("save" + File.separator + Var.gameName);
		FileUtils.addCreatedGameIfNotExists(Var.gameName);
		//System.out.println("created file : " + f.getAbsolutePath());
		try 
		{
			//out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			//out = new BufferedWriter(new FileWriter(f),32768);
			GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(f));
	        out = new BufferedWriter(new OutputStreamWriter(zip, "UTF-8"));
			//out.write("test\n");
			out.write("#ores:\n");
			double startTime = glapi.getTime();
			saveOres();
			double time = glapi.getTime();
			double result = time - startTime;
			Const.debug("saved ores in " + result + " ms");
			startTime = glapi.getTime();
			saveUpLeft();
			saveUpRight();
			saveDownLeft();
			saveDownRight();
			time = glapi.getTime();
			result = time - startTime;
			Const.debug("saved chunks in " + result + " ms");
			out.write("#player:\n");
			savePlayer();
			out.write("\n");
			out.write("#time:\n");
			out.write(map.time + " " + map.indice + "\n");
			out.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public CustomIndex2DList<Chunk> load()
	{
		cl = map.getCustomList();
		try 
		{
			File f = new File("save" + File.separator + Var.gameName);
			//System.out.println("reading file : " + f.getAbsolutePath());
			//in = new BufferedReader(new FileReader(gameName));
			GZIPInputStream zip = new GZIPInputStream(new FileInputStream(f));
			in = new BufferedReader(new InputStreamReader(zip, "UTF-8"));
			loadParts();
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return cl;
	}
	
	public String check(String readLine)
	{
		if(readLine.startsWith(" "))
		{
			return check(readLine.substring(1));
		}
		return readLine;
	}
	
	public void savePlayer() throws IOException
	{
		Player play = map.play;
		out.write(play.pos.x + " " + play.pos.y + " " + play.currentChunk.pos.x + " " + play.currentChunk.pos.y + "\n");
		Inventory inv = play.inv;
		out.write("#items:\n");
		for(int i = 0;i < inv.getSize();i++)
		{
			out.write(inv.itemForSave(i).id + " " + inv.itemForSave(i).subID + " " + inv.itemForSave(i).texture + " " + inv.itemForSave(i).level + " " + inv.itemForSave(i).tier + " " + inv.itemForSave(i).type + " ");
			if(inv.itemForSave(i).type == 2)
			{
				float[] tmp = inv.getMineralColor(i);
				for(int j = 0; j < 4;j++)
				{
					out.write(tmp[j] + " ");
				}
				out.write(((MineralItem)inv.itemForSave(i)).getOre().id);
			}
			out.write(inv.numberForSave(i) + "\n");
		}
	}
	
	public Player getLoadedPlayer()
	{
		return play;
	}
	
	private void loadParts() throws IOException
	{
		double startTime = glapi.getTime();
		String[] readLine = new String[2];int i = 0;String[][] s = new String[2][0]; 
		while((readLine[i] = in.readLine()) != null)
		{
			readLine[i] = check(readLine[i]);
			if(readLine[i].length() > 0)
			{
				if(!readLine[i].startsWith("//") && !readLine[i].startsWith("#"))
				{
					s[i] = readLine[i].split("\\s+");
					if(mode == 1)
					{
						loadPlayerItem(s[i],i);
					}
					else if(mode == 2)
					{
						if(i == 1)
						{
							//System.out.println("load parts, line content: " + readLine[i]);
							Chunk c = loadChunk(s);
							loadChunkBlocks(c,s[1]);
						}
						i = 1 - i;
					}
					else if(mode == 3)
					{
						Const.debug("(SaveHandler:loadParts): " + cl.get(Integer.decode(s[i][2]),Integer.decode(s[i][3])));
						play = new Player(Integer.decode(s[i][0]),Integer.decode(s[i][1]),cl.get(Integer.decode(s[i][2]),Integer.decode(s[i][3])),glapi);
					}
					else if(mode == 4)
					{
						map.ores = new Ore[Integer.decode(s[i][0])];
						mode = 5;
					}
					else if(mode == 5)
					{
						int id = Integer.decode(s[i][0]);
						String name = s[i][1];
						int maxVeinSize = Integer.decode(s[i][2]);
						int miningLevel = Integer.decode(s[i][3]);
						int hardness = Integer.decode(s[i][4]);
						int globalRarity = Integer.decode(s[i][5]);
						int veinRarity = Integer.decode(s[i][6]);
						float[] color = new float[4];
						for(int j = 0; j < 4; j++)
						{
							color[j] = Float.valueOf(s[i][7 + j]);
						}
						map.ores[id] = new Ore(id,name,miningLevel,hardness,globalRarity,maxVeinSize,veinRarity,color);
						System.out.println("loading ore: " + name);
					}
					else if(mode == 6)
					{
						map.time = Integer.decode(s[i][0]);
						map.indice = Integer.decode(s[i][1]);
					}
				}
				else if(readLine[i].startsWith("#"))
				{
					s[i] = readLine[i].split("\\s+");
					if(s[i][0].equals("#items:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading items");
						i = 0;
						mode = 1;
					}
					else if(s[i][0].equals("#chunks:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading chunks");
						i = 0;
						mode = 2;
					}
					else if(s[i][0].equals("#player:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading player");
						i = 0;
						mode = 3;
					}
					else if(s[i][0].equals("#ores:"))
					{
						Const.debug("(SaveHandler:loadParts): now loading ores");
						i = 0;
						mode = 4;
					}
					else if(s[i][0].equals("#time:"))
					{
						i = 0;
						mode = 6;
					}
				}
			}
		}
		double time = glapi.getTime();
		double result = time - startTime;
		Const.debug("loaded in " + result + " ms");
	}
	
	private void loadPlayerItem(String[] s,int i)
	{
		Inventory inv = play.inv;
		Item item;
		if(Integer.decode(s[5]) == 1)
		{
			item = new ItemBlock(Integer.decode(s[0]),Integer.decode(s[1]));
		}
		else if(Integer.decode(s[5]) == 2)
		{
			float[] f = new float[4];
			f[0] = Float.valueOf(s[6]);
			f[1] = Float.valueOf(s[7]);
			f[2] = Float.valueOf(s[8]);
			f[3] = Float.valueOf(s[9]);
			item = new MineralItem(Integer.decode(s[1]),f,map.ores[Integer.decode(s[10])]);
		}
		else if(Integer.decode(s[5]) == 3)
		{
			item = new WeaponItem(Integer.decode(s[1]),Integer.decode(s[4]),Integer.decode(s[3]),s[2]);
		}
		else
		{
			item = new Item(Integer.decode(s[0]),Integer.decode(s[1]),EnumItem.general,Integer.decode(s[4]),Integer.decode(s[3]),s[2]);
		}
		if(Integer.decode(s[5]) == 2)
		{
			inv.addItem(item,Integer.decode(s[10]));
		}
		else
		{
			inv.addItem(item,Integer.decode(s[6]));
		}
	}
	
	private Chunk loadChunk(String[][] s)
	{
		Point p1 = new Point(Integer.decode(s[0][0]),Integer.decode(s[0][1]));
		//loaded.add(p1);
		Chunk c;
		//Const.debug("(SaveHandler:loadChunk): p1:" + p1.x + ", " + p1.y);
		//System.out.print(Integer.decode(s[0][0]) + ", " + Integer.decode(s[0][1]) + ": ");
		if(p1.x == 0 && p1.y == 0)
		{
			//Const.debug("(SaveHandler:loadChunk): spawn will be loaded");
			c = new SpawnChunk(map,p1,true,false);
		}
		else if(p1.y < 0)
		{				
			//Const.debug("(SaveHandler:loadChunk): underground will be loaded");
			c = new UndergroundChunk(map,p1,true);
		}
		else if(p1.y > 0)
		{
			//Const.debug("(SaveHandler:loadChunk): sky will be loaded");
			c = new SkyChunk(map,p1,true);
		}
		else
		{
			//Const.debug("(SaveHandler:loadChunk): surface will be loaded");
			c = new SurfaceChunk(map,p1,true,false);
			((SurfaceChunk) c).biome = Integer.decode(s[0][2]);
			//((SurfaceChunk) c).sideBiome = Integer.decode(s[0][3]);
			if(p1.x == 0)
			{
				System.err.println("error while loading chunk, wrong selection");
				System.exit(1);
			}
		}
		cl.add(c.pos.x, c.pos.y, c);
		return c;
	}
	
	private void loadChunkBlocks(Chunk c,String[] parameters)
	{
		int k = 0;
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				/*if(parameters[k].equals("n"))				//l_anchor
				{
					c.cases[i][j].setBlock(Block.air);
					c.cases[i][j].subID = 0;
					loadOre(c.cases[i][j],"-1");
					c.behind[i][j].setBlock(Block.air);
					c.behind[i][j].subID = 0;
					loadOre(c.behind[i][j],"-1");
					k++;
				}
				else
				{*/
					int id = Integer.decode(parameters[k]);
					c.cases[i][j].setBlock(Block.getBlock(id));
					k++;
					c.cases[i][j].subID = Integer.decode(parameters[k]);
					k++;
					loadOre(c.cases[i][j],parameters[k]);
					k++;
					id = Integer.decode(parameters[k]);
					c.behind[i][j].setBlock(Block.getBlock(id));
					k++;
					c.behind[i][j].subID = Integer.decode(parameters[k]);
					k++;
					loadOre(c.behind[i][j],parameters[k]);
					k++;
				//}
			}
		}
	}
	
	private void loadOre(Tile tile,String parameter_k)
	{
		int oreid = Integer.decode(parameter_k);
		if(oreid == -1)
		{
			tile.ore = new Ore();
		}
		else
		{
			tile.ore = map.ores[oreid];
		}
	}
	
	private void saveOres() throws IOException
	{
		out.write(map.ores.length + "\n");
		for(int i = 0;i < map.ores.length;i++)
		{
			Ore ore = map.ores[i];
			out.write(ore.id + " " + ore.name + " " + ore.maxVeinSize + " " + ore.miningLevel + " ");
			out.write(ore.hardness + " " + ore.globalRarity + " " + ore.veinRarity);
			for(int j = 0; j < ore.color.length; j++)
			{
				out.write(" " + String.valueOf(ore.color[j]));
			}
			out.write("\n");
		}
	}
	
	private void saveUpLeft() throws IOException
	{
		out.write("#chunks:\n");
		out.write("#up left\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(-i-1,j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveUpRight() throws IOException
	{
		out.write("#up right\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(i,j);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveDownLeft() throws IOException
	{
		out.write("#down left\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(-i-1,-j-1);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveDownRight() throws IOException
	{
		out.write("#down right\n");
		for(int i = 0;i < sizes[0];i++)
		{
			boolean continuer = true;
			int j = 0;
			while(continuer)
			{
				Chunk c = cl.get(i,-j-1);
				j++;
				if(c == null)
				{
					continuer = false;
				}
				else
				{
					saveChunk(c);
				}
			}
		}
	}
	
	private void saveChunk(Chunk c) throws IOException
	{
		out.write(c.pos.x + " " + c.pos.y + " ");
		if(c instanceof SurfaceChunk && !(c instanceof SpawnChunk))
		{
			out.write(((SurfaceChunk) c).biome + " " + ((SurfaceChunk) c).sideBiome);
		}
		out.write("\n");
		for(int i = 0;i < Const.tailleChunkX;i++)
		{
			for(int j = 0;j < Const.tailleChunkY;j++)
			{
				/*if(c.cases[i][j].getBlockID() == Block.air.blockID && c.cases[i][j].subID == 0 && c.cases[i][j].ore.id == -1)
				{
					if(c.behind[i][j].getBlockID() == Block.air.blockID && c.behind[i][j].subID == 0 && c.behind[i][j].ore.id == -1)
					{
						out.write("n ");			//s_anchor
					}
				}
				else
				{*/
					out.write(c.cases[i][j].getBlockID() + " " + c.cases[i][j].subID + " " + c.cases[i][j].ore.id + " ");
					out.write(c.behind[i][j].getBlockID() + " " + c.behind[i][j].subID + " " + c.behind[i][j].ore.id + " ");
				//}
			}
		}
		out.write("\n");
	}
}
