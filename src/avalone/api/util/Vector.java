package avalone.api.util;


public class Vector
{	
	public float x;
	public float y;
	public float z;
	
	public Vector()
    {
    	setCoords(0,0,0);
    }
    
    public Vector(float x, float y)
    {
    	setCoords(x,y,0);
    }
    
    public Vector(float x, float y, float z)
    {
    	setCoords(x,y,z);
    }
    
    public Vector(Point p)
    {
    	x = p.x;
    	y = p.y;
    	z = p.z;
    }
    
    public Vector clone(int ad)
    {
    	return new Vector(x+ad,y+ad,z+ad);
    }
    
    public Vector clone(int adX,int adY)
    {
    	return new Vector(x+adX,y+adY,z);
    }
    
    public Vector clone(float ad)
    {
    	return new Vector(x+ad,y+ad,z+ad);
    }
    
    public Vector clone(float adX,float adY)
    {
    	return new Vector(x+adX,y+adY,z);
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
    
    public void moveCoords(float dx,float dy,float dz)
    {
    	setCoords(x+dx,y+dy,z+dz);
    }
    
    public void moveCoords(float dx,float dy)
    {
    	setCoords(x+dx,y+dy);
    }
    
    public Vector scale(float val)
    {
    	return new Vector(x * val,y * val);
    }
    
    public static Vector normalize(Vector v)
    {
    	float length = (float) Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
    	return new Vector(v.x/length,v.y/length,v.z/length);
    }
    
    public static Vector add(Vector p1,Vector p2)
    {
    	return new Vector(p1.x + p2.x,p1.y + p2.y,p1.z + p2.z);
    }
    
    public static Vector sub(Vector p1,Vector p2)
    {
    	return new Vector(p1.x - p2.x,p1.y - p2.y,p1.z - p2.z);
    }
    
    public static float dotProduct(Vector vec1,Vector vec2)
	{
		return vec1.x * vec2.x + vec1.y * vec2.y;
	}
    
    public void add(Vector p)
    {
    	x = x + p.x;
    	y = y + p.y;
    	z = z + p.z;
    }
    
    public void sub(Vector p)
    {
    	//System.out.println(x + "," + p.x);
    	x = x - p.x;
    	//System.out.println(x);
    	y = y - p.y;
    	z = z - p.z;
    }
    
    public void normalize()
    {
    	//System.out.println("before normalize: " + x + "," + y + "," + z);
    	float length = (float) Math.sqrt(x*x + y*y + z*z);
    	//System.out.println("length: " + length);
    	x = x/length;
    	y = y/length;
    	z = z/length;
    	//System.out.println("after normalize: " + x + "," + y + "," + z);
    }
    
    public static Point floor(Vector v)
    {
    	return new Point((int)v.x,(int)v.y);
    }
    
    public static Point round(Vector v)
    {
    	return new Point(Math.round(v.x),Math.round(v.y));
    }
    
    /*public void mul(Point p)
    {
    	setCoords(x * p.x,y * p.y,z * p.z);
    }
    
    public FPoint div(Point p)
    {
    	setCoords(x / p.x,y / p.y,z / p.z);
    	return new FPoint(x % p.x,y % p.y,z % p.z);
    }*/
}
