package avalone.negend.javafx;

import avalone.negend.GameFile;
import avalone.negend.global.Var;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class NameField extends DoubleComponent
{
	private TextField tf;
	private Label error;
	
	private GameFile game;
	private String[] cutLine;
	
	public NameField()
	{
		game = GameFile.configFile;
		tf = new TextField(Var.gameName);
		error = new Label();
		
		cutLine = game.al.get(1);
		setLabelFromLineParts(2);
		
		tf.textProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) 
			{
				String[] newNick = newVal.split("\\s+");
				modifyLine(2,newNick);
			}
		});
	}
	
	public void setLabelFromLinePart(int wordNumber)
	{
		tf.setText(cutLine[wordNumber]);
	}
	
	public void setLabelFromLineParts(int wordNumber)
	{
		String s = "";
		for(int i = wordNumber;i < cutLine.length - 1;i++)
		{
			s = s + cutLine[i] + " ";
		}
		s = s + cutLine[cutLine.length - 1]; //pas d'espace en fin de mot
		tf.setText(s);
	}

	
	public void modifyLine(int wordNumber,String message)
	{
		cutLine = game.al.get(1);
		cutLine[wordNumber] = message;
		game.write();
	}
	
	public void modifyLine(int wordNumber,String[] message)
	{
		cutLine = game.al.get(1);
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
		game.relink(cutLine,1);
		game.write();
	}

	@Override
	public void addSelf(HBox hbox) 
	{
		hbox.getChildren().addAll(tf,error);
	}

	@Override
	public void hide() 
	{
		tf.setVisible(false);
		error.setVisible(false);
		linked.setVisible(false);
	}

	@Override
	public void show() 
	{
		tf.setVisible(true);
		error.setVisible(true);
		linked.setVisible(true);
	}
}
