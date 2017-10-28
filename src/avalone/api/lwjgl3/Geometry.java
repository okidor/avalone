package avalone.api.lwjgl3;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.system.MemoryStack;

import avalone.api.util.FPoint;
import avalone.api.util.Point;
import avalone.api.util.StringBuilder;

public abstract class Geometry extends BasicFuncs
{
	protected StringBuilder stp;
	
	protected double lastFPS;
	protected int drawFPS;
	protected int fps;
	
	public Geometry(int width, int height, String title) 
	{
		super(width, height, title);
		stp = new StringBuilder();
		lastFPS = getTime();
	}
    
    public abstract int getIDFromName(String path);
    
    /* textured */
    
    public void drawTexturedMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
        drawMultiRect(p1s,p2s);
    }
    
    public void drawTexturedRect(Point p1,Point p2,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
    	drawRect(p1,p2);
    }
    
    public void drawTexturedRect(Point p1,Point p2,int id)
    {
    	glBindTexture(GL_TEXTURE_2D, id);
    	drawRect(p1,p2);
    }
    
    /* untextured */
    
    public void drawMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s)
    {
    	for(int i = 0;i < p1s.size();i++)
        {
        	drawRect(p1s.get(i),p2s.get(i));
        }
    }
    
    public void drawCenterRect(Point p1,int tailleX,int tailleY,String c)
    {
    	Point p2 = new Point(p1.x-tailleX/2,p1.y-tailleY/2);
    	Point p3 = new Point(p1.x+tailleX/2,p1.y+tailleY/2);
    	drawRect(p2,p3,c);
    }
    
    public void drawRect(Point p1,Point p2,String c)
    {
    	drawColoredRect(p1,p2,c,GL_QUADS,1.0f);
    }
    
    public void drawRect(Point p1,Point p2)
    {
    	drawBaseRect(p1,p2,GL_QUADS);
    }
    
    public void drawEmptyRect(Point p1,Point p2,String c)
    {
    	drawColoredRect(p1,p2,c,GL_LINE_LOOP,1.0f);
    	Point p3 = new Point(p1.x-1,p1.y-1);
    	drawPixel(p3,c);
    }
    
    public void drawAlphaRect(Point p1,Point p2,String c,float alpha)
    {
    	drawColoredRect(p1,p2,c,GL_QUADS,alpha);
    }
    
    private void drawColoredRect(Point p1,Point p2,String c,int GL_SET,float alpha)
    {
		float[] colortaker = AvColor.getColorByRGB(c);
		glColor4f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f,alpha);
    	drawBaseRect(p1,p2,GL_SET);
    }
    
    public void drawColoredRect(Point p1,Point p2,int r,int g,int b,int a)
    {
    	glColor4f(r/255.0f,g/255.0f,b/255.0f,a/255.0f);
    	drawBaseRect(p1,p2,GL_QUADS);
    }
    
    public void drawMultiAlphaRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String c,float alpha)
    {
    	drawColoredMultiRect(p1s,p2s,c,GL_QUADS,alpha);
    }
    
    private void drawColoredMultiRect(ArrayList<Point> p1s,ArrayList<Point> p2s,String c,int GL_SET,float alpha)
    {
    	float[] colortaker = AvColor.getColorByRGB(c);
		glColor4f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f,alpha);
    	for(int i = 0;i < p1s.size();i++)
        {
        	drawBaseRect(p1s.get(i),p2s.get(i),GL_SET);
        }
    }
    
    public void drawRectWithColors(Point p1,Point p2,float[] color)
    {
    	glColor4f(color[0],color[1],color[2],color[3]);
    	drawBaseRect(p1,p2,GL_QUADS);
    }
    
    /* partially textured */
    
    public void drawTexturedCoordRect(Point p1,Point p2,FPoint beginIndex,FPoint endIndex,String path)
    {
    	int id = getIDFromName(path);
        glBindTexture(GL_TEXTURE_2D, id);
		drawCoordRect(p1,p2,beginIndex,endIndex);
    }
    
    public void drawCoordRect(Point p1,Point p2,FPoint beginIndex,FPoint endIndex)
	{
		glBegin(GL_QUADS);
    	glTexCoord2f(beginIndex.x, beginIndex.y);
    	glVertex2i(p1.x, p1.y);
    	glTexCoord2f(beginIndex.x, endIndex.y);
    	glVertex2i(p1.x, p2.y);
    	glTexCoord2f(endIndex.x, endIndex.y);
    	glVertex2i(p2.x, p2.y);
    	glTexCoord2f(endIndex.x, beginIndex.y);
    	glVertex2i(p2.x, p1.y);
    	glEnd();
	}
    
    /* base */
    
    private void drawBaseRect(Point p1,Point p2,int GL_SET)
    {
    	glBegin(GL_SET);
    	glTexCoord2f(0.0f, 0.0f);
    	glVertex2i(p1.x, p1.y);
    	glTexCoord2f(0.0f, 1.0f);
    	glVertex2i(p1.x, p2.y);
    	glTexCoord2f(1.0f, 1.0f);
    	glVertex2i(p2.x, p2.y);
    	glTexCoord2f(1.0f, 0.0f);
    	glVertex2i(p2.x, p1.y);
    	glEnd();
    }
    
    /* others */
    
    public void drawFPS(Point p1,Point p2)
    {
    	if (getTime() - lastFPS > 1) 
		{
    		drawFPS = fps;
			fps = 0;
			lastFPS += 1;
		}
		fps++;
		drawText(p1,p2,drawFPS,"red");
    }
    
    public void drawText(Point p1,Point p2,String text,String c)
    {
    	setFilter(c);
		stp.buildFromImage(p1,p2,text);
		clearFilter();
    }
    
    public void drawText(Point p1,Point p2,int number,String c)
    {
    	String s = Integer.toString(number);
    	drawText(p1,p2,s,c);
    }

    public void drawBaseQuad(Point p1,Point p2,Point p3,Point p4,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p1.x, p1.y);
		glVertex2i(p2.x, p2.y);
    	if(p2.y>p1.y)
    	{
    		if(p3.y<p4.y)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.y<p1.y)
    	{
    		if(p3.y>p4.y)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.x<p1.x)
    	{
    		if(p3.x>p4.x)
    		{
    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	if(p2.x>p1.x)
    	{
    		if(p3.x<p4.x)
    		{

    			glVertex2i(p4.x, p4.y);
    			glVertex2i(p3.x, p3.y);
    		}
    		else
    		{
    			glVertex2i(p3.x, p3.y);
    			glVertex2i(p4.x, p4.y);
    		}
    	}
    	else
    	{
    		glVertex2i(p3.x, p3.y);
	   		glVertex2i(p4.x, p4.y);
    	}
    	glEnd();
    }

    public void drawQuad(Point p1,Point p2,Point p3,Point p4,String c)
    {
    	drawBaseQuad(p1,p2,p3,p4,c,GL_QUADS);
    }

    public void drawEmptyQuad(Point p1,Point p2,Point p3,Point p4,String c)
    {
    	drawBaseQuad(p1,p2,p3,p4,c,GL_LINE_LOOP);
    }

    public void drawCircle(Point centre,int radius,String c)
    {
    	//float colortaker[] = AvColor.getColorByRGB(c);
    	//glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_POINTS);
    	Point p1 = new Point();
		for(p1.x = centre.x-radius/2;p1.x <= centre.x+radius/2;p1.x++)
		{
			for(p1.y = centre.y-radius/2;p1.y <= centre.y+radius/2;p1.y++)
			{
				double dist = distance(centre,p1);
				if(dist <= radius/2)
				{
					drawRawPixel(p1,c);
				}
			}
		}
		glEnd();
    }

    public void drawEllipse(Point f1,Point f2,int dist,String c)
    {
    	//float colortaker[] = AvColor.getColorByRGB(c);
    	//glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	Point p1 = new Point();
    	double i,j;
    	glBegin(GL_POINTS);
    	for (p1.y=f1.y-dist;p1.y<f2.y+dist;p1.y++)
    	{
    		for (p1.x=f1.x-dist;p1.x<f2.x+dist;p1.x++)
    		{
    			i = distance(p1,f1);
    			j = distance(p1,f2);
    			if ((i+j)<=dist)
    			{
    				drawRawPixel(p1,c);
    			}
    		}
    	}
    	glEnd();
    }

    public void drawBaseThunder(Point p,int taille,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	float longu = taille/2 + taille/10;
    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taille);
    	glVertex2f(p.x+longu, p.y+taille-longu);
    	glVertex2d(p.x+taille-longu, p.y+longu-(taille/5)*Math.sqrt(2));

    	glVertex2i(p.x+taille, p.y);
    	glVertex2f(p.x+taille-longu, p.y+longu);
		glVertex2d(p.x+longu, p.y+taille-longu+(taille/5)*Math.sqrt(2));
		glEnd();
    }

    public void drawThunder(Point p,int taille,String c)
    {
    	drawBaseThunder(p,taille,c,GL_TRIANGLES);
    }

    public void drawEmptyThunder(Point p,int taille,String c)
    {
    	drawBaseThunder(p,taille,c,GL_LINE_LOOP);
    }

    public void drawBaseStar(Point p,int taillebranche,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taillebranche*3);
    	glVertex2i(p.x+taillebranche*4, p.y+taillebranche*3);
    	glVertex2i(p.x+taillebranche*2, p.y-1);
    	glEnd();

    	glBegin(GL_SET);
    	glVertex2i(p.x, p.y+taillebranche);
    	glVertex2i(p.x+taillebranche*4, p.y+taillebranche);
    	glVertex2i(p.x+taillebranche*2, p.y+taillebranche*4+1);
		glEnd();
    }

    public void drawStar(Point p,int taillebranche,String c)
    {
    	drawBaseStar(p,taillebranche,c,GL_TRIANGLES);
    }

    public void drawEmptyStar(Point p,int taillebranche,String c)
    {
    	drawBaseStar(p,taillebranche,c,GL_LINE_LOOP);
    }

    public void drawCenterStar(Point p,int taillebranche,String c)
    {
    	Point r = new Point(p.x-taillebranche*2,p.y-taillebranche*2);
    	drawStar(r,taillebranche,c);
    }

    public void drawPixel(Point p1, String c)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_POINTS);
    	glVertex2i(p1.x,p1.y);
    	glEnd();
    }
    
    protected void drawRawPixel(Point p1, String c)		//glBegin avant, glEnd apres la méthode
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glVertex2i(p1.x,p1.y);
    }
    
    public void drawPixelScreen(Point p1,Point p2,byte[][] colorId,String[] colorsArray)
    {
    	glBegin(GL_POINTS);
    	Point current = p1.clone(0);
    	for(int i = p1.x;i < p2.x;i++)
    	{
    		current.x = i;
    		for(int j = p1.y;j < p2.y;j++)
    		{
    			current.y = j;
    			drawRawPixel(current,colorsArray[colorId[i][j]]);
    		}
    	}
    	glEnd();
    }

    public void drawBaseTriangle(Point p1,Point p2,Point p3,String c,int GL_SET)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    	glBegin(GL_SET);
    	glVertex2i(p1.x, p1.y);
    	glVertex2i(p2.x, p2.y);
    	glVertex2i(p3.x, p3.y);
    	glEnd();
    }

    public void drawTriangle(Point p1,Point p2,Point p3,String c)
    {
    	drawBaseTriangle(p1,p2,p3,c,GL_TRIANGLES);
    }

    public void drawEmptyTriangle(Point p1,Point p2,Point p3,String c)
    {
    	drawBaseTriangle(p1,p2,p3,c,GL_LINE_LOOP);
    }
	
    public void drawLine(Point p1,Point p2,String c)
    {
    	float colortaker[] = AvColor.getColorByRGB(c);
    	glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
        glBegin(GL_LINES);
        glVertex2i(p1.x, p1.y);
        glVertex2i(p2.x, p2.y);
        glEnd();
    }
    
    /*public void drawVBOTriangle()
    {
    	try (MemoryStack stack = MemoryStack.stackPush()) {
    	FloatBuffer vertices = stack.mallocFloat(3 * 6);
    	vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
    	vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
    	vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
    	vertices.flip();
    	int vbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER, vbo);
    	glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    	}
    }
    
    public void drawVAOTriangle()
    {
    	int vao = glGenVertexArrays();
    	glBindVertexArray(vao);
    }*/
}
