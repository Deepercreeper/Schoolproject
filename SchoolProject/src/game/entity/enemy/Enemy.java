package game.entity.enemy;

import game.entity.Entity;

public abstract class Enemy extends Entity
{
	protected int	mLife;
	
	public Enemy(int aX, int aY, int aWidth, int aHeight)
	{
		super(aX, aY, aWidth, aHeight);
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
