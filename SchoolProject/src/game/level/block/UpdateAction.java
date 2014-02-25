package game.level.block;

import game.level.Level;
import util.Rectangle;
import util.Util;

public abstract class UpdateAction
{
	static UpdateAction	LIQUID	= new UpdateAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel)
									{
										liquid(aX, aY, aLevel);
									}
								};
	
	static UpdateAction	WIN		= new UpdateAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel)
									{
										win(aX, aY, aLevel);
									}
								};
	
	private static void liquid(int aX, int aY, Level aLevel)
	{
		if (Util.isPlayerInsideBlock(aX, aY, aLevel)) aLevel.getPlayer().setInLiquid();
	}
	
	private static void win(int aX, int aY, Level aLevel)
	{
		if (aLevel.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY - 7) * Block.SIZE, Block.SIZE * 2, Block.SIZE * 8))) aLevel.win();
	}
	
	abstract void execute(int aX, int aY, Level aLevel);
}
