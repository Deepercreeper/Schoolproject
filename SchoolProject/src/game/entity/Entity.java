package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;
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
		float restX = mXV, restY = mYV, xd, yd, stepX, stepY;
		boolean hitX = false, hitY = false;
		
		// Initializing the step size for x and y
		if (Math.abs(mYV) > Math.abs(mXV))
		{
			stepY = Math.signum(mYV);
			if (mXV != 0) stepX = mXV / mYV;
			else stepX = 0;
		}
		else
		{
			stepX = Math.signum(mXV);
			if (mYV != 0) stepY = mYV / mXV;
			else stepY = 0;
		}
		
		// Making each step
		while (restX != 0 && !hitX || restY != 0 && !hitY)
		{
			// Move in x direction
			if (restX != 0 && !hitX)
			{
				if (Math.abs(stepX) < Math.abs(restX)) stepX = restX;
				xd = mWorld.isFree(stepX, 0, this);
				if (Float.isNaN(xd)) mX += stepX;
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
				if (Math.abs(stepY) < Math.abs(restY)) stepY = restY;
				yd = mWorld.isFree(0, stepY, this);
				if (Float.isNaN(yd)) mY += stepY;
				else
				{
					mY += yd;
					hitWall(0, mYV);
					hitY = true;
				}
				restY -= stepY;
			}
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
			if (aYV > 0) mOnGround = true;
			mYV = 0;
		}
		System.out.println("HitWall: " + aXV + " " + aYV);
	}
	
	public Rectangle getRect()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public void update()
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
