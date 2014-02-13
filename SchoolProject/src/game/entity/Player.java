package game.entity;

import game.world.Block;
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
	
	public void updateInput(Input aInput)
	{
		// TODO Input
		if (isRemoved()) return;
		mXA = 0;
		if (aInput.isKeyDown(Input.KEY_D)) mXA += 1;
		if (aInput.isKeyDown(Input.KEY_A)) mXA -= 1;
		if (aInput.isKeyDown(Input.KEY_LSHIFT)) mXA *= 2;
		if (aInput.isKeyDown(Input.KEY_SPACE) && (mOnGround || mOnWall))
		{
			if (mOnWall)
			{
				mXA += mLeftWall ? 10 : -10;
				mYA -= 10;
			}
			else mYA -= 20;
		}
	}
	
	@Override
	public void update()
	{
		mXV += mXA;
		mYV += mYA;
		mOnGround = mOnWall = false;
		move();
		final int width = mWorld.getWidth() * Block.SIZE, height = mWorld.getHeight() * Block.SIZE;
		if (mX <= 0)
		{
			mXV = 0;
			mX = 0;
			mOnWall = true;
			mLeftWall = true;
		}
		if (mX + mWidth >= width)
		{
			mXV = 0;
			mX = width - mWidth;
			mOnWall = true;
			mLeftWall = false;
		}
		if (mY <= 0)
		{
			mYV = 0;
			mY = 0;
		}
		if (mY + mHeight >= height)
		{
			mYV = 0;
			mY = height - mHeight;
			mOnGround = true;
		}
		mXV *= 0.9;
		mYV *= 0.995;
		mYA = 0.9f;
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
