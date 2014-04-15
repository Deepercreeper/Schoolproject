package data.names;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public enum TexturePack
{
	MARIO("Mario"), MINECRAFT("Minecraft");
	
	private static final ArrayList<TexturePack>	TEXTUREPACKS	= new ArrayList<>();
	
	private TexturePack(final String aName)
	{
		mName = aName;
	}
	
	private final String	mName;
	
	/**
	 * Returns the suffix for any image file depending on this texture pack.
	 * 
	 * @return the suffix.
	 */
	public String getSuffix()
	{
		return mName;
	}
	
	@Override
	public String toString()
	{
		return mName;
	}
	
	/**
	 * Initializes all texture packs.
	 */
	public static void init()
	{
		for (final TexturePack texturePack : values())
			TEXTUREPACKS.add(texturePack);
		Collections.sort(TEXTUREPACKS, new Comparator<TexturePack>()
		{
			@Override
			public int compare(final TexturePack aTP0, final TexturePack aTP1)
			{
				return aTP0.mName.compareTo(aTP1.mName);
			}
		});
	}
	
	/**
	 * Returns a sorted array list of all texture packs.
	 * 
	 * @return all texture packs.
	 */
	public static ArrayList<TexturePack> getTexturePacks()
	{
		return TEXTUREPACKS;
	}
}
