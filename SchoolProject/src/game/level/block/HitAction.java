package game.level.block;

import game.entity.Entity;
import game.level.Level;
import java.util.HashMap;
import util.Direction;
import data.DataManager;

abstract class HitAction
{
	/**
	 * Destroys this block if the entity is able to destroy blocks.
	 */
	static HitAction	DESTROY	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										destroy(aX, aY, aLevel, aEntity, aBlock, aHitDirection);
									};
								};
	
	/**
	 * Hurts the hitting entity if hit from the right direction.
	 */
	static HitAction	HURT	= new HitAction()
								{
									@Override
									void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks)
									{
										hurt(aX, aY, aEntity, aBlock, aHitDirection, aOtherBlocks);
									};
								};
	
	/**
	 * Makes the entity run on ice if hit from the top.
	 */
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
		final Texture texture = Block.getBlockTexture(aX, aY, aLevel);
		
		for (Direction dir : Direction.values())
			if (aEntity.canDestroyBlock(dir) && aHitDirection == dir)
			{
				aLevel.setBlock(aX, aY, aBlock.getDestination().getId(texture));
				short alpha = aLevel.getAlpha(aX, aY);
				Item item = aBlock.getItem(alpha);
				if (item != null)
				{
					final int width = item.getWidth(), height = item.getHeight();
					int x = aX * Block.SIZE + Block.SIZE / 2, y = aY * Block.SIZE + Block.SIZE / 2;
					
					x -= dir.XD * (Block.SIZE / 2 + width / 2);
					y -= dir.YD * (Block.SIZE / 2 + height / 2);
					
					Entity entity = item.create(x - width / 2, y - height / 2);
					if ( !Block.get(aLevel.getBlock(aX - dir.XD, aY - dir.YD)).isSolid(aX - dir.XD, aY - dir.YD, entity)) aLevel.addEntity(entity);
					DataManager.playSound("item");
				}
				else DataManager.playSound("destroyBlock");
			}
	}
	
	/**
	 * Executes the given task.
	 * 
	 * @param aX
	 *            The x position of this block.
	 * @param aY
	 *            The y position of this block.
	 * @param aLevel
	 *            The parent level.
	 * @param aEntity
	 *            The hitting entity.
	 * @param aBlock
	 *            The hit block.
	 * @param aHitDirection
	 *            The direction from where this block was hit.
	 * @param aOtherBlocks
	 *            All other blocks that where hit at this tick.
	 */
	abstract void execute(int aX, int aY, Level aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks);
}
