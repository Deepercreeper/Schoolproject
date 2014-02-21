package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Heart extends Entity
{
	private final int		mSize;
	private int				mLife;
	private final boolean	mStatic;
	
	public Heart(int aX, int aY, int aSize, boolean aStatic)
	{
		super(aX, aY, 6 + aSize * 2, 6 + aSize * 2);
		mSize = aSize;
		mStatic = aStatic;
		if ( !mStatic) mLife = (int) (100 + Math.random() * 200 + mSize * 100);
	}
	
	@Override
	public void update(Input aInput)
	{
		if ( !mStatic && --mLife < 0) remove();
		
		mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = World.GRAVITY * 0.7 - (mInLiquid ? 0.1 : 0), friction = World.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
		
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getSplitImage("entity", 3).getScaledCopy(mWidth, mHeight), (float) (mX - mWorld.getScreenX()), (float) (mY - mWorld.getScreenY()));
	}
	
	public void collect()
	{
		if (isRemoved()) return;
		mWorld.getPlayer().addLife(mSize);
		DataManager.playSound("heart");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
