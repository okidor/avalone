package avalone.physics;

import avalone.api.util.Vector;
import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

import java.lang.Math;
import java.util.ArrayList;

public class Physics 
{
	public static final int gravity = -1;
	private static Point currentCollisionNormal;
	private static float currentOverlap;
	public static boolean debug;
	
	public static void applyGrav(Falling falling)
	{
		falling.setVerticalPos(falling.getVerticalPos() + falling.getVerticalSpeed());
		falling.setVerticalSpeed(falling.getVerticalSpeed() + gravity);
	}
	
	/*public static boolean hasCollidedDebug(Solid solid1,Solid solid2)
	{
		float overlap = 1000000000f;
		Point smallestAxis = null;
		ArrayList<Point> axes1 = solid1.getNormals();
		ArrayList<Point> axes2 = solid2.getNormals();
		solid1.debugGetNormals();
		solid2.debugGetNormals();
		Point means = null;
		for (int i = 0; i < axes1.size(); i++) 
		{
			Point axis = axes1.get(i);
			// project both shapes onto the axis
			Vector proj1 = solid1.project(axis);
			Vector proj2 = solid2.project(axis);
			// do the projections overlap?
			float tmpOverlap = computeOverlap(proj1,proj2);
			//System.out.println(String.format("%3.24f",tmpOverlap));
			if (tmpOverlap < 0.0001f) 
			{
				// then we can guarantee that the shapes do not overlap
				return false;
			}
			else
			{
				//System.out.println(proj1.x + "," + proj1.y);
				//System.out.println(proj2.x + "," + proj2.y);
				if(tmpOverlap < overlap)
				{
					overlap = tmpOverlap;
					smallestAxis = axis;
					Point p1 = solid1.vertices.get(i);
					Point p2 = solid1.vertices.get(i + 1 == solid1.vertices.size() ? 0 : i + 1);
					means = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
				}
			}
		}
		for (int i = 0; i < axes2.size(); i++) 
		{
			Point axis = axes2.get(i);
			// project both shapes onto the axis
			Vector proj1 = solid1.project(axis);
			Vector proj2 = solid2.project(axis);
			// do the projections overlap?
			float tmpOverlap = computeOverlap(proj1,proj2);
			//System.out.println(String.format("%3.24f",tmpOverlap));
			if (tmpOverlap < 0.0001f) 
			{
				// then we can guarantee that the shapes do not overlap
				return false;
			}
			else
			{
				if(tmpOverlap < overlap)
				{
					overlap = tmpOverlap;
					smallestAxis = axis;
					Point p1 = solid2.vertices.get(i);
					Point p2 = solid2.vertices.get(i + 1 == solid1.vertices.size() ? 0 : i + 1);
					means = new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
				}
			}
		}
		// if we get here then we know that every axis had overlap on it
		// so we can guarantee an intersection
		//System.out.println("collision detected "); //(" + solid1.posBottomLeft.x + "," + solid1.posBottomLeft.y + "," + solid1.posTopRight.x + "," + solid1.posTopRight.y + ")");
		//System.out.println(" <==> (" + solid2.posBottomLeft.x + "," + solid2.posBottomLeft.y + "," + solid2.posTopRight.x + "," + solid2.posTopRight.y + ")");
		currentCollisionNormal = smallestAxis;
		currentOverlap = overlap;
		AvaloneGLAPI.getInstance().drawArrow(means,Point.sub(means, smallestAxis), "green");
		AvaloneGLAPI.getInstance().drawCircle(means, 10, "green");
		debug = true;
		return true;
	}*/
	
	public static boolean hasCollided(Solid solid1,Solid solid2)
	{
		float overlap = 1000000000f;
		Point smallestAxis = null;
		ArrayList<Point> axes1 = solid1.getNormals();
		ArrayList<Point> axes2 = solid2.getNormals();
		// loop over the axes1
		for (int i = 0; i < axes1.size(); i++) 
		{
			Point axis = axes1.get(i);
			// project both shapes onto the axis
			Vector proj1 = solid1.project(axis);
			Vector proj2 = solid2.project(axis);
			// do the projections overlap?
			float tmpOverlap = computeOverlap(proj1,proj2);
			//System.out.println(String.format("%3.24f",tmpOverlap));
			if (tmpOverlap < 0.0001f) 
			{
				// then we can guarantee that the shapes do not overlap
				return false;
			}
			else
			{
				//System.out.println(proj1.x + "," + proj1.y);
				//System.out.println(proj2.x + "," + proj2.y);
				if(tmpOverlap < overlap)
				{
					overlap = tmpOverlap;
					smallestAxis = axis;
				}
			}
		}
		// loop over the axes2
		for (int i = 0; i < axes2.size(); i++) 
		{
			Point axis = axes2.get(i);
			// project both shapes onto the axis
			Vector proj1 = solid1.project(axis);
			Vector proj2 = solid2.project(axis);
			// do the projections overlap?
			float tmpOverlap = computeOverlap(proj1,proj2);
			//System.out.println(String.format("%3.24f",tmpOverlap));
			if (tmpOverlap < 0.0001f) 
			{
				// then we can guarantee that the shapes do not overlap
				return false;
			}
			else
			{
				if(tmpOverlap < overlap)
				{
					overlap = tmpOverlap;
					smallestAxis = axis;
				}
			}
		}
		// if we get here then we know that every axis had overlap on it
		// so we can guarantee an intersection
		//System.out.println("collision detected "); //(" + solid1.posBottomLeft.x + "," + solid1.posBottomLeft.y + "," + solid1.posTopRight.x + "," + solid1.posTopRight.y + ")");
		//System.out.println(" <==> (" + solid2.posBottomLeft.x + "," + solid2.posBottomLeft.y + "," + solid2.posTopRight.x + "," + solid2.posTopRight.y + ")");
		currentCollisionNormal = smallestAxis;
		currentOverlap = overlap;
		return true;
	}
	
