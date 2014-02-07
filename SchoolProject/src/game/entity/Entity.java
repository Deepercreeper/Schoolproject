package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;

public abstract class Entity
{
	protected int	mId;
	
	protected float	mX, mY, mXV, mYV, mXA, mYA, mWidth, mHeight;
	
	protected World	mWorld;
	
	private boolean	mRemoved;
	
	public int getId()
	{
		return mId;
	}
	
	public float getWidth()
	{
		return mWidth;
	}
	
	public float getHeight()
	{
		return mHeight;
	}
	
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
	
	public boolean isRemoved()
	{
		return mRemoved;
	}
	
	public void remove()
	{
		mRemoved = true;
	}
	
	public void init(World aWorld)
	{
		mWorld = aWorld;
		mId = aWorld.createId();
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Entity)
		{
			Entity e = (Entity) o;
			return mId == e.mId;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return mId;
	}
	
	@Override
	public String toString()
	{
		return this.getClass().getName() + ":" + mX + "-" + mY;
	}
}
