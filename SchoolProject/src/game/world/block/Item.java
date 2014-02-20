package game.world.block;

import game.entity.Banana;
import game.entity.Entity;
import java.util.HashMap;

public abstract class Item
{
	private static final HashMap<Integer, Item>	ITEMS			= new HashMap<>();
	
	public static final Item					BANANA			= new Item(0xFFFF00)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Banana(aX, aY, false);
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
	
	public static final Item					SUPER_BANANA	= new Item(0xFF00FF)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Banana(aX, aY, true);
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
	
	private Item(int aRGB)
	{
		ITEMS.put(aRGB, this);
	}
	
	public abstract Entity create(int aX, int aY);
	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
	public static boolean containsCode(int aRGB)
	{
		return ITEMS.containsKey(aRGB);
	}
	
	/**
	 * Creates a new entity that is referenced by the given color code.
	 * 
	 * @param aX
	 *            The x position of the entity.
	 * @param aY
	 *            The y position of the entity.
	 * @param aRGB
	 *            The color code.
	 * @return a new item.
	 */
	public static Entity getItem(int aX, int aY, int aRGB)
	{
		return ITEMS.get(aRGB).create(aX, aY);
	}
}
