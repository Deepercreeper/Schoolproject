package game.entity.enemy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import util.Direction;
import data.DataManager;
import data.ImageName;

public class Roller extends Enemy
{
	private static final int	START_ID	= 40;
	
	private Direction			mDir		= Direction.NONE;
	
	public Roller(final int aX, final int aY)
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
	protected void hitWall(final double aXV, final double aYV)
	{
		if (aXV != 0)
		{
			if (aXV > 0) mDir = Direction.LEFT;
			else mDir = Direction.RIGHT;
		}
		super.hitWall(aXV, aYV);
	}
	
	@Override
	public void render(final Graphics aG)
	{
		int id = 0;
		final int delay = 6;
		id = mTime % (delay * 6) / delay;
		Image image = DataManager.instance().getTexturedSplitImage(ImageName.ENEMY, START_ID + id);
		if (mDir == Direction.LEFT) image = image.getFlippedCopy(true, false);
		aG.drawImage(image, (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
}
