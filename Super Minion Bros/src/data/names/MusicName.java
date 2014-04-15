package data.names;

import java.util.HashMap;

public enum MusicName
{
	
	MENU("menu"), WORLD_0("world0", 0), WORLD_1("world1", 1), WORLD_2("world2", 2), WORLD_3("world3", 3), WORLD_4("world4", 4), WORLD_5("world5", 5);
	
	private static final HashMap<Integer, MusicName>	WORLD_TITLES	= new HashMap<>();
	
	private MusicName(final String aPath)
	{
		mPath = aPath;
		mWorldId = -1;
	}
	
	private MusicName(final String aPath, final int aWorldId)
	{
		mPath = aPath;
		mWorldId = aWorldId;
	}
	
	private final int		mWorldId;
	
	private final String	mPath;
	
	/**
	 * Returns the path of this music title.
	 * 
	 * @return the path.
	 */
	public String getPath()
	{
		return mPath;
	}
	
	/**
	 * Returns the corresponding word id if set.
	 * 
	 * @return the world id.
	 */
	public int getWorldId()
	{
		return mWorldId;
	}
	
	/**
	 * initiates all music titles.
	 */
	public static void init()
	{
		for (final MusicName music : values())
			if (music.mWorldId != -1) WORLD_TITLES.put(music.mWorldId, music);
	}
	
	/**
	 * Returns the music title for the given world.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @return the music title.
	 */
	public static MusicName get(final int aWorldId)
	{
		return WORLD_TITLES.get(aWorldId);
	}
}
