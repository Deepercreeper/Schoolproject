package view;

import game.Game;
import game.Save;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import data.DataManager;

public class MainMenu extends Menu
{
	private enum State
	{
		NOTHING, NEW, LOAD;
	}
	
	private final int	mWorld		= 0;
	
	private Save		mSave		= null;
	
	private String		mTextBar	= "";
	
	private State		mInput		= State.NOTHING;
	
	public MainMenu(GameContainer gc, Game aGame)
	{
		super(aGame, 0, 0, gc.getWidth(), gc.getHeight());
	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(mX, mY, mWidth, mHeight);
		g.setColor(Color.white);
		if (mInput != State.NOTHING)
		{
			g.drawString("Name: " + mTextBar, mWidth / 2 - 100, mHeight / 2 - 15);
			g.drawString("Enter - Bestätigen", mWidth / 2 - 100, mHeight / 2);
			g.drawString("Escape - Zurück", mWidth / 2 - 100, mHeight / 2 + 15);
			return;
		}
		if (mSave == null)
		{
			g.drawString("Space - Neues Spiel", mWidth / 2 - 100, mHeight / 2 - 15);
			g.drawString("Enter - Spiel Laden", mWidth / 2 - 100, mHeight / 2);
			g.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 15);
		}
		else
		{
			// TODO Selection
			g.drawString("Spiel: " + mSave.getName(), mWidth / 2 - 100, mHeight / 2 - 15);
			g.drawString("Space - Start", mWidth / 2 - 100, mHeight / 2);
			g.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 15);
		}
	}
	
	@Override
	public void update(final Input aInput)
	{
		if (mInput != State.NOTHING) return;
		if (mSave == null)
		{
			if (aInput.isKeyPressed(Input.KEY_SPACE)) mInput = State.NEW;
			if (aInput.isKeyPressed(Input.KEY_ENTER)) mInput = State.LOAD;
			if (mInput != State.NOTHING)
			{
				aInput.addKeyListener(new KeyListener()
				{
					@Override
					public void setInput(Input aArg0)
					{}
					
					@Override
					public boolean isAcceptingInput()
					{
						return true;
					}
					
					@Override
					public void inputStarted()
					{}
					
					@Override
					public void inputEnded()
					{}
					
					@Override
					public void keyReleased(int aKey, char aChar)
					{}
					
					@Override
					public void keyPressed(int aKey, char aChar)
					{
						if (aKey == Input.KEY_ENTER)
						{
							if (mInput == State.NEW) newGame(mTextBar);
							else if (mInput == State.LOAD) loadGame(mTextBar);
							mInput = State.NOTHING;
							aInput.removeAllKeyListeners();
							mTextBar = "";
						}
						else if (Character.isLetter(aChar) || aChar == ' ') mTextBar += aChar;
						else if (aKey == Input.KEY_BACK && mTextBar.length() > 0) mTextBar = mTextBar.substring(0, mTextBar.length() - 1);
						else if (aKey == Input.KEY_ESCAPE) mInput = State.NOTHING;
					}
				});
			}
			else if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.stop();
		}
		else
		{
			if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mSave.getLastWorld());
			if (aInput.isKeyPressed(Input.KEY_ESCAPE)) save();
		}
		// if (aInput.isKeyPressed(Input.KEY_LEFT)) mWorld = (mWorld - 1 + DataManager.getWorlds().length) % DataManager.getWorlds().length;
		// if (aInput.isKeyPressed(Input.KEY_RIGHT)) mWorld = (mWorld + 1) % DataManager.getWorlds().length;
	}
	
	private void newGame(String aName)
	{
		mSave = new Save(aName);
	}
	
	private void loadGame(String aName)
	{
		mSave = DataManager.loadSave(aName);
	}
	
	private void save()
	{
		DataManager.save(mSave);
		mSave = null;
	}
}
