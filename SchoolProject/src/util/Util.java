package util;

import game.entity.Entity;
import game.world.World;
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
	 * @param aWorld
	 *            The parent world.
	 * @return {@code true} if the player is inside the block and {@code false} if not.
	 */
	public static boolean isPlayerInsideBlock(int aX, int aY, World aWorld)
	{
		return aWorld.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY) * Block.SIZE, Block.SIZE, Block.SIZE));
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
	 * If any entity hits this block, this method returns whether this block was hit from the bottom side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	public static boolean hitsBlockBottom(int aX, int aY, Entity aEntity)
	{
		return aEntity.getY() >= (aY + 1) * Block.SIZE && aEntity.getX() + aEntity.getWidth() > aX * Block.SIZE && aEntity.getX() < (aX + 1) * Block.SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the left side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	public static boolean hitsBlockLeft(int aX, int aY, Entity aEntity)
	{
		return aEntity.getX() + aEntity.getWidth() <= aX * Block.SIZE && aEntity.getY() + aEntity.getHeight() > aY * Block.SIZE && aEntity.getY() < (aY + 1) * Block.SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the right side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	public static boolean hitsBlockRight(int aX, int aY, Entity aEntity)
	{
		return aEntity.getX() <= (aX + 1) * Block.SIZE && aEntity.getY() + aEntity.getHeight() > aY * Block.SIZE && aEntity.getY() < (aY + 1) * Block.SIZE;
	}
	
	/**
	 * If any entity hits this block, this method returns whether this block was hit from the top side.
	 * 
	 * @param aX
	 *            the x position.
	 * @param aY
	 *            the y position.
	 * @param aEntity
	 *            the hitting entity.
	 * @return {@code true} if the entity hit this block with its head and {@code false} if not.
	 */
	public static boolean hitsBlockTop(int aX, int aY, Entity aEntity)
	{
		return aEntity.getY() + aEntity.getHeight() <= aY * Block.SIZE && aEntity.getX() + aEntity.getWidth() > aX * Block.SIZE && aEntity.getX() < (aX + 1) * Block.SIZE;
	}
	
	/**
	 * Checks whether the entity is the current player and is doing a cannon ball.
	 * 
	 * @param aEntity
	 *            The requested entity.
	 * @param aWorld
	 *            The parent world.
	 * @return {@code true} if the entity is doing a cannon ball and {@code false} if not.
	 */
	public static boolean isCannonBall(Entity aEntity, World aWorld)
	{
		return aEntity == aWorld.getPlayer() && aWorld.getPlayer().isCannonBall();
	}
}
