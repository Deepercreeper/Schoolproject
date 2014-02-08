package game;

import game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Game
{
	private boolean			mRunning;
	
	private final boolean[]	mMouseDown;
	
	private final World		mWorld;
	
	private Input			mInput;
	
	public Game()
	{
		mMouseDown = new boolean[3];
		mWorld = new World();
	}
	
	public void render(GameContainer gc, Graphics g)
	{
		mWorld.render(g);
	}
	
	public void init(GameContainer gc)
	{
		mRunning = true;
		mInput = gc.getInput();
		mWorld.init(gc.getWidth(), gc.getHeight());
		mWorld.addPlayer();
	}
	
	public void update(GameContainer gc, int aDelta)
	{
		if (mInput.isKeyPressed(Input.KEY_ESCAPE)) stop();
		mWorld.update(mInput);
	}
	
	public void mouseClick(int aButton, int aX, int aY)
	{	
		
	}
	
	public void mouseClick(int aButton, boolean aDown)
	{
		mMouseDown[aButton] = aDown;
	}
	
	public void mouseMove(int aXD, int aYD)
	{}
	
	public boolean isRunning()
	{
		return mRunning;
	}
	
	public void stop()
	{
		mRunning = false;
	}
}
