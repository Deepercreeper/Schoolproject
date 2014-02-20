package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Stats
{
	private static Stats	STATS;
	
	private int				mBananas	= 0, mDeaths = 0, mTime = 0, mBananasInLevel = 0;
	
	private Stats()
	{}
	
	public static Stats instance()
	{
		if (STATS == null) STATS = new Stats();
		return STATS;
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString("Bananas: " + mBananas, 10, 20);
		g.drawString("Deaths: " + mDeaths, 10, 35);
		g.drawString("Time: " + mTime / 100, 10, 50);
	}
	
	public void setBananaLevel()
	{
		mBananasInLevel = 0;
	}
	
	public void addBanana(int aAmount)
	{
		mBananas += aAmount;
		mBananasInLevel += aAmount;
	}
	
	public void tick()
	{
		mTime++ ;
	}
	
	public void addDeath()
	{
		mDeaths++ ;
		mBananas -= mBananasInLevel;
		mBananasInLevel = 0;
	}
	
	public int getScore()
	{
		return mBananas * 100 - mTime / 1000 - mDeaths * 10;
	}
}
