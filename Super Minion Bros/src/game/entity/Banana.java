package game.entity;

import game.Stats;
import org.newdawn.slick.Graphics;
import data.DataManager;

public class Banana extends Entity
{
	private final boolean	mSuper;
	
	/**
	 * Creates a banana that floats at the given position and can be collected to add score.
	 * 
	 * @param aX
	 *            The x position.
	 * @param aY
	 *            The y position.
	 * @param aSuper
	 *            Whether this banana should add 5 bananas to the score or just one.
	 */
	public Banana(final int aX, final int aY, final boolean aSuper)
	{
		super(aX, aY, 16, 16);
		mSuper = aSuper;
	}
	
	@Override
	public void render(final Graphics aG)
	{
		aG.drawImage(DataManager.instance().getSplitImage("entity" + DataManager.instance().getTexturePack(), mSuper ? 2 : 1), (float) (mX - mLevel.getScreenX()), (float) (mY - mLevel.getScreenY()));
	}
	
	/**
	 * Collects this banana, removes it and adds the amount to the current banana statistics.
	 */
	public void collect()
	{
		if (isRemoved()) return;
		Stats.instance().addBanana(mSuper ? 5 : 1);
		DataManager.instance().playSound("banana");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
