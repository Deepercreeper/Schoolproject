package game.entity.enemy;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import util.Direction;

public class Roller extends Enemy
{
	private Direction	mDir	= Direction.NONE;
	
	public Roller(int aX, int aY)
	{
		super(aX, aY, 16, 16, 1);
	}
	
	@Override
	protected void getInput()
	{
		if (mDir == Direction.NONE) mDir = mLevel.getPlayer().getX() + mLevel.getPlayer().getWidth() < mX ? Direction.LEFT : Direction.RIGHT;
		mXA = mDir.XD;
		if ( !mOnGround) mXA *= 0.125;
	}
	
	@Override
	protected void hitWall(double aXV, double aYV)
	{
		if (aXV != 0)
		{
			if (aXV > 0) mDir = Direction.LEFT;
			else mDir = Direction.RIGHT;
		}
		super.hitWall(aXV, aYV);
	}
	
	@Override
	public void render(Graphics aG)
	{
		aG.setColor(Color.red);
		aG.fillOval((float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()), mWidth, mHeight);
	}
}
