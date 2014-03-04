package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Stats
{
	private static Stats	STATS;
	
	private int				mBananas	= 0, mHits = 0, mTime = 0, mBananasInLevel = 0;
	
	private Stats()
	{}
	
	/**
	 * Returns the current statistics instance and creates one if none exists.
	 * 
	 * @return the current statistics.
	 */
	public static Stats instance()
	{
		if (STATS == null) STATS = new Stats();
		return STATS;
	}
	
	/**
	 * Resets all statistics.
	 */
	public void reset()
	{
		mBananas = mHits = mTime = mBananasInLevel = 0;
	}
	
	/**
	 * Renders the statistics at the top left corner of the screen.
	 * 
	 * @param aG
	 *            The graphics to draw into.
	 */
	public void render(Graphics aG)
	{
		aG.setColor(Color.white);
		aG.drawString("Bananas: " + mBananas, 10, 20);
		aG.drawString("Hits: " + mHits, 10, 35);
		aG.drawString("Time: " + mTime / 1000, 10, 50);
		aG.drawString("Score: " + getScore(), 10, 80);
	}
	
	/**
	 * Resets the amount of bananas collected to 0.
	 */
	public void setBananaLevel()
	{
		mBananasInLevel = 0;
	}
	
	/**
	 * Adds {@code aAmount} of bananas to the current statistics.
	 * 
	 * @param aAmount
	 *            The amount of bananas.
	 */
	public void addBanana(int aAmount)
	{
		mBananas += aAmount;
		mBananasInLevel += aAmount;
	}
	
	/**
	 * Adds the amount of ticks done to the used time.
	 * 
	 * @param aAmount
	 *            The amount of ticks done.
	 */
	public void tick(int aAmount)
	{
		mTime += aAmount;
	}
	
	/**
	 * Adds a hit to the statistics.
	 */
	public void addHit()
	{
		mHits++ ;
	}
	
	/**
	 * Resets the hits, bananas and bananas inside the level.
	 */
	public void die()
	{
		mHits = 0;
		mBananas -= mBananasInLevel;
		mBananasInLevel = 0;
	}
	
	/**
	 * Calculates the current score depending on the number of bananas, the used time and the deaths.
	 * 
	 * @return the current score.
	 */
	public int getScore()
	{
		return Math.max(mBananas * 100 + 500 - mTime / 100 - mHits * 1000, 0);
	}
}
