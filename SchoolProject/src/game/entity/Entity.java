package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;

public abstract class Entity
{
	protected float	mX, mY, mXV, mYV, mXA, mYA;
	
	protected World	mWorld;
	
	private boolean	mRemoved;
	
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
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
	@Override
	public String toString()
	{
		return this.getClass().getName() + ":" + mX + "-" + mY;
	}
}
