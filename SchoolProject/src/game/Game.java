package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class Game
{
	private boolean	mRunning;
	
	public void render(GameContainer gc, Graphics g)
	{
		g.setColor(Color.red);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}
	
	public void init(GameContainer gc)
	{
		mRunning = true;
	}
	
	public void update(GameContainer arg0, int arg1)
	{
		// TODO updates
	}
	
	public boolean isRunning()
	{
		return mRunning;
	}
	
	public void stop()
	{
		mRunning = false;
	}
}
