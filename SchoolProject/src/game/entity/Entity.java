package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;

public abstract class Entity
{
	private int	mId;
	
	protected float	mX, mY, mXV, mYV, mXA, mYA;
	
	protected int	mWidth, mHeight;
	
	protected boolean	mOnGround, mOnWall, mLeftWall;
	
	private boolean		mRemoved;
	
	protected World	mWorld;
	
	protected void move()
	{
		float restX = mXV, restY = mYV, xd, yd, stepX, stepY;
		boolean hitX = false, hitY = false;
		final float rat = mYV / mXV;
		
		// Initializing the step size for x and y
		if (Math.abs(mYV) > Math.abs(mXV))
		{
			stepY = 1;
			stepX = stepY / rat;
		}
		else
		{
			stepX = 1;
			stepY = stepX * rat;
		}
		
		// Making each step
		while ((restX != 0 || restY != 0) && (!hitX || !hitY))
		{
			// Move in x direction
			if (stepX > restX) stepX = restX;
			xd = mWorld.isFree(stepX, 0, this);
			if (xd != Float.NaN) mX += stepX;
			else
			{
				mX += xd;
				hitWall(mXV, 0);
				hitX = true;
			}
			
			// Move in y direction
			if (stepY > restY) stepY = restY;
			yd = mWorld.isFree(0, stepY, this);
			if (yd != Float.NaN) mY += stepY;
			else
			{
				mY += yd;
				hitWall(0, mYV);
				hitY = true;
			}
		}
	}
	
	public void hitWall(float aXV, float aYV)
	{
		if (aXV != 0)
		{
			mOnWall = true;
			mLeftWall = aXV < 0;
			mXV = 0;
		}
		if (aYV != 0)
		{
			if (aYV > 0) mOnGround = true;
			mYV = 0;
		}
	}
	
	public void update()
	{
	}
	
	public void render(Graphics g)
	{
	}
	
	public float getX()
	{
		return mX;
	}
	
	public float getY()
	{
		return mY;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public abstract boolean isSolid();
	
	public void remove()
	{
		mRemoved = true;
	}
	
	public int getId()
	{
		return mId;
	}
	
	public void init(World aWorld, int aId)
	{
		mWorld = aWorld;
	}
	
	public boolean isRemoved()
	{
		return mRemoved;
	}
}
