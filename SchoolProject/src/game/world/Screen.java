package game.world;

import game.entity.Entity;
import game.entity.Player;
import game.world.block.Block;
import org.newdawn.slick.geom.Rectangle;

public class Screen
{
	private final int		MOVEMENT_SPACE	= 20 / 2;
	
	private final Rectangle	mRect;
	
	private final World		mWorld;
	
	public Screen(World aWorld, int aWidth, int aHeight)
	{
		mWorld = aWorld;
		mRect = new Rectangle(0, 0, aWidth, aHeight);
	}
	
	public void update(Player aPlayer)
	{
		if (Math.abs(mRect.getCenterX() - aPlayer.getRect().getCenterX()) > Block.SIZE * MOVEMENT_SPACE)
		{
			if (aPlayer.getRect().getCenterX() > mRect.getCenterX()) mRect.setCenterX(aPlayer.getRect().getCenterX() - Block.SIZE * MOVEMENT_SPACE);
			else mRect.setCenterX(aPlayer.getRect().getCenterX() + Block.SIZE * MOVEMENT_SPACE);
		}
		mRect.setCenterY(Math.round(aPlayer.getY()) + aPlayer.getHeight());
		if (mRect.getX() < 0) mRect.setX(0);
		if (mRect.getY() < 0) mRect.setY(0);
		if (mRect.getMaxX() > mWorld.getWidth() * Block.SIZE) mRect.setX(mWorld.getWidth() * Block.SIZE - mRect.getWidth());
		if (mRect.getMaxY() > mWorld.getHeight() * Block.SIZE) mRect.setY(mWorld.getHeight() * Block.SIZE - mRect.getHeight());
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
