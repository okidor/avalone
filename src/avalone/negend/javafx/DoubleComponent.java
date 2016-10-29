package avalone.negend.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public abstract class DoubleComponent 
{
	protected Node linked;
	protected Label error;
	
	public abstract void addSelf(HBox hbox);
	
	public abstract void hide();
	
	public abstract void show();
	
	public void link(Node linked)
	{
		this.linked = linked;
	}
	
	public Node getLinked()
	{
		return linked;
	}
	
	public void setError(String message)
	{
		error.setStyle("-fx-text-fill: red;");
		error.setText(message);
	}
	
	public void setWarning(String message)
	{
		error.setStyle("-fx-text-fill: blue;");
		error.setText(message);
	}
}
