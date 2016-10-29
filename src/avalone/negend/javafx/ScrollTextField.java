package avalone.negend.javafx;

import avalone.negend.global.Var;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ScrollTextField extends DoubleComponent
{
	private ScrollBar sc;
	private TextField tf;
	private Label error;
	
	public ScrollTextField(int minVal,int maxVal)
	{
		this(minVal,maxVal,minVal + (maxVal-minVal)/2);
	}
	
	public ScrollTextField(int minVal,int maxVal,int val)
	{
		if(val < minVal || val > maxVal)
		{
			val = minVal + (maxVal-minVal)/2;
		}
		sc = new ScrollBar();
		tf = new TextField(String.valueOf(val));
		Var.oresNumber = val;
		error = new Label("");
		
		sc.setOrientation(Orientation.HORIZONTAL);
		sc.setMin(minVal);sc.setMax(maxVal);sc.setValue(val);
		
		sc.valueProperty().addListener(new ChangeListener<Number>() 
		{
            public void changed(ObservableValue<? extends Number> ov,Number oldVal, Number newVal) 
            {
            	tf.setText(Integer.toString(newVal.intValue()));
            	Var.oresNumber = newVal.intValue();
            }
        });
		
		tf.textProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) 
			{
				if(isInteger(newVal))
				{
					int val = Integer.parseInt(newVal);
					if(val < (int)sc.getMin())
					{
						setError("value too low");
					}
					else if(val > (int)sc.getMax())
					{
						setError("value too high");
					}
					else
					{
						sc.setValue(val);
						setWarning("");
					}
				}
				else
				{
					if(newVal.equals(""))
					{
						setError("field cannot be empty");
					}
					else
					{
						tf.setText(oldVal);
					}
				}
			}
			
		});
	}
	
	public void hide()
	{
		sc.setVisible(false);
		tf.setVisible(false);
		error.setVisible(false);
		linked.setVisible(false);
	}
	
	public void show()
	{
		sc.setVisible(true);
		tf.setVisible(true);
		error.setVisible(true);
		linked.setVisible(true);
	}
	
	public void addSelf(HBox hbox)
	{
		hbox.getChildren().addAll(sc,tf,error);
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

}
