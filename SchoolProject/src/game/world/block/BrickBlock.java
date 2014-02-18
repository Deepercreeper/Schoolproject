package game.world.block;

import game.entity.Entity;
import game.world.World;
import data.DataManager;

public class BrickBlock extends OldBlock
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
	protected void hitBottom(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		aWorld.setBlock(aX, aY, Block.AIR.getId());
		DataManager.playSound("destroyBlock");
	}
	
	@Override
	protected void hitTop(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (aCannon)
		{
			aWorld.setBlock(aX, aY, Block.AIR.getId());
			DataManager.playSound("destroyBlock");
		}
	}
}
