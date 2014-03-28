package game.entity.weapon;

import game.entity.Bullet;
import game.entity.Entity;
import org.newdawn.slick.Input;

public class Pistol extends Weapon
{
	public Pistol(final Entity aParent)
	{
		super(aParent, 10, 4);
	}
	
	public Pistol(final int aX, final int aY)
	{
		super(aX, aY, 10, 4);
	}
	
	@Override
	protected int getXOffset()
	{
		return 3;
	}
	
	@Override
	protected int getYOffset()
	{
		return 0;
	}
	
	@Override
	public void shoot(final Input aInput)
	{
		final int speed = 10;
		final int mouseX = aInput.getMouseX() + mLevel.getScreenX(), mouseY = aInput.getMouseY() + mLevel.getScreenY();
		int startX, startY = (int) (mY + mHeight / 2);
		if (mouseX > mX + mWidth) startX = (int) (mX + mWidth);
		else if (mouseX < mX) startX = (int) mX;
		else
		{
			startX = (int) (mX + mWidth / 2);
			startY = (int) mY;
		}
		final double xd = mouseX - startX, yd = mouseY - startY;
		final double a = Math.acos(Math.abs(xd) / Math.sqrt(xd * xd + yd * yd));
		final double xv = Math.cos(a) * speed * Math.signum(xd), yv = Math.sin(a) * speed * Math.signum(yd);
		mLevel.addEntity(new Bullet(startX, startY, xv, yv, 5, this));
	}
}
