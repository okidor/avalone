package avalone.negend;

import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class Ore
{
	public ArrayList<String> voyelles = new ArrayList<String>();
	public ArrayList<String> consonnes = new ArrayList<String>();
	public ArrayList<String> banned = new ArrayList<String>();
	
	public String name;
	public int id;
	public int miningLevel;
	public int hardness;
	public int globalRarity;//influe les deux valeurs suivantes
	public int maxVeinSize;
	public int veinRarity;
	public float[] color;
	private AvaloneGLAPI glapi;
	
	public Ore(int id,AvaloneGLAPI glapi)
	{
		this.id = id;
		this.glapi = glapi;
		initLists();
		createName();
		createColor();
		createStats();
	}
	
	public Ore(int id,String name,int miningLevel,int hardness,int globalRarity,int maxVeinSize,int veinRarity,float[] color)
	{
		this.id = id;
		this.name = name;
		this.miningLevel = miningLevel;
		this.hardness = hardness;
		this.globalRarity = globalRarity;
		this.maxVeinSize = maxVeinSize;
		this.veinRarity = veinRarity;
		this.color = color;
	}
	
	public Ore()
	{
		this.id = -1;
		this.name = "placebo";
	}
	
	private void initLists()
	{
		voyelles.add("a");voyelles.add("e");voyelles.add("i");voyelles.add("o");voyelles.add("u");voyelles.add("y");
		
		consonnes.add("b");consonnes.add("c");consonnes.add("d");consonnes.add("f");consonnes.add("g");consonnes.add("h");
		consonnes.add("j");consonnes.add("k");consonnes.add("l");consonnes.add("m");consonnes.add("n");consonnes.add("p");
		consonnes.add("q");consonnes.add("r");consonnes.add("s");consonnes.add("t");consonnes.add("v");consonnes.add("w");
		consonnes.add("x");consonnes.add("z");
		
		banned.add("qc");banned.add("xw");banned.add("pw");banned.add("xx");banned.add("vw");banned.add("fv");
		banned.add("vh");banned.add("fh");banned.add("bg");banned.add("qg");
	}
	
	private void createName()
	{
		createPrefixe();
		//System.out.println("prefixe is: "+ name);
		createAtom();
		//System.out.println("prefixe + atom are: "+ name);
		createSuffixe();
		//System.out.println("final word is: "+ name);
		checkIfBanned();
	}
	
	private void createColor()
	{
		color = glapi.getFilter(Var.rand.nextInt(256),Var.rand.nextInt(256),Var.rand.nextInt(2));
	}
	
	private void createStats()
	{
		
	}
	
	private void createPrefixe()
	{
		int first = Var.rand.nextInt(2);
		if (first == 0)
		{
			name = chooseVoyelle() + chooseConsonne() + chooseVoyelle();
		}
		else
		{
			name = chooseConsonne() + chooseVoyelle() + chooseConsonne();
		}
    	
	}
	
	private void createAtom()
	{
		int nbSyllabes = Var.rand.nextInt(4);
		boolean dou = dedouble();
		if(dou) //ca ne peut pas etre des voyelles
		{
			name = name + name.charAt(name.length()-1);
			//System.out.println("consonne dedoublee, le prefixe est maintenant: "+ s);
			createSyllabes(nbSyllabes);
		}
		else
		{
			char c = name.charAt(name.length()-1);
			if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
			{
				name = name + chooseConsonne();
				//System.out.println("consonne ajoutee, le prefixe est maintenant: "+ s);
				createSyllabes(nbSyllabes);
			}
			else
			{
				createSyllabes(nbSyllabes);
			}
		}
		//System.out.println("string retourne: " + s);
	}
	
	private void createSyllabes(int nbSyllabes)
	{
		for(int i = 0;i < nbSyllabes;i++)
		{
			createSyllabe();
		}
	}
	
	private String chooseVoyelle()
	{
		int i = Var.rand.nextInt(voyelles.size());
		return voyelles.get(i);
	}
	
	private String chooseConsonne()
	{
		int i = Var.rand.nextInt(consonnes.size());
		return consonnes.get(i);
	}
	
	private void createSyllabe()//commence par une voyelle et finit par une consonne
	{
		name = name + chooseVoyelle();
		int i = Var.rand.nextInt(5);
		if(i == 0)
		{
			name = name + chooseVoyelle();
		}
		name = name + chooseConsonne();
		int j = Var.rand.nextInt(3);
		if(j == 0)
		{
			name = name + chooseConsonne();
		}
	}
	
	private boolean dedouble()
	{
		char c = name.charAt(name.length()-1);
		if(c == 'h' || c == 'j' || c == 'q' || c == 'v' || c == 'w' || c == 'x' || c == 'b' || c == 'd' || c == 'g')
		{
			return false;
		}
		else if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private void createSuffixe()
	{
		int last = Var.rand.nextInt(3);
		if(last == 0)
		{
			name = name + "ium";
		}
		else if(last == 1)
		{
			name = name + "ite";
		}
		else
		{
			name = name + "one";
		}
	}
	
	private void checkIfBanned()
	{
		for(int i = 0;i < banned.size();i++)
		{
			if(name.contains(banned.get(i)))
			{
				//System.out.println("found: " + banned.get(i));
				Const.debug("detected banned letters for ore " + id + ": " + name);
				replaceLetters(banned.get(i));
			}
		}
	}
	
	private void replaceLetters(String s)
	{
		int i = findPos(s);
		System.out.println("pos detected to be " + i);
		if(i >= 0)
		{
			String newS = "";
			for(int j = 0; j < i;j++)
			{
				newS = newS + name.charAt(j);
			}
			for(int j = i; j < i + s.length();j++)
			{
				int z = Var.rand.nextInt(2);
				if(z == 0)
				{
					newS = newS + chooseConsonne();
				}
				else
				{
					newS = newS + chooseVoyelle();
				}
			}
			for(int j = i + s.length(); j < name.length();j++)
			{
				newS = newS + name.charAt(j);
			}
			Const.debug("new string = " + newS);
			name = newS;
		}
	}
	
	private int findPos(String s)
	{
		for(int i = 0;i < name.length();i++)
		{
			int stringOffset = 0;
			while(name.charAt(i+stringOffset) == s.charAt(stringOffset))
			{
				if(stringOffset == s.length() - 1)
				{
					return i;
				}
				else
				{
					stringOffset++;
				}
			}
		}
		return -1;
	}
}
