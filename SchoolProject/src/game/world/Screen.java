package game.world;

import game.entity.Entity;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.newdawn.slick.geom.Rectangle;

public class Screen
{
	private final Rectangle	mRect;
	
	public Screen()
	{
		final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		mRect = new Rectangle(0, 0, size.width, size.height);
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
	
	public int getWidth()
	{
		return (int) mRect.getWidth();
	}
	
	public int getHeight()
	{
		return (int) mRect.getHeight();
	}
	
	public boolean intersectsWith(Entity aEntity)
	{
		return aEntity.getRectangle().intersects(mRect);
	}
}
