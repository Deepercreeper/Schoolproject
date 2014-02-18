package game.world.block;

import game.entity.Entity;
import game.world.World;
import util.Rectangle;
import util.Util;
import data.DataManager;

abstract class BlockAction
{
	static BlockAction	NOTHING			= new BlockAction()
										{
											@Override
											void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
											{}
										};
	
	static BlockAction	LIQUID			= new BlockAction()
										{
											@Override
											void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
											{
												setBlockLiquid(aX, aY, aWorld);
											}
										};
	
	static BlockAction	DESTROY_ON_HIT	= new BlockAction()
										{
											@Override
											void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
											{
												destroyBlock(aX, aY, aWorld, aEntity, aBlock);
											};
										};
	
	static BlockAction	HURT_ENTITY		= new BlockAction()
										{
											@Override
											void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
											{
												hurtEntity(aX, aY, aEntity, aBlock);
											};
										};
	
	static BlockAction	WIN				= new BlockAction()
										{
											@Override
											void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
											{
												win(aX, aY, aWorld);
											}
										};
	
	private static void win(int aX, int aY, World aWorld)
	{
		if (aWorld.getPlayer().getRect().intersects(new Rectangle(aX * Block.SIZE, (aY - 7) * Block.SIZE, Block.SIZE * 2, Block.SIZE * 8))) aWorld.win();
	}
	
	private static void hurtEntity(int aX, int aY, Entity aEntity, Block aBlock)
	{
		switch (aBlock.getHurtDirection())
		{
			case UP :
				if (Util.hitsBlockTop(aX, aY, aEntity)) aEntity.hurt(1, 0, -3);
				break;
			case DOWN :
				if (Util.hitsBlockBottom(aX, aY, aEntity)) aEntity.hurt(1, 0, 3);
				break;
			case RIGHT :
				if (Util.hitsBlockRight(aX, aY, aEntity)) aEntity.hurt(1, 5, -2);
				break;
			case LEFT :
				if (Util.hitsBlockLeft(aX, aY, aEntity)) aEntity.hurt(1, -5, -2);
			default :
		}
	}
	
	private static void destroyBlock(int aX, int aY, World aWorld, Entity aEntity, Block aBlock)
	{
		final boolean isSnow = Block.isSnowBlock(aX, aY, aWorld);
		if (Util.hitsBlockBottom(aX, aY, aEntity))
		{
			aWorld.setBlock(aX, aY, isSnow ? aBlock.getDestination().getSnowId() : aBlock.getDestination().getId());
			Entity item = aBlock.getItem();
			if (item != null)
			{
				aWorld.addEntity(item, aX * Block.SIZE + Block.SIZE / 2 - item.getWidth() / 2, aY * Block.SIZE - item.getHeight());
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
		else if (Util.hitsBlockTop(aX, aY, aEntity) && Util.isCannonBall(aEntity, aWorld))
		{
			aWorld.setBlock(aX, aY, isSnow ? aBlock.getDestination().getSnowId() : aBlock.getDestination().getId());
			Entity item = aBlock.getItem();
			if (item != null)
			{
				aWorld.addEntity(item, aX * Block.SIZE + Block.SIZE / 2 - item.getWidth() / 2, (aY + 1) * Block.SIZE);
				DataManager.playSound("item");
			}
			else DataManager.playSound("destroyBlock");
		}
	}
	
	private static void setBlockLiquid(int aX, int aY, World aWorld)
	{
		if (Util.isPlayerInsideBlock(aX, aY, aWorld)) aWorld.getPlayer().setInLiquid();
	}
	
	abstract void execute(int aX, int aY, World aWorld, Entity aEntity, Block aBlock);
}
