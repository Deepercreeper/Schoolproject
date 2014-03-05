package game.entity;

import game.level.Level;
import game.level.block.Block;
import java.util.HashMap;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import util.Direction;
import util.Rectangle;

public abstract class Entity
{
	private int									mId;
	private boolean								mRemoved;
	private final HashMap<Integer, Direction>	mTouchingBlocks	= new HashMap<>();
	
	/**
	 * The position and velocity of this entity.
	 */
	protected double							mX, mY, mXV, mYV, mXA, mYA;
	
	/**
	 * The size of this entity.
	 */
	protected int								mWidth, mHeight;
	
	/**
	 * States that tell whether this entity is on the ground, on the wall, on which wall, whether it was hurt at the last update, is inside a liquid block, is standing on ice or dead.
	 */
	protected boolean							mOnGround, mOnWall, mLeftWall, mHurt, mInLiquid, mOnIce, mDead;
	
	/**
	 * The parent level within this entity is living.
	 */
	protected Level								mLevel;
	
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
	protected final void move()
	{
		mOnGround = false;
		
		// On Wall?
		if (mLeftWall && mXV > 0 || !mLeftWall && mXV < 0 || Double.isNaN(mLevel.isFree(mLeftWall ? -0.1 : 0.1, 0, this))) mOnWall = false;
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
				xd = mLevel.isFree(stepX, 0, this);
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
				yd = mLevel.isFree(0, stepY, this);
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
		
		touchBlocks();
		
		// Out of bounds
		final int width = mLevel.getWidth() * Block.SIZE, height = mLevel.getHeight() * Block.SIZE;
		if (mX <= 0)
		{
			mX = 0;
			hitWall(mXV, 0);
			mOnWall = false;
			mLeftWall = true;
		}
		if (mX + mWidth >= width)
		{
			mX = width - mWidth;
			hitWall(mXV, 0);
			mOnWall = false;
			mLeftWall = false;
		}
		if (mY <= 0)
		{
			mY = 0;
			hitWall(0, mYV);
		}
		if (mY >= height) die();
	}
	
	private final void touchBlocks()
	{
		final HashMap<Block, Direction> blocks = new HashMap<>();
		for (int tile : mTouchingBlocks.keySet())
		{
			final int x = tile % mLevel.getWidth(), y = tile / mLevel.getWidth();
			blocks.put(Block.get(mLevel.getBlock(x, y)), mTouchingBlocks.get(tile));
		}
		for (int tile : mTouchingBlocks.keySet())
		{
			final int x = tile % mLevel.getWidth(), y = tile / mLevel.getWidth();
			final Direction dir = mTouchingBlocks.get(tile);
			Block.get(mLevel.getBlock(x, y)).hit(x, y, mLevel, this, dir, blocks);
		}
		mTouchingBlocks.clear();
	}
	
	/**
	 * Adds a block to the list of this entity touching blocks.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aDir
	 *            The touching direction.
	 */
	public final void addTouchingBlock(int aX, int aY, Direction aDir)
	{
		mTouchingBlocks.put(aX + aY * mLevel.getWidth(), aDir);
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
		if (aEntity.isSolid()) hitWall(aXV, aYV);
	}
	
