package avalone.physics;

import java.util.ArrayList;

import avalone.api.util.Vector;
import avalone.api.util.Point;

public class Solid 
{
	//public Point posBottomLeft;
	//public Point posTopRight;
	protected ArrayList<Point> vertices;
	private ArrayList<Point> oldVertices;
	public Vector centerOfMass;
	public Vector oldCenterOfMass;
	public int orientation;
	public float mass;
	public Vector linearVelocity;
	public Vector angularVelocity;
	public Vector acceleration;
	public float torque;
	public float restitution;
	
	public Solid(Point... points)
	{
		this(1.0f,points);
	}
	
	public Solid(float mass,Point... points)
	{
		//this.posBottomLeft = posBottomLeft;
		//this.posTopRight = posTopRight;
		vertices = new ArrayList<Point>();
		oldVertices = new ArrayList<Point>();
		for(int i = 0;i < points.length;i++)
		{
			vertices.add(points[i]);
			oldVertices.add(points[i]);
		}
		this.mass = mass;
		centerOfMass = getCenterOfMass();
		oldCenterOfMass = centerOfMass.clone(0);
		orientation = 0;
		linearVelocity = new Vector(0,0);
		angularVelocity = new Vector(0,0);
		acceleration = new Vector(0,0);
		torque = 0;
		restitution = 1;
	}
	
	public Vector getCenterOfMass()
	{
		float areaSum = 0;
		float cmx = 0;
		float cmy = 0;
		for(int i = 0;i < vertices.size() - 1;i++)
		{
			areaSum = areaSum + vertices.get(i).x * vertices.get(i+1).y - vertices.get(i+1).x * vertices.get(i).y;
			cmx = cmx + (vertices.get(i).x + vertices.get(i+1).x) * (vertices.get(i).x * vertices.get(i+1).y - vertices.get(i+1).x * vertices.get(i).y);
			cmy = cmy + (vertices.get(i).y + vertices.get(i+1).y) * (vertices.get(i).x * vertices.get(i+1).y - vertices.get(i+1).x * vertices.get(i).y);
		}
		int lastIndex = vertices.size() - 1;
		areaSum = areaSum + vertices.get(lastIndex).x * vertices.get(0).y - vertices.get(0).x * vertices.get(lastIndex).y;
		cmx = cmx + (vertices.get(lastIndex).x + vertices.get(0).x) * (vertices.get(lastIndex).x * vertices.get(0).y - vertices.get(0).x * vertices.get(lastIndex).y);
		cmy = cmy + (vertices.get(lastIndex).y + vertices.get(0).y) * (vertices.get(lastIndex).x * vertices.get(0).y - vertices.get(0).x * vertices.get(lastIndex).y);
		areaSum = 0.5f*areaSum;
		cmx = (1/(6*areaSum)) * cmx; 
		cmy = (1/(6*areaSum)) * cmy;
		
		Vector cm = new Vector(cmx,cmy);
		return cm;
	}
	
	public void update()
	{
		linearVelocity.add(acceleration);
		Point p = new Point(Math.round(linearVelocity.x),Math.round(linearVelocity.y));
		moveAllPoints(p);
		//posBottomLeft.add(p);
		//posTopRight.add(p);
		//centerOfMass.add(p);
	}
	
	public void moveAllPoints(Point p)
	{
		for(int i = 0;i < vertices.size();i++)
		{
			oldVertices.get(i).x = vertices.get(i).x;oldVertices.get(i).y = vertices.get(i).y;
			vertices.get(i).add(p);
		}
		oldCenterOfMass.x = centerOfMass.x;
		oldCenterOfMass.y = centerOfMass.y;
		centerOfMass.x = centerOfMass.x + p.x;
		centerOfMass.y = centerOfMass.y + p.y;
	}
	
	public void setAllPoints(Point p)
	{
		//System.out.println("(" + Math.round(centerOfMass.x) + "," + Math.round(centerOfMass.x) + ") ");
		for(int i = 0;i < vertices.size();i++)
		{
			Point relativeToCm = new Point(vertices.get(i).x - Math.round(centerOfMass.x),vertices.get(i).y - Math.round(centerOfMass.y));
			Point vertice = vertices.get(i);
			Point oldVertice = vertices.get(i);
			Point tmp = Point.add(p,relativeToCm);
			vertice.setCoords(tmp.x, tmp.y);
			oldVertice.setCoords(tmp.x, tmp.y);
			//System.out.print("(" + vertice.x + "," + vertice.y + ") ");
		}
		//System.out.println();
		oldCenterOfMass.x = centerOfMass.x;
		oldCenterOfMass.y = centerOfMass.y;
		centerOfMass.x = p.x;
		centerOfMass.y = p.y;
	}
	
