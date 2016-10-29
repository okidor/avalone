package avalone.todelete;

/*import org.gnome.gtk.Adjustment;
import org.gnome.gtk.Entry;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.HScrollbar;
import org.gnome.gtk.Label;
import org.gnome.gtk.Range;
import org.gnome.gtk.Range.ValueChanged;*/

public class ScrollEntry 
{
	/*private HScrollbar scroll;
	private Label label;
	private Entry entry;
	private String scrollValue;
	private String lastValid;
	private boolean lock;
	private int minOreNumber;
	private int maxOreNumber;
	
	public ScrollEntry(Adjustment adj,String labelName)
	{
		scroll = new HScrollbar(adj);
		label = new Label(labelName);
		entry = new Entry();
		lock = false;
		minOreNumber = (int) adj.getLower();
		maxOreNumber = (int) adj.getUpper();
		scroll.setSizeRequest(160, 30);
		scrollValue = String.valueOf((int)adj.getValue());
		lastValid = String.valueOf((int)adj.getValue());
		entry.setText(scrollValue);
		scroll.connect(new ValueChanged()
		{
			@Override
			public void onValueChanged(Range arg0) 
			{
				if(!lock)
				{
					scrollValue = String.valueOf((int)scroll.getValue());
					lastValid = String.valueOf((int)scroll.getValue());
					lock = true;
					entry.setText(scrollValue);
					lock = false;
				}
			}
		});
		entry.connect(new Entry.Changed()
		{
			public void onChanged(Entry entry) 
			{
				if(!lock)
				{
					String message = entry.getText();
					lock = true;
					if(!message.isEmpty())
					{
						if(isInteger(message))
						{
							if(Integer.valueOf(message) > maxOreNumber)
							{
								lastValid = String.valueOf(maxOreNumber);
								entry.setText(lastValid);
								scroll.setValue(maxOreNumber);
							}
							else if(Integer.valueOf(message) < minOreNumber)
							{
								lastValid = String.valueOf(minOreNumber);
								entry.setText(String.valueOf(lastValid));
								scroll.setValue(minOreNumber);
							}
							else
							{
								//System.out.println("entry changed and made scroll value change to " + message);
								scroll.setValue(Double.valueOf(message));
								lastValid = new String(message);
							}
						}
						else
						{
							//System.out.println("invalid value (not an integer)");
							entry.setText(lastValid);
						}
					}
					else
					{
						//System.out.println("invalid value (empty)");
						entry.setText(lastValid);
					}
					lock = false;
				}
			}
		});
	}
	
	public void put(Fixed fix,int x,int y)
	{
		int posX = x;
		int posY = y;
		fix.put(label, posX, posY+3);
		fix.put(scroll,posX+170,posY+15);
		fix.put(entry,posX+340,posY);
	}
	
	public static boolean isInteger(String str)  
	{  
		try  
		{  
			Integer.parseInt(str);  
		}
		catch(NumberFormatException nfe)  
		{  
			return false;
		}
		return true;  
	}
	
	public int getValue()
	{
		return (int) scroll.getValue();
	}
	
	public void setSensitive(boolean bool)
	{
		scroll.setSensitive(bool);
		entry.setSensitive(bool);
	}*/
}
