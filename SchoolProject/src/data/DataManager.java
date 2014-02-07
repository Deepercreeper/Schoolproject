package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.newdawn.slick.util.Log;

public class DataManager
{
	private static final HashMap<String, BufferedImage>	IMAGES	= new HashMap<>();
	
	public static BufferedImage get(String aName)
	{
		BufferedImage image = IMAGES.get(aName);
		if (image == null) image = load(aName);
		return image;
	}
	
	private static BufferedImage load(String aName)
	{
		try
		{
			return ImageIO.read(new FileInputStream(new File("data/" + aName
					+ ".png")));
		}
		catch (IOException e)
		{
			Log.error("Could not read Image " + aName);
		}
		return null;
	}
}
