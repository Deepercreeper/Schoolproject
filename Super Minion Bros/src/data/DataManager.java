package data;

import game.Save;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import data.names.ImageName;
import data.names.MusicName;
import data.names.SoundName;
import data.names.Texture;
import data.names.TexturePack;

public class DataManager
{
	private static DataManager		INSTANCE;
	
	private final Cache				mCache;
	
	private final ArrayList<String>	mSaves;
	
	private final String			mVersion;
	
	private Music					mCurrentMusic;
	
	private int						mCurrentTexturepack	= 0;
	
	private float					mVolume				= 1;
	private boolean					mInitiated			= false, mLoading = false, mWasLoading = false;
	
	private DataManager()
	{
		mCache = new Cache();
		mSaves = new ArrayList<>();
		mVersion = loadVersion();
	}
	
	public static DataManager instance()
	{
		if (INSTANCE == null) INSTANCE = new DataManager();
		return INSTANCE;
	}
	
	/**
	 * Plays a sound with the given name. All sounds have to have the type wav and sounds can be played more times simultanely.
	 * 
	 * @param aSound
	 *            The name of the sound to play.
	 */
	public void playSound(final SoundName aSound)
	{
		final Sound sound = mCache.loadSound(aSound);
		if (sound.playing()) sound.stop();
		if (mVolume > 0) sound.play(1, mVolume);
	}
	
	/**
	 * Starts to play a music with the given name. All music titles have to have the type ogg and only one music title can be played at one time.
	 * 
	 * @param aName
	 *            The name of the music title.
	 */
	public void playMusic(final MusicName aName)
	{
		mCurrentMusic = mCache.loadMusic(aName);
		mCurrentMusic.loop();
		mCurrentMusic.setVolume(mVolume);
	}
	
	/**
	 * If any texture pack has been loaded this method returns {@code true}. After invoking this method it returns false again.
	 * 
	 * @return {@code true} the first time invoked after loading a texture pack and {@code false} otherwise.
	 */
	public boolean wasloading()
	{
		if (mWasLoading)
		{
			mWasLoading = false;
			return true;
		}
		return false;
	}
	
	/**
	 * The volume of all music titles and sounds at this time.
	 * 
	 * @return a float out of {@code [0,1]}.
	 */
	public float getVolume()
	{
		return mVolume;
	}
	
	/**
	 * Sets the volume of music titles and sounds.
	 * 
	 * @param aVolume
	 *            The new volume.
	 */
	public void setVolume(final float aVolume)
	{
		mVolume = aVolume;
		mCurrentMusic.setVolume(mVolume);
	}
	
	/**
	 * Returns whether all split images and music titles where loaded already.
	 * 
	 * @return {@code true} if it has finished or {@code false} if not.
	 */
	public boolean isInitiated()
	{
		return mInitiated;
	}
	
	/**
	 * Returns whether the DataManager is loading titles and/or images or not.
	 * 
	 * @return {@code true} if busy and {@code false} if not.
	 */
	public boolean isLoading()
	{
		return mLoading;
	}
	
	public Image getImage(final ImageName aImage)
	{
		return getImage(aImage, "");
	}
	
	public Image getLevelImage(final int aWorldId, final int aLevelId)
	{
		return getImage(ImageName.LEVEL, aWorldId + "-" + aLevelId);
	}
	
	public Image getBackgroundImage(final int aWorldId)
	{
		return getImage(ImageName.BACKGROUND, "" + aWorldId);
	}
	
	private Image getSplitImage(final ImageName aImage, final boolean aTexturePack, final Texture aTexture, final int aIndex)
	{
		return mCache.loadSplitImage(aImage, aTexturePack ? getTexturePack() : null, aTexture, aIndex);
	}
	
	public Image getTexturedSplitImage(final ImageName aImage, final int aIndex)
	{
		return getSplitImage(aImage, true, null, aIndex);
	}
	
	public Image getBlockImage(final Texture aTexture, final int aIndex)
	{
		return getSplitImage(ImageName.BLOCKS, true, aTexture, aIndex);
	}
	
