package editor;

import game.level.block.Block;
import game.level.block.Item;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.newdawn.slick.util.Log;
import data.names.Texture;
import data.names.TexturePack;

public class EditorDataManager
{
	private static EditorDataManager												INSTANCE;
	
	private static final HashMap<TexturePack, HashMap<Texture, BufferedImage[]>>	SPLIT_IMAGES	= new HashMap<>();
	private static final HashMap<String, BufferedImage>								IMAGES			= new HashMap<>();
	
	private static ArrayList<String>												mLevels			= new ArrayList<>();
	
	private static TexturePack														sTexturePack	= TexturePack.MARIO;
	
	private EditorDataManager()
	{}
	
	public static EditorDataManager instance()
	{
		if (INSTANCE == null) INSTANCE = new EditorDataManager();
		return INSTANCE;
	}
	
	public void init()
	{
		TexturePack.init();
		Texture.init();
		try
		{
			for (final TexturePack texturePack : TexturePack.getTexturePacks())
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
	}
	
	private void loadLevels()
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
		if ( !data.toString().isEmpty()) for (final String level : data.toString().split("\n"))
			mLevels.add(level);
	}
	
	public ArrayList<String> getLevels()
	{
		return mLevels;
	}
	
	public void setTexturePack(final TexturePack aTexturePack)
	{
		sTexturePack = aTexturePack;
	}
	
	public void saveMapImage(final short[][] aData, final short[][] aAlphas, final int aWorld, final int aLevel)
	{
		final BufferedImage image = new BufferedImage(aData.length, aData[0].length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < aData.length; x++ )
			for (int y = 0; y < aData[x].length; y++ )
			{
				final short id = aData[x][y];
				final int rgb;
				final Block block = Block.get(id);
				if (block.isItemBlock()) rgb = Item.getItem(aAlphas[x][y]).getRGB();
				else rgb = Block.getCodeFromId(id);
				image.setRGB(x, y, rgb);
				image.getAlphaRaster().setPixel(x, y, new int[] { aAlphas[x][y] });
			}
		try
		{
			ImageIO.write(image, "PNG", new File("data/images/worldData/level" + aWorld + "-" + aLevel + ".png"));
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if (mLevels.contains(aWorld + "-" + aLevel)) return;
		final File levels = new File("data/images/worldData/#Levels#.txt");
		if (levels.exists()) levels.delete();
		try
		{
			new File(levels.getParent()).mkdir();
			levels.createNewFile();
			final BufferedWriter writer = new BufferedWriter(new FileWriter(levels));
			for (final String level : mLevels)
				writer.write(level + "\n");
			writer.write(aWorld + "-" + aLevel);
			mLevels.add(aWorld + "-" + aLevel);
			writer.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public BufferedImage getMapImage(final int aWorld, final int aLevel)
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
	
	public BufferedImage getImage(final String aName)
	{
		BufferedImage image = IMAGES.get(aName);
		if (image == null) image = loadImage(aName);
		return image;
	}
	
	public BufferedImage getBlockImage(final int aId, final Texture aTexture)
	{
		return SPLIT_IMAGES.get(sTexturePack).get(aTexture)[aId];
	}
	
	private BufferedImage loadImage(final String aName)
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
