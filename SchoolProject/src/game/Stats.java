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
	
	public void reset()
	{
		mBananas = mDeaths = mTime = mBananasInLevel = 0;
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.drawString("Bananas: " + mBananas, 10, 20);
		g.drawString("Deaths: " + mDeaths, 10, 35);
		g.drawString("Time: " + mTime / 1000, 10, 50);
		g.drawString("Score: " + getScore(), 10, 80);
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
	
	public void tick(int aAmount)
	{
		mTime += aAmount;
	}
	
	public void addDeath()
	{
		mDeaths++ ;
		mBananas -= mBananasInLevel;
		mBananasInLevel = 0;
	}
	
	public int getScore()
	{
		return Math.max(mBananas * 100 + 500 - mTime / 1000 - mDeaths * 10, 0);
	}
}
