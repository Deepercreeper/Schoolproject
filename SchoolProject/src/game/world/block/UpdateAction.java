package game.world.block;

import game.world.World;
import util.Rectangle;
import util.Util;

public abstract class UpdateAction
{
	static UpdateAction	LIQUID	= new UpdateAction()
								{
									@Override
									void execute(int aX, int aY, World aWorld)
									{
										liquid(aX, aY, aWorld);
									}
								};
	
	static UpdateAction	WIN		= new UpdateAction()
								{
									@Override
									void execute(int aX, int aY, World aWorld)
									{
										win(aX, aY, aWorld);
									}
								};
	
	private static void liquid(int aX, int aY, World aWorld)
	{
		if (Util.isPlayerInsideBlock(aX, aY, aWorld)) aWorld.getPlayer().setInLiquid();
	}
	
	private static void win(int aX, int aY, World aWorld)
	{
		if (aWorld.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY - 7) * Block.SIZE, Block.SIZE * 2, Block.SIZE * 8))) aWorld.win();
	}
	
	abstract void execute(int aX, int aY, World aWorld);
}
