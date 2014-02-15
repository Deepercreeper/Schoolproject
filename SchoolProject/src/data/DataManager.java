package data;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;

public class DataManager
{
	private static final HashMap<String, Image>	IMAGES	= new HashMap<>();
	
	private static final HashMap<String, Sound>	SOUNDS	= new HashMap<>();
	
	private static Music						MUSIC;
	
	public static void playSound(String aName)
	{
		Sound sound = SOUNDS.get(aName);
		if (sound == null) sound = loadSound(aName);
		if (sound.playing()) sound.stop();
		sound.play();
	}
	
	public static void playMusic(String aName)
	{
		new MusicLoader(aName).start();
	}
	
	public static boolean isLoading()
	{
		if (MUSIC != null) return !MUSIC.playing();
		return true;
	}
	
	public static Image getImage(String aName)
	{
		Image image = IMAGES.get(aName);
		if (image == null) image = loadImage(aName);
		return image;
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
	
	private static class MusicLoader extends Thread
	{
		private final String	mName;
		
		public MusicLoader(String aName)
		{
			mName = aName;
		}
		
		@Override
		public void run()
		{
			MUSIC = loadMusic(mName);
			if (MUSIC != null) MUSIC.loop();
		}
	}
}
