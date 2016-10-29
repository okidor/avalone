package avalone.negend.javafx;

import java.awt.image.BufferedImage;
import java.io.File;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.negend.CharacterEditor;
import avalone.negend.Negend;
import avalone.negend.global.Const;
import avalone.negend.global.Var;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class NegendWindow extends Application
{
	private Negend neg;
	
	ImageView player0;
	ImageView player1;
	ImageView player2;
	
	private Button startButton;
	private ScrollTextField se;
	private FilepathTextField pf;
	private NameField nf;
	
	public void start(Stage primaryStage) 
	{
		neg = new Negend();
		Var.newGame = true;
		startButton = new Button();
		se = new ScrollTextField(5,40,10);
		pf = new FilepathTextField(se,startButton);
		nf = new NameField();
		Platform.setImplicitExit(false);
		try 
		{
			VBox vbox = new VBox();
			HBox hbox1 = new HBox();
			HBox startBox = new HBox();
			HBox oresBox = new HBox();
			HBox pathBox = new HBox();
			HBox nicknameBox = new HBox();
			
			Region begin = new Region();
			HBox.setHgrow(begin, Priority.ALWAYS);
			begin.setMinHeight(20);
			begin.setMaxHeight(20);
			
			vbox.getChildren().addAll(begin,hbox1,pathBox,oresBox,nicknameBox,startBox);
			Scene scene = new Scene(vbox,500,400);
			firstHboxAdd(primaryStage,hbox1);
			pathBoxAdd(primaryStage,pathBox);
			oresHBoxAdd(primaryStage,oresBox);
			nicknameBoxAdd(primaryStage,nicknameBox);
			lastHBoxAdd(primaryStage,startBox);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
		    @Override public void handle(WindowEvent t) 
		    {
		        Platform.exit();
		    }
		});
	}
	
	private void firstHboxAdd(Stage primaryStage,HBox hbox)
	{
		player0 = new ImageView(new Image(getURL("player0.png")));
		player1 = new ImageView(new Image(getURL("player1.png")));
		player2 = new ImageView(new Image(getURL("player2.png")));
		
		Region begin = new Region();
		HBox.setHgrow(begin, Priority.ALWAYS);
		
		Region interPic1 = new Region();
		HBox.setHgrow(interPic1, Priority.ALWAYS);     
		interPic1.setMinWidth(2);
		interPic1.setMaxWidth(2);
		
		Region interPic2 = new Region();
		HBox.setHgrow(interPic2, Priority.ALWAYS);     
		interPic2.setMinWidth(2);
		interPic2.setMaxWidth(2);
        
        Region inter = new Region();
		HBox.setHgrow(inter, Priority.ALWAYS);
		
		Region end = new Region();
		HBox.setHgrow(end, Priority.ALWAYS);
		
		Button editButton = new Button();
		editButton.setText("edit");
		editButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.hide();
                new CharacterEditor();
                reloadTextures();
                primaryStage.show();
            }
        });
        hbox.getChildren().addAll(begin,player0,interPic1,player1,interPic2,player2,inter,editButton,end);
	}
	
	private void pathBoxAdd(Stage primaryStage,HBox pathBox)
	{
		Label label = new Label("save path: ");
		
		pathBox.getChildren().add(label);
		pf.addSelf(pathBox);
		pf.link(label);
	}
	
	private void oresHBoxAdd(Stage primaryStage,HBox oresBox)
	{		
		Label label = new Label("ores amount: ");
		
		oresBox.getChildren().add(label);
		se.addSelf(oresBox);
		se.link(label);
	}
	
	private void nicknameBoxAdd(Stage primaryStage,HBox nicknameBox)
	{
		Label label = new Label("nickname: ");
		
		nicknameBox.getChildren().add(label);
		nf.addSelf(nicknameBox);
		nf.link(label);
	}
	
	private void lastHBoxAdd(Stage primaryStage,HBox startBox)
	{
		startButton.setText("start");
		
		Region center = new Region();
		HBox.setHgrow(center, Priority.ALWAYS);
		center.setMinWidth(230);
		center.setMaxWidth(230);
		
		startButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event) 
            {
                primaryStage.hide();
                neg.startGame();
                pf.update();
                primaryStage.show();
            }
        });
		startBox.getChildren().addAll(center,startButton);	
	}
	
	public void reloadTextures()
	{
		player0.setImage(new Image(getURL("player0.png")));
		player1.setImage(new Image(getURL("player1.png")));
		player2.setImage(new Image(getURL("player2.png")));
	}
	
	public String getURL(String textureName)
	{
		return "file:" +System.getProperty("user.dir") + File.separator + "textures" + 
				File.separator + "negend" + File.separator + textureName;
	}
	
	public static void main(String[] args)
    {	
    	NegendWindow.launch(args);
    }
}
