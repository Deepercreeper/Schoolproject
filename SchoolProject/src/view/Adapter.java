package view;

import game.Game;
import org.newdawn.slick.Input;

public class Adapter
{
	private final Game	mGame;
	
	public Adapter(Game aGame)
	{
		mGame = aGame;
	}
	
	public void key(int aKey, boolean aDown)
	{
		if (aDown)
		{
			// Instant keys
			
		}
		else
		{
			// Up keys
			switch (aKey)
			{
				case Input.KEY_ESCAPE :
					mGame.stop();
			}
		}
	}
}
