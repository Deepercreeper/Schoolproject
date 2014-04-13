package game.entity.enemy;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import util.Direction;
import data.DataManager;

public class Minion extends Enemy
{
	private static final int	START_ID	= 20;
	
	private Direction			mDir		= Direction.NONE;
	
	private boolean				mJumping	= false;
	
	public Minion(final int aX, final int aY)
	{
		super(aX, aY, 16, 32, 2);
	}
	
	@Override
	protected void getInput()
	{
		if (mJumping && mOnGround)
		{
			mJumping = false;
			mYV = -4;
		}
		mDir = getXDistanceToPlayer() > 0 ? Direction.LEFT : Direction.RIGHT;
		mXA = mDir.XD;
		if ( !mOnGround) mXA *= 0.125;
	}
	
	@Override
	protected void hitWall(final double aXV, final double aYV)
	{
		if (aXV != 0) mJumping = true;
		super.hitWall(aXV, aYV);
	}
	
	@Override
	public void render(final Graphics aG)
	{
		int id = 0;
		final int delay = 6;
		id = mTime % (delay * 6) / delay;
		Image image = DataManager.instance().getSplitImage("enemy" + DataManager.instance().getTexturePack(), id);
		if (mDir == Direction.LEFT) image = image.getFlippedCopy(true, false);
		aG.drawImage(image, (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
		image = DataManager.instance().getSplitImage("enemy" + DataManager.instance().getTexturePack(), START_ID + id);
		if (mDir == Direction.LEFT) image = image.getFlippedCopy(true, false);
		aG.drawImage(image, (float) (mX - mLevel.getScreenX()), (float) (mY + mHeight / 2 - mLevel.getScreenY()));
	}
}
