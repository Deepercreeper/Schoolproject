package game.level.block;

import game.level.Map;
import util.Rectangle;
import util.Util;

public abstract class UpdateAction
{
	/**
	 * Sets all entities state, that are inside this block to inside a liquid.
	 */
	static UpdateAction	LIQUID	= new UpdateAction()
								{
									@Override
									void execute(final int aX, final int aY, final Map aLevel)
									{
										liquid(aX, aY, aLevel);
									}
								};
	
	/**
	 * Makes the player win the level if touching this block.
	 */
	static UpdateAction	WIN		= new UpdateAction()
								{
									@Override
									void execute(final int aX, final int aY, final Map aLevel)
									{
										win(aX, aY, aLevel);
									}
								};
	
	private static void liquid(final int aX, final int aY, final Map aLevel)
	{
		if (Util.isPlayerInsideBlock(aX, aY, aLevel)) aLevel.getPlayer().setInLiquid();
	}
	
	private static void win(final int aX, final int aY, final Map aLevel)
	{
		if (aLevel.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY - 7) * Block.SIZE, Block.SIZE * 2, Block.SIZE * 8))) aLevel.win();
	}
	
	/**
	 * Executes the given task.
	 * 
	 * @param aX
	 *            The x position of this block.
	 * @param aY
	 *            The y position of this block.
	 * @param aLevel
	 *            The parent level.
	 */
	abstract void execute(int aX, int aY, Map aLevel);
}
