package avalone.todelete;

import avalone.negend.GameFile;
import avalone.negend.global.Const;

public class FilePathEntry
{
	/*private GameFile game;
	private Label label;
	private Entry entry;
	
	public FilePathEntry(final NegendWindow negend)
	{
		game = new GameFile(Const.savePath);
		label = new Label();
		entry = new Entry();
		entry.connect(new Entry.Changed()
		{
			public void onChanged(Entry entry) 
			{
				String message = entry.getText();
				//System.out.println("fpath = " + entry.getText());
				if(message.length() > 0)
				{
					if(!gameExists(message))
					{
						setWarning("a new game will start");
						negend.setSensitivity(true);
					}
					else
					{
						setWarning("a game will continue");
						negend.setSensitivity(false);
					}
					negend.setButtonSensitivity(true);
				}
				else
				{
					setError("empty");
					negend.setButtonSensitivity(false);
				}
			}
		});
		setWarning("a new game will start");
		entry.setText(defaultName());
	}
	
	public boolean gameExists(String name)
	{
		for(int i = 0;i < game.al.size();i++)
		{
			String[] s = game.al.get(i);
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
	
	public String defaultName()
	{
		boolean searching = true;
		boolean keep = true;
		int nb = 0;
		while(searching)
		{
			keep = true;
			//System.out.println("nb vaut " + nb);
			for(int i = 0; i < game.al.size();i++)
			{
				String[] s = game.al.get(i);
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
	
	public void addCreatedGameIfNotExists()
	{
		if(!gameExists(entry.getText()))
    	{
			game.addNewLine(entry.getText());
    	}
	}
	
	public void setError(String message)
	{
		label.overrideColor(StateFlags.NORMAL, RGBA.RED);
		label.setLabel(message);
	}
	
	public void setWarning(String message)
	{
		label.overrideColor(StateFlags.NORMAL, RGBA.BLUE);
		label.setLabel(message);
	}
	
	public void put(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(entry, posX, posY);
		fix.put(label,posX+175,posY+5);
	}
	
	public String getText()
	{
		return entry.getText();
	}
	
	public boolean update(Button partie)
	{
		String message = entry.getText();
		//System.out.println("fpath = " + entry.getText());
		if(message.length() > 0)
		{
			partie.setSensitive(true);
			if(!gameExists(message))
			{
				setWarning("a new game will start");
				return true;
			}
			else
			{
				setWarning("a game will continue");
				return false;
			}
		}
		else
		{
			setError("empty");
			partie.setSensitive(false);
			return false;
		}
	}*/
}
