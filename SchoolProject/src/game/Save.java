package game;

import game.entity.Player;
import java.util.HashMap;
import data.DataManager;

public class Save
{
	private final String										mName;
	
	private int													mLastWorldId, mLastLevelId;
	
	private int													mVolume;
	
	private Player												mPlayer;
	
	private final HashMap<Integer, HashMap<Integer, Integer>>	mScores	= new HashMap<>();
	
	/**
	 * Creates a new save with the given name.
	 * 
	 * @param aName
	 *            The save name.
	 */
	public Save(String aName)
	{
		mName = aName;
		mPlayer = new Player();
		mVolume = 10;
		openWorld(0);
	}
	
	/**
	 * Creates a save out of the given data. Used when loading out of a file.
	 * 
	 * @param aData
	 *            The save data.
	 */
	public Save(String[] aData)
	{
		mName = readData(aData);
		DataManager.setVolume(mVolume / 10f);
	}
	
	/**
	 * Increases the volume.
	 */
	public void volumeUp()
	{
		if (mVolume < 10) mVolume += 1;
		DataManager.setVolume((float) mVolume / 10);
	}
	
	/**
	 * Decreases the volume.
	 */
	public void volumeDown()
	{
		if (mVolume > 0) mVolume -= 1;
		DataManager.setVolume((float) mVolume / 10);
	}
	
	/**
	 * Returns whether the given world is available to play already.
	 * 
	 * @param aWorldId
	 *            the world id.
	 * @return {@code true} if the world is available and {@code false} if not.
	 */
	public boolean isAvailable(int aWorldId)
	{
		return mScores.containsKey(aWorldId);
	}
	
	/**
	 * Returns whether the given level is available to play already.
	 * 
	 * @param aWorldId
	 *            the world id.
	 * @param aLevelId
	 *            The level id.
	 * @return {@code true} if the level is available and {@code false} if not.
	 */
	public boolean isAvailable(int aWorldId, int aLevelId)
	{
		return mScores.containsKey(aWorldId) && mScores.get(aWorldId).containsKey(aLevelId);
	}
	
	/**
	 * Sets the reached score of the given level.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 * @param aScore
	 *            The reached score.
	 */
	public void setScore(int aWorldId, int aLevelId, int aScore)
	{
		if (mScores.get(aWorldId).get(aLevelId) < aScore) mScores.get(aWorldId).put(aLevelId, aScore);
	}
	
	/**
	 * Returns the sum of all scores together.
	 * 
	 * @return the score of all worlds.
	 */
	public int getScore()
	{
		int score = 0;
		for (int worldId : mScores.keySet())
			score += getScore(worldId);
		return score;
	}
	
	/**
	 * Returns the reached score in the given world.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @return the reached score.
	 */
	public int getScore(int aWorldId)
	{
		int score = 0;
		if ( !mScores.containsKey(aWorldId)) return 0;
		for (int levelScore : mScores.get(aWorldId).values())
			if (levelScore != -1) score += levelScore;
		return score;
	}
	
	/**
	 * Returns the reached score in the given level.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 * @return the reached score.
	 */
	public int getScore(int aWorldId, int aLevelId)
	{
		if (mScores.containsKey(aWorldId) && mScores.get(aWorldId).containsKey(aLevelId))
		{
			int score = mScores.get(aWorldId).get(aLevelId);
			if (score != -1) return score;
		}
		return 0;
	}
	
	/**
	 * Returns the name of this save.
	 * 
	 * @return the name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Creates a string representing this save.
	 * 
	 * @return a string to create a save file.
	 */
	public String getSaveData()
	{
		StringBuilder data = new StringBuilder();
		data.append(mName + "\n");
		data.append(mVolume + "\n");
		data.append(mLastWorldId + "\n");
		data.append(mLastLevelId + "\n");
		data.append(mPlayer.getData());
		
		for (int world : mScores.keySet())
			for (int level : mScores.get(world).keySet())
				data.append("\n" + world + ":" + level + "=" + mScores.get(world).get(level));
		return data.toString();
	}
	
	/**
	 * Sets the last played world.
	 * 
	 * @param aLastWorldId
	 *            The world id.
	 */
	public void setLastWorldId(int aLastWorldId)
	{
		mLastWorldId = aLastWorldId;
	}
	
	/**
	 * Sets the last played level.
	 * 
	 * @param aLastLevelId
	 *            The level id.
	 */
	public void setLastLevelId(int aLastLevelId)
	{
		mLastLevelId = aLastLevelId;
	}
	
	/**
	 * Returns the last played world.
	 * 
	 * @return the world id.
	 */
	public int getLastWorldId()
	{
		return mLastWorldId;
	}
	
	/**
	 * Returns the last played level.
	 * 
	 * @return the level id.
	 */
	public int getLastLevelId()
	{
		return mLastLevelId;
	}
	
	/**
	 * Makes the given level available for the player.
	 * 
	 * @param aWorld
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 */
	public void openLevel(int aWorld, int aLevelId)
	{
		if (mScores.containsKey(aWorld) && !mScores.get(aWorld).containsKey(aLevelId)) mScores.get(aWorld).put(aLevelId, -1);
	}
	
	/**
	 * Makes the given world available for the player.
	 * 
	 * @param aWorldId
	 *            The world id.
	 */
	public void openWorld(int aWorldId)
	{
		if ( !mScores.containsKey(aWorldId))
		{
			HashMap<Integer, Integer> levelScore = new HashMap<>();
			levelScore.put(0, -1);
			mScores.put(aWorldId, levelScore);
		}
	}
	
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	private String readData(String[] aData)
	{
		String name = aData[0];
		mVolume = Integer.parseInt(aData[1]);
		mLastWorldId = Integer.parseInt(aData[2]);
		mLastLevelId = Integer.parseInt(aData[3]);
		mPlayer = new Player(aData[4]);
		
		String[] levelAndScore, worldAndLevel;
		int world, level, score;
		for (int i = 5; i < aData.length; i++ )
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
}
