package game.level.block;

import game.entity.Entity;
import game.entity.weapon.Weapon;
import game.level.Map;
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
									void execute(final int aX, final int aY, final Map aLevel, final Entity aEntity, final Block aBlock, final Direction aHitDirection,
											final HashMap<Block, Direction> aOtherBlocks)
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
									void execute(final int aX, final int aY, final Map aLevel, final Entity aEntity, final Block aBlock, final Direction aHitDirection,
											final HashMap<Block, Direction> aOtherBlocks)
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
									void execute(final int aX, final int aY, final Map aLevel, final Entity aEntity, final Block aBlock, final Direction aHitDirection,
											final HashMap<Block, Direction> aOtherBlocks)
									{
										ice(aEntity, aHitDirection, aOtherBlocks);
									};
								};
	
	private static void ice(final Entity aEntity, final Direction aDirection, final HashMap<Block, Direction> aOtherBlocks)
	{
		if (aDirection == Direction.TOP)
		{
			for (final Block block : aOtherBlocks.keySet())
				if (aOtherBlocks.get(block) == Direction.TOP && !block.isIce()) return;
			aEntity.setOnIce();
		}
	}
	
	private static void hurt(final int aX, final int aY, final Entity aEntity, final Block aBlock, final Direction aHitDirection, final HashMap<Block, Direction> aOtherBlocks)
	{
		if (aHitDirection != aBlock.getHurtDirection()) return;
		switch (aBlock.getHurtDirection())
		{
			case TOP :
				for (final Block block : aOtherBlocks.keySet())
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
	
	private static void destroy(final int aX, final int aY, final Map aLevel, final Entity aEntity, final Block aBlock, final Direction aHitDirection)
	{
		final Texture texture = Block.getBlockTexture(aX, aY, aLevel.getBlock(aX, aY));
		
		for (final Direction dir : Direction.values())
			if (aEntity.canDestroyBlock(dir) && aHitDirection == dir)
			{
				aLevel.setBlock(aX, aY, aBlock.getDestination().getId(texture));
				if (aBlock.containsItems())
				{
					final short alpha = aLevel.getAlpha(aX, aY);
					final Item item = Item.getItem(alpha);
					final int width = item.getWidth(), height = item.getHeight();
					int x = aX * Block.SIZE + Block.SIZE / 2, y = aY * Block.SIZE + Block.SIZE / 2;
					
					x -= dir.XD * (Block.SIZE / 2 + width / 2);
					y -= dir.YD * (Block.SIZE / 2 + height / 2);
					
					final Entity entity = item.create(x - width / 2, y - height / 2);
					if ( !Block.get(aLevel.getBlock(aX - dir.XD, aY - dir.YD)).isSolid(aX - dir.XD, aY - dir.YD, entity))
					{
						boolean add = true;
						if (entity instanceof Weapon && aLevel.getPlayer().hasWeapon((Weapon) entity)) add = false;
						if (add) aLevel.addEntity(entity);
					}
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
	abstract void execute(int aX, int aY, Map aLevel, Entity aEntity, Block aBlock, Direction aHitDirection, HashMap<Block, Direction> aOtherBlocks);
}
