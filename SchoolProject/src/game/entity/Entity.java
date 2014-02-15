package game.entity;

import game.world.World;
import game.world.block.Block;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import util.Rectangle;

public abstract class Entity
{
	private int	mId;
	
	protected double	mX, mY, mXV, mYV, mXA;
	
	protected int		mWidth, mHeight;
	
	protected boolean	mOnGround, mOnWall, mLeftWall, mHurted;
	
	private boolean		mRemoved;
	
	protected World		mWorld;
	
	public Entity(int aX, int aY, int aWidth, int aHeight)
	{
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	protected void move()
	{
		mOnGround = mOnWall = false;
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
		while ((restX != 0 && !hitX || restY != 0 && !hitY))
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
	
	public void hitWall(double aXV, double aYV)
	{
		if (mHurted) return;
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
	
	public double getX()
	{
		return mX;
	}
	
	public double getY()
	{
		return mY;
	}
	
	public double getXV()
	{
		return mXV;
	}
	
	public double getYV()
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
	
	public void hurt(int aAmount, float aXV, float aYV)
	{}
	
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
