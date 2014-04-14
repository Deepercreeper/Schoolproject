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
	
	public String getSuffix()
	{
		return mName;
	}
	
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
	
	public static TexturePack get(final String aName)
	{
		for (final TexturePack texturePack : values())
			if (texturePack.getSuffix().equals(aName)) return texturePack;
		return null;
	}
	
	public static ArrayList<TexturePack> getTexturePacks()
	{
		return TEXTUREPACKS;
	}
}
