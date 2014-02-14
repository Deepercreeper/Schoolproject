package game.entity;

import game.world.Block;
import game.world.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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
		if ( !mOnGround) mXA *= 0.7;
		
		if (aInput.isKeyPressed(Input.KEY_SPACE) && (mOnGround || mOnWall))
		{
			if (mOnWall)
			{
				mXA += mLeftWall ? 6 : -6;
				mYA -= 6;
			}
			else mYA -= 6;
		}
		
		mXV += mXA;
		mYV += mYA;
		
		mOnGround = mOnWall = false;
		
		move();
		
		mXV *= 0.7f - (mOnGround ? 0.2 : 0);
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
		if (mOnWall && mYV > 0) mYA *= 0.6f;
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO render
		g.setColor(Color.white);
		g.fillRect(getX() - mWorld.getScreenX(), getY() - mWorld.getScreenY(), getWidth(), getHeight());
	}
	
	@Override
	public boolean isSolid()
	{
		return true;
	}
}
