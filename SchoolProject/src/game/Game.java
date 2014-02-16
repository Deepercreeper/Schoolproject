package game;

import game.world.World;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Game
{
	private boolean	mRunning;
	
	private World	mWorld;
	
	private Input	mInput;
	
	public void render(GameContainer gc, Graphics g)
	{
		if ( !DataManager.hasLoaded()) g.drawImage(DataManager.getImage("splash"), 0, 0);
		else mWorld.render(g);
	}
	
	public void init(GameContainer gc)
	{
		mRunning = true;
	}
	
	private void createWorld(GameContainer gc, int aLevel)
	{
		if (mInput == null) mInput = gc.getInput();
		mWorld = new World(aLevel, gc);
	}
	
	public void update(GameContainer gc, int aDelta)
	{
		if ( !DataManager.hasLoaded()) DataManager.init();
		
		if (mWorld == null) createWorld(gc, 0);
		if (mWorld.hasWon()) createWorld(gc, mWorld.getId() + 1);
		
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
