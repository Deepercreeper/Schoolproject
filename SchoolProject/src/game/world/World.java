package game.world;

import game.Stats;
import game.entity.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import data.DataManager;

public class World
{
	private final byte		mId;
	
	private final Player	mPlayer;
	
	private final int[]		mLevels;
	
	private Level			mLevel;
	
	private int				mLevelIndex;
	
	private final Screen	mScreen;
	
	private boolean			mWon;
	
	/**
	 * Creates a world defined by the given id and the given game container.
	 * 
	 * @param aId
	 *            The id of this world.
	 * @param gc
	 *            the containing game container.
	 */
	public World(int aId, GameContainer gc, int[] aLevels)
	{
		mId = (byte) aId;
		mLevels = aLevels;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
		mPlayer = new Player();
		mLevelIndex = 0;
		mLevel = new Level(mLevels[mLevelIndex], mScreen, mPlayer);
		DataManager.nextTitle();
	}
	
	public World(int aId, GameContainer gc, Player aPlayer, int[] aLevels)
	{
		mId = (byte) aId;
		mLevels = aLevels;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
		mPlayer = aPlayer;
		mLevelIndex = 0;
		mLevel = new Level(mLevels[mLevelIndex], mScreen, mPlayer);
		DataManager.nextTitle();
	}
	
	/**
	 * Updates all entities, blocks and the screen.
	 * 
	 * @param aInput
	 *            The information about keyboard and mouse activity.
	 */
	public void update(Input aInput)
	{
		if (mPlayer.isDead())
		{
			Stats.instance().addDeath();
			mLevel.reload();
			mPlayer.respawn();
			mLevel.addPlayer(mPlayer);
			return;
		}
		
		if (mLevel.hasWon())
		{
			if (mLevelIndex == mLevels.length - 1) mWon = true;
			else mLevel = new Level(mLevels[++mLevelIndex], mScreen, mPlayer);
		}
		
		mLevel.update(aInput);
	}
	
	/**
	 * Returns the world id.
	 * 
	 * @return the id.
	 */
	public byte getId()
	{
		return mId;
	}
	
	/**
	 * Returns whether the player has gone through the finishing flag.
	 * 
	 * @return {@code true} if the player has finished and {@code false} if not.
	 */
	public boolean hasWon()
	{
		return mWon;
	}
	
	/**
	 * The current player of this world.
	 * 
	 * @return the player.
	 */
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	/**
	 * Renders the background, all blocks and entities.
	 * 
	 * @param g
	 *            the graphics to draw into.
	 */
	public void render(Graphics g)
	{
		// Render background
		Image background = DataManager.getBackgroundImage(0);
		g.drawImage(background, -(mScreen.getX() / 5 % background.getWidth()), 0);
		g.drawImage(background, background.getWidth() - (mScreen.getX() / 5 % background.getWidth()), 0);
		
		mLevel.render(g);
	}
}
