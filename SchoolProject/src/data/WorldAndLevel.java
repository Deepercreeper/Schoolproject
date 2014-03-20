package data;

public class WorldAndLevel implements Comparable<WorldAndLevel>
{
	private byte	mWorld	= -1, mLevel = -1;
	
	public WorldAndLevel(int aWorld, int aLevel)
	{
		mLevel = (byte) aLevel;
		mWorld = (byte) aWorld;
	}
	
	public byte getLevel()
	{
		return mLevel;
	}
	
	public byte getWorld()
	{
		return mWorld;
	}
	
	@Override
	public String toString()
	{
		return mWorld + "-" + mLevel;
	}
	
	@Override
	public boolean equals(Object aObj)
	{
		if (aObj instanceof WorldAndLevel)
		{
			WorldAndLevel w = (WorldAndLevel) aObj;
			return mWorld == w.mWorld && mLevel == w.mLevel;
		}
		return false;
	}
	
	@Override
	public int compareTo(WorldAndLevel aObj)
	{
		if (mWorld == aObj.mWorld) return Byte.compare(mLevel, aObj.mLevel);
		return Byte.compare(mWorld, aObj.mWorld);
	}
}
