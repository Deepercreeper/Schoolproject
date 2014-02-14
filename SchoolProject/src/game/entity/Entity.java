package game.entity;

import game.world.World;
import game.world.block.Block;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public abstract class Entity
{
	private int	mId;
	
	protected float	mX, mY, mXV, mYV, mXA, mYA;
	
	protected int	mWidth, mHeight;
	
	protected boolean	mOnGround, mOnWall, mLeftWall;
	
	private boolean		mRemoved;
	
	protected World		mWorld;
	
	protected void move()
	{
		if (mXV == 0 && mYV == 0) return;
		
		double restX = mXV, restY = mYV, xd, yd, stepX, stepY;
		boolean hitX = false, hitY = false;
		
		double a = Math.acos(Math.abs(mXV) / Math.sqrt(mXV * mXV + mYV * mYV));
		
		// Initializing the step size for x and y
		if (mXV == 0) stepX = 0;
		else
		{
			stepX = Math.cos(a);
			if (mXV < 0) stepX *= -1;
		}
		if (mYV == 0) stepY = 0;
		else
		{
			stepY = Math.sin(a);
			if (mYV < 0) stepY *= -1;
		}
		
		// Making each step
		while (restX != 0 && !hitX || restY != 0 && !hitY)
		{
			// Move in x direction
			if (restX != 0 && !hitX)
			{
				if (Math.abs(stepX) > Math.abs(restX)) stepX = restX;
				xd = mWorld.isFree((float) stepX, 0, this);
				if (Double.isNaN(xd)) mX += stepX;
				else
				{
					mX += xd;
					hitWall(mXV, 0);
					hitX = true;
				}
				restX -= stepX;
			}
			
			// Move in y direction
			if (restY != 0 && !hitY)
			{
				if (Math.abs(stepY) > Math.abs(restY)) stepY = restY;
				yd = mWorld.isFree(0, (float) stepY, this);
				if (Double.isNaN(yd)) mY += stepY;
				else
				{
					mY += yd;
					hitWall(0, mYV);
					hitY = true;
				}
				restY -= stepY;
			}
		}
		
		// Out of bounds
		final int width = mWorld.getWidth() * Block.SIZE, height = mWorld.getHeight() * Block.SIZE;
		if (mX <= 0)
		{
			mXV = 0;
			mX = 0;
			mLeftWall = true;
		}
		if (mX + mWidth >= width)
		{
			mXV = 0;
			mX = width - mWidth;
			mLeftWall = false;
		}
		if (mY <= 0)
		{
			mYV = 0;
			mY = 0;
		}
		if (mY + mHeight >= height)
		{
			mYV = 0;
			mY = height - mHeight;
			mOnGround = true;
		}
	}
	
	public void hitEntity(float aXV, float aYV, Entity aEntity)
	{
		hitWall(aXV, aYV);
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
			if (aYV > 0)
			{
				mOnGround = true;
				mOnWall = false;
			}
			mYV = 0;
		}
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public void update(Input aInput)
	{}
	
	public void render(Graphics g)
	{}
	
	public float getX()
	{
		return mX;
	}
	
	public float getY()
	{
		return mY;
	}
	
	public float getXV()
	{
		return mXV;
	}
	
	public float getYV()
	{
		return mYV;
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
