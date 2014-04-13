package game.entity;

import game.level.Map;
import game.level.block.Block;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Heart extends Entity
{
	private final int		mSize;
	private int				mLife;
	private final boolean	mStatic;
	
	/**
	 * Creates a new heart that is able to restore {@code aSize} of lives to the collecting entity. Decays after a random time if {@code aStatic} is {@code true}.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aSize
	 *            The size of this heart.
	 * @param aStatic
	 *            Whether this heart is able to stay or going to decay.
	 */
	public Heart(final int aX, final int aY, final int aSize, final boolean aStatic)
	{
		super(aX, aY, 6 + aSize * 2, 6 + aSize * 2);
		mSize = aSize;
		mStatic = aStatic;
		if ( !mStatic) mLife = (int) (100 + Math.random() * 200 + mSize * 100);
	}
	
	@Override
	public void update(final Input aInput)
	{
		if ( !mStatic && --mLife < 0) remove();
		
		mOnIce = false;
		
		final double distance = Math.sqrt(Math.pow(mLevel.getPlayer().getY() + mLevel.getPlayer().getHeight() / 2 - mY - mHeight / 2, 2)
				+ Math.pow(mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() / 2 - mX - mWidth / 2, 2));
		if (distance < Block.SIZE * 3) mXV = (Block.SIZE * 3 - distance) * 0.05 * Math.signum(mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() / 2 - mX - mWidth / 2);
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0) + (mOnIce ? 0.48 : 0) - (mInLiquid ? 0.3 : 0);
		
		final double gravity = Map.GRAVITY * 0.7 - (mInLiquid ? 0.1 : 0), friction = Map.FRICTION - (mInLiquid ? 0.1 : 0);
		
		mYV *= friction;
		mYV += gravity;
		
		// Reset attributes
		mInLiquid = false;
		
	}
	
	@Override
	public void render(final Graphics aG)
	{
		if (mLife > 40 || mLife % 8 < 4) aG.drawImage(DataManager.instance().getSplitImage("entity" + DataManager.instance().getTexturePack(), 3).getScaledCopy(mWidth, mHeight),
				(float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
	
	/**
	 * Collects and removes this entity and adds {@code mSize} lives to the players life.
	 */
	public void collect()
	{
		if (isRemoved()) return;
		mLevel.getPlayer().addLife(mSize);
		DataManager.instance().playSound("heart");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
