package util;

public class Util
{
	public static double minAbs(double aFirst, double aSecond)
	{
		if (Double.isNaN(aFirst)) return aSecond;
		if (Math.abs(aFirst) < Math.abs(aSecond)) return aFirst;
		return aSecond;
	}
}
