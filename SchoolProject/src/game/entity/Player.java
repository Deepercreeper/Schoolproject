package game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Player extends Entity
{
	public void updateInput(Input aInput)
	{
		// TODO Input
		mXA = mYA = 0;
		if (aInput.isKeyDown(Input.KEY_D)) mXA += 0.01;
		if (aInput.isKeyDown(Input.KEY_A)) mXA -= 0.01;
		if (aInput.isKeyDown(Input.KEY_S)) mYA += 0.01;
		if (aInput.isKeyDown(Input.KEY_W)) mYA -= 0.01;
	}
	
	@Override
	public void update()
	{
		System.out.println(mX + " " + mY);
		mXV += mXA;
		mYV += mYA;
		mX += mXV;
		mY += mYV;
		mXV *= 0.98;
		mYV *= 0.98;
	}
	
	@Override
	public void render(Graphics g)
	{
		// TODO render
		g.setColor(Color.white);
		g.fillRect(mX, mY, 5, 5);
	}
}