	/**
	 * Invoked when this entity hits a wall after moving. By default {@code mOnGround} and {@code mOnWall} are updated.
	 * 
	 * @param aXV
	 *            The x velocity.
	 * @param aYV
	 *            The y velocity.
	 */
	protected void hitWall(double aXV, double aYV)
	{
		// if (mHurt) return;
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
	public final Rectangle getRect()
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
	 * Please don't render at {@code mX:mY} but at {@code mX - mLevel.getScreenX():mY - mLevel.getScreenY()}.
	 * 
	 * @param aG
	 *            The graphics wherein this entity is rendered.
	 */
	public void render(Graphics aG)
	{}
	
	/**
	 * Moves this entity along the x axis.
	 * 
	 * @param aX
	 *            The new x position.
	 */
	public final void setX(double aX)
	{
		mX = aX;
	}
	
	/**
	 * Moves this entity along the y axis.
	 * 
	 * @param aY
	 *            The new y position.
	 */
	public final void setY(double aY)
	{
		mY = aY;
	}
	
	/**
	 * Returns the actual x position of this entity.
	 * 
	 * @return the x position.
	 */
	public final double getX()
	{
		return mX;
	}
	
	/**
	 * Returns the actual y position of this entity.
	 * 
	 * @return the y position.
	 */
	public final double getY()
	{
		return mY;
	}
	
	/**
	 * Returns the actual x velocity of this entity.
	 * 
	 * @return the x velocity.
	 */
	public final double getXV()
	{
		return mXV;
	}
	
	/**
	 * Returns the actual y velocity of this entity.
	 * 
	 * @return the y velocity.
	 */
	public final double getYV()
	{
		return mYV;
	}
	
	/**
	 * The width of this entity defined at instantiation.
	 * 
	 * @return the width.
	 */
	public final int getWidth()
	{
		return mWidth;
	}
	
	/**
	 * The height of this entity defined at instantiation.
	 * 
	 * @return the height.
	 */
	public final int getHeight()
	{
		return mHeight;
	}
	
	/**
	 * Sets the entity state to on an ice block. That influences the moving.
	 */
	public final void setOnIce()
	{
		mOnIce = true;
	}
	
	/**
	 * Sets the entity state to inside a liquid. That influences the rendering and updating.
	 */
	public final void setInLiquid()
	{
		mInLiquid = true;
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
	public void hurt(int aAmount, double aXV, double aYV)
	{}
	
	/**
	 * Returns whether this entity is solid. If so it is able to fly through walls.
	 * 
	 * @return {@code true} if solid and {@code false} if not.
	 */
	public abstract boolean isSolid();
	
	/**
	 * Makes this entity die and removes it by default.
	 */
	public void die()
	{
		mDead = true;
		remove();
	}
	
	/**
	 * Resets all attributes to the state when this entity is created.
	 */
	public void respawn()
	{
		mDead = false;
	}
	
	/**
	 * Returns whether this entity has been killed or died itself.
	 * 
	 * @return {@code true} if this entity is dead and {@code false} if not.
	 */
	public final boolean isDead()
	{
		return mDead;
	}
	
	/**
	 * Removes this entity from the parent level.
	 */
	protected final void remove()
	{
		mRemoved = true;
	}
	
	/**
	 * The id given by the id handler of the level.
	 * 
	 * @return the id of this entity.
	 */
	public final int getId()
	{
		return mId;
	}
	
	/**
	 * Binds this entity to the given level and sets the created id.
	 * 
	 * @param aLevel
	 *            The parent level.
	 * @param aId
	 *            The created id.
	 */
	public final void init(Level aLevel, int aId)
	{
		mLevel = aLevel;
		mId = aId;
	}
	
	/**
	 * Returns whether this entity is able to destroy blocks. Please override if so.
	 * 
	 * @return {@code true} if this entity destroys blocks and {@code false} if not.
	 */
	public boolean canDestroyBlock(Direction aDirection)
	{
		return false;
	}
	
	/**
	 * Returns whether this entity has been removed before.
	 * 
	 * @return {@code true} if removed and {@code false} if not.
	 */
	public final boolean isRemoved()
	{
		return mRemoved;
	}
	
	@Override
	public final String toString()
	{
		return "<" + mId + "> Position: (" + mX + "," + mY + ") Size: (" + mWidth + "," + mHeight + ")";
	}
	
	@Override
	public final int hashCode()
	{
		return mId;
	}
	
	@Override
	public final boolean equals(Object aO)
	{
		if (aO instanceof Entity)
		{
			Entity e = (Entity) aO;
			return e.mLevel == mLevel && e.mId == mId;
		}
		return false;
	}
}
