package util;

import game.entity.Entity;
import game.world.Level;
import game.world.block.Block;

public class Util
{
	private Util()
	{}
	
	/**
	 * Calculates whether the player is inside the given block.
	 * 
	 * @param aX
	 *            The x position of the block.
	 * @param aY
	 *            The y position of the block.
	 * @param aLevel
	 *            The parent world.
	 * @return {@code true} if the player is inside the block and {@code false} if not.
	 */
	public static boolean isPlayerInsideBlock(int aX, int aY, Level aLevel)
	{
		return aLevel.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY) * Block.SIZE, Block.SIZE, Block.SIZE));
	}
	
	/**
	 * Returns the parameter of the given ones that has the minimum absolute value.
	 * 
	 * @param aFirst
	 *            The first value to compare.
	 * @param aSecond
	 *            The second value to compare.
	 * @return the least absolute value.
	 */
	public static double minAbs(double aFirst, double aSecond)
	{
		if (Double.isNaN(aFirst)) return aSecond;
		if (Math.abs(aFirst) < Math.abs(aSecond)) return aFirst;
		return aSecond;
	}
	
	/**
	 * Checks whether the entity is the current player and is doing a cannon ball.
	 * 
	 * @param aEntity
	 *            The requested entity.
	 * @param aLevel
	 *            The parent world.
	 * @return {@code true} if the entity is doing a cannon ball and {@code false} if not.
	 */
	public static boolean isCannonBall(Entity aEntity, Level aLevel)
	{
		return aEntity == aLevel.getPlayer() && aLevel.getPlayer().isCannonBall();
	}
}
