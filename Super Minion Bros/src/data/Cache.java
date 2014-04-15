package data;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;
import data.names.ImageName;
import data.names.MusicName;
import data.names.SoundName;
import data.names.Texture;
import data.names.TexturePack;

public class Cache
{
	private final HashMap<String, Image>					mImages;
	private final HashMap<String, HashMap<Integer, Image>>	mSplitImages;
	private final HashMap<String, Sound>					mSounds;
	private final HashMap<String, Music>					mMusic;
	
	/**
	 * Creates a new cache that handles loading and saving images, sounds and music.
	 */
	public Cache()
	{
		mImages = new HashMap<>();
		mSplitImages = new HashMap<>();
		mSounds = new HashMap<>();
		mMusic = new HashMap<>();
	}
	
	/**
	 * Loads the given sound and caches it if not done already.
	 * 
	 * @param aSound
	 *            The sound to load.
	 * @return the requested sound.
	 */
	public Sound loadSound(final SoundName aSound)
	{
		Sound sound = mSounds.get(aSound);
		if (sound == null) sound = loadSoundData(aSound.getPath());
		return sound;
	}
	
	/**
	 * Loads the given music and caches it if not done already.
	 * 
	 * @param aMusic
	 *            The music to load.
	 * @return the requested music.
	 */
	public Music loadMusic(final MusicName aMusic)
	{
		Music music = mMusic.get(aMusic);
		if (music == null) music = loadMusicData(aMusic.getPath());
		return music;
	}
	
	/**
	 * Loads a normal image and caches it if not done already.
	 * 
	 * @param aImage
	 *            The image to load.
	 * @param aSuffix
	 *            A suffix that is concatenated at the end of the images path if needed.
	 * @return the requested image.
	 */
	public Image loadImage(final ImageName aImage, final String aSuffix)
	{
		final String path = aImage.getPath() + aSuffix;
		Image image = mImages.get(path);
		if (image == null)
		{
			image = loadImageData(path);
			mImages.put(path, image);
		}
		return image;
	}
	
	/**
	 * Loads a image and splits it if not done already. The {@code aIndex}s subimage is returned.
	 * 
	 * @param aImage
	 *            The image to load.
	 * @param aTexturePack
	 *            (Optional) The texture pack out of which the image should be loaded.
	 * @param aTexture
	 *            (Optional) The texture of which type the image should be.
	 * @param aIndex
	 *            The sub image index.
	 * @return the requested sub image.
	 */
	public Image loadSplitImage(final ImageName aImage, final TexturePack aTexturePack, final Texture aTexture, final int aIndex)
	{
		String path = aImage.getPath();
		if (aTexturePack != null) path += aTexturePack.getSuffix();
		if (aTexture != null) path += aTexture.getSuffix();
		HashMap<Integer, Image> images = mSplitImages.get(path);
		if (images == null)
		{
			images = loadSplitImageData(path, aImage.getWidth(), aImage.getHeight());
			mSplitImages.put(path, images);
		}
		final Image image = images.get(aIndex);
		if (image == null) throw new IndexOutOfBoundsException("Requested an index outside of the image " + path + "!");
		return image;
	}
	
	private Sound loadSoundData(final String aPath)
	{
		try
		{
			final Sound sound = new Sound("data/sounds/" + aPath + ".wav");
			return sound;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Sound " + aPath);
		}
		return null;
	}
	
	private Music loadMusicData(final String aPath)
	{
		try
		{
			final Music music = new Music("data/sounds/" + aPath + ".ogg");
			return music;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Music " + aPath);
		}
		return null;
	}
	
	private Image loadImageData(final String aPath)
	{
		try
		{
			final Image image = new Image("data/images/" + aPath + ".png");
			return image;
		}
		catch (final SlickException e)
		{
			Log.error("Could not read Image " + aPath);
		}
		return null;
	}
	
	private HashMap<Integer, Image> loadSplitImageData(final String aPath, final int aWidth, final int aHeight)
	{
		final Image image = loadImageData(aPath);
		final int width = image.getWidth() / aWidth, height = image.getHeight() / aHeight;
		final HashMap<Integer, Image> images = new HashMap<>();
		for (int tile = 0; tile < width * height; tile++ )
			images.put(tile, image.getSubImage((tile % width) * aWidth, (tile / width) * aHeight, aWidth, aHeight));
		return images;
	}
}
