package game.world.block;

import game.entity.Banana;
import game.entity.Entity;

public interface Item
{
	public static final Item	BANANA	= new Item()
										{
											@Override
											public Entity create(int aX, int aY)
											{
												return new Banana(aX, aY);
											}
											
											@Override
											public int getWidth()
											{
												return 16;
											}
											
											@Override
											public int getHeight()
											{
												return 16;
											}
										};
	
	public Entity create(int aX, int aY);
	
	public int getWidth();
	
	public int getHeight();
}
