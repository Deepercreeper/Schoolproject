package game.world.block;

import game.entity.Entity;
import game.world.World;
import data.DataManager;

public class BrickBlock extends Block
{
	/**
	 * Creates a brick, that may contain items or is breakable.
	 * 
	 * @param aId
	 *            This blocks id.
	 * @param aRGB
	 *            The RGB code of this block.
	 */
	protected BrickBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
	}
	
	@Override
	public void hit(int aX, int aY, World aWorld, Entity aEntity)
	{
		if (isUnder(aX, aY, aEntity) || isCannon(aX, aY, aEntity))
		{
			aWorld.setBlock(aX, aY, Block.AIR.getId());
			DataManager.playSound("destroyBlock");
		}
	}
}
