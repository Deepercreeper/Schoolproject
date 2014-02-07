package data;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class DataManager
{
	private static final HashMap<String, Image>	IMAGES	= new HashMap<>();
	
	public static Image get(String aName)
	{
		Image image = IMAGES.get(aName);
		if (image == null) image = load(aName);
		return image;
	}
	
	private static Image load(String aName)
	{
		try
		{
			Image image = new Image("data/" + aName + ".png");
			return image;
		}
		catch (SlickException e)
		{
			Log.error("Could not read Image " + aName);
		}
		return null;
	}
}