	public Solid interpSolid(float fx,float fy)
	{
		Point[] interpolatedVertices = new Point[vertices.size()];
		for(int i = 0;i < vertices.size();i++)
		{
			interpolatedVertices[i] = new Point(lerp(vertices.get(i).x,oldVertices.get(i).x,fx),lerp(vertices.get(i).y,oldVertices.get(i).y,fy));
		}
		Solid inter = new Solid(mass, interpolatedVertices);
		inter.orientation = orientation;
		inter.linearVelocity = linearVelocity;
		inter.angularVelocity = angularVelocity;
		inter.acceleration = acceleration;
		inter.torque = torque;
		return inter;
	}
	
	public void copyState(Solid origin)
	{
		vertices = origin.vertices;
		oldVertices = origin.oldVertices;
		centerOfMass = origin.centerOfMass;
		oldCenterOfMass = origin.oldCenterOfMass;
		orientation = origin.orientation;
		mass = origin.mass;
		linearVelocity = origin.linearVelocity;
		angularVelocity = origin.angularVelocity;
		acceleration = origin.acceleration;
		torque = origin.torque;
		restitution = origin.restitution;
	}
	
	public int getSizeX()
	{
		int min = vertices.get(0).x;
		int max = vertices.get(0).x;
		for(int i = 1;i < vertices.size();i++)
		{
			if(vertices.get(i).x < min)
			{
				min = vertices.get(i).x;
			}
			if(vertices.get(i).x > max)
			{
				max = vertices.get(i).x;
			}
		}
		return max - min;
	}
	
	public int getSizeY()
	{
		int min = vertices.get(0).y;
		int max = vertices.get(0).y;
		for(int i = 1;i < vertices.size();i++)
		{
			if(vertices.get(i).y < min)
			{
				min = vertices.get(i).y;
			}
			if(vertices.get(i).y > max)
			{
				max = vertices.get(i).y;
			}
		}
		return max - min;
	}
	
	public ArrayList<Point> getVertices()
	{
		ArrayList<Point> clonedList = new ArrayList<Point>(vertices.size());
	    for (Point point : vertices) {
	        clonedList.add(point.clone(0));
	    }
	    return clonedList;
	}
	
	public ArrayList<Point> getOldVertices()
	{
		ArrayList<Point> clonedList = new ArrayList<Point>(oldVertices.size());
	    for (Point point : oldVertices) {
	        clonedList.add(point.clone(0));
	    }
	    return clonedList;
	}
	
	public ArrayList<Point> getNormals()
	{
		return getNormals(vertices);
	}
	
	public ArrayList<Point> getOldNormals()
	{
		return getNormals(oldVertices);
	}
	
	private ArrayList<Point> getNormals(ArrayList<Point> vertices)
	{
		ArrayList<Point> axes = new ArrayList<Point>();
		// loop over the vertices
		for (int i = 0; i < vertices.size(); i++) 
		{
			// get the current vertex
			Point p1 = vertices.get(i);
			// get the next vertex
			Point p2 = vertices.get(i + 1 == vertices.size() ? 0 : i + 1);
			// subtract the two to get the edge vector
			Point edge = Point.sub(p1, p2);
			// get either perpendicular vector
			Point normal = new Point(-edge.y,edge.x);
			// the perp method is just (x, y) => (-y, x) or (y, -x)
			axes.add(normal);
		}
		return axes;
	}
	
	/*public void debugGetNormals()
	{
		// loop over the vertices
		for (int i = 0; i < vertices.size(); i++) 
		{
			// get the current vertex
			Point p1 = vertices.get(i);
			// get the next vertex
			Point p2 = vertices.get(i + 1 == vertices.size() ? 0 : i + 1);
			// subtract the two to get the edge vector
			Point edge = Point.sub(p1, p2);
			// get either perpendicular vector
			Point normal = new Point(-edge.y,edge.x);
			// the perp method is just (x, y) => (-y, x) or (y, -x)
			Point means = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
			AvaloneGLAPI.getInstance().drawArrow(means,Point.sub(means, normal), "brown");
		}
	}*/
	
	public Vector project(Point axis)
	{
		ArrayList<Point> vertices = getVertices();
		return project(vertices,axis);
	}
	
	public Vector oldProject(Point axis)
	{
		ArrayList<Point> vertices = getOldVertices();
		return project(vertices,axis);
	}
	
	public Vector project(ArrayList<Point> vertices,Point axis)
	{
		Vector fAxis = new Vector(axis);
		fAxis.normalize();
		float min = Vector.dotProduct(fAxis,new Vector(vertices.get(0)));
		float max = min;
		for (int i = 1; i < vertices.size(); i++) 
		{
			// NOTE: the axis must be normalized to get accurate projections
			float p = Vector.dotProduct(fAxis,new Vector(vertices.get(i)));
			if (p < min) 
			{
				min = p;
			}
			else if (p > max) 
			{
				max = p;
			}
		}
		Vector proj = new Vector(min, max);
		return proj;
	}
	
	public static int lerp(int x,int y,float a)
	{
		return (int) (x + a * (y - x));
	}
}
