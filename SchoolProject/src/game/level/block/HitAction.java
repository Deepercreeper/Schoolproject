package game.level.block;

import game.entity.Entity;
import game.level.Level;
import java.util.HashMap;
import util.Direction;
import util.Util;
import data.DataManager;

abstract class HitAction
{
	static HitAction	DESTROY	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										destroy(aX, aY, aLevel, aEntity, aBlock, aHitDirection);
									};
								};
	
	static HitAction	HURT	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										hurt(aX, aY, aEntity, aBlock, aHitDirection, aOtherBlocks);
									};
								};
	
	static HitAction	ICE		= new HitAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
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
	
	private static void destroy(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection)
	{
		if ( !aEntity.canDestroyBlocks()) return;
		final Texture texture = Block.getBlockTexture(aX, aY, aLevel);
		if (aHitDirection == Direction.BOTTOM)
		{
			aLevel.setBlock(aX, aY, aBlock.getDestination().getId(texture));
			short alpha = aLevel.getAlpha(aX, aY);
			Item item = aBlock.getItem(alpha);
			if (item != null)
			{
				aLevel.addEntity(item.create(aX * Block.SIZE + Block.SIZE / 2 - aBlock.getItem(alpha).getWidth() / 2, aY * Block.SIZE - aBlock.getItem(alpha).getHeight()));
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
		else if (aHitDirection == Direction.TOP && Util.isCannonBall(aEntity, aLevel))
		{
			aLevel.setBlock(aX, aY, aBlock.getDestination().getId(texture));
			short alpha = aLevel.getAlpha(aX, aY);
			Item item = aBlock.getItem(alpha);
			if (item != null)
			{
				aLevel.addEntity(item.create(aX * Block.SIZE + Block.SIZE / 2 - aBlock.getItem(alpha).getWidth() / 2, (aY + 1) * Block.SIZE));
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
	}
	
	abstract void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks);
}
