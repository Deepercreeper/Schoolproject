package data;

public class Level
{
	private final byte	mId;
	
	private final World	mWorld;
	
	/**
	 * Creates a new level, which will be added to the parent world.
	 * 
	 * @param aId
	 *            The level id.
	 * @param aWorld
	 *            The parent world.
	 */
	Level(final int aId, final World aWorld)
	{
		mId = (byte) aId;
		mWorld = aWorld;
	}
	
	/**
	 * Returns the identification number of this level. Each level inside one world has its own.
	 * 
	 * @return the id.
	 */
	public int getId()
	{
		return mId;
	}
	
	@Override
	public String toString()
	{
		return mWorld.getId() + "-" + mId;
	}
}
