package game.level.block;

import java.util.HashSet;
import org.newdawn.slick.util.Log;

public class RGBManager
{
	private static RGBManager		INSTANCE;
	
	private final HashSet<Integer>	mRGBs;
	
	private final HashSet<Integer>	mAlphas;
	
	private RGBManager()
	{
		mRGBs = new HashSet<>();
		mAlphas = new HashSet<>();
	}
	
	public static RGBManager instance()
	{
		if (INSTANCE == null) INSTANCE = new RGBManager();
		return INSTANCE;
	}
	
	public void addRGB(final int aRGB)
	{
		if (mRGBs.contains(aRGB)) Log.error("Duplicate RGB code: " + aRGB + "!");
		mRGBs.add(aRGB);
	}
	
	public void addAlpha(final int aAlpha)
	{
		if (mAlphas.contains(aAlpha)) Log.error("Duplicate alpha code: " + aAlpha + "!");
		mAlphas.add(aAlpha);
	}
}
