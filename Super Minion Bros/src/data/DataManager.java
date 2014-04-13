package data;

import game.Save;
import game.level.block.Texture;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;

public class DataManager
{
	private static DataManager								INSTANCE;
	
	private final HashMap<String, Image>					mImages;
	private final HashMap<String, HashMap<Integer, Image>>	mSplitImages;
	private final HashMap<String, Sound>					mSounds;
	private final HashMap<String, Music>					mMusic;
	private final ArrayList<String>							mSaves;
	
	private final String									mVersion;
	
	private final String[]									mMusicTitles;
	private final String[]									mSplitImageNames;
	private final int[][]									mSplitImageSizes;
	private final String[]									mTexturepacks;
	
	private int												mCurrentTexturepack	= 0;
	
	private final int										mCurrentTitle			= 0;
	private float											mVolume			= 1;
	private boolean											mInitiated		= false, mLoading = false, mWasLoading = false;
	
	private DataManager()
	{
		mImages = new HashMap<>();
		mSplitImages = new HashMap<>();
		mSounds = new HashMap<>();
		mMusic = new HashMap<>();
		mSaves = new ArrayList<>();
		
		mMusicTitles = new String[] { "world4", "world0", "world3", "world1", "world2", "world5", "menu" };
		mSplitImageNames = new String[] { "player", "entity", "enemy", "weapon" };
		mSplitImageSizes = new int[][] { { 14, 30 }, { 16, 16 }, { 16, 16 }, { 20, 20 } };
		mTexturepacks = new String[] { "Mario", "Minecraft" };
		
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
	 * @param aName
	 *            The name of the sound to play.
	 */
	public void playSound(final String aName)
	{
		Sound sound = mSounds.get(aName);
		if (sound == null) sound = loadSound(aName);
		if (sound.playing()) sound.stop();
		if (mVolume > 0) sound.play(1, mVolume);
	}
	
	/**
	 * Starts to play a music with the given name. All music titles have to have the type ogg and only one music title can be played at one time.
	 * 
	 * @param aName
	 *            The name of the music title.
	 */
	public void playMusic(final String aName)
	{
		final Music music = mMusic.get(aName);
		music.loop();
		music.setVolume(mVolume);
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
		return mMusic.get(mMusicTitles[0]).getVolume();
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
		for (final Music music : mMusic.values())
			music.setVolume(mVolume);
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
	
	/**
	 * Loads and caches an image that is not split but a simple png image.
	 * 
	 * @param aName
	 *            the name of the image.
	 * @return an image with name {@code aName} that is laying inside {@code data/images/}.
	 */
	public Image getImage(final String aName)
	{
		Image image = mImages.get(aName);
		if (image == null) image = loadImage(aName);
		return image;
	}
	
	/**
	 * Loads an level data image.
	 * 
	 * @param aId
	 *            The world id.
	 * @param aLevelId
	 *            The level id.
	 * @return an image that contains level data.
	 */
	public Image getLevelImage(final int aWorldId, final int aLevelId)
	{
		return getImage("worldData/level" + aWorldId + "-" + aLevelId);
	}
	
	/**
	 * Loads an background image belonging to the given world id.
	 * 
	 * @param aWorldId
	 *            The world id.
	 * @return an image that contains a background.
	 */
	public Image getBackgroundImage(final int aWorldId)
	{
		return getImage("backgrounds/background" + aWorldId);
	}
	
	/**
	 * Returns a part of the split image {@code aName.png} with position {@code aIndex}.
	 * 
	 * @param aName
	 *            The name of the split image.
	 * @param aIndex
	 *            The index of the image part.
	 * @return the {@code aIndex}s part of {@code aName}.png.
	 */
	public Image getSplitImage(final String aName, final int aIndex)
	{
		return mSplitImages.get(aName).get(aIndex);
	}
	
	/**
	 * Returns a part of the split image {@code aName.png} with position {@code aIndex}.
	 * 
	 * @param aName
	 *            The name of the split image.
	 * @param aIndex
	 *            The index of the image part.
	 * @return the {@code aIndex}s part of {@code <aName><aTexture.getSuffix()>}.png.
	 */
	public Image getTextureImage(final String aName, final Texture aTexture, final int aIndex)
	{
		final HashMap<Integer, Image> images = mSplitImages.get(aName + aTexture.getSuffix());
		final Image image = images.get(aIndex);
		return image;
	}
	
	/**
	 * Loads the given texture into the cache so for example all blocks in one level are loaded.
	 * 
	 * @param aName
	 *            The image name.
	 * @param aTexture
	 *            The used texture.
	 * @param aIndex
	 *            The block id.
	 */
	public void loadTexture(final String aName, final Texture aTexture, final int aIndex)
	{
		HashMap<Integer, Image> images = mSplitImages.get(aName + aTexture.getSuffix());
		if (images == null)
		{
			images = new HashMap<>();
			mSplitImages.put(aName + aTexture.getSuffix(), images);
		}
		Image image = images.get(aIndex);
		if (image == null)
		{
			mLoading = mWasLoading = true;
			image = loadSplittedImage("texturepacks/blocks" + aName + aTexture.getSuffix(), aIndex, new int[] { 16, 16 });
			images.put(aIndex, image);
			mLoading = false;
		}
	}
	
	/**
	 * Selects the next texture pack.
	 */
	public void nextTexturePack()
	{
		mCurrentTexturepack = (mCurrentTexturepack + 1) % mTexturepacks.length;
	}
	
	/**
	 * Selects the previous texture pack.
	 */
	public void previousTexturePack()
	{
		mCurrentTexturepack = (mCurrentTexturepack - 1 + mTexturepacks.length) % mTexturepacks.length;
	}
	
	/**
	 * Returns the current used texture pack name.
	 * 
	 * @return the name of the texture pack.
	 */
	public String getTexturePack()
	{
		return mTexturepacks[mCurrentTexturepack];
	}
	
	/**
	 * Returns the name of the current music title.
	 * 
	 * @return the music title.
	 */
	public String getTitle()
	{
		return mMusicTitles[mCurrentTitle];
	}
	
	/**
	 * Loads all split images and music titles.
	 */
	public void init()
	{
		mLoading = true;
		for (int tile = 0; tile < mSplitImageNames.length; tile++ )
		{
			for (final String texture : mTexturepacks)
			{
				final HashMap<Integer, Image> images = loadSplittedImages(mSplitImageNames[tile] + texture, mSplitImageSizes[tile]);
				mSplitImages.put(mSplitImageNames[tile] + texture, images);
			}
		}
		for (final String name : mMusicTitles)
			mMusic.put(name, loadMusic(name));
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
	
	private Image loadImage(final String aName)
	{
		try
		{
			final Image image = new Image("data/images/" + aName + ".png");
			return image;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Image " + aName);
		}
		return null;
	}
	
	private Sound loadSound(final String aName)
	{
		try
		{
			final Sound sound = new Sound("data/sounds/" + aName + ".wav");
			return sound;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Sound " + aName);
		}
		return null;
	}
	
	private Music loadMusic(final String aName)
	{
		try
		{
			final Music music = new Music("data/sounds/" + aName + ".ogg");
			return music;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Music " + aName);
		}
		return null;
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
	
	private Image loadSplittedImage(final String aName, final int aIndex, final int[] aSize)
	{
		final Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth;
		return image.getSubImage((aIndex % width) * imageWidth, (aIndex / width) * imageHeight, imageWidth, imageHeight);
	}
	
	private HashMap<Integer, Image> loadSplittedImages(final String aName, final int[] aSize)
	{
		final Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
		final HashMap<Integer, Image> images = new HashMap<>();
		for (int tile = 0; tile < width * height; tile++ )
			images.put(tile, image.getSubImage((tile % width) * imageWidth, (tile / width) * imageHeight, imageWidth, imageHeight));
		return images;
	}
}
