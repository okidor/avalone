package avalone.todelete;

//import org.gnome.gtk.*;

import avalone.negend.GameFile;

//import org.gnome.gdk.RGBA;

public class NameEntry
{
	/*private GameFile game;
	private String[] cutLine;
	private Label warningLabel;
	private Label label;
	private Entry entry;
	private int line;
	private String nickname;
	
	public NameEntry(int line,String labelName)
	{
		game = GameFile.configFile;
		label = new Label(labelName);
		warningLabel = new Label();
		entry = new Entry();
		cutLine = game.al.get(line);
		this.line = line;
		setLabelFromLineParts(2);
		nickname = entry.getText();
		entry.connect(new Entry.Changed()
		{
			public void onChanged(Entry entry)
			{
				nickname = entry.getText();
				//System.out.println("nickname = '" + entry.getText() + "'");
				String[] newNick = nickname.split("\\s+");
				modifyLine(2,newNick);
			}
		});
	}
	
	public void setLabelFromLinePart(int wordNumber)
	{
		entry.setText(cutLine[wordNumber]);
	}
	
	public void setLabelFromLineParts(int wordNumber)
	{
		String s = "";
		for(int i = wordNumber;i < cutLine.length - 1;i++)
		{
			s = s + cutLine[i] + " ";
		}
		s = s + cutLine[cutLine.length - 1]; //pas d'espace en fin de mot
		entry.setText(s);
	}
	
	public void modifyLine(int wordNumber,String message)
	{
		cutLine = game.al.get(line);
		cutLine[wordNumber] = message;
		game.write();
	}
	
	public void modifyLine(int wordNumber,String[] message)
	{
		cutLine = game.al.get(line);
		String tmp[] = new String[wordNumber + message.length];
		for(int i = 0;i < wordNumber;i++)
		{
			tmp[i] = cutLine[i];
		}
		for(int i = 0;i < message.length;i++)
		{
			tmp[wordNumber + i] = message[i];
		}
		cutLine = tmp;
		game.relink(cutLine,line);
		game.write();
	}
	
	public void setError(String message)
	{
		warningLabel.overrideColor(StateFlags.NORMAL, RGBA.RED);
		warningLabel.setLabel(message);
	}
	
	public void put(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(warningLabel,posX + 350 ,posY);
		fix.put(label, posX, posY+5);
		fix.put(entry, posX+170, posY);
	}*/
}
