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
	private static final HashMap<String, Image[]>	SPLITTED_IMAGES		= new HashMap<>();
	private static final HashMap<String, Sound>		SOUNDS				= new HashMap<>();
	private static final HashMap<String, Music>		MUSIC				= new HashMap<>();
	
	private static final String[]					sMusicTitles		= new String[] { "world0" };
	
	private static final String[]					sSplittedImages		= new String[] { "blocks", "player" };
	private static final int[][]					sSplittedImageSizes	= new int[][] { { 16, 16 }, { 14, 30 } };
	
	private static boolean							sLoaded				= false;
	
	public static void playSound(String aName)
	{
		Sound sound = SOUNDS.get(aName);
		if (sound == null) sound = loadSound(aName);
		if (sound.playing()) sound.stop();
		sound.play();
	}
	
	public static void playMusic(String aName)
	{
		MUSIC.get(aName).loop();
	}
	
	public static boolean hasLoaded()
	{
		return sLoaded;
	}
	
	public static Image getImage(String aName)
	{
		Image image = IMAGES.get(aName);
		if (image == null) image = loadImage(aName);
		return image;
	}
	
	public static Image getSplittedImage(String aName, int aTile)
	{
		return SPLITTED_IMAGES.get(aName)[aTile];
	}
	
	private static Image[] loadSplittedImages(int aTile)
	{
		String name = sSplittedImages[aTile];
		Image image = loadImage(name);
		final int imageWidth = sSplittedImageSizes[aTile][0], imageHeight = sSplittedImageSizes[aTile][1], width = image.getWidth() / imageWidth, height = image.getHeight() / imageHeight;
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
	
	public static void init()
	{
		for (int tile = 0; tile < sSplittedImages.length; tile++ )
		{
			Image[] images = loadSplittedImages(tile);
			SPLITTED_IMAGES.put(sSplittedImages[tile], images);
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
