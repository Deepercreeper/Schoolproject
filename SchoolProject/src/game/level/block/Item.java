package game.level.block;

import game.entity.Banana;
import game.entity.Entity;
import game.entity.Heart;
import game.entity.enemy.Minion;
import game.entity.enemy.Roller;
import java.util.HashMap;

public abstract class Item
{
	private static final HashMap<Integer, Item>	ITEMS			= new HashMap<>();
	
	/**
	 * Creates a new static heart.
	 */
	public static final Item					HEART_STATIC	= new Item(0xFF0000)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Heart(aX, aY, 2, true);
																	}
																	
																	@Override
																	public int getWidth()
																	{
																		return 10;
																	}
																	
																	@Override
																	public int getHeight()
																	{
																		return 10;
																	}
																};
	
	/**
	 * Creates a new non static heart.
	 */
	public static final Item					HEART			= new Item(0xFE0000)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Heart(aX, aY, 2, false);
																	}
																	
																	@Override
																	public int getWidth()
																	{
																		return 10;
																	}
																	
																	@Override
																	public int getHeight()
																	{
																		return 10;
																	}
																};
	
	/**
	 * Creates a new banana.
	 */
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
	
	/**
	 * Creates anew super banana.
	 */
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
	
	public static final Item					ROLLER			= new Item(0x3E4C5E)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Roller(aX, aY);
																	};
																	
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
																
	public static final Item					MINION = new Item(0x3D4C5E)
																{
																	@Override
																	public Entity create(int aX, int aY)
																	{
																		return new Minion(aX, aY);
																	};
																	
																	@Override
																	public int getWidth()
																	{
																		return 16;
																	}
																	
																	@Override
																	public int getHeight()
																	{
																		return 32;
																	}
																};
	
	private Item(int aRGB)
	{
		ITEMS.put(aRGB, this);
	}
	
	/**
	 * Creates the entity.
	 * 
	 * @param aX
	 *            The x position to create at.
	 * @param aY
	 *            The y position to create at.
	 * @return a new entity.
	 */
	public abstract Entity create(int aX, int aY);
	
	/**
	 * Returns the width of the entity to create.
	 * 
	 * @return an integer representing the width.
	 */
	public abstract int getWidth();
	
	/**
	 * Returns the height of the entity to create.
	 * 
	 * @return an integer representing the height.
	 */
	public abstract int getHeight();
	
	/**
	 * Returns whether an items was defined with the given RGB code.
	 * 
	 * @param aRGB
	 *            The color to check.
	 * @return {@code true} if an item with color {@code aRGB} exists and {@code false} if not.
	 */
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
