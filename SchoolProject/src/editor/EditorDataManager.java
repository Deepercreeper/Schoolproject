package editor;

import game.level.block.Block;
import game.level.block.Texture;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.newdawn.slick.util.Log;

public class EditorDataManager
{
	private static final HashMap<String, HashMap<Texture, BufferedImage[]>>	SPLIT_IMAGES	= new HashMap<>();
	private static final HashMap<String, BufferedImage>						IMAGES			= new HashMap<>();
	
	private static final String[]											TEXTUREPACKS	= new String[] { "Mario", "Minecraft" };
	
	private static ArrayList<String>										mLevels			= new ArrayList<>();
	private static String													sTexturePack	= TEXTUREPACKS[0];
	
	private EditorDataManager()
	{}
	
	public static void init()
	{
		try
		{
			for (final String texturePack : TEXTUREPACKS)
			{
				final HashMap<Texture, BufferedImage[]> textureImages = new HashMap<>();
				for (final Texture texture : Texture.values())
				{
					final BufferedImage blocks = ImageIO.read(new File("data/images/texturePacks/blocks" + texturePack + texture.getSuffix() + ".png"));
					final int width = blocks.getWidth() / 16, height = blocks.getHeight() / 16;
					final BufferedImage[] images = new BufferedImage[width * height];
					for (int i = 0; i < images.length; i++ )
						images[i] = blocks.getSubimage((i % width) * 16, (i / width) * 16, 16, 16);
					textureImages.put(texture, images);
				}
				SPLIT_IMAGES.put(texturePack, textureImages);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		loadLevels();
		System.out.println(mLevels);
	}
	
	private static void loadLevels()
	{
		final File levels = new File("data/images/worldData/#Levels#.txt");
		final StringBuilder data = new StringBuilder();
		if (levels.exists()) try
		{
			final BufferedReader reader = new BufferedReader(new FileReader(levels));
			int c;
			while ((c = reader.read()) != -1)
				data.append((char) c);
			reader.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if ( !data.toString().isEmpty()) for (final String level : data.toString().split("\r\n"))
			mLevels.add(level.replace("\r", "").replace("\n", ""));
	}
	
	public static ArrayList<String> getLevels()
	{
		return mLevels;
	}
	
	public static void setTexturePack(final String aTexturePack)
	{
		sTexturePack = aTexturePack;
	}
	
	public static String[] getTexturePacks()
	{
		return TEXTUREPACKS;
	}
	
	public static void saveMapImage(final short[][] aData, final int aWorldId, final int aLevelId)
	{
		final BufferedImage image = new BufferedImage(aData.length, aData[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < aData.length; x++ )
			for (int y = 0; y < aData[x].length; y++ )
			{
				final short id = aData[x][y];
				final int rgb = Block.getCodeFromId(id);
				image.setRGB(x, y, rgb);
			}
		try
		{
			ImageIO.write(image, "PNG", new File("data/images/worldData/level" + aWorldId + "-" + aLevelId + ".png"));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getMapImage(final int aWorld, final int aLevel)
	{
		try
		{
			final BufferedImage image = ImageIO.read(new File("data/images/worldData/level" + aWorld + "-" + aLevel + ".png"));
			return image;
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage getImage(final String aName)
	{
		BufferedImage image = IMAGES.get(aName);
		if (image == null) image = loadImage(aName);
		return image;
	}
	
	public static BufferedImage getBlockImage(final int aId, final Texture aTexture)
	{
		return SPLIT_IMAGES.get(sTexturePack).get(aTexture)[aId];
	}
	
	private static BufferedImage loadImage(final String aName)
	{
		try
		{
			final BufferedImage image = ImageIO.read(new File("data/images/" + aName + ".png"));
			return image;
		}
		catch (final IOException e)
		{
			Log.error("Could not read Image " + aName);
		}
		return null;
	}
}
