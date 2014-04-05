package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.newdawn.slick.SlickException;
import view.View;
import editor.Editor;

public class Main
{
	public static void main(final String[] args)
	{
		try
		{
			if (args.length > 0 && args[0].equals("Editor"))
			{
				new Editor();
				return;
			}
			try
			{
				new View();
			}
			catch (final SlickException e)
			{
				e.printStackTrace();
			}
		}
		catch (final Exception e)
		{
			final String userHomeFolder = System.getProperty("user.home");
			final File log = new File(userHomeFolder + "/Desktop", "log.txt");
			try
			{
				log.createNewFile();
				final BufferedWriter writer = new BufferedWriter(new FileWriter(log));
				final String message = e.getMessage();
				if (message.startsWith("Resource not found")) writer.write("Data Verzeichnis fehlt!");
				else writer.write(message);
				writer.close();
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
}
