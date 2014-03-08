package main;

import org.newdawn.slick.SlickException;
import view.View;

public class Main
{
	public static void main(final String[] args)
	{
		try
		{
			new View();
		}
		catch (final SlickException e)
		{
			e.printStackTrace();
		}
	}
}
