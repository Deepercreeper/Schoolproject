package game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Player extends Entity
{
	public Player()
	{
		super(10, 20);
	}
	
	public void updateInput(Input aInput)
	{
		// TODO Input
		mXA = 0;
		if (aInput.isKeyDown(Input.KEY_D)) mXA += 0.5;
		if (aInput.isKeyDown(Input.KEY_A)) mXA -= 0.5;
		if (aInput.isKeyDown(Input.KEY_LSHIFT)) mXA *= 2;
		if (aInput.isKeyDown(Input.KEY_SPACE) && mOnGround) mYA -= 20;
	}
	
	@Override
	public void update()
	{
		mXV += mXA;
		mYV += mYA;
		move();
		mOnGround = false;
		final int width = mWorld.getWidth(), height = mWorld.getHeight();
		if (getX() <= 0)
		{
			mXV = 0;
			moveX(0);
		}
		if (mRect.getMaxX() >= width)
		{
			mXV = 0;
			moveX((width - mRect.getWidth()));
		}
		if (getY() <= 0)
		{
			mYV = 0;
			moveY(0);
		}
		if (mRect.getMaxY() >= height)
		{
			mYV = 0;
			moveY((height - mRect.getHeight()));
			mOnGround = true;
		}
		mXV *= 0.985;
		mYV *= 0.995;
		mYA = 0.9f;
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO render
		g.setColor(Color.white);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
