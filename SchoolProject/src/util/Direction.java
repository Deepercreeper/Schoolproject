package util;

public enum Direction
{
	TOP(0, -1), BOTTOM(0, 1), LEFT( -1, 0), RIGHT(1, 0), NONE(0, 0);
	
	public final int	XD, YD;
	
	private Direction(int aXD, int aYD)
	{
		XD = aXD;
		YD = aYD;
	}
}
