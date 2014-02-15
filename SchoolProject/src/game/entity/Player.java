package game.entity;

import game.world.World;
import game.world.block.Block;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import data.DataManager;

public class Player extends Entity
{
	public Player()
	{
		mX = mY = 18 * Block.SIZE;
		mWidth = 16;
		mHeight = 30;
	}
	
	@Override
	public void update(Input aInput)
	{
		if (isRemoved()) return;
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
		
		mXV += mXA;
		mYV += mYA;
		
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
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getImage("minion0"), mX - mWorld.getScreenX(), mY - mWorld.getScreenY());
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
