package game.entity;

import game.world.Level;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Gore extends Entity
{
	private int	mLife;
	
	public Gore(int aX, int aY)
	{
		super(aX, aY, 4, 3);
		mXV = (Math.random() - Math.random()) * 5;
		mYV = -Math.random() * 5;
		mLife = (int) (150 + Math.random() * 250);
	}
	
	@Override
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{}
	
	public void hit(Entity aEntity)
	{
		if ( !mOnGround) return;
		mXV += Math.signum(mX - aEntity.getX());
		mYV -= 1;
	}
	
	@Override
	public void update(Input aInput)
	{
		if (--mLife < 0) remove();
		
		mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Level.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Level.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getSplitImage("entity", 0), (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
