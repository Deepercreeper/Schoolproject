package game.world.block;

import game.entity.Entity;
import game.world.World;
import data.DataManager;

public class QuestionBlock extends Block
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
	public void hit(int aX, int aY, World aWorld, Entity aEntity)
	{
		if (isUnder(aX, aY, aEntity) || isCannon(aX, aY, aEntity))
		{
			aWorld.setBlock(aX, aY, Block.OPENED.getId());
			DataManager.playSound("item");
		}
	}
}
