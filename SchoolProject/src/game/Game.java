package game;

import game.entity.Player;
import game.world.Level;
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
	
	private Level			mLevel;
	
	private Input			mInput;
	
	private int				mShowingVolume	= 0;
	
	private GameContainer	mGC;
	
	private Save			mSave;
	
	private Player			mPlayer;
	
	/**
	 * Renders the splash screen and the world. The volume is also displayed.
	 * 
	 * @param aG
	 *            The graphics to draw into.
	 */
	public void render(Graphics aG)
	{
		if ( !DataManager.isInitiated() || DataManager.isLoading()) aG.drawImage(DataManager.getImage("splash"), 0, 0);
		else
		{
			if (mMain)
			{
				mMainMenu.render(aG);
				return;
			}
			
			mLevel.render(aG);
			
			Stats.instance().render(aG);
			
			if (mPause)
			{
				mPauseMenu.render(aG);
				return;
			}
			
			// Render volume
			if (mShowingVolume > 0)
			{
				float volume = DataManager.getVolume();
				aG.setColor(Color.white);
				aG.drawString("Volume", mGC.getWidth() / 2 - 25, mGC.getHeight() - 40);
				aG.setColor(Color.darkGray);
				aG.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 20, 100, 10);
				aG.setColor(Color.lightGray);
				aG.fillRect(mGC.getWidth() / 2 - 50, mGC.getHeight() - 20, 100 * volume, 10);
			}
		}
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
		if (mLevel.hasWon())
		{
			int levelIndex = mLevel.getLevelId();
			mSave.setScore(mLevel.getWorldId(), levelIndex, Stats.instance().getScore());
			if (levelIndex < DataManager.getLevels()[mLevel.getWorldId()] - 1)
			{
				mSave.openLevel(mLevel.getWorldId(), levelIndex + 1);
				mainMenu();
				return;
			}
			if (mLevel.getWorldId() < DataManager.getLevels().length - 1) mSave.openWorld(mLevel.getWorldId() + 1);
			mainMenu();
			return;
		}
		
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
		mLevel.update(mInput);
	}
	
	public void init(GameContainer aGC)
	{
		mGC = aGC;
		mInput = mGC.getInput();
		mMainMenu = new MainMenu(mGC, this);
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
	
	public void mainMenu()
	{
		mPause = false;
		mMain = true;
		DataManager.playMusic("Menu");
		mLevel = null;
	}
	
	public void start(int aWorld, int aLevelIndex, Save aSave)
	{
		mMain = false;
		mSave = aSave;
		Stats.instance().reset();
		initWorld(aWorld, aLevelIndex);
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
