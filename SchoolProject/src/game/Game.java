package game;

import game.entity.Player;
import game.level.Level;
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
	private int				mShowingVolume	= 0;
	
	private Menu			mPauseMenu, mMainMenu;
	private Level			mLevel;
	private Input			mInput;
	private GameContainer	mGC;
	private Save			mSave;
	private Player			mPlayer;
	
	/**
	 * Renders the splash screen and the level. The volume is also displayed.
	 * 
	 * @param aG
	 *            The graphics to draw into.
	 */
	public void render(Graphics aG)
	{
		if ( !DataManager.isInitiated() || DataManager.isLoading()) aG.drawImage(DataManager.getImage("splash"), 0, 0);
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
				float volume = DataManager.getVolume();
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
	public void update(int aDelta)
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
			int levelIndex = mLevel.getLevelId();
			mSave.setScore(mLevel.getWorldId(), levelIndex, Stats.instance().getScore());
			if (levelIndex < DataManager.getLevelsPerWorld()[mLevel.getWorldId()] - 1)
			{
				mSave.openLevel(mLevel.getWorldId(), levelIndex + 1);
				mainMenu();
				return;
			}
			if (mLevel.getWorldId() < DataManager.getLevelsPerWorld().length - 1) mSave.openWorld(mLevel.getWorldId() + 1);
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
			mSave.volumeUp();
			showVolume();
		}
		if (mInput.isKeyPressed(Input.KEY_SUBTRACT))
		{
			mSave.volumeDown();
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
	public void init(GameContainer aGC)
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
		DataManager.playMusic("Menu");
		mLevel = null;
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
	public void start(int aWorldId, int aLevelId, Save aSave)
	{
		mMain = false;
		mSave = aSave;
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
	
	private void initWorld(int aWorld, int aLevelIndex)
	{
		if (mInput == null) mInput = mGC.getInput();
		if (mPauseMenu == null) mPauseMenu = new PauseMenu(mGC, this);
		if (mPlayer == null)
		{
			mLevel = new Level(aWorld, aLevelIndex, mGC);
			mPlayer = mLevel.getPlayer();
		}
		else mLevel = new Level(aWorld, aLevelIndex, mGC, mPlayer);
	}
}
