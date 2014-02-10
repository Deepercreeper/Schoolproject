package game;

import game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Game
{
	private boolean	mRunning;
	
	private World	mWorld;
	
	private Input	mInput;
	
	public void render(GameContainer gc, Graphics g)
	{
		mWorld.render(g);
	}
	
	public void init(GameContainer gc)
	{
		mRunning = true;
		createWorld(gc);
	}
	
	private void createWorld(GameContainer gc)
	{
		if (mInput == null) mInput = gc.getInput();
		mWorld = new World();
		mWorld.init(gc.getWidth(), gc.getHeight());
		mWorld.createPlayer();
	}
	
	public void update(GameContainer gc, int aDelta)
	{
		if (mInput.isKeyPressed(Input.KEY_ESCAPE)) stop();
		
		mWorld.update(mInput);
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
