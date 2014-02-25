package game.entity;

import game.Stats;
import org.newdawn.slick.Graphics;
import data.DataManager;

public class Banana extends Entity
{
	private final boolean	mSuper;
	
	public Banana(int aX, int aY, boolean aSuper)
	{
		super(aX, aY, 16, 16);
		mSuper = aSuper;
	}
	
	@Override
	public void render(Graphics aG)
	{
		aG.drawImage(DataManager.getSplitImage("entity", mSuper ? 2 : 1), (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
	
	public void collect()
	{
		if (isRemoved()) return;
		Stats.instance().addBanana(mSuper ? 5 : 1);
		DataManager.playSound("banana");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
