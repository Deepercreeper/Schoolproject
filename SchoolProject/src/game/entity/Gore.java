package game.entity;

import game.level.Level;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Gore extends Entity
{
	private int	mLife;
	
	/**
	 * Creates a new gore that will fly in a random direction and decay after a random time. It can be kicked by a solid entity.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 */
	public Gore(int aX, int aY)
	{
		super(aX, aY, 4, 3);
		mXV = (Math.random() - Math.random()) * 5;
		mYV = -Math.random() * 5;
		mLife = (int) (150 + Math.random() * 250);
	}
	
	/**
	 * If this gore is kicked by another entity this method is invoked.
	 * 
	 * @param aEntity
	 *            The kicking entity.
	 */
	public void hit(Entity aEntity)
	{
		if ( !mOnGround) return;
		mXV += Math.signum(mX - aEntity.getX()) * (Math.random() * 5 + 2);
		mYV -= Math.random() * 5 + 2;
	}
	
	@Override
	public void update(Input aInput)
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
	public void render(Graphics aG)
	{
		aG.drawImage(DataManager.getSplitImage("entity" + DataManager.getTexturePack(), 0), (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
