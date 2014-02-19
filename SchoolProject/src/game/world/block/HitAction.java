package game.world.block;

import game.entity.Entity;
import game.world.World;
import java.util.HashMap;
import util.Direction;
import util.Util;
import data.DataManager;

abstract class HitAction
{
	static HitAction	DESTROY	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										destroy(aX, aY, aWorld, aEntity, aBlock, aHitDirection);
									};
								};
	
	static HitAction	HURT	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										hurt(aX, aY, aEntity, aBlock, aHitDirection, aOtherBlocks);
									};
								};
	
	static HitAction	ICE		= new HitAction()
								{
									@Override
									void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										ice(aEntity, aHitDirection, aOtherBlocks);
									};
								};
	
	private static void ice(Entity aEntity, Direction aDirection, HashMap<Block, Direction> aOtherBlocks)
	{
		if (aDirection == Direction.TOP)
		{
			for (Block block : aOtherBlocks.keySet())
				if (aOtherBlocks.get(block) == Direction.TOP && !block.isIce()) return;
			aEntity.setOnIce();
		}
	}
	
	private static void hurt(int aX, int aY, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
	{
		if (aHitDirection != aBlock.getHurtDirection()) return;
		switch (aBlock.getHurtDirection())
		{
			case TOP :
				for (Block block : aOtherBlocks.keySet())
					if (aOtherBlocks.get(block) == Direction.TOP && block.getHurtDirection() != Direction.TOP) return;
				aEntity.hurt(1, 0, -3);
				break;
			case BOTTOM :
				aEntity.hurt(1, 0, 3);
				break;
			case RIGHT :
				aEntity.hurt(1, 5, -2);
				break;
			case LEFT :
				aEntity.hurt(1, -5, -2);
			default :
		}
	}
	
	private static void destroy(int aX, int aY, World aWorld, Entity aEntity, Block aBlock, Direction aHitDirection)
	{
		if ( !aEntity.canDestroyBlocks()) return;
		final boolean isSnow = Block.isSnowBlock(aX, aY, aWorld);
		if (aHitDirection == Direction.BOTTOM)
		{
			aWorld.setBlock(aX, aY, isSnow ? aBlock.getDestination().getSnowId() : aBlock.getDestination().getId());
			Item item = aBlock.getItem();
			if (item != null)
			{
				aWorld.addEntity(item.create(aX * Block.SIZE + Block.SIZE / 2 - aBlock.getItem().getWidth() / 2, aY * Block.SIZE - aBlock.getItem().getHeight()));
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
		else if (aHitDirection == Direction.TOP && Util.isCannonBall(aEntity, aWorld))
		{
			aWorld.setBlock(aX, aY, isSnow ? aBlock.getDestination().getSnowId() : aBlock.getDestination().getId());
			Item item = aBlock.getItem();
			if (item != null)
			{
				aWorld.addEntity(item.create(aX * Block.SIZE + Block.SIZE / 2 - aBlock.getItem().getWidth() / 2, (aY + 1) * Block.SIZE));
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
	}
	
	abstract void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks);
}
