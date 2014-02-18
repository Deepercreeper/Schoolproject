package game.world.block;

import game.entity.Entity;
import game.world.World;
import data.DataManager;

public class QuestionBlock extends OldBlock
{
	/**
	 * Creates a question block that is unbreakable but contains items.
	 * 
	 * @param aId
	 *            This blocks id.
	 * @param aRGB
	 *            The RGB code of this block.
	 */
	protected QuestionBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
	}
	
	@Override
	protected void hitBottom(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		aWorld.setBlock(aX, aY, isSnowBlock() ? OldBlock.SNOW_OPENED.getId() : Block.OPENED.getId());
		DataManager.playSound("item");
	}
	
	@Override
	protected void hitTop(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (aCannon)
		{
			aWorld.setBlock(aX, aY, isSnowBlock() ? OldBlock.SNOW_OPENED.getId() : Block.OPENED.getId());
			DataManager.playSound("item");
		}
	}
}
