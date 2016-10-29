package avalone.todelete;

/*import org.gnome.gtk.Fixed;
import org.gnome.gtk.Label;
import org.gnome.gtk.CheckButton;
import org.gnome.gtk.ToggleButton;
import org.gnome.gtk.ToggleButton.Toggled;*/

import avalone.negend.GameFile;

public class ConfigTickBox 
{
	/*private Label label;
	private CheckButton tickBox;
	private GameFile game;
	private String[] cutLine;
	private int line;
	
	public ConfigTickBox(String labelName,int line)
	{
		tickBox = new CheckButton();
		label = new Label(labelName);
		game = GameFile.configFile;
		cutLine = game.al.get(line);
		this.line = line;
		tickBox.connect(new Toggled()
		{
			@Override
			public void onToggled(ToggleButton arg0) 
			{
				modifyLine();
			}
		});
		checkToggleStart();
	}
	
	public void checkToggleStart()
	{
		tickBox.setActive(Boolean.parseBoolean(cutLine[2]));
	}
	
	public void modifyLine()
	{
		boolean activ = tickBox.getActive();
		cutLine = game.al.get(line);
		cutLine[2] = String.valueOf(activ);
		game.write();
	}
	
	public void put(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(label, posX, posY);
		fix.put(tickBox,posX+170,posY-3);
	}*/
}
