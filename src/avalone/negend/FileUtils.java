package avalone.negend;

import java.io.File;
import java.io.IOException;

public class FileUtils 
{
	public static boolean gameExists(String name)
	{
		for(int i = 0;i < GameFile.masterSaveFile.al.size();i++)
		{
			String[] s = GameFile.masterSaveFile.al.get(i);
			String[] splitName = name.split("\\s+");
			if(s.length == splitName.length)
			{
				boolean found = true;
				for(int j = 0;j < s.length;j++)
				{
					if(!s[j].equals(splitName[j]))
					{
						found = false;
					}
				}
				if(found)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static void addCreatedGameIfNotExists(String path)
	{
		if(!gameExists(path))
    	{
			GameFile.masterSaveFile.addNewLine(path);
    	}
	}
	
	public static String defaultName()
	{
		boolean searching = true;
		boolean keep = true;
		int nb = 0;
		while(searching)
		{
			keep = true;
			//System.out.println("nb vaut " + nb);
			for(int i = 0; i < GameFile.masterSaveFile.al.size();i++)
			{
				String[] s = GameFile.masterSaveFile.al.get(i);
				if(s.length == 3)
				{
					if(s[0].equals("new") && s[1].equals("game") && s[2].equals(String.valueOf(nb)))
					{
						nb++;
						//System.out.println("trouve on cherche un autre");
						keep = false;
						break;
					}
				}
			}
			if(keep)
			{
				searching = false;
			}
		}
		return "new game " + nb;
	}
	
	public static File createFile(String path)
    {
    	File f = new File(path);
        boolean exists = f.exists();
        if(exists)
        {
            f.delete();
        }
        try
        {
        	f.createNewFile();
        }
        catch(IOException ie)
        {
        	System.out.println("couldn't create file");
        }
        return f;
    }
	
	public static File checkExists(String path,String filetype)
    {
    	File f = new File(path);
    	if(!f.exists())
    	{
    		System.out.println(filetype + " " + path + " does not exist.");
    		return new File("error");
    	}
    	return f;
    }
}
