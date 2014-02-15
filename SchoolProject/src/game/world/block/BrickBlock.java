package game.world.block;

import game.entity.Entity;
import game.world.World;
import data.DataManager;

public class BrickBlock extends Block
{
	
	protected BrickBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
	}
	
	@Override
	public void hit(int aX, int aY, float aXV, float aYV, World aWorld, Entity aEntity)
	{
		if (aYV < 0 && aEntity.getY() >= (aY + 1) * Block.SIZE)
		{
			aWorld.setBlock(aX, aY, Block.AIR.getId());
			DataManager.playSound("destroyBlock");
		}
	}
}
