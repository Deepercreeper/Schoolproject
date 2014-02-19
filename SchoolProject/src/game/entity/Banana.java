package game.entity;

import game.Stats;
import org.newdawn.slick.Graphics;
import data.DataManager;

public class Banana extends Entity
{
	public Banana(int aX, int aY)
	{
		super(aX, aY, 16, 16);
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getSplitImage("entity", 1), (float) (mX - mWorld.getScreenX()), (float) (mY - mWorld.getScreenY()));
	}
	
	public void collect()
	{
		if (isRemoved()) return;
		Stats.instance().addBanana();
		DataManager.playSound("banana");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
