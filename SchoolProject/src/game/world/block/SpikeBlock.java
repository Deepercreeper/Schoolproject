package game.world.block;

import game.entity.Entity;
import game.world.World;

public class SpikeBlock extends Block
{
	private final byte	mDirection;
	
	protected SpikeBlock(int aId, int aRGB, int aDirection)
	{
		super(aId, aRGB);
		mDirection = (byte) aDirection;
	}
	
	@Override
	public void hit(int aX, int aY, float aXV, float aYV, World aWorld, Entity aEntity)
	{
		boolean hit = false;
		float xv = 0, yv = 0;
		switch (mDirection)
		{
			case 0 :
				hit = aYV > 0 && aEntity.getY() + aEntity.getHeight() <= aY * Block.SIZE;
				yv = -10;
				break;
			case 1 :
				hit = aYV < 0 && aEntity.getY() >= (aY + 1) * Block.SIZE;
				yv = 10;
				break;
			case 2 :
				hit = aXV < 0 && aEntity.getX() >= (aX + 1) * Block.SIZE;
				xv = -10;
				break;
			case 3 :
				hit = aXV > 0 && aEntity.getX() + aEntity.getWidth() <= aX * Block.SIZE;
				xv = 10;
				break;
		}
		if (hit) aEntity.hurt(1, xv, yv);
	}
}
