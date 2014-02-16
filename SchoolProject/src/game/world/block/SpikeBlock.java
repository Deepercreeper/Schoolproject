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
	public void hit(int aX, int aY, World aWorld, Entity aEntity)
	{
		boolean hit = false;
		float xv = 0, yv = 0;
		switch (mDirection)
		{
			case 0 :
				hit = aEntity.getYV() > 0 && Math.round(aEntity.getY() + aEntity.getHeight()) <= aY * Block.SIZE;
				yv = -3;
				break;
			case 1 :
				hit = aEntity.getYV() < 0 && Math.round(aEntity.getY()) >= (aY + 1) * Block.SIZE;
				yv = 3;
				break;
			case 2 :
				hit = aEntity.getXV() < 0 && Math.round(aEntity.getX()) >= (aX + 1) * Block.SIZE;
				xv = 5;
				yv = -2;
				break;
			case 3 :
				hit = aEntity.getXV() > 0 && Math.round(aEntity.getX() + aEntity.getWidth()) <= aX * Block.SIZE;
				xv = -5;
				yv = -2;
				break;
		}
		if (hit) aEntity.hurt(1, xv, yv);
	}
}
