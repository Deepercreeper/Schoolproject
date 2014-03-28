package util;

import org.newdawn.slick.Input;

public enum Key
{
	LEFT(Input.KEY_A), RIGHT(Input.KEY_D), UP(Input.KEY_W), DOWN(Input.KEY_S), FAST(Input.KEY_LSHIFT), JUMP(Input.KEY_SPACE), NEXT_WEAPON(Input.KEY_E), PREVIOUS_WEAPON(Input.KEY_Q);
	
	private final int	mDefault;
	
	private Key(final int aDefault)
	{
		mDefault = aDefault;
	}
	
	public int getDefault()
	{
		return mDefault;
	}
}
