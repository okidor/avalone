package avalone.physics;

import java.util.ArrayList;

import avalone.api.util.Vector;
import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Solid 
{
	//public Point posBottomLeft;
	//public Point posTopRight;
	public ArrayList<Point> vertices;
	public Vector centerOfMass;
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
		for(int i = 0;i < points.length;i++)
		{
			vertices.add(points[i]);
		}
		this.mass = mass;
		centerOfMass = getCenterOfMass();
		orientation = 0;
		linearVelocity = new Vector(0,0);
		angularVelocity = new Vector(0,0);
		acceleration = new Vector(0,0);
		torque = 0;
		restitution = 0.75f;
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
			vertices.get(i).add(p);
		}
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
			Point tmp = Point.add(p,relativeToCm);
			vertice.setCoords(tmp.x, tmp.y);
			//System.out.print("(" + vertice.x + "," + vertice.y + ") ");
		}
		//System.out.println();
		centerOfMass.x = p.x;
		centerOfMass.y = p.y;
	}
	
	public ArrayList<Point> getVertices()
	{
		return vertices;
	}
	
	public ArrayList<Point> getNormals()
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
	
	public void debugGetNormals()
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
	}
	
	public Vector project(Point axis)
	{
		ArrayList<Point> vertices = getVertices();
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
}
