package avalone.socket;

public class ThreadTest extends Thread
{
	private String str;
	 
	public ThreadTest(String str) 
	{
	    this.str = str;
	}
	
	@Override
	public void run()
	{
		System.out.println(getId() + ", " + Thread.currentThread().getId());
	}
}
