package avalone.api.lwjgl3;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import avalone.api.util.Point;
import avalone.api.util.TexturesLoader;

public class AvaloneGLAPI extends Geometry
{
	private static AvaloneGLAPI instance;
	protected Point modif;
	protected Point scale;
	protected String subFolder;
	protected int[] texturesID;
	protected String[] texturesName;
	protected BufferedImage[] bufImg;
	protected HashMap<String,Integer> hm;
	protected HashMap<String,BufferedImage> hmb;
	
	public AvaloneGLAPI(int width, int height, String title)
    {
		this(width,height,title,"");
    }
	
	public AvaloneGLAPI(int width, int height, String title, boolean invertYAxis)
    {
		this(width,height,title,"", invertYAxis);
    }
	
	public AvaloneGLAPI(int width, int height, String title,String subFolder)
    {
		this(width,height,title, subFolder, false);
    }
	
	public AvaloneGLAPI(int width, int height, String title,String subFolder, boolean invertYAxis)
    {
		super(width,height,title, invertYAxis);
    	modif = new Point();
    	scale = new Point(1,1);
    	this.subFolder = subFolder;
        initTextures();
        lastFPS = GLFW.glfwGetTime();
        instance = this;
    }
	
	public static AvaloneGLAPI getInstance()
	{
		return instance;
	}
	
	public void enableTextures()
    {
    	glEnable(GL_TEXTURE_2D);
    }
	
	private void initTextures()
    {
		hm = new HashMap<String,Integer>();
    	hmb = new HashMap<String,BufferedImage>();
    	
    	initTexturesInFolder("");
    	if(!subFolder.equals(""))
    	{
    		initTexturesInFolder(subFolder);
    	}
    	
    	unbindTexture();
    }
	
	public void loadTmpTextures(String subSubFolder)
	{
		initTexturesInFolder(subFolder + File.separator + subSubFolder);
	}
	
	public void unloadTmpTextures(String subSubFolder)
	{
		File dir = new File("textures" + File.separator + subFolder + File.separator + subSubFolder);
    	File[] files = dir.listFiles();
    	if(files != null)
    	{
    		unloadFolderTextures(files);
    	}
	}
	
	private void initTexturesInFolder(String subFolder)
	{
		File dir = new File("textures" + File.separator + subFolder);
    	File[] files = dir.listFiles();
    	if(files != null)
    	{
    		loadFolderTextures(files,subFolder);
    	}
	}
    
    private void loadFolderTextures(File[] files,String path)
    {
    	for(int i = 0; i < files.length;i++)
    	{
    		String textureName = files[i].getName();
    		if(textureName.endsWith(".jpg") || textureName.endsWith(".png"))
    		{
    			System.out.println("loaded texture " + textureName);
    			BufferedImage bufImg = TexturesLoader.loadImage(path + File.separator + textureName);
    			Integer textureID = TexturesLoader.loadTexture(bufImg);
    			System.out.println("id attributed: " + textureID);
    			hm.put(textureName,textureID);
    			hmb.put(textureName, bufImg);
    		}
    	}
    }
    
    private void unloadFolderTextures(File[] files)
    {
    	for(int i = 0; i < files.length;i++)
    	{
    		String textureName = files[i].getName();
    		if(textureName.endsWith(".jpg") || textureName.endsWith(".png"))
    		{
    			Integer j = hm.get(textureName);
    			if(j != null)
    	    	{
    				System.out.println("unloaded texture " + textureName);
    				hm.remove(textureName);
        			hmb.remove(textureName);
    				GL11.glDeleteTextures(j);
    	    	}
    			else
    			{
    				System.out.println("could not unload texture " + textureName);
    			}
    		}
    	}
    }
    
    public int getIDFromName(String path)
    {
    	Integer i = hm.get(path);
    	if(i != null)
    	{
    		return i.intValue();
    	}
    	else
    	{
    		return hm.get("default.png");
    	}
    }
    
    public BufferedImage getImgFromName(String path)
    {
    	BufferedImage img = hmb.get(path);
    	if(img != null)
    	{
    		return img;
    	}
    	else
    	{
    		return hmb.get("default.png");
    	}
    }
    
    public float[] getFilter(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	Color col = new Color(img.getRGB(i, j));
    	float[] colorComp = new float[4];
    	colorComp[0] = col.getRed()/255.0f;
    	colorComp[1] = col.getGreen()/255.0f;
    	colorComp[2] = col.getBlue()/255.0f;
    	colorComp[3] = 0.8f;
    	return colorComp;
    }
    
    public void setRandomFilter(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	Color col = new Color(img.getRGB(i, j));
    	glColor4f(col.getRed()/255.0f,col.getGreen()/255.0f,col.getBlue()/255.0f,0.4f);
    }
    
    public void randTexture(int i,int j,int deg)
    {
    	deg = deg % 2;
    	BufferedImage img = getImgFromName("deg" + deg + ".png");
    	BufferedImage img2 = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
    	img2.setRGB(0, 0, img.getRGB(i, j));
    }
    
    public void unbindTexture()
    {
    	glBindTexture(GL_TEXTURE_2D, 0);
    }
}
