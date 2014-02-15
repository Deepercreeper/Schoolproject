package game.entity;

import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Player extends Entity
{
	private final int	mMaxLife	= 10;
	
	private final int	mLife		= mMaxLife;
	
	private boolean		mCannon;
	
	private int			mCannonDelay;
	
	public Player()
	{
		super(0, 0, 14, 30);
	}
	
	@Override
	public void update(Input aInput)
	{
		if (isRemoved()) return;
		
		if (aInput.isKeyPressed(Input.KEY_S))
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
				else mYA -= 6;
				DataManager.playSound("jump");
			}
		}
		
		mXV += mXA;
		mYV += mYA;
		
		if (mCannon)
		{
			mXV = 0;
			if (--mCannonDelay <= 0) mYV = 10;
			else mYV = 0;
		}
		
		mOnGround = mOnWall = false;
		
		move();
		
		mXV *= 0.95f - (mOnGround ? 0.45 : 0);
		if (mYV < 0 && aInput.isKeyDown(Input.KEY_SPACE))
		{
			mYV *= 0.992;
			mYA = World.GRAVITY * 0.5f;
		}
		else
		{
			mYV *= World.FRICTION;
			mYA = World.GRAVITY;
		}
		if (mOnWall && mYV > 0) mYA *= 0.3f;
	}
	
	public boolean isCannonBall()
	{
		return mCannon;
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getImage("minion0"), mX - mWorld.getScreenX(), mY - mWorld.getScreenY());
		g.setColor(Color.red);
		g.fillRect(10, mWorld.getScreenHeight() - 20, 100, 10);
		g.setColor(Color.green);
		g.fillRect(10, mWorld.getScreenHeight() - 20, 100 * mLife / mMaxLife, 10);
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
