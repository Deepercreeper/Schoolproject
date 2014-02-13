package game.world;

import game.entity.Entity;
import game.entity.Player;
import org.newdawn.slick.geom.Rectangle;

public class Screen
{
	private final Rectangle	mRect;
	
	public Screen(int aWidth, int aHeight)
	{
		mRect = new Rectangle(0, 0, aWidth, aHeight);
	}
	
	public void update(Player aPlayer)
	{	
		
	}
	
	public void addX(int aXD)
	{
		if (mRect.getX() + aXD < 0) mRect.setX(0);
		mRect.setX(mRect.getX() + aXD);
	}
	
	public void addY(int aYD)
	{
		if (mRect.getY() + aYD < 0) mRect.setY(0);
		mRect.setY(mRect.getY() + aYD);
	}
	
	public int getX()
	{
		return (int) mRect.getX();
	}
	
	public int getY()
	{
		return (int) mRect.getY();
	}
	
	public int getWidth()
	{
		return (int) mRect.getWidth();
	}
	
	public int getHeight()
	{
		return (int) mRect.getHeight();
	}
	
	public boolean contains(Entity aEntity)
	{
		return aEntity.getRect().intersects(mRect);
	}
}
