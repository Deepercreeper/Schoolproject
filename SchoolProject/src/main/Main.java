package main;

import org.newdawn.slick.SlickException;
import view.View;

public class Main
{
	public static void main(final String[] args)
	{
		// if (args.length > 0)
		// {
		// if (args[0].equals("Editor")) new Editor();
		// else if (args[0].equals("Game"))
		try
		{
			new View();
		}
		catch (final SlickException e)
		{
			e.printStackTrace();
		}
		// }
	}
}
