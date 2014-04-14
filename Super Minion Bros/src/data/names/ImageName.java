package data.names;

import data.DataManager;

public enum ImageName
{
	SPLASH("splash"), FLAG("flag"), LEVEL("worldData/level"), BACKGROUND("backgrounds/background"), TEXTUREPACK(null)
	{
		@Override
		public String getPath()
		{
			return DataManager.instance().getTexturePack().getSuffix();
		}
	},
	PLAYER("player", 14, 30), ENTITY("entity", 16, 16), ENEMY("enemy", 16, 16), WEAPON("weapon", 20, 20), BLOCKS("texturepacks/blocks", 16, 16);
	
	private ImageName(final String aPath)
	{
		mPath = aPath;
		mWidth = mHeight = -1;
	}
	
	private ImageName(final String aPath, final int aWidth, final int aHeight)
	{
		mPath = aPath;
		mWidth = aWidth;
		mHeight = aHeight;
	}
	
	private final String	mPath;
	
	private final int		mWidth, mHeight;
	
	public String getPath()
	{
		return mPath;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
}
