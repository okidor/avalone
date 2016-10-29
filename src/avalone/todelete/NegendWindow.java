package avalone.todelete;

import java.io.File;

import avalone.negend.global.Const;

public class NegendWindow// extends Window
{
	/*private Fixed fix;
	public int mode;
	public boolean newGame;
	FilePathEntry fpath;
	private NameEntry pseudo;
	public String nickname;
	public Button partie;
	public ScrollEntry scroll;
	
	public NegendWindow()
	{
		newGame = true;
		setTitle("Negend");
		fix = new Fixed();
		nickname = "new player";
		initUI();
		
		add(fix);
        
        setDefaultSize(500, 400);
        setPosition(WindowPosition.CENTER);
        
        showAll();
	}
	
	public void initUI()
	{
		connect(new Window.DeleteEvent() 
	    {
			public boolean onDeleteEvent(Widget source, Event event) 
	        {
	            mode = Const.QUIT_MODE;
	            Gtk.mainQuit();
	            return false;
	        }
	    });
	
		initButtons();
		initLabels();
		initImages();
		initScroll();
		initEntries();
		initTickBox();
	}
	
	public void initButtons()
	{
        Button edit = new Button("edit");
        partie = new Button("start game");
        //Button btn4 = new Button("Button");
        //btn4.setSizeRequest(80, 40);

        fix.put(edit, 290, 33);
        fix.put(partie, 191, 370);
        //fix.put(btn4, 100, 80);
       // System.out.println(close.getAllocation().toString());
        
        partie.connect(new Button.Clicked()
        {
            public void onClicked(Button source)
            {
            	mode = Const.PLAY_MODE;
            	fpath.addCreatedGameIfNotExists();
            	//fpath.update();
            	//destroy();
            	source.getParent().getParent().hide();
            	Gtk.mainQuit();
            	//caller.startGame(nb,fpath.getText(),newGame);
            	//showAll();
            }
        });
        
        edit.connect(new Button.Clicked()
        {
            public void onClicked(Button source)
            {
            	mode = Const.EDIT_MODE;
            	//hide();
            	source.getParent().getParent().hide();
            	//destroy();
            	Gtk.mainQuit();
            	//new CharacterEditor();
            	//initImages();
            	//source.getParent().getParent().showAll();
            }
        });
	}
	
	public void initLabels()
	{
		Label avatar = new Label("Avatar :");
		Label game = new Label("game name :");
		
		fix.put(avatar, 50, 38);
		fix.put(game, 10, 245);
	}
	
	public void initScroll()
	{
		scroll = new ScrollEntry(new Adjustment(10, 5, 200, 1, 0, 0),"ores number :");
		scroll.put(fix,10,110);
	}
	
	public void setSensitivity(boolean bool)
	{
		scroll.setSensitive(bool);
		newGame = bool;
	}
	
	public void setButtonSensitivity(boolean partieBool)
	{
		partie.setSensitive(partieBool);
	}
	
	public void initTickBox()
	{
		ConfigTickBox tick = new ConfigTickBox("render shadows :",0);
		tick.put(fix, 10, 200);
	}
	
	public void initEntries()
	{
		fpath = new FilePathEntry(this);
		fpath.put(fix, 180, 240);
		
		pseudo = new NameEntry(1,"nickname :");
		pseudo.put(fix, 10, 160);
	}
	
	public void update()
	{
		newGame = fpath.update(partie);
	}
	
	public String realPath(String path)
	{
		return System.getProperty("user.dir") + File.separator + "textures" + File.separator + path;
	}
	
	public void initImages()
	{
		Image img0 = new Image(realPath("player0.png"));
		Image img1 = new Image(realPath("player1.png"));
		Image img2 = new Image(realPath("player2.png"));
		fix.put(img0, 200, 30);
		fix.put(img1, 220, 30);
		fix.put(img2, 240, 30);
	}*/
}
