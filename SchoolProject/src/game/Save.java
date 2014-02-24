package game;

import java.util.HashMap;
import data.DataManager;

public class Save
{
	private final String										mName;
	
	private int													mLastWorld, mLastLevelIndex;
	
	private final HashMap<Integer, HashMap<Integer, Integer>>	mScores	= new HashMap<>();
	
	public Save(String aName)
	{
		mName = aName;
		openWorld(0);
	}
	
	public Save(String[] aData)
	{
		mName = readData(aData);
	}
	
	public boolean isAvailable(int aWorld)
	{
		return mScores.containsKey(aWorld);
	}
	
	public boolean isAvailable(int aWorld, int aLevelIndex)
	{
		return mScores.containsKey(aWorld) && mScores.get(aWorld).containsKey(aLevelIndex);
	}
	
	public void setScore(int aWorld, int aLevelIndex, int aScore)
	{
		if (mScores.get(aWorld).get(aLevelIndex) < aScore) mScores.get(aWorld).put(aLevelIndex, aScore);
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
	
	public int getScore(int aWorld, int aLevelIndex)
	{
		if (mScores.containsKey(aWorld) && mScores.get(aWorld).containsKey(aLevelIndex))
		{
			int score = mScores.get(aWorld).get(aLevelIndex);
			if (score != -1) return score;
		}
		return 0;
	}
	
	public String getName()
	{
		return mName;
	}
	
	private String readData(String[] aData)
	{
		String name = aData[0];
		mLastWorld = Integer.parseInt(aData[1]);
		mLastLevelIndex = Integer.parseInt(aData[2]);
		
		String[] levelAndScore, worldAndLevel;
		int world, level, score;
		for (int i = 3; i < aData.length; i++ )
		{
			levelAndScore = aData[i].split("=");
			worldAndLevel = levelAndScore[0].split(":");
			world = Integer.parseInt(worldAndLevel[0]);
			level = Integer.parseInt(worldAndLevel[1]);
			score = Integer.parseInt(levelAndScore[1]);
			HashMap<Integer, Integer> worldScore = mScores.get(world);
			if (worldScore == null)
			{
				worldScore = new HashMap<>();
				mScores.put(world, worldScore);
			}
			worldScore.put(level, score);
		}
		return name;
	}
	
	public String getSaveData()
	{
		StringBuilder data = new StringBuilder();
		data.append(mName + "\n");
		data.append(mLastWorld + "\n");
		data.append(mLastLevelIndex);
		for (int world : mScores.keySet())
			for (int level : mScores.get(world).keySet())
				data.append("\n" + world + ":" + level + "=" + mScores.get(world).get(level));
		return data.toString();
	}
	
	public void setLastWorld(int aLastWorld)
	{
		mLastWorld = aLastWorld;
	}
	
	public void setLastLevelIndex(int aLastLevelIndex)
	{
		mLastLevelIndex = aLastLevelIndex;
	}
	
	public int getLastWorld()
	{
		return mLastWorld;
	}
	
	public int getLastLevelIndex()
	{
		return mLastLevelIndex;
	}
	
	public void openLevel(int aWorld, int aLevelIndex)
	{
		if (mScores.containsKey(aWorld) && !mScores.get(aWorld).containsKey(aLevelIndex)) mScores.get(aWorld).put(aLevelIndex, -1);
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
