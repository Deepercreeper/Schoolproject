package data.names;

public enum MusicName
{
	MENU("menu"), WORLD_0("world0"), WORLD_1("world1"), WORLD_2("world2"), WORLD_3("world3"), WORLD_4("world4"), WORLD_5("world5");
	
	private MusicName(final String aPath)
	{
		mPath = aPath;
	}
	
	private final String	mPath;
	
	public static MusicName get(final String aName)
	{
		for (final MusicName music : values())
			if (music.getPath().equals(aName)) return music;
		return null;
	}
	
	public String getPath()
	{
		return mPath;
	}
}
