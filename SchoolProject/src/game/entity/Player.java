package game.entity;

import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Player extends Entity
{
	private final int	mMaxLife	= 10;
	
	private int			mLife		= mMaxLife;
	
	private int			mHurtDelay;
	
	private boolean		mCannon;
	
	private int			mCannonDelay;
	
	/**
	 * Creates a new player at the given position.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 */
	public Player(int aX, int aY)
	{
		super(aX, aY, 14, 30);
	}
	
	@Override
	public void update(Input aInput)
	{
		if (mHurtDelay > 0) mHurtDelay-- ;
		if ( !mHurt)
		{
			if (aInput.isKeyPressed(Input.KEY_S) && !mOnGround)
			{
				mCannon = true;
				mCannonDelay = 10;
				DataManager.playSound("cannon");
			}
			if (aInput.isKeyPressed(Input.KEY_W) || mOnGround)
			{
				if (mCannon && mOnGround) DataManager.playSound("bomb");
				mCannon = false;
			}
			
			if ( !mCannon)
			{
				mXA = 0;
				if (aInput.isKeyDown(Input.KEY_D)) mXA += 1.5;
				if (aInput.isKeyDown(Input.KEY_A)) mXA -= 1.5;
				if (aInput.isKeyDown(Input.KEY_LSHIFT)) mXA *= 1.5;
				if ( !mOnGround) mXA *= 0.125;
				
				if (aInput.isKeyPressed(Input.KEY_SPACE) && (mOnGround || mOnWall))
				{
					if (mOnWall)
					{
						mXV = 10 * (mLeftWall ? 1 : -1);
						mYV = -5;
					}
					else mYV = -6;
					DataManager.playSound("jump");
				}
			}
			else
			{
				mXV = 0;
				if (--mCannonDelay <= 0) mYV = 10;
				else mYV = 0;
			}
		}
		mHurt = false;
		
		mXV += mXA;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0);
		
		if (mYV < 0 && aInput.isKeyDown(Input.KEY_SPACE))
		{
			mYV *= 0.992;
			mYV += World.GRAVITY * 0.5f;
		}
		else
		{
			mYV *= World.FRICTION;
			if (mOnWall) mYV += (float) (World.GRAVITY * 0.3);
			else mYV += World.GRAVITY;
		}
	}
	
	/**
	 * Returns whether this player has started to do a cannon ball (ass bomb).
	 * 
	 * @return {@code true} if doing and {@code false} if not.
	 */
	public boolean isCannonBall()
	{
		return mCannon;
	}
	
	@Override
	public void render(Graphics g)
	{
		// Player
		g.drawImage(DataManager.getSplitImage("player", 0), (float) mX - mWorld.getScreenX(), (float) mY - mWorld.getScreenY());
		
		// HUD
		g.setColor(Color.red);
		g.fillRect(10, mWorld.getScreenHeight() - 20, 100, 10);
		g.setColor(Color.green);
		g.fillRect(10, mWorld.getScreenHeight() - 20, 100 * mLife / mMaxLife, 10);
	}
	
	@Override
	public void hurt(int aAmount, float aXV, float aYV)
	{
		if (mHurtDelay <= 0)
		{
			mLife -= aAmount;
			mHurtDelay = 100;
			mHurt = true;
			mXV = aXV;
			mYV = aYV;
			if (mLife <= 0) remove();
		}
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
