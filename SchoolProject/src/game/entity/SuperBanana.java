package game.entity;

import game.Stats;
import org.newdawn.slick.Graphics;
import data.DataManager;

public class SuperBanana extends Entity
{
	public SuperBanana(int aX, int aY)
	{
		super(aX, aY, 16, 16);
	}
	
	@Override
	public void render(Graphics g)
	{
		g.drawImage(DataManager.getSplitImage("entity", 2), (float) (mX - mWorld.getScreenX()), (float) (mY - mWorld.getScreenY()));
	}
	
	public void collect()
	{
		if (isRemoved()) return;
		Stats.instance().addBanana(5);
		DataManager.playSound("banana");
		remove();
	}
	
	@Override
	public boolean isSolid()
	{
		return false;
	}
}
