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
	private static final HashMap<String, Image>						IMAGES				= new HashMap<>();
	private static final HashMap<String, HashMap<Integer, Image>>	SPLIT_IMAGES		= new HashMap<>();
	private static final HashMap<String, Sound>						SOUNDS				= new HashMap<>();
	private static final HashMap<String, Music>						MUSIC				= new HashMap<>();
	private static final ArrayList<String>							mSaves				= new ArrayList<>();
	
	private static final String[]									sMusicTitles		= new String[] { "world4", "world0", "world3", "world1", "world2", "world5", "menu" };
	private static final String[]									sSplitImages		= new String[] { "player", "entity", "enemy" };
	private static final int[][]									sSplitImageSizes	= new int[][] { { 14, 30 }, { 16, 16 }, { 16, 16 } };
	private static final String[]									sTexturepacks		= new String[] { "Mario", "Minecraft" };
	
	private static int												sTexturepack		= 0, sTitle = 0;
	private static float											sVolume				= 1;
	private static boolean											sInitiated			= false, sLoading = false, sWasLoading = false;
	
	/**
	 * Plays a sound with the given name. All sounds have to have the type wav and sounds can be played more times simultanely.
	 * 
	 * @param aName
	 *            The name of the sound to play.
	 */
	public static void playSound(final String aName)
	{
		Sound sound = SOUNDS.get(aName);
		if (sound == null) sound = loadSound(aName);
		if (sound.playing()) sound.stop();
		if (sVolume > 0) sound.play(1, sVolume);
	}
	
	/**
	 * Starts to play a music with the given name. All music titles have to have the type ogg and only one music title can be played at one time.
	 * 
	 * @param aName
	 *            The name of the music title.
	 */
	public static void playMusic(final String aName)
	{
		final Music music = MUSIC.get(aName);
		music.loop();
		music.setVolume(sVolume);
	}
	
	/**
	 * If any texture pack has been loaded this method returns {@code true}. After invoking this method it returns false again.
	 * 
	 * @return {@code true} the first time invoked after loading a texture pack and {@code false} otherwise.
	 */
	public static boolean wasloading()
	{
		if (sWasLoading)
		{
			sWasLoading = false;
			return true;
		}
		return false;
	}
	
	/**
	 * The volume of all music titles and sounds at this time.
	 * 
	 * @return a float out of {@code [0,1]}.
	 */
	public static float getVolume()
	{
		return MUSIC.get(sMusicTitles[0]).getVolume();
	}
	
	/**
	 * Sets the volume of music titles and sounds.
	 * 
	 * @param aVolume
	 *            The new volume.
	 */
	public static void setVolume(final float aVolume)
	{
		sVolume = aVolume;
		for (final Music music : MUSIC.values())
			music.setVolume(sVolume);
	}
	
	/**
	 * Returns whether all split images and music titles where loaded already.
	 * 
	 * @return {@code true} if it has finished or {@code false} if not.
	 */
	public static boolean isInitiated()
	{
		return sInitiated;
	}
	
	/**
	 * Returns whether the DataManager is loading titles and/or images or not.
	 * 
	 * @return {@code true} if busy and {@code false} if not.
	 */
	public static boolean isLoading()
	{
		return sLoading;
	}
	
	/**
	 * Loads and caches an image that is not split but a simple png image.
	 * 
	 * @param aName
	 *            the name of the image.
	 * @return an image with name {@code aName} that is laying inside {@code data/images/}.
	 */
	public static Image getImage(final String aName)
	{
		Image image = IMAGES.get(aName);
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
	public static Image getLevelImage(final int aWorldId, final int aLevelId)
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
	public static Image getBackgroundImage(final int aWorldId)
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
	public static Image getSplitImage(final String aName, final int aIndex)
	{
		return SPLIT_IMAGES.get(aName).get(aIndex);
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
	public static Image getTextureImage(final String aName, final Texture aTexture, final int aIndex)
	{
		final HashMap<Integer, Image> images = SPLIT_IMAGES.get(aName + aTexture.getSuffix());
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
	public static void loadTexture(final String aName, final Texture aTexture, final int aIndex)
	{
		HashMap<Integer, Image> images = SPLIT_IMAGES.get(aName + aTexture.getSuffix());
		if (images == null)
		{
			images = new HashMap<>();
			SPLIT_IMAGES.put(aName + aTexture.getSuffix(), images);
		}
		Image image = images.get(aIndex);
		if (image == null)
		{
			sLoading = sWasLoading = true;
			image = loadSplittedImage("texturepacks/blocks" + aName + aTexture.getSuffix(), aIndex, new int[] { 16, 16 });
			images.put(aIndex, image);
			sLoading = false;
		}
	}
	
	/**
	 * Selects the next texture pack.
	 */
	public static void nextTexturePack()
	{
		sTexturepack = (sTexturepack + 1) % sTexturepacks.length;
	}
	
	/**
	 * Selects the previous texture pack.
	 */
	public static void previousTexturePack()
	{
		sTexturepack = (sTexturepack - 1 + sTexturepacks.length) % sTexturepacks.length;
	}
	
	/**
	 * Returns the current used texture pack name.
	 * 
	 * @return the name of the texture pack.
	 */
	public static String getTexturePack()
	{
		return sTexturepacks[sTexturepack];
	}
	
	/**
	 * Returns the name of the current music title.
	 * 
	 * @return the music title.
	 */
	public static String getTitle()
	{
		return sMusicTitles[sTitle];
	}
	
	/**
	 * Loads all split images and music titles.
	 */
	public static void init()
	{
		sLoading = true;
		for (int tile = 0; tile < sSplitImages.length; tile++ )
		{
			for (final String texture : sTexturepacks)
			{
				final HashMap<Integer, Image> images = loadSplittedImages(sSplitImages[tile] + texture, sSplitImageSizes[tile]);
				SPLIT_IMAGES.put(sSplitImages[tile] + texture, images);
			}
		}
		for (final String name : sMusicTitles)
			MUSIC.put(name, loadMusic(name));
		loadSaves();
		LevelManager.instance();
		sLoading = false;
		sInitiated = true;
	}
	
	/**
	 * Creates a save out of the save file with name {@code aName}.
	 * 
	 * @param aName
	 *            The name of the save.
	 */
	public static void loadSave(final String aName)
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
	public static void save()
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
	public static void deleteSave(final int aIndex)
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
	public static ArrayList<String> getSaves()
	{
		return mSaves;
	}
	
	private static Image loadImage(final String aName)
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
	
	// private static void loadLevels()
	// {
	// final File levels = new File("data/images/worldData/#Levels#.txt");
	// final StringBuilder data = new StringBuilder();
	// if (levels.exists()) try
	// {
	// final BufferedReader reader = new BufferedReader(new FileReader(levels));
	// int c;
	// while ((c = reader.read()) != -1)
	// data.append((char) c);
	// reader.close();
	// }
	// catch (final IOException e)
	// {
	// e.printStackTrace();
	// }
	// final ArrayList<String> levelNames = new ArrayList<>();
	// if ( !data.toString().isEmpty()) for (final String level : data.toString().split("\n"))
	// levelNames.add(level.replace("\r", "").replace("\n", ""));
	// final String[] levelNamesArray = levelNames.toArray(new String[levelNames.size()]);
	// Arrays.sort(levelNamesArray);
	// final int lastLevel = Integer.parseInt(levelNamesArray[levelNamesArray.length - 1].split("-")[0]);
	// sLevelsPerWorld = new int[lastLevel + 1];
	// for (final String level : levelNamesArray)
	// sLevelsPerWorld[Integer.parseInt(level.split("-")[0])]++ ;
	// // TODO more beautiful...
	// // for (final int levelNumber : sLevelsPerWorld)
	// // System.out.println(levelNumber);
	// }
	
	private static Sound loadSound(final String aName)
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
	
	private static Music loadMusic(final String aName)
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
	
	private static void loadSaves()
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
	
	private static Image loadSplittedImage(final String aName, final int aIndex, final int[] aSize)
	{
		final Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth;
		return image.getSubImage((aIndex % width) * imageWidth, (aIndex / width) * imageHeight, imageWidth, imageHeight);
	}
	
	private static HashMap<Integer, Image> loadSplittedImages(final String aName, final int[] aSize)
	{
		final Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
		final HashMap<Integer, Image> images = new HashMap<>();
		for (int tile = 0; tile < width * height; tile++ )
			images.put(tile, image.getSubImage((tile % width) * imageWidth, (tile / width) * imageHeight, imageWidth, imageHeight));
		return images;
	}
}
