package log;

public class Log
{
	private Log()
	{}
	
	public static void log(String aMessage)
	{
		System.out.println("[I] - " + aMessage);
	}
	
	public static void error(String aMessage)
	{
		System.out.println("[E] - " + aMessage);
	}
}
