package game.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import util.Direction;

public class Bullet extends Entity
{
	private final Entity	mSource;
	
	private int				mBouncing;
	
	public Bullet(int aX, int aY, double aXV, double aYV, int aBouncing, Entity aSource)
	{
		super(aX, aY, 2, 2);
		mXV = aXV;
		mYV = aYV;
		mSource = aSource;
		mBouncing = aBouncing;
	}
	
	@Override
	public void update(Input aInput)
	{
		move();
	}
	
	@Override
	public void render(Graphics aG)
	{
		// aG.setColor(Color.white);
		// aG.fillRect((float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()), mWidth, mHeight);
		aG.setColor(Color.yellow);
		aG.drawLine((float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()), (float) (mX - mXV / 2 - mLevel.getScreenX()), (float) (mY - mYV / 2 - mLevel.getScreenY()));
		aG.setColor(Color.black);
		aG.drawLine((float) (mX - mXV / 2 - mLevel.getScreenX()), (float) (mY - mYV / 2 - mLevel.getScreenY()), (float) (mX - mXV - mLevel.getScreenX()), (float) (mY - mYV - mLevel.getScreenY()));
	}
	
	@Override
	public void hitEntity(double aXV, double aYV, Entity aEntity)
	{
		if (aEntity == mSource) return;
		if (aEntity instanceof Banana) ((Banana) aEntity).collect();
		if (mSource instanceof Player && aEntity instanceof Heart) ((Heart) aEntity).collect();
	}
	
	@Override
	protected void hitWall(double aXV, double aYV)
	{
		if (mBouncing > 0)
		{
			mBouncing-- ;
			if (aXV != 0) mXV *= -1;
			if (aYV != 0) mYV *= -1;
		}
		else die();
	}
	
	@Override
	public boolean canDestroyBlocks(Direction aDirection)
	{
		return true;
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
