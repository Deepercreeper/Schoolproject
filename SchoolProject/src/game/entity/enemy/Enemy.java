package game.entity.enemy;

import game.entity.Blood;
import game.entity.Entity;
import game.entity.Gore;
import game.entity.Player;
import game.level.Level;
import org.newdawn.slick.Input;

public abstract class Enemy extends Entity
{
	protected int	mLife, mHurtDelay, mTime;
	
	public Enemy(int aX, int aY, int aWidth, int aHeight, int aStartLife)
	{
		super(aX, aY, aWidth, aHeight);
		mLife = aStartLife;
	}
	
	@Override
	public void update(Input aInput)
	{
		mTime++ ;
		if (mHurtDelay > 0) mHurtDelay-- ;
		if ( !mHurt) getInput();
		
		mXV += mXA;
		
		// Reset attributes
		mHurt = mOnIce = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Level.GRAVITY - (mInLiquid ? 0.1 : 0), friction = Level.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		if (mOnWall) mYV += gravity * 0.3;
		else mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
	}
	
	@Override
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{
		if (aEntity instanceof Gore) ((Gore) aEntity).hit(this);
		if (aEntity instanceof Player) ((Player) aEntity).hurt(1, (float) (Math.signum(mXV) * 3), -5);
		// if (aEntity.isSolid()) hitWall(aXV, aYV);
	}
	
	@Override
	public void hurt(int aAmount, double aXV, double aYV)
	{
		if (mHurtDelay <= 0)
		{
			mLife -= aAmount;
			mHurtDelay = 100;
			mHurt = true;
			mXV = aXV;
			mYV = aYV;
			for (int i = (int) (Math.random() * 100 + 50 + (mLife <= 0 ? 50 : 0)); i > 0; i-- )
				mLevel.addEntity(new Blood((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
			for (int i = (int) (Math.random() * 3 + (mLife <= 0 ? 10 : 0)); i > 0; i-- )
				mLevel.addEntity(new Gore((int) (mX + mWidth / 2), (int) (mY + mHeight / 2)));
			if (mLife <= 0) die();
		}
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
