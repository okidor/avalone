package avalone.api.util;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import static org.lwjgl.opengl.GL11.glBindTexture;

import java.awt.image.BufferedImage;


public class StringBuilder 
{
	private int id;
	
	public StringBuilder()
	{
		BufferedImage buf = TexturesLoader.loadImage("characters.png");
		if(buf != null)
		{
			id = TexturesLoader.loadTexture(buf);
		}
	}
	
	public int getTextureID()
	{
		return id;
	}

	public void buildFromImage(Point p1,Point p2,String word)
	{
		//System.out.println(p1 + " " + p2 + " " + word);
		glBindTexture(GL_TEXTURE_2D, id);
		Point[] p = adjust(p1,p2,word);
		for(int i = 0; i < word.length();i++)
		{
			GraphicCharacter.getFromChar(word.charAt(i)).drawCoordRect(p[i*2], p[i*2+1]);
		}
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private Point[] adjust(Point p1,Point p2,String word)
	{
		int wordLength = word.length();
		float ind = (float)(p2.x - p1.x)/(float)wordLength;
		float indY = (float)(p2.y - p1.y);
		float rapport = ind / indY;
		/*System.out.println(ind + "/" + indY + "=" + rapport);
		System.out.println(GraphicCharacter.rapport);*/
		if(rapport > GraphicCharacter.rapport)
		{
			ind = (GraphicCharacter.width * indY)/GraphicCharacter.height;
		}
		else if(rapport < GraphicCharacter.rapport)
		{
			indY = (GraphicCharacter.height * ind)/GraphicCharacter.width;
			p2.y = p1.y + (int)indY;
		}
		
		/*rapport = ind / indY;
		System.out.println(ind + "/" + indY + "=" + rapport);*/
		Point[] p = new Point[wordLength*2];
		for(int i = 0; i < wordLength;i++)
		{
			p[i*2] = new Point(p1.x + i*(int)ind,p1.y);
			p[i*2+1] = new Point(p1.x + (i+1)*(int)ind,p2.y);
		}
		return p;
	}
}
