package editor;

import game.level.block.Texture;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class EditorDataManager
{
	private static final HashMap<String, HashMap<Texture, BufferedImage[]>>	IMAGES			= new HashMap<>();
	
	private static final String[]											TEXTUREPACKS	= new String[] { "Mario", "Minecraft" };
	
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
				IMAGES.put(texturePack, textureImages);
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setTexturePack(final String aTexturePack)
	{
		sTexturePack = aTexturePack;
	}
	
	public static String[] getTexturePacks()
	{
		return TEXTUREPACKS;
	}
	
	public static BufferedImage getImage(final int aId, final Texture aTexture)
	{
		return IMAGES.get(sTexturePack).get(aTexture)[aId];
	}
}
