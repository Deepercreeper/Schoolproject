package game.world;

import game.entity.Entity;
import game.entity.Player;
import game.world.block.Block;
import util.Rectangle;

public class Screen
{
	private final int		MOVEMENT_SPACE	= 20 / 2;
	
	private final Rectangle	mRect;
	
	private final World		mWorld;
	
	/**
	 * Creates a screen that defines which area should be rendered.
	 * 
	 * @param aWorld
	 *            The parent world.
	 * @param aWidth
	 *            The screen width.
	 * @param aHeight
	 *            The screen height.
	 */
	public Screen(World aWorld, int aWidth, int aHeight)
	{
		mWorld = aWorld;
		mRect = new Rectangle(0, 0, aWidth, aHeight);
	}
	
	/**
	 * Makes the screen follow the player.
	 * 
	 * @param aPlayer
	 *            the player of the parent world.
	 */
	public void update(Player aPlayer)
	{
		if (Math.abs(mRect.getCenterX() - aPlayer.getRect().getCenterX()) > Block.SIZE * MOVEMENT_SPACE)
		{
			if (aPlayer.getRect().getCenterX() > mRect.getCenterX()) mRect.setCenterX(aPlayer.getRect().getCenterX() - Block.SIZE * MOVEMENT_SPACE);
			else mRect.setCenterX(aPlayer.getRect().getCenterX() + Block.SIZE * MOVEMENT_SPACE);
		}
		mRect.setCenterY(Math.round(aPlayer.getY()) + aPlayer.getHeight());
		if (mRect.getX() < 0) mRect.setX(0);
		if (mRect.getY() < 0) mRect.setY(0);
		if (mRect.getMaxX() > mWorld.getWidth() * Block.SIZE) mRect.setX(mWorld.getWidth() * Block.SIZE - mRect.getWidth());
		if (mRect.getMaxY() > mWorld.getHeight() * Block.SIZE) mRect.setY(mWorld.getHeight() * Block.SIZE - mRect.getHeight());
	}
	
	/**
	 * Moves this screen along the x axis.
	 * 
	 * @param aXD
	 *            The length of moving.
	 */
	public void addX(int aXD)
	{
		if (mRect.getX() + aXD < 0) mRect.setX(0);
		mRect.setX(mRect.getX() + aXD);
	}
	
	/**
	 * Moves this screen along the y axis.
	 * 
	 * @param aYD
	 *            The length of moving.
	 */
	public void addY(int aYD)
	{
		if (mRect.getY() + aYD < 0) mRect.setY(0);
		mRect.setY(mRect.getY() + aYD);
	}
	
	/**
	 * Returns the current x position of this screen.
	 * 
	 * @return the current x position.
	 */
	public int getX()
	{
		return (int) mRect.getX();
	}
	
	/**
	 * Returns the current y position of this screen.
	 * 
	 * @return the current y position.
	 */
	public int getY()
	{
		return (int) mRect.getY();
	}
	
	/**
	 * Returns the width of this screen.
	 * 
	 * @return the screen width.
	 */
	public int getWidth()
	{
		return (int) mRect.getWidth();
	}
	
	/**
	 * Returns the height of this screen.
	 * 
	 * @return the screen height.
	 */
	public int getHeight()
	{
		return (int) mRect.getHeight();
	}
	
	/**
	 * Returns whether the given entity is inside this screen and has to be rendered.
	 * 
	 * @param aEntity
	 *            the entity to render.
	 * @return {@code true} if this screen contains the entity and {@code false} if not.
	 */
	public boolean contains(Entity aEntity)
	{
		return aEntity.getRect().intersects(mRect);
	}
}
