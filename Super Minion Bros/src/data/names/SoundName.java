package data.names;

public enum SoundName
{
	BANANA("banana"), HEART("heart"), CANNON("cannon"), BOMB("bomb"), JUMP("jump"), ITEM("item"), DESTROY_BLOCK("destroyBlock");
	
	private SoundName(final String aPath)
	{
		mPath = aPath;
	}
	
	private final String	mPath;
	
	/**
	 * Returns the path of this sound.
	 * 
	 * @return the path.
	 */
	public String getPath()
	{
		return mPath;
	}
}
