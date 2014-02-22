package data;

import game.world.block.Texture;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;

public class DataManager
{
	private static final HashMap<String, Image>		IMAGES				= new HashMap<>();
	private static final HashMap<String, Image[]>	SPLIT_IMAGES		= new HashMap<>();
	private static final HashMap<String, Sound>		SOUNDS				= new HashMap<>();
	private static final HashMap<String, Music>		MUSIC				= new HashMap<>();
	
	private static final String[]					sTitles				= new String[] { "Bossfight", "Overworld", "Mountain", "Desert", "Underground", "Menu" };
	
	private static final String[]					sSplitImages		= new String[] { "player", "entity" };
	private static final int[][]					sSplitImageSizes	= new int[][] { { 14, 30 }, { 16, 16 } };
	private static final String[]					sTexturepacks		= new String[] { "Mario", "Minecraft" };
	private static final int[][]					sLevels				= new int[][] { { 0, 1 }, { 2, 3 } };
	
	private static int								sTexturepack		= 0, sTitle = 0;
	private static float							sVolume				= 1;
	private static boolean							sInitiated			= false, sLoading = false;
	
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
		sound.play(1, sVolume);
	}
	
	/**
	 * Starts to play a music with the given name. All music titles have to have the type ogg and only one music title can be played at one time.
	 * 
	 * @param aName
	 *            The name of the music title.
	 */
	public static void playMusic(String aName)
	{
		float volume = 1;
		Music lastMusic = MUSIC.get(aName);
		if (lastMusic != null) volume = lastMusic.getVolume();
		MUSIC.get(aName).loop();
		for (Music music : MUSIC.values())
			music.setVolume(volume);
	}
	
	/**
	 * Starts to play a music with the current name. All music titles have to have the type ogg and only one music title can be played at one time.
	 */
	public static void playMusic()
	{
		float volume = 1;
		Music lastMusic = MUSIC.get(sTitles[sTitle]);
		if (lastMusic != null) volume = lastMusic.getVolume();
		MUSIC.get(sTitles[sTitle]).loop();
		for (Music music : MUSIC.values())
			music.setVolume(volume);
	}
	
	/**
	 * The volume of all music titles at this time.
	 * 
	 * @return A float out of {@code [0,1]}.
	 */
	public static float getVolume()
	{
		return MUSIC.get(sTitles[0]).getVolume();
	}
	
	/**
	 * Increases the volume by {@code 0.1}.
	 */
	public static void volumeUp()
	{
		if (sVolume < 1) sVolume += 0.1f;
		for (Music music : MUSIC.values())
		{
			if (music.getVolume() == 1) return;
			music.setVolume(music.getVolume() + 0.1f);
		}
	}
	
	/**
	 * Decreases the volume by {@code 0.1}.
	 */
	public static void volumeDown()
	{
		if (sVolume > 0) sVolume -= 0.1f;
		for (Music music : MUSIC.values())
		{
			if (music.getVolume() == 0) return;
			music.setVolume(music.getVolume() - 0.1f);
		}
	}
	
	public static void nextTitle()
	{
		sTitle = (sTitle + 1) % sTitles.length;
		playMusic();
	}
	
	public static void previousTitle()
	{
		sTitle = (sTitle - 1 + sTitles.length) % sTitles.length;
		playMusic();
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
	
	public static boolean isLoading()
	{
		return sLoading;
	}
	
	public static int[][] getWorlds()
	{
		return sLevels;
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
	 * Loads an world data image.
	 * 
	 * @param aId
	 *            The world id.
	 * @return an image that contains world data.
	 */
	public static Image getLevelImage(int aId)
	{
		return getImage("worldData/worldData" + aId);
	}
	
	/**
	 * Loads an background image.
	 * 
	 * @param aId
	 *            The background id.
	 * @return an image that contains world data.
	 */
	public static Image getBackgroundImage(int aId)
	{
		return getImage("backgrounds/background" + aId);
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
		return SPLIT_IMAGES.get(aName)[aIndex];
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
		checkTexturePack(aName);
		return SPLIT_IMAGES.get(aName + aTexture.getSuffix())[aIndex];
	}
	
	/**
	 * Sets the next texture pack.
	 */
	public static void nextTexturePack()
	{
		sTexturepack = (sTexturepack + 1) % sTexturepacks.length;
		checkTexturePack(sTexturepacks[sTexturepack]);
	}
	
	/**
	 * Sets the previous texture pack.
	 */
	public static void previousTexturePack()
	{
		sTexturepack = (sTexturepack - 1 + sTexturepacks.length) % sTexturepacks.length;
		checkTexturePack(sTexturepacks[sTexturepack]);
	}
	
	private static void checkTexturePack(String aName)
	{
		if ( !SPLIT_IMAGES.containsKey(aName + Texture.NORMAL.getSuffix()))
		{
			sLoading = true;
			for (Texture texture : Texture.values())
			{
				Image[] images = loadSplittedImages("texturepacks/blocks" + aName + texture.getSuffix(), new int[] { 16, 16 });
				SPLIT_IMAGES.put(aName + texture.getSuffix(), images);
			}
			sLoading = false;
		}
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
		return sTitles[sTitle];
	}
	
	private static Image[] loadSplittedImages(String aName, int[] aSize)
	{
		Image image = loadImage(aName);
		final int imageWidth = aSize[0], imageHeight = aSize[1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
		Image[] images = new Image[width * height];
		try
		{
			for (int tile = 0; tile < width * height; tile++ )
			{
				Image tileImage = new Image(imageWidth, imageHeight);
				tileImage.getGraphics().drawImage(image, -(tile % width) * imageWidth, -tile / width * imageHeight);
				images[tile] = tileImage;
			}
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
		return images;
	}
	
	/**
	 * Loads all split images and music titles.
	 */
	public static void init()
	{
		sLoading = true;
		for (int tile = 0; tile < sSplitImages.length; tile++ )
		{
			Image[] images = loadSplittedImages(sSplitImages[tile], sSplitImageSizes[tile]);
			SPLIT_IMAGES.put(sSplitImages[tile], images);
		}
		for (String name : sTitles)
			MUSIC.put(name, loadMusic(name));
		sLoading = false;
		sInitiated = true;
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
	
}
