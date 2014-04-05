package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class LevelManager
{
	private static LevelManager				INSTANCE;
	
	private final HashMap<Integer, World>	mWorlds	= new HashMap<>();
	
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
		if ( !data.toString().isEmpty())
		{
			final String[] dataLines = data.toString().split("\n");
			Arrays.sort(dataLines);
			for (final String level : dataLines)
			{
				final String[] worldAndLevel = level.split("-");
				final int worldId = Integer.parseInt(worldAndLevel[0]), levelId = Byte.parseByte(worldAndLevel[1]);
				World world = mWorlds.get(worldId);
				if (world == null) world = new World(worldId);
				world.addLevel(levelId);
				mWorlds.put(worldId, world);
			}
		}
	}
	
	public static LevelManager instance()
	{
		if (INSTANCE == null) INSTANCE = new LevelManager();
		return INSTANCE;
	}
	
	public int getWorldsCount()
	{
		return mWorlds.size();
	}
	
	public int getLevelsCount(final int aWorld)
	{
		return mWorlds.get(aWorld).getLevelsCount();
	}
	
	public HashMap<Integer, World> getWorlds()
	{
		return mWorlds;
	}
}
