package avalone.api.lwjgl3;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import avalone.api.util.Point;

public class BasicFuncs 
{
	/*protected long window;
	protected long[] otherWindows;*/
	protected long[] windows;
	private int counter;
	protected GLFWErrorCallback errorCallback;
	//protected GLFWKeyCallback keyCallback;
	protected GLFWKeyCallback[] keyCallbacks;
	protected GLFWScrollCallback[] scrollCallbacks;
	protected GLFWMouseButtonCallback[] mouseCallbacks;
	protected int threadCounter;
	protected Thread[] threads;
	
	private HashMap<Integer,Character> keys;
	private ArrayList<Integer> keyPile;
	private boolean leftClick;
	private boolean rightClick;

	private double lastFrame;
	private long variableYieldTime, lastTime;
	
	protected Point scroll;
	protected Point modif;
	protected Point scale;
	private double mouseScrollX;
	private double mouseScrollY;
	
	protected BasicFuncs(int width, int height, String title)
	{
		scroll = new Point();
		modif = new Point();
		scale = new Point(1,1);
		mouseScrollX = 0;
		mouseScrollY = 0;
		/*otherW*/windows = new long[10];
		keyCallbacks = new GLFWKeyCallback[10];
		scrollCallbacks = new GLFWScrollCallback[10];
		mouseCallbacks = new GLFWMouseButtonCallback[10];
		threads = new Thread[10];
		keys = new HashMap<Integer,Character>();
		keyPile = new ArrayList<Integer>();
		leftClick = false;
		rightClick = false;
		initKeys();
		counter = 0;
		threadCounter = -1;
		loadNatives();
    	initOpenGL(width, height, title);
	}
	
	public void initKeys()
	{
		keys.put(GLFW_KEY_A, 'a');
		keys.put(GLFW_KEY_B, 'b');
		keys.put(GLFW_KEY_C, 'c');
		keys.put(GLFW_KEY_D, 'd');
		keys.put(GLFW_KEY_E, 'e');
		keys.put(GLFW_KEY_F, 'f');
		keys.put(GLFW_KEY_G, 'g');
		keys.put(GLFW_KEY_H, 'h');
		keys.put(GLFW_KEY_I, 'i');
		keys.put(GLFW_KEY_J, 'j');
		keys.put(GLFW_KEY_K, 'k');
		keys.put(GLFW_KEY_L, 'l');
		keys.put(GLFW_KEY_M, 'm');
		keys.put(GLFW_KEY_N, 'n');
		keys.put(GLFW_KEY_O, 'o');
		keys.put(GLFW_KEY_P, 'p');
		keys.put(GLFW_KEY_Q, 'q');
		keys.put(GLFW_KEY_R, 'r');
		keys.put(GLFW_KEY_S, 's');
		keys.put(GLFW_KEY_T, 't');
		keys.put(GLFW_KEY_U, 'u');
		keys.put(GLFW_KEY_V, 'v');
		keys.put(GLFW_KEY_W, 'w');
		keys.put(GLFW_KEY_X, 'x');
		keys.put(GLFW_KEY_Y, 'y');
		keys.put(GLFW_KEY_Z, 'z');
		keys.put(GLFW_KEY_SEMICOLON, ';');
		keys.put(GLFW_KEY_COMMA, ',');
		keys.put(GLFW_KEY_SPACE, ' ');
		keys.put(GLFW_KEY_LEFT, '←');
		keys.put(GLFW_KEY_RIGHT, '→');
		keys.put(GLFW_KEY_UP, '↑');
		keys.put(GLFW_KEY_DOWN, '↓');
		azertyTransform();
	}
	
	public void azertyTransform()
	{
		keys.replace(GLFW_KEY_A, 'a', 'q');
		keys.replace(GLFW_KEY_Q, 'q', 'a');
		keys.replace(GLFW_KEY_W, 'w', 'z');
		keys.replace(GLFW_KEY_Z, 'z', 'w');
		keys.replace(GLFW_KEY_SEMICOLON, ';', 'm');
		keys.replace(GLFW_KEY_M, 'm', ',');
		keys.replace(GLFW_KEY_COMMA, ',', ';');
	}
	
