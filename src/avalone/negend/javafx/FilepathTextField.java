package avalone.negend.javafx;

import avalone.negend.FileUtils;
import avalone.negend.GameFile;
import avalone.negend.global.Const;
import avalone.negend.global.Var;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FilepathTextField extends DoubleComponent
{
	private GameFile game;
	private TextField tf;
	private Button startButton;
	private DoubleComponent se;
	
	public FilepathTextField(DoubleComponent se,Button startButton)
	{
		game = new GameFile(Const.savePath);
		this.startButton = startButton;
		this.se = se;
		Var.gameName = FileUtils.defaultName();
		tf = new TextField(Var.gameName);
		error = new Label();
		
		tf.textProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) 
			{
				if(newVal.length() > 0)
				{
					System.out.println(newVal + ", " + FileUtils.gameExists(newVal));
					if(!FileUtils.gameExists(newVal))
					{
						setWarning("a new game will start");
						se.show();
						Var.newGame = true;
					}
					else
					{
						setWarning("a game will continue");
						se.hide();
						Var.newGame = false;
					}
					startButton.setDisable(false);
					Var.gameName = newVal;
				}
				else
				{
					setError("empty");
					startButton.setDisable(true);
				}

			}
		});
		setWarning("a new game will start");
	}
	
	public void hide() 
	{
		tf.setVisible(false);
		error.setVisible(false);
		linked.setVisible(false);
	}

	public void show() 
	{
		tf.setVisible(true);
		error.setVisible(true);
		linked.setVisible(true);
	}

	public void update()
	{
		String message = tf.getText();
		//System.out.println("fpath = " + entry.getText());
		if(message.length() > 0)
		{
			if(!FileUtils.gameExists(message))
			{
				setWarning("a new game will start");
				Var.newGame = true;
				se.show();
			}
			else
			{
				setWarning("a game will continue");
				Var.newGame =  false;
				se.hide();
			}
			startButton.setDisable(false);
		}
		else
		{
			setError("empty");
			startButton.setDisable(true);
		}
	}

	public void addSelf(HBox hbox)
	{
		hbox.getChildren().addAll(tf,error);
	}
}
