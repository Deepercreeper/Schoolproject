package data;

import java.util.ArrayList;

public class LevelManager
{
	private static LevelManager				INSTANCE;
	
	private final ArrayList<WorldAndLevel>	mLevels	= new ArrayList<>();
	
	private LevelManager()
	{}
	
	public static void init()
	{}
	
	public static LevelManager instance()
	{
		if (INSTANCE == null) INSTANCE = new LevelManager();
		return INSTANCE;
	}
}
