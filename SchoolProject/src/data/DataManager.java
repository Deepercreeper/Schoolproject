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
	
	private static final String[]									sMusicTitles		= new String[] { "world4", "world0", "world3", "world1", "world2", "menu" };
	private static final String[]									sSplitImages		= new String[] { "player", "entity" };
	private static final int[][]									sSplitImageSizes	= new int[][] { { 14, 30 }, { 16, 16 } };
	private static final String[]									sTexturepacks		= new String[] { "Mario", "Minecraft" };
	private static final int[]										sLevelsPerWorld		= new int[] { 3, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	
	private static int												sTexturepack		= 0, sTitle = 0;
	private static float											sVolume				= 1;
	private static boolean											sInitiated			= false, sLoading = false, sWasLoading = false;
	
	/**
	 * Plays a sound with the given name. All sounds have to have the type wav and sounds can be played more times simultanely.
	 * 
	 * @param aName
	 *            The name of the sound to play.
	 */
	public static void playSound(String aName)
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
	public static void playMusic(String aName)
	{
		Music music = MUSIC.get(aName);
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
	public static void setVolume(float aVolume)
	{
		sVolume = aVolume;
		for (Music music : MUSIC.values())
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
	 * Returns of how many levels the worlds do consist.
	 * 
	 * @return an integer array that consists of the number of levels each world.
	 */
	public static int[] getLevelsPerWorld()
	{
		return sLevelsPerWorld;
	}
	
	/**
	 * Loads and caches an image that is not split but a simple png image.
	 * 
	 * @param aName
	 *            the name of the image.
	 * @return an image with name {@code aName} that is laying inside {@code data/images/}.
	 */
	public static Image getImage(String aName)
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
	public static Image getLevelImage(int aWorldId, int aLevelId)
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
	public static Image getBackgroundImage(int aWorldId)
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
	public static Image getSplitImage(String aName, int aIndex)
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
	 * @return the {@code aIndex}s part of {@code aName}.png.
	 */
	public static Image getTextureImage(String aName, Texture aTexture, int aIndex)
	{
		HashMap<Integer, Image> images = SPLIT_IMAGES.get(aName + aTexture.getSuffix());
		Image image = images.get(aIndex);
		return image;
	}
	
	public static void loadTexture(String aName, Texture aTexture, int aIndex)
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
			HashMap<Integer, Image> images = loadSplittedImages(sSplitImages[tile], sSplitImageSizes[tile]);
			SPLIT_IMAGES.put(sSplitImages[tile], images);
		}
		for (String name : sMusicTitles)
			MUSIC.put(name, loadMusic(name));
		loadSaves();
		sLoading = false;
		sInitiated = true;
	}
	
	/**
	 * Creates a save out of the save file with name {@code aName}.
	 * 
	 * @param aName
	 *            The name of the save.
	 * @return a save with name {@code aName}.
	 */
	public static Save loadSave(String aName)
	{
		File save = new File("data/saves/" + aName + ".txt");
		StringBuilder data = new StringBuilder();
		if (save.exists()) try
		{
			BufferedReader reader = new BufferedReader(new FileReader(save));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return new Save(data.toString().split("\n"));
	}
	
	/**
	 * Creates a save file out of the given save.
	 * 
	 * @param aSave
	 *            The save to create inside the saves folder.
	 */
	public static void save(Save aSave)
	{
		File save = new File("data/saves/" + aSave.getName() + ".txt");
		if (save.exists()) save.delete();
		try
		{
			new File(save.getParent()).mkdir();
			save.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			writer.write(aSave.getSaveData());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if ( !mSaves.contains(aSave.getName()))
		{
			save = new File("data/saves/#Saves#.txt");
			if (save.exists()) save.delete();
			try
			{
				new File(save.getParent()).mkdir();
				save.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(save));
				for (String saveName : mSaves)
					writer.write(saveName + "\n");
				writer.write(aSave.getName());
				mSaves.add(aSave.getName());
				writer.close();
			}
			catch (IOException e)
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
	public static void deleteSave(int aIndex)
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
			BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			for (int i = 0; i < mSaves.size(); i++ )
				writer.write(mSaves.get(i) + (i < mSaves.size() - 1 ? "\n" : ""));
			writer.close();
		}
		catch (IOException e)
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
	
	private static Image loadImage(String aName)
	{
		try
		{
			Image image = new Image("data/images/" + aName + ".png");
			return image;
		}
		catch (SlickException e)
		{
			Log.error("Could not read Image " + aName);
		}
		return null;
	}
	
	private static Sound loadSound(String aName)
	{
		try
		{
			Sound sound = new Sound("data/sounds/" + aName + ".wav");
			return sound;
		}
		catch (SlickException e)
		{
			Log.error("Could not read Sound " + aName);
		}
		return null;
	}
	
	private static Music loadMusic(String aName)
	{
		try
		{
			Music music = new Music("data/sounds/" + aName + ".ogg");
			return music;
		}
		catch (SlickException e)
		{
			Log.error("Could not read Music " + aName);
		}
		return null;
	}
	
	private static void loadSaves()
	{
		File saves = new File("data/saves/#Saves#.txt");
		StringBuilder data = new StringBuilder();
		if (saves.exists()) try
		{
			BufferedReader reader = new BufferedReader(new FileReader(saves));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if ( !data.toString().isEmpty()) for (String save : data.toString().split("\n"))
			mSaves.add(save);
	}
	
	private static Image loadSplittedImage(String aName, int aIndex, int[] aSize)
	{
		Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth;
		try
		{
			Image tileImage = new Image(imageWidth, imageHeight);
			tileImage.getGraphics().drawImage(image, -(aIndex % width) * imageWidth, -aIndex / width * imageHeight);
			return tileImage;
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private static HashMap<Integer, Image> loadSplittedImages(String aName, int[] aSize)
	{
		Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
		HashMap<Integer, Image> images = new HashMap<>(width * height);
		try
		{
			for (int tile = 0; tile < width * height; tile++ )
			{
				Image tileImage = new Image(imageWidth, imageHeight);
				tileImage.getGraphics().drawImage(image, -(tile % width) * imageWidth, -tile / width * imageHeight);
				images.put(tile, tileImage);
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return images;
	}
}
