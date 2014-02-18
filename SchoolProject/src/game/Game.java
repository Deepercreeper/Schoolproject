package game;

import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import view.Menu;
import data.DataManager;

public class Game
{
	private boolean		mRunning, mShowingMenu;
	
	private final Menu	mMenu			= new Menu(this);
	
	private World		mWorld;
	
	private Input		mInput;
	
	private int			mShowingVolume	= 0;
	
	/**
	 * Renders the splash screen and the world. The volume is also displayed.
	 * 
	 * @param gc
	 *            The containing game container.
	 * @param g
	 *            The graphics to draw into.
	 */
	public void render(GameContainer gc, Graphics g)
	{
		if ( !DataManager.hasLoaded()) g.drawImage(DataManager.getImage("splash"), 0, 0);
		else
		{
			mWorld.render(g);
			
			if (mShowingMenu)
			{
				mMenu.render(gc, g);
				return;
			}
			
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
	
	/**
	 * Prepares this game for running.
	 * 
	 * @param gc
	 *            The containing game container.
	 */
	public void init(GameContainer gc)
	{
		mRunning = true;
	}
	
	private void createWorld(GameContainer gc, int aLevel)
	{
		if (mInput == null) mInput = gc.getInput();
		mWorld = new World(aLevel, gc);
	}
	
	/**
	 * Initiates the data manager, creates a world, handles menu input and updates the world.
	 * 
	 * @param gc
	 *            The containing game container.
	 * @param aDelta
	 *            The done ticks after the last update.
	 */
	public void update(GameContainer gc, int aDelta)
	{
		if ( !DataManager.hasLoaded()) DataManager.init();
		
		if (mShowingMenu)
		{
			mMenu.update(mInput);
			return;
		}
		
		// Creating world
		if (mWorld == null) createWorld(gc, 0);
		if (mWorld.hasWon()) createWorld(gc, mWorld.getId() + 1);
		
		// Updates
		if (mShowingVolume > 0) mShowingVolume-- ;
		
		// Input
		if (mInput.isKeyPressed(Input.KEY_ESCAPE))
		{
			mMenu.initKeys(mInput);
			mShowingMenu = true;
		}
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
	
	/**
	 * Hides the menu.
	 */
	public void hideMenu()
	{
		mShowingMenu = false;
	}
	
	/**
	 * Returns whether this game is running or has stopped.
	 * 
	 * @return {@code true} if running and {@code false} if not.
	 */
	public boolean isRunning()
	{
		return mRunning;
	}
	
	/**
	 * Stops this game.
	 */
	public void stop()
	{
		mRunning = false;
	}
}
