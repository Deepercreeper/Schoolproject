package game.entity.enemy;

import game.entity.Blood;
import game.entity.Entity;
import game.entity.Gore;
import game.entity.Player;
import game.level.Map;
import org.newdawn.slick.Input;

public abstract class Enemy extends Entity
{
	protected int	mLife, mTime;
	
	public Enemy(final int aX, final int aY, final int aWidth, final int aHeight, final int aStartLife)
	{
		super(aX, aY, aWidth, aHeight);
		mLife = aStartLife;
	}
	
	@Override
	public void update(final Input aInput)
	{
		mTime++ ;
		if ( !mHurt) getInput();
		
		mXV += mXA;
		
		// Reset attributes
		mHurt = mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Map.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Map.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
	}
	
	@Override
	public void hitEntity(final double aXV, final double aYV, final Entity aEntity)
	{
		if (aEntity instanceof Gore) ((Gore) aEntity).hit(this);
		if (aEntity instanceof Player) ((Player) aEntity).hurt(1, (float) (Math.signum(mXV) * 3), -5);
		if (aEntity instanceof Enemy) hitWall(aXV, aYV);
	}
	
	/**
	 * Calculates the distance between the player and this enemy.
	 * 
	 * @return a double that is {@code > 0} if this enemy is at the right side to the player and {@code < 0} if vice versa.
	 */
	protected double getXDistanceToPlayer()
	{
		return mX + mWidth / 2 - (mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() / 2);
	}
	
	/**
	 * Calculates the distance between the player and this enemy.
	 * 
	 * @return a double that is {@code > 0} if this enemy is under the player and {@code < 0} if vice versa.
	 */
	protected double getYDistanceToPlayer()
	{
		return mY + mHeight / 2 - (mLevel.getPlayer().getY() + mLevel.getPlayer().getHeight() / 2);
	}
	
	public void hitTop(final boolean mCannonBall, final Entity aEntity)
	{
		hurt(mCannonBall ? 2 : 1, Math.signum(getXDistanceToPlayer()) * 2, 0);
	}
	
	@Override
	public void hurt(final int aAmount, final double aXV, final double aYV)
	{
		if (mHurt) return;
		mLife -= aAmount;
		mHurt = true;
		mXV = aXV;
		mYV = aYV;
		for (int i = (int) (Math.random() * 100 + 50 + (mLife <= 0 ? 50 : 0)); i > 0; i-- )
			mLevel.addEntity(new Blood((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
		for (int i = (int) (Math.random() * 3 + (mLife <= 0 ? 10 : 0)); i > 0; i-- )
			mLevel.addEntity(new Gore((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
		if (mLife <= 0) die();
	}
	
	/**
	 * Sets {@code mXA} and {@code mYV} depending on the players position.
	 */
	protected abstract void getInput();
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
