package data;

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
	
	private static final String[]					sMusicTitles		= new String[] { "world0" };
	
	private static final String[]					sSplitImages		= new String[] { "blocks", "player" };
	private static final int[][]					sSplitImageSizes	= new int[][] { { 16, 16 }, { 14, 30 } };
	
	private static boolean							sLoaded				= false;
	
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
		sound.play();
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
		Music music = MUSIC.get(sSplitImages[0]);
		if (music != null) volume = music.getVolume();
		MUSIC.get(aName).loop();
		MUSIC.get(aName).setVolume(volume);
	}
	
	/**
	 * The volume of all music titles at this time.
	 * 
	 * @return A float out of {@code [0,1]}.
	 */
	public static float getVolume()
	{
		return MUSIC.get(sMusicTitles[0]).getVolume();
	}
	
	/**
	 * Increases the volume by {@code 0.1}.
	 */
	public static void volumeUp()
	{
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
		for (Music music : MUSIC.values())
		{
			if (music.getVolume() == 0) return;
			music.setVolume(music.getVolume() - 0.1f);
		}
	}
	
	/**
	 * Returns whether all split images and music titles where loaded already.
	 * 
	 * @return {@code true} if it has finished or {@code false} if not.
	 */
	public static boolean hasLoaded()
	{
		return sLoaded;
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
	 * Returns a part of the split image {@code aName.png} with position {@code aIndex}.
	 * 
	 * @param aName
	 *            The name of the split image.
	 * @param aIndex
	 *            The index of the image part.
	 * @return the {@code aIndex}s part of {@code aName}.png.
	 */
	public static Image getSplittedImage(String aName, int aIndex)
	{
		return SPLIT_IMAGES.get(aName)[aIndex];
	}
	
	private static Image[] loadSplittedImages(int aTile)
	{
		String name = sSplitImages[aTile];
		Image image = loadImage(name);
		final int imageWidth = sSplitImageSizes[aTile][0], imageHeight = sSplitImageSizes[aTile][1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
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
		for (int tile = 0; tile < sSplitImages.length; tile++ )
		{
			Image[] images = loadSplittedImages(tile);
			SPLIT_IMAGES.put(sSplitImages[tile], images);
		}
		for (String name : sMusicTitles)
			MUSIC.put(name, loadMusic(name));
		sLoaded = true;
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
