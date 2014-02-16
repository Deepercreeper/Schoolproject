package game.entity;

import game.world.World;
import game.world.block.Block;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import util.Rectangle;

public abstract class Entity
{
	private int	mId;
	
	/**
	 * The position and velocity of this entity.
	 */
	protected double	mX, mY, mXV, mYV, mXA;
	
	/**
	 * The size of this entity.
	 */
	protected int		mWidth, mHeight;
	
	/**
	 * States that tell whether this entity is on the ground, on the wall, on which wall and whether it was hurt at the last update.
	 */
	protected boolean	mOnGround, mOnWall, mLeftWall, mHurt;
	
	private boolean		mRemoved;
	
	/**
	 * The parent world within this entity is living.
	 */
	protected World		mWorld;
	
	/**
	 * Creates a entity with position {@code aX:aY} and size {@code aWidth:aHeight}.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aWidth
	 *            The width.
	 * @param aHeight
	 *            The height.
	 */
	public Entity(int aX, int aY, int aWidth, int aHeight)
	{
		mX = aX;
		mY = aY;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	/**
	 * Moves this entity in {@code mXV} and {@code mYV} direction.<br>
	 * If this entity hits a block, the {@code hit()} method of block will be invoked.<br>
	 * After that {@code hitWall()} is invoked with the current velocity.<br>
	 * If this entity hits another entity hitEntity is invoked (if the other.{@code isSolid()}).
	 */
	protected void move()
	{
		mOnGround = false;
		
		// On Wall?
		if (mLeftWall && mXV > 0 || !mLeftWall && mXV < 0 || Double.isNaN(mWorld.isFree(mLeftWall ? -0.1 : 0.1, 0, this))) mOnWall = false;
		if (mXV == 0 && mYV == 0) return;
		
		final double a = Math.acos(Math.abs(mXV) / Math.sqrt(mXV * mXV + mYV * mYV));
		double restX = mXV, restY = mYV, xd, yd, stepX, stepY;
		boolean hitX = false, hitY = false;
		
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
	
	/**
	 * Invoked, when this entity hits another entity which is solid. By default {@code hitWall()} with the same parameters is invoked.
	 * 
	 * @param aXV
	 *            The x velocity with which the entity is hit.
	 * @param aYV
	 *            The y velocity with which the entity is hit.
	 * @param aEntity
	 *            The other entity.
	 */
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{
		hitWall(aXV, aYV);
	}
	
	/**
	 * Invoked when this entity hits a wall after moving. By default {@code mOnGround} and {@code mOnWall} are updated.
	 * 
	 * @param aXV
	 *            The x velocity.
	 * @param aYV
	 *            The y velocity.
	 */
	private void hitWall(double aXV, double aYV)
	{
		if (mHurt) return;
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
	
	/**
	 * Creates a rectangle with position and size of this entity.
	 * 
	 * @return a rectangle with position {@code mX:mY} and size {@code mWidth:mHeight}.
	 */
	public Rectangle getRect()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	/**
	 * Invoked to update this entity after each tick. By default nothing is done.
	 * 
	 * @param aInput
	 *            The input data containing whether keys and mouse buttons are pressed.
	 */
	public void update(Input aInput)
	{}
	
	/**
	 * Invoked to render this entity on the screen.<br>
	 * Please don't render at {@code mX:mY} but at {@code mX - mWorld.getScreenX():mY - mWorld.getScreenY()}.
	 * 
	 * @param g
	 *            The graphics wherein this entity is rendered.
	 */
	public void render(Graphics g)
	{}
	
	/**
	 * Returns the actual x position of this entity.
	 * 
	 * @return the x position.
	 */
	public double getX()
	{
		return mX;
	}
	
	/**
	 * Returns the actual y position of this entity.
	 * 
	 * @return the y position.
	 */
	public double getY()
	{
		return mY;
	}
	
	/**
	 * Returns the actual x velocity of this entity.
	 * 
	 * @return the x velocity.
	 */
	public double getXV()
	{
		return mXV;
	}
	
	/**
	 * Returns the actual y velocity of this entity.
	 * 
	 * @return the y velocity.
	 */
	public double getYV()
	{
		return mYV;
	}
	
	/**
	 * The width of this entity defined at instantiation.
	 * 
	 * @return the width.
	 */
	public int getWidth()
	{
		return mWidth;
	}
	
	/**
	 * The height of this entity defined at instantiation.
	 * 
	 * @return the height.
	 */
	public int getHeight()
	{
		return mHeight;
	}
	
	/**
	 * Invoked by other classes like SpikeBlock when hurting this entity.
	 * 
	 * @param aAmount
	 *            The number of lives that should be subtracted.
	 * @param aXV
	 *            The x velocity at the hurt time.
	 * @param aYV
	 *            The y velocity at the hurt time.
	 */
	public void hurt(int aAmount, float aXV, float aYV)
	{}
	
	/**
	 * Returns whether this entity is solid. If so it is able to fly through walls.
	 * 
	 * @return {@code true} if solid and {@code false} if not.
	 */
	public abstract boolean isSolid();
	
	/**
	 * Removes this entity. Maybe seen as killing.
	 */
	public void remove()
	{
		mRemoved = true;
	}
	
	/**
	 * The id given by the id handler of the world.
	 * 
	 * @return the id of this entity.
	 */
	public int getId()
	{
		return mId;
	}
	
	/**
	 * Binds this entity to the given world and sets the created id.
	 * 
	 * @param aWorld
	 *            The parent world.
	 * @param aId
	 *            The created id.
	 */
	public void init(World aWorld, int aId)
	{
		mWorld = aWorld;
	}
	
	/**
	 * Returns whether this entity has been removed before.
	 * 
	 * @return {@code true} if removed and {@code false} if not.
	 */
	public boolean isRemoved()
	{
		return mRemoved;
	}
}
