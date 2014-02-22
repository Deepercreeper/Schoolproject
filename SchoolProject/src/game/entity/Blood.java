package game.entity;

import game.world.Level;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Blood extends Entity
{
	private int	mLife;
	
	public Blood(int aX, int aY)
	{
		super(aX, aY, 2, 2);
		mXV = (Math.random() - Math.random()) * 5;
		mYV = -Math.random() * 5;
		mLife = (int) (100 + Math.random() * 200);
	}
	
	@Override
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{}
	
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
		g.setColor(Color.red);
		g.fillRect((float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()), mWidth, mHeight);
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
