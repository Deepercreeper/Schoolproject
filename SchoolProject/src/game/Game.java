package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Game
{
	private boolean		mRunning;
	
	private int			mR, mG, mB;
	
	private boolean[]	mMouseDown;
	
	public void render(GameContainer gc, Graphics g)
	{
		g.setColor(new Color(mR, mG, mB));
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}
	
	public void init(GameContainer gc)
	{
		mRunning = true;
		mMouseDown = new boolean[3];
	}
	
	public void update(GameContainer arg0, int arg1)
	{
		if (mMouseDown[Input.MOUSE_LEFT_BUTTON]) color(0, 0, 1);
		if (mMouseDown[Input.MOUSE_RIGHT_BUTTON]) color(0, 0, -1);
	}
	
	public void key(int aKey, boolean aDown)
	{
		if (aDown)
		{
			// Instant keys
			
		}
		else
		{
			// Up keys
			switch (aKey)
			{
				case Input.KEY_ESCAPE :
					stop();
					break;
				default :
					break;
			}
		}
	}
	
	public void mouseClick(int aButton, int aX, int aY)
	{	
		
	}
	
	public void mouseClick(int aButton, boolean aDown)
	{
		mMouseDown[aButton] = aDown;
	}
	
	public void mouseMove(int aXD, int aYD)
	{
		color((int) Math.signum(aXD), (int) Math.signum(aYD), 0);
	}
	
	public boolean isRunning()
	{
		return mRunning;
	}
	
	public void stop()
	{
		mRunning = false;
	}
	
	public void color(int aRd, int aGd, int aBd)
	{
		mR += aRd;
		mG += aGd;
		mB += aBd;
		if (mR < 0) mR = 0;
		if (mG < 0) mG = 0;
		if (mB < 0) mB = 0;
		if (mR > 255) mR = 255;
		if (mG > 255) mG = 255;
		if (mB > 255) mB = 255;
	}
}
