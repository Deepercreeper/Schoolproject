package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LevelManager
{
	private static LevelManager				INSTANCE;
	
	private final ArrayList<WorldAndLevel>	mLevels	= new ArrayList<>();
	
	private LevelManager()
	{
		final File levels = new File("data/images/worldData/#Levels#.txt");
		final StringBuilder data = new StringBuilder();
		if (levels.exists()) try
		{
			final BufferedReader reader = new BufferedReader(new FileReader(levels));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if ( !data.toString().isEmpty()) for (final String level : data.toString().split("\n"))
		{
			String[] worldAndLevel = level.split("-");
			mLevels.add(new WorldAndLevel(Integer.parseInt(worldAndLevel[0]), Integer.parseInt(worldAndLevel[1])));
		}
		sort();
	}
	
	private void sort()
	{
		Collections.sort(mLevels);
	}
	
	public static LevelManager instance()
	{
		if (INSTANCE == null) INSTANCE = new LevelManager();
		return INSTANCE;
	}
	
	public ArrayList<WorldAndLevel> getLevels()
	{
		return mLevels;
	}
}
