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
	
	private final Level		mLevel;
	
	private final Screen	mScreen;
	
	/**
	 * Creates a world defined by the given id and the given game container.
	 * 
	 * @param aId
	 *            The id of this world.
	 * @param gc
	 *            the containing game container.
	 */
	public World(int aId, GameContainer gc, int aLevel)
	{
		mId = (byte) aId;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
		mPlayer = new Player();
		mLevel = new Level(aLevel, mScreen, mPlayer);
		DataManager.nextTitle();
	}
	
	public World(int aId, GameContainer gc, Player aPlayer, int aLevel)
	{
		mId = (byte) aId;
		mScreen = new Screen(gc.getWidth(), gc.getHeight());
		mPlayer = aPlayer;
		mPlayer.respawn();
		mLevel = new Level(aLevel, mScreen, mPlayer);
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
		
		mLevel.update(aInput);
	}
	
	public byte getLevelId()
	{
		return mLevel.getId();
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
		return mLevel.hasWon();
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
