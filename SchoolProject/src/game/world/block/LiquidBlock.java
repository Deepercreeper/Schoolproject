package game.world.block;

import game.world.World;

public class LiquidBlock extends Block
{
	/**
	 * Creates a new liquid block, that makes entities swim or float inside this block.
	 * 
	 * @param aId
	 *            This blocks id.
	 * @param aRGB
	 *            The RGB code of this block.
	 */
	protected LiquidBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
		setLiquid();
		setUnSolid();
		setUpdatable();
	}
	
	@Override
	public void update(int aX, int aY, World aWorld)
	{
		if (isPlayerInside(aX, aY, aWorld)) aWorld.getPlayer().setInLiquid();
	}
}
