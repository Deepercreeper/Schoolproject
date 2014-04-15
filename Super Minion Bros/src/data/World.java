package data;

public class World
{
	private final byte		mId;
	
	private final Level[]	mLevels	= new Level[10];
	
	/**
	 * Creates a new world.
	 * 
	 * @param aId
	 *            The world id.
	 */
	public World(final int aId)
	{
		mId = (byte) aId;
	}
	
	/**
	 * Returns an array of all levels inside this world.
	 * 
	 * @return the underlying levels.
	 */
	public Level[] getLevels()
	{
		return mLevels;
	}
	
	/**
	 * Counts the number of levels inside this world and returns it.
	 * 
	 * @return the number of levels.
	 */
	public int getLevelsCount()
	{
		int length = 0;
		for (final Level level : mLevels)
			if (level != null) length++ ;
		return length;
	}
	
	/**
	 * Adds a new level to this world.
	 * 
	 * @param aId
	 *            The id of the new level.
	 */
	public void addLevel(final int aId)
	{
		mLevels[aId] = new Level(aId, this);
	}
	
	/**
	 * Returns the identification number of this world. Each world has its own id.
	 * 
	 * @return the id.
	 */
	public byte getId()
	{
		return mId;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder text = new StringBuilder(mId + ":");
		for (int i = 0; i < mLevels.length; i++ )
			if (mLevels[i] != null) text.append(i + ", ");
		return text.toString();
	}
	
	@Override
	public boolean equals(final Object aObj)
	{
		if (aObj instanceof World)
		{
			final World w = (World) aObj;
			return mId == w.mId;
		}
		return false;
	}
}
