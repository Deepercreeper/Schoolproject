package game.world;

import game.entity.Entity;
import org.newdawn.slick.geom.Rectangle;

public class Screen
{
	private final Rectangle	mRect;
	
	public Screen(int aWidth, int aHeight)
	{
		mRect = new Rectangle(0, 0, aWidth, aHeight);
	}
	
	public void move(int aX, int aY)
	{
		mRect.setLocation(aX, aY);
	}
	
	public void resize(int aWidth, int aHeight)
	{
		mRect.setSize(aWidth, aHeight);
	}
	
	public int getX()
	{
		return (int) mRect.getX();
	}
	
	public int getY()
	{
		return (int) mRect.getY();
	}
	
	public boolean intersectsWith(Entity aEntity)
	{
		return aEntity.getRectangle().intersects(mRect);
	}
}
