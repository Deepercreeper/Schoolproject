package game;

import java.util.HashMap;
import data.DataManager;

public class Save
{
	private final String										mName;
	
	private int													mLastWorld, mLastLevel;
	
	private final HashMap<Integer, HashMap<Integer, Integer>>	mScores	= new HashMap<>();
	
	public Save(String aName)
	{
		mName = aName;
		openWorld(0);
	}
	
	public boolean isAvailable(int aWorld)
	{
		return mScores.containsKey(aWorld);
	}
	
	public boolean isAvailable(int aWorld, int aLevel)
	{
		return mScores.containsKey(aWorld) && mScores.get(aWorld).get(aLevel) != null;
	}
	
	public int getScore()
	{
		int score = 0;
		for (HashMap<Integer, Integer> levelScores : mScores.values())
			for (int levelScore : levelScores.values())
				if (levelScore != -1) score += levelScore;
		return score;
	}
	
	public int getScore(int aWorld)
	{
		int score = 0;
		if ( !mScores.containsKey(aWorld)) return 0;
		for (int levelScore : mScores.get(aWorld).values())
			if (levelScore != -1) score += levelScore;
		return score;
	}
	
	public int getScore(int aWorld, int aLevel)
	{
		if (mScores.containsKey(aWorld) && mScores.get(aWorld).containsKey(aLevel))
		{
			int score = mScores.get(aWorld).get(aLevel);
			if (score != -1) return score;
		}
		return 0;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getLastWorld()
	{
		return mLastWorld;
	}
	
	public int getLastLevel()
	{
		return mLastLevel;
	}
	
	public void openLevel(int aWorld, int aLevel)
	{
		if (mScores.containsKey(aWorld) && !mScores.get(aWorld).containsKey(aLevel)) mScores.get(aWorld).put(aLevel, -1);
	}
	
	public void openWorld(int aWorld)
	{
		if ( !mScores.containsKey(aWorld))
		{
			HashMap<Integer, Integer> levelScore = new HashMap<>();
			levelScore.put(DataManager.getWorlds()[aWorld][0], -1);
			mScores.put(aWorld, levelScore);
		}
	}
}
