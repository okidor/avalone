package avalone.api.util;


public class FPoint extends Point
{
	private static final long serialVersionUID = 1L;
	
	public float x;
	public float y;
	public float z;
	
	public FPoint()
    {
    	setCoords(0,0,0);
    }
    
    public FPoint(float x, float y)
    {
    	setCoords(x,y,0);
    }
    
    public FPoint(float x, float y, float z)
    {
    	setCoords(x,y,z);
    }
    
    public FPoint clone(int ad)
    {
    	return new FPoint(x+ad,y+ad,z+ad);
    }
    
    public FPoint clone(int adX,int adY)
    {
    	return new FPoint(x+adX,y+adY,z);
    }
    
    public FPoint clone(float ad)
    {
    	return new FPoint(x+ad,y+ad,z+ad);
    }
    
    public FPoint clone(float adX,float adY)
    {
    	return new FPoint(x+adX,y+adY,z);
    }
    
    public void setCoords(float newx,float newy,float newz)
    {
    	x = newx;
    	y = newy;
    	z = newz;
    }
    
    public void setCoords(float newx,float newy)
    {
    	x = newx;
    	y = newy;
    }
    
    public void movex(float dx)
    {
    	x = x + dx;
    }
    
    public void movey(float dy)
    {
    	y = y + dy;
    }
    
    public void movez(float dz)
    {
    	z = z + dz;
    }
    
    public void moveCoords(float dx,float dy,float dz)
    {
    	setCoords(x+dx,y+dy,z+dz);
    }
    
    public void moveCoords(float dx,float dy)
    {
    	setCoords(x+dx,y+dy);
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
    
    public FPoint div(Point p)
    {
    	setCoords(x / p.x,y / p.y,z / p.z);
    	return new FPoint(x % p.x,y % p.y,z % p.z);
    }
}
