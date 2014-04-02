package game;

import game.entity.Player;
import game.level.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import view.MainMenu;
import view.Menu;
import view.PauseMenu;
import data.DataManager;
import data.LevelManager;

public class Game
{
	private boolean			mRunning		= true, mPause, mMain;
	private int				mShowingVolume	= 0;
	
	private Menu			mPauseMenu, mMainMenu;
	private Map				mLevel;
	private Input			mInput;
	private GameContainer	mGC;
	private Player			mPlayer;
	
	/**
	 * Renders the splash screen and the level. The volume is also displayed.
	 * 
	 * @param aG
	 *            The graphics to draw into.
	 */
	public void render(final Graphics aG)
	{
		if ( !DataManager.isInitiated() || DataManager.isLoading()) aG.drawImage(DataManager.getImage("splash").getScaledCopy(mGC.getWidth(), mGC.getHeight()), 0, 0);
		else
		{
			if (mMain) mMainMenu.render(aG);
			
			if ( !mMain)
			{
				mLevel.render(aG);
				
				Stats.instance().render(aG);
				
				if (mPause)
				{
					mPauseMenu.render(aG);
					return;
				}
			}
			
			// Render volume
			if (mShowingVolume > 0)
			{
				final float volume = DataManager.getVolume();
				aG.setColor(Color.white);
				aG.drawString("Volume", mGC.getWidth() / 2 - 25, mGC.getHeight() - 100);
				aG.setColor(Color.darkGray);
				aG.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 80, 100, 10);
				aG.setColor(Color.lightGray);
				aG.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 80, 100 * volume, 10);
			}
		}
	}
	
	/**
	 * Initiates the data manager, creates a level, handles menu input and updates the level.
	 * 
	 * @param aDelta
	 *            The done ticks after the last update.
	 */
	public void update(final int aDelta)
	{
		if ( !DataManager.isInitiated())
		{
			DataManager.init();
			mainMenu();
		}
		
		// Updates
		if (mShowingVolume > 0) mShowingVolume-- ;
		
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
		
		// Creating level
		if (mLevel.hasWon())
		{
			final int levelIndex = mLevel.getLevelId();
			Save.instance().setScore(mLevel.getWorldId(), levelIndex, Stats.instance().getScore());
			if (levelIndex < LevelManager.instance().getLevelsCount(mLevel.getWorldId()))
			{
				Save.instance().openLevel(mLevel.getWorldId(), levelIndex + 1);
				mainMenu();
				return;
			}
			if (mLevel.getWorldId() < LevelManager.instance().getWorldsCount() - 1) Save.instance().openWorld(mLevel.getWorldId() + 1);
			mainMenu();
			return;
		}
		
		// Input
		if (mInput.isKeyPressed(Input.KEY_ESCAPE))
		{
			mInput.clearKeyPressedRecord();
			mPause = true;
		}
		if (mInput.isKeyPressed(Input.KEY_ADD))
		{
			Save.instance().volumeUp();
			showVolume();
		}
		if (mInput.isKeyPressed(Input.KEY_SUBTRACT))
		{
			Save.instance().volumeDown();
			showVolume();
		}
		
		if ( !DataManager.wasloading()) Stats.instance().tick(aDelta);
		
		// Updating level
		mLevel.update(mInput);
	}
	
	/**
	 * Displays the volume bar for 3 seconds.
	 */
	public void showVolume()
	{
		mShowingVolume = 30;
	}
	
	/**
	 * Initiates the containing game container.
	 * 
	 * @param aGC
	 *            The containing game container.
	 */
	public void init(final GameContainer aGC)
	{
		mGC = aGC;
		mInput = mGC.getInput();
		mMainMenu = new MainMenu(mGC, this);
	}
	
	/**
	 * Shows the main menu and deletes all current levels.
	 */
	public void mainMenu()
	{
		mPause = false;
		mMain = true;
		DataManager.playMusic("menu");
		mLevel = null;
		mInput.clearKeyPressedRecord();
	}
	
	/**
	 * Starts the given level.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 * @param aSave
	 *            The save to play with.
	 */
	public void start(final int aWorldId, final int aLevelId)
	{
		mMain = false;
		mPlayer = Save.instance().getPlayer();
		Stats.instance().reset();
		initWorld(aWorldId, aLevelId);
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
	
	private void initWorld(final int aWorldId, final int aLevelId)
	{
		if (mInput == null) mInput = mGC.getInput();
		if (mPauseMenu == null) mPauseMenu = new PauseMenu(mGC, this);
		mLevel = new Map(aWorldId, aLevelId, mGC, mPlayer);
	}
}
