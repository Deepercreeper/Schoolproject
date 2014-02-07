package game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Player extends Entity
{
	public Player()
	{
		mWidth = mHeight = 20;
	}
	
	public void updateInput(Input aInput)
	{
		// TODO Input
		mXA = 0;
		if (aInput.isKeyDown(Input.KEY_D)) mXA += 0.01;
		if (aInput.isKeyDown(Input.KEY_A)) mXA -= 0.01;
		if (aInput.isKeyDown(Input.KEY_SPACE)) mYA -= 0.15;
	}
	
	@Override
	public void update()
	{
		mXV += mXA;
		mYV += mYA;
		mX += mXV;
		mY += mYV;
		if (mX <= 0) mX = 0;
		if (mX + mWidth >= 1920) mX = 1920 - mWidth;
		if (mY <= 0) mY = 0;
		if (mY + mHeight >= 1080) mY = 1080 - mHeight;
		mXV *= 0.985;
		mYV *= 0.995;
		mYA = 0.02f;
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO render
		g.setColor(Color.white);
		g.fillRect(mX, mY, mWidth, mHeight);
	}
}