	public boolean windowShouldNotClose()
    {
    	return glfwWindowShouldClose(windows[counter]) == GL_FALSE ;
    }
	
	public boolean windowShouldClose()
    {
    	return glfwWindowShouldClose(windows[counter]) == GL_TRUE ;
    }
	
	public boolean lastSubWindowShouldNotClose()
    {
    	return glfwWindowShouldClose(windows[counter]) == GL_FALSE ;
    }
	
	public boolean lastSubWindowwindowShouldClose()
    {
    	return glfwWindowShouldClose(windows[counter]) == GL_TRUE ;
    }

    public void glLoopBegin()
    {
    	glClear(GL_COLOR_BUFFER_BIT);
    }

    public void glLoopEnd(int i)
    {
    	sync(i);
    	glfwSwapBuffers(windows[counter]);
    	glfwPollEvents();
    }
    
    public void glLoopEnd()
    {
    	glLoopEnd(80);
    }
    
    private void sync(int fps) {
        if (fps <= 0) return;
          
        long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
        // yieldTime + remainder micro & nano seconds if smaller than sleepTime
        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
        long overSleep = 0; // time the sync goes over by
          
        try {
            while (true) {
                long t = System.nanoTime() - lastTime;
                  
                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);
                }else if (t < sleepTime) {
                    // burn the last few CPU cycles to ensure accuracy
                    Thread.yield();
                }else {
                    overSleep = t - sleepTime;
                    break; // exit while loop
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);
             
            // auto tune the time sync should yield
            if (overSleep > variableYieldTime) {
                // increase by 200 microseconds (1/5 a ms)
                variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
            }
            else if (overSleep < variableYieldTime - 200*1000) {
                // decrease by 2 microseconds
                variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
            }
        }
    }
    
    public void destroyDisplay()
    {
    	while(counter > 0)
    	{
    		destroyLastSubWindow();
    	}
    	glfwDestroyWindow(windows[0]);
    	keyCallbacks[0].release();
    	scrollCallbacks[0].release();
    	glfwTerminate();
   	 	errorCallback.release();
    }
    
    public int abs(int nb)
    {
        if(nb < 0)
        {
            return -nb;
        }
        return nb;
    }
    
    public static int maxi(int n1,int n2)
    {
        if(n1 > n2)
        {
            return n1;
        }
        return n2;
    }
    
    public static int mini(int n1,int n2)
    {
        if(n1 < n2)
        {
            return n1;
        }
        return n2;
    }
    
    public double getTime()
    {
    	return GLFW.glfwGetTime();
    }
    
    public void clearFilter()
    {
    	glColor4f(1.0f,1.0f,1.0f,1.0f);
    }
    
    public void setFilter(String c)
    {
    	float[] colortaker = AvColor.getColorByRGB(c);
		glColor3f(colortaker[0]/255.0f,colortaker[1]/255.0f,colortaker[2]/255.0f);
    }
    
    public void setFilter(float[] color)
    {
		glColor4f(color[0],color[1],color[2],color[3]);
    }
    
    public double distance(Point p1,Point p2)
    {
    	double distance = Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
    	return distance;
    }
    
    public Point getMouse()
    {
    	DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    	DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
    	glfwGetCursorPos(windows[counter],x,y);
        x.rewind();	y.rewind();
        double tempX = x.get();
        double tempY = y.get();
        
        Point size = getWindowSize(windows[counter]);
        
        Point p = new Point((int)tempX + scroll.x,size.y - (int)tempY + scroll.y);
    	return p;
    }
    
    public Point getGuiMouse()
    {
    	DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    	DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
    	glfwGetCursorPos(windows[counter],x,y);
        x.rewind();	y.rewind();
        double tempX = x.get();
        double tempY = y.get();
        
        Point p = new Point((int)tempX,(int)tempY);
        return p;
    }
    
    public boolean hasLeftClicked()
    {
    	//return glfwGetMouseButton(windows[counter], GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;
    	return leftClick;
    }
    
    public boolean hasRightClicked()
    {
    	//return glfwGetMouseButton(windows[counter], GLFW_MOUSE_BUTTON_2) == GLFW_PRESS;
    	return rightClick;
    }
    
    public char lastPressedKey()
    {
    	/*if(pressedKey != -1)
    	{
    		return keys.get(pressedKey);
    	}*/
    	if(keyPile.size() > 0)
    	{
    		Character key = keys.get(keyPile.get(keyPile.size() - 1));
    		if(key == null)
    		{
    			return 0;
    		}
    		//System.out.println(keyPile.get(0));
    		return key;
    	}
    	else
    	{
    		return 0;
    	}
    }
    
    public char lastPressedKeyBetween(Character... chars)
    {
    	/*System.out.print("chars a trouver : ");
    	for(int j = 0;j < chars.length;j++)
    	{
    		System.out.print(chars[j] + ", ");
    	}
    	System.out.println();
    	System.out.print("chars dans la pile : ");
    	for(int i = keyPile.size()-1;i >= 0;i--)
    	{
    		System.out.print(keys.get(keyPile.get(i)) + ", ");
    	}
    	System.out.println();*/
    	for(int i = keyPile.size()-1;i >= 0;i--)
    	{
    		for(int j = 0;j < chars.length;j++)
        	{
    			if(keys.get(keyPile.get(i)) != null && keys.get(keyPile.get(i)) == chars[j].charValue())
    			{
    				return chars[j];
    			}
        	}
    	}
    	return 0;
    }
    
    public boolean isKeyDown(char c)
    {
    	for(int i = 0;i < keyPile.size();i++)
    	{
    		Character key = keys.get(keyPile.get(i));
    		if(key != null && key == c)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    public String checkMouseWheel()
    {
        if (mouseScrollY < 0) 
        {
        	mouseScrollY = 0;
            return "DOWN";
        } 
        else if (mouseScrollY > 0)
        {
        	mouseScrollY = 0;
            return "UP";
        }
        else
        {
        	return "EQUAL";
        }
    }
    
    public int changeValueWithWheel(int val,int mul)
    {
    	if(mul == 0)
    	{
    		System.out.println("Warning, value linked to wheel won't change.");
    	}
    	String s = checkMouseWheel();
    	if(s.equals("DOWN"))
    	{
    		val = val - mul;
    	}
    	else if(s.equals("UP"))
    	{
    		val = val + mul;
    	}
    	return val;
    }
    
    public void scroll(int dx,int dy)
    {
    	scroll.moveCoords(dx, dy);
    	glLoadIdentity();
    	
    	Point size = getWindowSize(windows[counter]);
        
    	glOrtho(scroll.x, size.x + scroll.x, scroll.y, size.y + scroll.y, -1.0, 1.0);
    }
    
    public void setView(int x,int y)
    {
    	scroll.setCoords(x, y);
    	glLoadIdentity();
    	
    	Point size = getWindowSize(windows[counter]);
        
    	glOrtho(scroll.x,size.x + scroll.x, scroll.y, size.y + scroll.y, -1.0, 1.0);
    }
    
    public void beginRotate(Point p1,int angle)
    {
    	glPushMatrix();
    	glTranslatef(p1.x ,p1.y , 0); //Translate to the center
        glRotatef( angle, 0, 0, 1.0f ); // now rotate
        glTranslatef(-p1.x ,-p1.y , 0);
    }
    
    public void endRotate()
    {
    	glPopMatrix();
    }
    
    public void zoom(int z)
    {
    	if(z % 2 != 0)
    	{
    		z++;
    	}
    	Point size = getWindowSize(windows[counter]);
    	modif.x = -((z-1)*size.x)/2;
    	modif.y = -((z-1)*size.y)/2;
    	scale.x = z;
    	scale.y = z;
    	glViewport(scroll.x+modif.x,scroll.y+modif.y,size.x*z,size.y*z);
    }
    
    public void unzoom(int z)
    {
    	if(z % 2 != 0)
    	{
    		z--;
    	}
    	Point size = getWindowSize(windows[counter]);
    	modif.x = ((z-1)*size.x)/(2*z);
    	modif.y = ((z-1)*size.y)/(2*z);
    	glViewport(scroll.x+modif.x,scroll.y+modif.y,size.x/z,size.y/z);
    }
    
    public void clearZoom()
    {
    	modif.x = 0;
    	modif.y = 0;
    	scale.x = 1;
    	scale.y = 1;
    	Point size = getWindowSize(windows[counter]);
    	glViewport(scroll.x,scroll.y,size.x,size.y);
    }
    
    private Point getWindowSize(long window)
    {
    	IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        width.rewind(); height.rewind();
        
        return new Point(width.get(),height.get());
    }
    
    public void mustsleep(int p)
    {
    	try
    	{
    		Thread.sleep(p);
    	}
    	catch(InterruptedException ie)
    	{
    		ie.printStackTrace();
    		System.exit(0);
    	}
    }
    
    public void addSubWindow(int width,int heigth, String title)
    {
    	counter++;
    	baseWindowCreation(counter,width,heigth,title,windows[0]);
    }
    
    public void addThreadWindow(int width,int heigth, String title)
    {
    	threadCounter++;
    	threads[threadCounter] = new Thread(new Runnable()
    	{
			public void run() 
			{
				counter++;
		    	baseWindowCreation(counter,width,heigth,title,NULL);
				glEnable(GL_TEXTURE_2D);
			    while (lastSubWindowShouldNotClose()) 
			    {
			    	glLoopBegin();
			    	 
			    	System.out.println("press escape");
			    	 
			    	glLoopEnd();
			    	//System.out.println(glapi.lastSubWindowShouldNotClose());
			    	try 
			    	{
			    		Thread.currentThread().sleep(100);
			    	} 
			    	catch (InterruptedException e) 
			    	{
						e.printStackTrace();
					}
			    }
				destroyLastSubWindow();
			}
		});
    	threads[threadCounter].start();
    	try 
    	{
			threads[threadCounter].join();
		} 
    	catch (InterruptedException e) 
    	{
			e.printStackTrace();
		}
    	threadCounter--;
    }
    
    public void destroyLastSubWindow()
    {
    	glfwDestroyWindow(windows[counter]);
    	keyCallbacks[counter].release();
    	scrollCallbacks[counter].release();
    	counter--;
    	glfwMakeContextCurrent(windows[counter]);
    }

    public void initOpenGL(int width,int heigth, String title)
    {
    	glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");
	 
	    glfwDefaultWindowHints();
	    glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
	    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
	 
	    baseWindowCreation(0,width,heigth,title,NULL);
	}
    
    private void baseWindowCreation(int indice,int width,int heigth, String title,long attachedWindow)
    {
    	windows[indice] = glfwCreateWindow(width, heigth, title, NULL, attachedWindow);
	    if ( windows[indice] == NULL )
	    	throw new RuntimeException("Failed to create the GLFW window");
	 
	    glfwSetKeyCallback(windows[indice], keyCallbacks[indice] = new GLFWKeyCallback() 
	    {
	    	@Override
	        public void invoke(long window, int key, int scancode, int action, int mods) 
	        {
	            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
	            	glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
	            else if(action == GLFW_PRESS)
	            {
	            	keyPile.add(key);
	            }
	            else if(action == GLFW_RELEASE)
	            {
	            	keyPile.remove(Integer.valueOf(key));
	            }
	        }
	     });
	    
	    glfwSetMouseButtonCallback(windows[indice], mouseCallbacks[indice] = new GLFWMouseButtonCallback() {
	        @Override
	        public void invoke(long window, int button, int action, int mods) 
	        {
	        	if(button == GLFW_MOUSE_BUTTON_1)
	        	{
	        		if(action == GLFW_PRESS)
	        		{
	        			leftClick = true;
	        		}
	        		else if(action == GLFW_RELEASE)
	        		{
	        			leftClick = false;
	        		}
	        	}
	        	else if(button == GLFW_MOUSE_BUTTON_2)
	        	{
	        		if(action == GLFW_PRESS)
	        		{
	        			rightClick = true;
	        		}
	        		else if(action == GLFW_RELEASE)
	        		{
	        			rightClick = false;
	        		}
	        	}
	        }
	    });
	    
	    glfwSetScrollCallback(windows[indice], scrollCallbacks[indice] = new GLFWScrollCallback() {
	        @Override
	        public void invoke(long window, double xoffset, double yoffset) 
	        {
	        	mouseScrollX = xoffset;
	        	mouseScrollY = yoffset;
	        }
	    });
	     ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	     glfwSetWindowPos(windows[indice], (GLFWvidmode.width(vidmode) - width) / 2,
	    		 		  		  (GLFWvidmode.height(vidmode) - heigth) / 2);
	     glfwMakeContextCurrent(windows[indice]);
	     //glfwSwapInterval(0);
	     glfwShowWindow(windows[indice]);
	     
	     GLContext.createFromCurrent();
	     
	     glOrtho(0.0, width, 0.0, heigth, -1.0, 1.0);
	     glViewport(0,0,width,heigth);
	     glEnable(GL_BLEND);
	     glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public void setWindowTitle(String title)
    {
    	glfwSetWindowTitle(windows[counter], title);
    }
	
    private void loadNatives()
	{
	     System.out.println("[AvaloneGLAPI] Loading LWJGL native files...");
	     String arch = findArchAndOS();
	     try
	     {
	    	 System.out.println(System.getProperty("user.dir")
	        		 + File.separator + "native" + File.separator + arch);
	         System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir")
	        		 + File.separator + "native" + File.separator + arch);
	         System.out.println("[AvaloneGLAPI] Native files loaded!\n");
	         
	         /*final String classAsPath = className.replace('.', '/') + ".class";
	         final InputStream input = ClassLoader.getSystemResourceAsStream( path/to/class );

	         Now if the class is not part of the JAR, and it isn't in the manifest's Class-Path, then the class loader won't find it. Instead, you can use the URLClassLoader, with some care to deal with differences between windows and Unix/Linux/MacOSX.

	         // the class to load
	         final String classAsPath = className.replace('.', '/') + ".class";

	         // the URL to the `app.jar` file (Windows and Unix/Linux/MacOSX below)
	         final URL url = new URL( "file", null, "///C:/Users/diffusive/app.jar" );
	         //final URL url = new URL( "file", null, "/Users/diffusive/app.jar" );

	         // create the class loader with the JAR file
	         final URLClassLoader urlClassLoader = new URLClassLoader( new URL[] { url } );

	         // grab the resource, through, this time from the `URLClassLoader` object
	         // rather than from the `ClassLoader` class
	         final InputStream input = urlClassLoader.getResourceAsStream( classAsPath );*/
	     }
	     catch(Exception e)
	     {
	         System.err.println("[AvaloneGLAPI] Failed to load natives\nYour application will shut down...");
	         System.exit(1);
	     }
	 }
   
    private String findArchAndOS()
	{
		 String toReturn = "";
		 if(System.getProperty("os.name").toLowerCase().contains("linux"))
		 {
			 toReturn = "linux";
		 }
		 else if(System.getProperty("os.name").toLowerCase().contains("win"))
		 {
			 toReturn = "windows";
		 }
		 else if(System.getProperty("os.name").toLowerCase().contains("mac"))
		 {
			 toReturn = "macosx";
		 }
		 if(System.getProperty("os.arch").contains("64"))
		 {
			 return toReturn + File.separator + "x64";
		 }
		 else if(System.getProperty("os.arch").contains("86"))
		 {
			 return toReturn + File.separator + "x86";
		 }
		 else
		 {
			 System.err.println("error, could not find OS architecture");
			 System.exit(1);
			 return "";
		 }
	 }
}
