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
		return aEntity.getRectangle().intersects(mRect);
	}
}
