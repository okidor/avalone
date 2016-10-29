package avalone.negend.annule;

import avalone.api.util.Point;
import avalone.negend.Chunk;

public interface Storage 
{	
	public abstract Point getPos();
	
	public abstract Chunk getCurrentChunk();
}