	/**
	 * Loads the given texture into the cache so for example all blocks in one level are loaded.
	 * 
	 * @param aTexturePack
	 *            The image name.
	 * @param aTexture
	 *            The used texture.
	 * @param aIndex
	 *            The block id.
	 */
	public void loadTexture(final Texture aTexture, final int aIndex)
	{
		mLoading = mWasLoading = true;
		mCache.loadSplitImage(ImageName.BLOCKS, getTexturePack(), aTexture, aIndex);
		mLoading = false;
	}
	
	/**
	 * Selects the next texture pack.
	 */
	public void nextTexturePack()
	{
		mCurrentTexturepack = (mCurrentTexturepack + 1) % TexturePack.getTexturePacks().size();
	}
	
	/**
	 * Selects the previous texture pack.
	 */
	public void previousTexturePack()
	{
		mCurrentTexturepack = (mCurrentTexturepack - 1 + TexturePack.getTexturePacks().size()) % TexturePack.getTexturePacks().size();
	}
	
	/**
	 * Returns the current used texture pack name.
	 * 
	 * @return the name of the texture pack.
	 */
	public TexturePack getTexturePack()
	{
		return TexturePack.getTexturePacks().get(mCurrentTexturepack);
	}
	
	/**
	 * Loads all split images and music titles.
	 */
	public void init()
	{
		mLoading = true;
		TexturePack.init();
		loadSaves();
		LevelManager.instance();
		mLoading = false;
		mInitiated = true;
	}
	
	/**
	 * Creates a save out of the save file with name {@code aName}.
	 * 
	 * @param aName
	 *            The name of the save.
	 */
	public void loadSave(final String aName)
	{
		final File save = new File("data/saves/" + aName + ".txt");
		final StringBuilder data = new StringBuilder();
		if (save.exists()) try
		{
			final BufferedReader reader = new BufferedReader(new FileReader(save));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
			Save.loadInstance(data.toString().split("\n"));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a save file out of the current save.
	 */
	public void save()
	{
		final String name = Save.instance().getName();
		File save = new File("data/saves/" + name + ".txt");
		if (save.exists()) save.delete();
		try
		{
			new File(save.getParent()).mkdir();
			save.createNewFile();
			final BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			writer.write(Save.instance().getSaveData());
			writer.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if ( !mSaves.contains(name))
		{
			save = new File("data/saves/#Saves#.txt");
			if (save.exists()) save.delete();
			try
			{
				new File(save.getParent()).mkdir();
				save.createNewFile();
				final BufferedWriter writer = new BufferedWriter(new FileWriter(save));
				for (final String saveName : mSaves)
					writer.write(saveName + "\n");
				writer.write(name);
				mSaves.add(name);
				writer.close();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Deletes the save file with index {@code aIndex}.
	 * 
	 * @param aIndex
	 *            The save index to delete.
	 */
	public void deleteSave(final int aIndex)
	{
		File save = new File("data/saves/" + mSaves.get(aIndex) + ".txt");
		if (save.exists()) save.delete();
		mSaves.remove(aIndex);
		save = new File("data/saves/#Saves#.txt");
		if (save.exists()) save.delete();
		try
		{
			new File(save.getParent()).mkdir();
			save.createNewFile();
			final BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			for (int i = 0; i < mSaves.size(); i++ )
				writer.write(mSaves.get(i) + (i < mSaves.size() - 1 ? "\n" : ""));
			writer.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns all current saved files.
	 * 
	 * @return an array list of all saves.
	 */
	public ArrayList<String> getSaves()
	{
		return mSaves;
	}
	
	public String getVersion()
	{
		return mVersion;
	}
	
	private Image getImage(final ImageName aImage, final String aSuffix)
	{
		return mCache.loadImage(aImage, aSuffix);
	}
	
	private String loadVersion()
	{
		final File version = new File("data/version.txt");
		final StringBuilder data = new StringBuilder();
		if (version.exists()) try
		{
			final BufferedReader reader = new BufferedReader(new FileReader(version));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		final String[] versionData = data.toString().split(": ");
		if (versionData.length == 2) return versionData[1];
		throw new IllegalArgumentException("Not the right version format!");
	}
	
	private void loadSaves()
	{
		final File saves = new File("data/saves/#Saves#.txt");
		final StringBuilder data = new StringBuilder();
		if (saves.exists()) try
		{
			final BufferedReader reader = new BufferedReader(new FileReader(saves));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if ( !data.toString().isEmpty()) for (final String save : data.toString().split("\n"))
			mSaves.add(save);
	}
}
