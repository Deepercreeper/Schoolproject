package main;

import org.newdawn.slick.SlickException;
import view.View;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			new View();
		}
		catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
}
