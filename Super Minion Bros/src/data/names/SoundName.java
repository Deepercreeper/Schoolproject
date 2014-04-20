package data.names;

public enum SoundName implements DataName
{
	BANANA("banana"), HEART("heart"), CANNON("cannon"), BOMB("bomb"), JUMP("jump"), ITEM("item"), DESTROY_BLOCK("destroyBlock");
	
	private SoundName(final String aPath)
	{
		mPath = aPath;
	}
	
	private final String	mPath;
	
	@Override
	public String getPath()
	{
		return mPath;
	}
}
