package game.entity;

import java.awt.Toolkit;
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
		if (aInput.isKeyDown(Input.KEY_D)) mXA += 0.015;
		if (aInput.isKeyDown(Input.KEY_A)) mXA -= 0.015;
		if (aInput.isKeyDown(Input.KEY_SPACE)) mYA -= 0.15;
	}
	
	@Override
	public void update()
	{
		mXV += mXA;
		mYV += mYA;
		mX += mXV;
		mY += mYV;
		Toolkit tk = Toolkit.getDefaultToolkit();
		int width = tk.getScreenSize().width, height = tk.getScreenSize().height;
		if (mX <= 0) mX = 0;
		if (mX + mWidth >= width) mX = width - mWidth;
		if (mY <= 0) mY = 0;
		if (mY + mHeight >= height) mY = height - mHeight;
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
