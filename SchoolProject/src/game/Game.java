package game;

import game.entity.Player;
import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import view.MainMenu;
import view.Menu;
import view.PauseMenu;
import data.DataManager;

public class Game
{
	private boolean			mRunning		= true, mPause, mMain;
	
	private Menu			mPauseMenu, mMainMenu;
	
	private World			mWorld;
	
	private int[][]			mWorlds;
	
	private int				mWorldIndex;
	
	private Input			mInput;
	
	private int				mShowingVolume	= 0;
	
	private GameContainer	mGC;
	
	private Save			mSave;
	
	/**
	 * Renders the splash screen and the world. The volume is also displayed.
	 * 
	 * @param g
	 *            The graphics to draw into.
	 */
	public void render(Graphics g)
	{
		if ( !DataManager.isInitiated() || DataManager.isLoading()) g.drawImage(DataManager.getImage("splash"), 0, 0);
		else
		{
			if (mMain)
			{
				mMainMenu.render(g);
				return;
			}
			
			mWorld.render(g);
			
			Stats.instance().render(g);
			
			if (mPause)
			{
				mPauseMenu.render(g);
				return;
			}
			
			// Render volume
			if (mShowingVolume > 0)
			{
				float volume = DataManager.getVolume();
				g.setColor(Color.white);
				g.drawString("Volume", mGC.getWidth() / 2 - 25, mGC.getHeight() - 40);
				g.setColor(Color.darkGray);
				g.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 20, 100, 10);
				g.setColor(Color.lightGray);
				g.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 20, 100 * volume, 10);
			}
		}
	}
	
	private void nextWorld()
	{
		Player player = mWorld.getPlayer();
		if (mWorldIndex == mWorlds.length - 1) mainMenu();
		else mWorld = new World(++mWorldIndex, mGC, player, mWorlds[mWorldIndex]);
	}
	
	/**
	 * Initiates the data manager, creates a world, handles menu input and updates the world.
	 * 
	 * @param aDelta
	 *            The done ticks after the last update.
	 */
	public void update(int aDelta)
	{
		if ( !DataManager.isInitiated())
		{
			DataManager.init();
			mainMenu();
		}
		
		if (mMain)
		{
			mMainMenu.update(mInput);
			return;
		}
		if (mPause)
		{
			mPauseMenu.update(mInput);
			return;
		}
		
		// Creating world
		if (mWorld.hasWon()) nextWorld();
		
		// Updates
		if (mShowingVolume > 0) mShowingVolume-- ;
		
		// Input
		if (mInput.isKeyPressed(Input.KEY_ESCAPE))
		{
			mInput.clearKeyPressedRecord();
			mPause = true;
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
		
		Stats.instance().tick(aDelta);
		
		// Updating world
		mWorld.update(mInput);
	}
	
	public void init(GameContainer gc)
	{
		mGC = gc;
		mInput = mGC.getInput();
		mMainMenu = new MainMenu(mGC, this);
	}
	
	private void initWorld(int aWorld)
	{
		mInput = mGC.getInput();
		mPauseMenu = new PauseMenu(mGC, this);
		mWorlds = DataManager.getWorlds();
		mWorldIndex = aWorld;
		mWorld = new World(mWorldIndex, mGC, mWorlds[mWorldIndex]);
	}
	
	public void mainMenu()
	{
		mPause = false;
		mMain = true;
		DataManager.playMusic("Menu");
		// TODO kill all worlds.
	}
	
	public void start(int aWorld)
	{
		mMain = false;
		initWorld(aWorld);
	}
	
	/**
	 * Hides the menu.
	 */
	public void resume()
	{
		mPause = false;
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
