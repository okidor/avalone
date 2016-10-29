package avalone.negend;

import avalone.api.util.Point;

public interface Behavior
{
	public void determineDraw(Point renderPosDownLeft,Point renderPosUpRight,String componentName,PlayerInfo infos);
	
	public void reactionToClick(Point renderPosDownLeft, Point renderPosUpRight, Point mousePoint,String componentName,boolean leftClick);
}
