package data;

public class World
{
	private final byte		mId;
	
	private final Level[]	mLevels	= new Level[10];
	
	public World(final int aId)
	{
		mId = (byte) aId;
	}
	
	public Level[] getLevels()
	{
		return mLevels;
	}
	
	public int getLevelsCount()
	{
		int length = 0;
		for (final Level level : mLevels)
			if (level != null) length++ ;
		return length;
	}
	
	public void addLevel(final int aId)
	{
		mLevels[aId] = new Level(aId, this);
	}
	
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
