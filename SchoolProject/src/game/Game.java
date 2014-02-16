package game;

import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Game
{
	private boolean	mRunning;
	
	private World	mWorld;
	
	private Input	mInput;
	
	private int		mShowingVolume	= 0;
	
	public void render(GameContainer gc, Graphics g)
	{
		if ( !DataManager.hasLoaded()) g.drawImage(DataManager.getImage("splash"), 0, 0);
		else
		{
			mWorld.render(g);
			
			// Render volume
			if (mShowingVolume > 0)
			{
				float volume = DataManager.getVolume();
				g.setColor(Color.white);
				g.drawString("Volume", gc.getWidth() / 2 - 25, gc.getHeight() - 40);
				g.setColor(Color.darkGray);
				g.fillRect(gc.getWidth() / 2 - 50, gc.getHeight() - 20, 100, 10);
				g.setColor(Color.lightGray);
				g.fillRect(gc.getWidth() / 2 - 50, gc.getHeight() - 20, 100 * volume, 10);
			}
		}
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
		
		// Creating world
		if (mWorld == null) createWorld(gc, 0);
		if (mWorld.hasWon()) createWorld(gc, mWorld.getId() + 1);
		
		// Updates
		if (mShowingVolume > 0) mShowingVolume-- ;
		
		// Input
		if (mInput.isKeyPressed(Input.KEY_ESCAPE)) stop();
		if (mInput.isKeyPressed(Input.KEY_ADD))
		{
			DataManager.volumeUp();
			mShowingVolume = 30;
		}
		if (mInput.isKeyPressed(Input.KEY_SUBTRACT))
		{
			DataManager.volumeDown();
			mShowingVolume = 30;
		}
		
		// Updating world
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
