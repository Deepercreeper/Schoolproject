package data;

public class Level
{
	private final byte	mId;
	
	private final World	mWorld;
	
	Level(final int aId, final World aWorld)
	{
		mId = (byte) aId;
		mWorld = aWorld;
	}
	
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
