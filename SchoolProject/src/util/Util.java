package util;

public class Util
{
	private Util()
	{}
	
	/**
	 * Returns the parameter of the given ones that has the minimum absolute value.
	 * 
	 * @param aFirst
	 *            The first value to compare.
	 * @param aSecond
	 *            The second value to compare.
	 * @return the least absolute value.
	 */
	public static double minAbs(double aFirst, double aSecond)
	{
		if (Double.isNaN(aFirst)) return aSecond;
		if (Math.abs(aFirst) < Math.abs(aSecond)) return aFirst;
		return aSecond;
	}
}
