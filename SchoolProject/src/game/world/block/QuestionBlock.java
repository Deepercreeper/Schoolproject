package game.world.block;

import game.entity.Entity;
import game.entity.Player;
import game.world.World;
import data.DataManager;

public class QuestionBlock extends Block
{
	protected QuestionBlock(int aId, int aRGB)
	{
		super(aId, aRGB);
	}
	
	@Override
	public void hit(int aX, int aY, double aXV, double aYV, World aWorld, Entity aEntity)
	{
		if (aYV < 0 && aEntity.getY() >= (aY + 1) * Block.SIZE || (aEntity instanceof Player && ((Player) aEntity).isCannonBall()))
		{
			aWorld.setBlock(aX, aY, Block.EMPTY.getId());
			DataManager.playSound("item");
		}
	}
}
