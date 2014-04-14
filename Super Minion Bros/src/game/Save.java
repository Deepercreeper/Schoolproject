package game;

import game.entity.Player;
import java.util.HashMap;
import util.InputKeys;
import data.DataManager;
import data.names.TexturePack;

public class Save
{
	private static Save											INSTANCE;
	
	private final String										mName;
	
	private int													mLastWorldId, mLastLevelId, mVolume;
	
	private TexturePack											mTexturePack;
	
	private Player												mPlayer;
	
	private final HashMap<Integer, HashMap<Integer, Integer>>	mScores	= new HashMap<>();
	
	/**
	 * Returns the current save.
	 * 
	 * @return the last loaded or created save.
	 */
	public static Save instance()
	{
		return INSTANCE;
	}
	
	/**
	 * Creates a new save with the given name.
	 * 
	 * @param aName
	 *            The name of the new save.
	 */
	public static void newInstance(final String aName)
	{
		INSTANCE = new Save(aName);
	}
	
	/**
	 * Loads a save out of the given data.
	 * 
	 * @param aData
	 *            The save data.
	 */
	public static void loadInstance(final String[] aData)
	{
		INSTANCE = new Save(aData);
	}
	
	private Save(final String aName)
	{
		mName = aName;
		mPlayer = new Player();
		mVolume = 10;
		mTexturePack = DataManager.instance().getTexturePack();
		openWorld(0);
	}
	
	private Save(final String[] aData)
	{
		mName = readData(aData);
		while ( !mTexturePack.equals(DataManager.instance().getTexturePack()))
			DataManager.instance().nextTexturePack();
		DataManager.instance().setVolume(mVolume / 10f);
	}
	
	/**
	 * Selects the next texture pack.
	 */
	public void nextTexturePack()
	{
		DataManager.instance().nextTexturePack();
		mTexturePack = DataManager.instance().getTexturePack();
	}
	
	/**
	 * Selects the previous texture pack.
	 */
	public void previousTexturePack()
	{
		DataManager.instance().previousTexturePack();
		mTexturePack = DataManager.instance().getTexturePack();
	}
	
	/**
	 * Increases the volume.
	 */
	public void volumeUp()
	{
		if (mVolume < 10) mVolume += 1;
		DataManager.instance().setVolume((float) mVolume / 10);
	}
	
	/**
	 * Decreases the volume.
	 */
	public void volumeDown()
	{
		if (mVolume > 0) mVolume -= 1;
		DataManager.instance().setVolume((float) mVolume / 10);
	}
	
	/**
	 * Returns whether the given world is available to play already.
	 * 
	 * @param aWorldId
	 *            the world id.
	 * @return {@code true} if the world is available and {@code false} if not.
	 */
	public boolean isAvailable(final int aWorldId)
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
	public boolean isAvailable(final int aWorldId, final int aLevelId)
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
	public void setScore(final int aWorldId, final int aLevelId, final int aScore)
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
		for (final int worldId : mScores.keySet())
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
	public int getScore(final int aWorldId)
	{
		int score = 0;
		if ( !mScores.containsKey(aWorldId)) return 0;
		for (final int levelScore : mScores.get(aWorldId).values())
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
	public int getScore(final int aWorldId, final int aLevelId)
	{
		if (mScores.containsKey(aWorldId) && mScores.get(aWorldId).containsKey(aLevelId))
		{
			final int score = mScores.get(aWorldId).get(aLevelId);
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
		final StringBuilder data = new StringBuilder();
		data.append(mName + "\n");
		data.append(mVolume + "\n");
		data.append(mLastWorldId + "\n");
		data.append(mLastLevelId + "\n");
		data.append(mPlayer.getData() + "\n");
		data.append(InputKeys.instance().getData() + "\n");
		data.append(mTexturePack);
		
		for (final int world : mScores.keySet())
			for (final int level : mScores.get(world).keySet())
				data.append("\n" + world + ":" + level + "=" + mScores.get(world).get(level));
		return data.toString();
	}
	
	/**
	 * Sets the last played world.
	 * 
	 * @param aLastWorldId
	 *            The world id.
	 */
	public void setLastWorldId(final int aLastWorldId)
	{
		mLastWorldId = aLastWorldId;
	}
	
	/**
	 * Sets the last played level.
	 * 
	 * @param aLastLevelId
	 *            The level id.
	 */
	public void setLastLevelId(final int aLastLevelId)
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
	public void openLevel(final int aWorld, final int aLevelId)
	{
		if (mScores.containsKey(aWorld) && !mScores.get(aWorld).containsKey(aLevelId)) mScores.get(aWorld).put(aLevelId, -1);
	}
	
	/**
	 * Makes the given world available for the player.
	 * 
	 * @param aWorldId
	 *            The world id.
	 */
	public void openWorld(final int aWorldId)
	{
		if ( !mScores.containsKey(aWorldId))
		{
			final HashMap<Integer, Integer> levelScore = new HashMap<>();
			levelScore.put(0, -1);
			mScores.put(aWorldId, levelScore);
		}
	}
	
	/**
	 * Returns the loaded or created player.
	 * 
	 * @return the player.
	 */
	public Player getPlayer()
	{
		return mPlayer;
	}
	
	private String readData(final String[] aData)
	{
		final String name = aData[0];
		mVolume = Integer.parseInt(aData[1]);
		mLastWorldId = Integer.parseInt(aData[2]);
		mLastLevelId = Integer.parseInt(aData[3]);
		mPlayer = new Player(aData[4], aData[5]);
		InputKeys.loadInstance(aData[6]);
		mTexturePack = TexturePack.valueOf(aData[7]);
		
		String[] levelAndScore, worldAndLevel;
		int world, level, score;
		for (int i = 8; i < aData.length; i++ )
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
