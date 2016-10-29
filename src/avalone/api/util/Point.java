package avalone.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class Point implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int x;
    public int y;
    public int z;

    public Point()
    {
    	x = 0;
    	y = 0;
    	z = 0;
    }
    
    public Point(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    	z = 0;
    }
    
    public Point(int x, int y, int z)
    {
    	this.x = x;
    	this.y = y;
    	this.z = z;
    }
    
    public Point clone(int ad)
    {
    	return new Point(x+ad,y+ad,z+ad);
    }
    
    public Point clone(int adX,int adY)
    {
    	return new Point(x+adX,y+adY,z);
    }
    
    public void setCoords(int newx,int newy,int newz)
    {
    	x = newx;
    	y = newy;
    	z = newz;
    }
    
    public void setCoords(int newx,int newy)
    {
    	x = newx;
    	y = newy;
    }
    
    public void moveCoords(int dx,int dy,int dz)
    {
    	setCoords(x+dx,y+dy,z+dz);
    }
    
    public void moveCoords(int dx,int dy)
    {
    	setCoords(x+dx,y+dy);
    }
    
    public static Point add(Point p1,Point p2)
    {
    	return new Point(p1.x + p2.x,p1.y + p2.y,p1.z + p2.z);
    }
    
    public static Point sub(Point p1,Point p2)
    {
    	return new Point(p1.x - p2.x,p1.y - p2.y,p1.z - p2.z);
    }
    
    public void add(Point p)
    {
    	moveCoords(p.x,p.y,p.z);
    }
    
    public void sub(Point p)
    {
    	moveCoords(-p.x,-p.y,-p.z);
    }
    
    public void mul(Point p)
    {
    	setCoords(x * p.x,y * p.y,z * p.z);
    }
    
    public Point div(Point p)
    {
    	setCoords(x / p.x,y / p.y,z / p.z);
    	return new Point(x % p.x,y % p.y,z % p.z);
    }
    
    public static Point receivePoint(BufferedReader in)
    {
        try
        {
            String tmp = in.readLine();
            String tmp2 = in.readLine();
            int x = Integer.parseInt(tmp);
            int y = Integer.parseInt(tmp2);  
            Point p1 = new Point(x,y);
            return p1;
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        return new Point(0,0);
    }
    
    public static void sendPoint(PrintWriter out,Point p)
    {
        int x = p.x;
        int y = p.y;
        //String tmp = String.valueOf(x);
        //String tmp2 = String.valueOf(y);
        out.println(x);
        out.println(y);
    }
}