	public static float getOverlap()
	{
		return currentOverlap;
	}
	
	public static Point getCollisionNormal()
	{
		return currentCollisionNormal;
	}
	
	public static float computeOverlap(Vector proj1,Vector proj2)
	{
		float min1 = proj1.x;
		float max1 = proj1.y;
		float min2 = proj2.x;
		float max2 = proj2.y;
		return Math.max(0, Math.min(max1, max2) - Math.max(min1, min2));
	}
	
	public static boolean resolveCollision(Solid solid1,Solid solid2)
	{
		if(hasCollided(solid1,solid2))
		{
			//System.out.println("current: " + currentCollisionNormal.x + "," + currentCollisionNormal.y + "," + currentOverlap);
			// Calculate relative velocity
			Vector rv = Vector.sub(solid2.linearVelocity, solid1.linearVelocity);
			//System.out.println("rv: " + rv.x + "," + rv.y);
			
			// Calculate relative velocity in terms of the normal direction
			Vector fCollisionNormal = new Vector(currentCollisionNormal);
			fCollisionNormal.normalize();
			//System.out.println("fcurrent: " + fCollisionNormal.x + "," + fCollisionNormal.y);
			float velAlongNormal = Vector.dotProduct( rv, fCollisionNormal);
			//System.out.println("velAlongNormal: " + velAlongNormal);
			 
			// Do not resolve if velocities are separating
			/*if(velAlongNormal > 0)
				return;*/
			 
			// Calculate restitution
			float e = Math.min( solid1.restitution, solid2.restitution);
			//System.out.println("restitution: " + e);
			 
			// Calculate impulse scalar
			float j = -(1 + e) * velAlongNormal;
			//System.out.println("j1:" + j);
			//System.out.println("j /= 1 / " + solid1.mass + " + 1 / " + solid2.mass);
			j /= 1 / solid1.mass + 1 / solid2.mass;
			//System.out.println("j2:" + j);
			// Apply impulse
			//System.out.println("normal:" + fCollisionNormal.x + "," + fCollisionNormal.y);
			Vector impulse = fCollisionNormal.scale(j);
			//System.out.println("impulse:" + impulse.x + "," + impulse.y);
			//System.out.println(solid1.linearVelocity.x + "," + solid1.linearVelocity.y);
			//System.out.println("scaled impulse:" + impulse.scale(1 / solid1.mass).x + "," + impulse.scale(1 / solid1.mass).y);
			solid1.linearVelocity.sub(impulse.scale(1 / solid1.mass));
			//System.out.println(solid1.linearVelocity.x + "," + solid1.linearVelocity.y);
			//System.out.println(solid2.linearVelocity.x + "," + solid2.linearVelocity.y);
			//System.out.println("scaled impulse:" + impulse.scale(1 / solid2.mass).x + "," + impulse.scale(1 / solid2.mass).y);
			solid2.linearVelocity.add(impulse.scale(1 / solid2.mass));
			//System.out.println(solid2.linearVelocity.x + "," + solid2.linearVelocity.y);
			
			currentCollisionNormal = null;
			currentOverlap = 0;
			//AvaloneGLAPI.getInstance().mustsleep(10000);
			//System.out.println("==================================================================");
			return true;
		}
		return false;
	}
	
	public static boolean resolveCollisionGlobalLocal(Solid localSolid,Point globalPos,Solid globalSolid)
	{
		int size = localSolid.vertices.size();
		for(int i = 0;i < size;i++)
		{
			localSolid.vertices.get(i).add(globalPos);
		}
		boolean hasCollided = resolveCollision(localSolid,globalSolid);
		for(int i = 0;i < size;i++)
		{
			localSolid.vertices.get(i).sub(globalPos);
		}
		return hasCollided;
	}
	
	public static void resolveAllCollisions(ArrayList<Solid> solids)
	{
		for(int i = 0;i < solids.size();i++)
		{
			for(int j = i;j < solids.size();j++)
			{
				if(i != j)
				{
					resolveCollision(solids.get(i),solids.get(j));
					//hasCollided(solids.get(i),solids.get(j));
					//hasCollidedDebug(solids.get(i),solids.get(j));
				}
			}
		}
	}
	
	public static boolean resolveInterpolatedCollision(Solid solid1,Solid solid2)
	{
		ArrayList<Point> s1Vertices = solid1.getVertices();
		ArrayList<Point> s1OldVertices = solid1.getOldVertices();
		ArrayList<Point> s2Vertices = solid2.getVertices();
		ArrayList<Point> s2OldVertices = solid2.getOldVertices();
		for(float fx1 = 0;fx1 <= 1.0f;fx1 = fx1 + 0.1f)	//TODO scale with speed
		{
			for(float fy1 = 0;fy1 <= 1.0f;fy1 = fy1 + 0.1f)	//TODO scale with speed
			{
				for(float fx2 = 0;fx2 <= 1.0f;fx2 = fx2 + 0.1f)	//TODO scale with speed
				{
					for(float fy2 = 0;fy2 <= 1.0f;fy2 = fy2 + 0.1f)	//TODO scale with speed
					{
						Solid fakeSolid1 = solid1.interpSolid(fx1,fy1);
						Solid fakeSolid2 = solid2.interpSolid(fx2,fy2);
						boolean collided = resolveCollision(fakeSolid1,fakeSolid2);
						if(collided)
						{
							//TODO register floats
						}
					}
				}
			}
		}
		//TODO use all registered floats
		return false;
	}
	
	public static int lerp(int x,int y,float a)
	{
		return (int) (x + a * (y - x));
	}
}
