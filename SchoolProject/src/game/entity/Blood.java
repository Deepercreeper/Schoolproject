package game.entity;

import game.level.Level;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Blood extends Entity
{
	private int	mLife;
	
	/**
	 * Creates a blood drop that will fly in a random direction and decay after a random time.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 */
	public Blood(final int aX, final int aY)
	{
		super(aX, aY, 2, 2);
		mXV = (Math.random() - Math.random()) * 5;
		mYV = -Math.random() * 5;
		mLife = (int) (100 + Math.random() * 200);
	}
	
	@Override
	public void update(final Input aInput)
	{
		if (--mLife < 0) remove();
		
		mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Level.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Level.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
	}
	
	@Override
	public boolean isParticle()
	{
		return true;
	}
	
	@Override
	public void render(final Graphics aG)
	{
		aG.setColor(Color.red);
		aG.fillRect((float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()), mWidth, mHeight);
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
