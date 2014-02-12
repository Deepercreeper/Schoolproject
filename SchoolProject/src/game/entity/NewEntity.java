package game.entity;

import game.world.NewWorld;
import org.newdawn.slick.Graphics;

public abstract class NewEntity
{
	private int	mId;
	
	protected float	mX, mY, mXV, mYV, mXA, mYA;
	
	protected int	mWidth, mHeight;
	
	protected boolean	mOnGround, mOnWall, mLeftWall;
	
	private boolean		mRemoved;
	
	protected NewWorld	mWorld;
	
	protected void move()
	{
		float xVLeft = mXV, yVLeft = mYV;
		int xDir = (int) Math.signum(xVLeft), yDir = (int) Math.signum(yVLeft);
		boolean colX = false, colY = false;
		while ((xVLeft != 0 || yVLeft != 0) && ( !colX || !colY))
		{
			if ( !colX)
			{
				if (mWorld.isFree(xDir, 0, this))
				{	
					
				}
				else
				{	
					
				}
			}
			if ( !colY)
			{
				if (mWorld.isFree(0, yDir, this))
				{	
					
				}
				else
				{	
					
				}
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
	
	public void init(NewWorld aWorld, int aId)
	{
		mWorld = aWorld;
	}
	
	public boolean isRemoved()
	{
		return mRemoved;
	}
}
