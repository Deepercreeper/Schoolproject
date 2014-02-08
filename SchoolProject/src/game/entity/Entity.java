package game.entity;

import game.world.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public abstract class Entity
{
	protected int		mId;
	
	protected float		mXV, mYV, mXA, mYA;
	
	protected Rectangle	mRect;
	
	protected World		mWorld;
	
	protected boolean	mOnGround;
	
	private boolean		mRemoved;
	
	public Entity(int aWidth, int aHeight)
	{
		mRect = new Rectangle(0, 0, aWidth, aHeight);
	}
	
	public int getId()
	{
		return mId;
	}
	
	public int getWidth()
	{
		return (int) mRect.getWidth();
	}
	
	public int getHeight()
	{
		return (int) mRect.getHeight();
	}
	
	public float getX()
	{
		return mRect.getX();
	}
	
	public float getY()
	{
		return mRect.getY();
	}
	
	public float getXV()
	{
		return mXV;
	}
	
	public float getYV()
	{
		return mYV;
	}
	
	protected void move()
	{
		mRect.setLocation(mRect.getX() + mXV, mRect.getY() + mYV);
	}
	
	protected void move(float aX, float aY)
	{
		mRect.setLocation(aX, aY);
	}
	
	protected void moveX(float aX)
	{
		mRect.setX(aX);
	}
	
	protected void moveY(float aY)
	{
		mRect.setY(aY);
	}
	
	public Rectangle getRectangle()
	{
		return mRect;
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
		return this.getClass().getName() + ":" + mRect;
	}
}
