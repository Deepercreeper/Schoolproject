package game.world.block;

import game.entity.Entity;
import game.world.World;

public class SpikeBlock extends OldBlock
{
	private final byte	mDirection;
	
	/**
	 * Creates a spike block that points into the given direction.<br>
	 * 0 -> up, 1 -> down, 2 -> right, 3 -> left
	 * 
	 * @param aId
	 *            This blocks id.
	 * @param aRGB
	 *            The RGB code of this block.
	 * @param aDirection
	 *            The direction of this spike.
	 */
	protected SpikeBlock(int aId, int aRGB, int aDirection)
	{
		super(aId, aRGB);
		mDirection = (byte) aDirection;
	}
	
	@Override
	protected void hitTop(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (mDirection == 0) aEntity.hurt(1, 0, -3);
	}
	
	@Override
	protected void hitBottom(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (mDirection == 1) aEntity.hurt(1, 0, 3);
	}
	
	@Override
	protected void hitRight(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (mDirection == 2) aEntity.hurt(1, 5, -2);
	}
	
	@Override
	protected void hitLeft(int aX, int aY, World aWorld, Entity aEntity, boolean aCannon)
	{
		if (mDirection == 3) aEntity.hurt(1, -5, -2);
	}
}
