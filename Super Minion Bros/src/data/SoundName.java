package data;

public enum SoundName
{
	BANANA("banana"), HEART("heart"), CANNON("cannon"), BOMB("bomb"), JUMP("jump"), ITEM("item"), DESTROY_BLOCK("destroyBlock");
	
	private SoundName(final String aPath)
	{
		mPath = aPath;
	}
	
	private final String	mPath;
	
	public String getPath()
	{
		return mPath;
	}
}
