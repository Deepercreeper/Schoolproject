package game.entity;

import game.level.Map;
import game.level.block.Block;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Experience extends Entity
{
	private int			mSize, mLife;
	
	private final Color	mColor;
	
	public Experience(final int aX, final int aY, final int aSize)
	{
		super(aX, aY, 2 + aSize, 2 + aSize);
		mXV += (int) (Math.random() * 5 - Math.random() * 5);
		mYV -= (int) (Math.random() * 5);
		mSize = aSize;
		mLife = (int) (Math.random() * aSize * 100) + 200;
		mColor = new Color(0f, (float) (Math.random() * 0.5f + 0.25f), (float) (Math.random() * 0.5f + 0.5f));
	}
	
	@Override
	public void update(final Input aInput)
	{
		if (--mLife < 0)
		{
			mSize -= (Math.random() * 3) + 1;
			if (mSize <= 0) remove();
			else mLife = (int) (Math.random() * 100) + 50;
		}
		
		mOnIce = false;
		
		final double distance = Math.sqrt(Math.pow(mLevel.getPlayer().getY() + mLevel.getPlayer().getHeight() / 2 - mY - mHeight / 2, 2)
				+ Math.pow(mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() / 2 - mX - mWidth / 2, 2));
		if (distance < Block.SIZE * 3) mXV = (Block.SIZE * 3 - distance) * 0.05 * Math.signum(mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() / 2 - mX - mWidth / 2);
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Map.GRAVITY * 0.7 - (mInLiquid ? 0.1 : 0), friction = Map.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
	}
	
	@Override
	public void render(final Graphics aG)
	{
		aG.setColor(mColor);
		aG.fillOval((int) (mX - mLevel.getScreenX()), (int) (mY - mLevel.getScreenY()), mWidth, mHeight);
	}
	
	public void collect()
	{
		if (isRemoved()) return;
		mLevel.getPlayer().addExp(mSize);
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
