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
		MAIN, NEW_INPUT, LOAD_INPUT, GAME;
	}
	
	private int		mWorld		= 0;
	
	private int		mLevelIndex	= 0;
	
	private Save	mSave		= null;
	
	private String	mText		= "";
	
	private State	mState;
	
	public MainMenu(GameContainer gc, Game aGame)
	{
		super(aGame, 0, 0, gc.getWidth(), gc.getHeight());
		mState = State.MAIN;
	}
	
	@Override
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(mX, mY, mWidth, mHeight);
		g.setColor(Color.white);
		switch (mState)
		{
			case MAIN :
				g.drawString("Space - Neues Spiel", mWidth / 2 - 100, mHeight / 2 - 15);
				g.drawString("Enter - Spiel Laden", mWidth / 2 - 100, mHeight / 2);
				g.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 15);
				break;
			case LOAD_INPUT :
			case NEW_INPUT :
				g.drawString("Name: " + mText, mWidth / 2 - 100, mHeight / 2 - 15);
				g.drawString("Enter - Bestätigen", mWidth / 2 - 100, mHeight / 2);
				g.drawString("Escape - Zurück", mWidth / 2 - 100, mHeight / 2 + 15);
				break;
			case GAME :
				g.drawString("Spiel: " + mSave.getName(), mWidth / 2 - 100, mHeight / 2 - 15);
				g.drawString("< > - World, Level: " + mWorld + ":" + mLevelIndex + " Level Score: " + mSave.getScore(mWorld, mLevelIndex) + " World Score: " + mSave.getScore(mWorld),
						mWidth / 2 - 100, mHeight / 2);
				g.drawString("Space - Start", mWidth / 2 - 100, mHeight / 2 + 15);
				g.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 30);
				break;
			default :
				break;
		
		}
	}
	
	@Override
	public void update(final Input aInput)
	{
		switch (mState)
		{
			case MAIN :
				if (aInput.isKeyPressed(Input.KEY_SPACE)) mState = State.NEW_INPUT;
				else if (aInput.isKeyPressed(Input.KEY_ENTER)) mState = State.LOAD_INPUT;
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.stop();
				if (mState == State.NEW_INPUT || mState == State.LOAD_INPUT) aInput.addKeyListener(new Listener(aInput));
				break;
			case GAME :
				if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mWorld, DataManager.getWorlds()[mWorld][mLevelIndex], mSave);
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE))
				{
					save();
					mState = State.MAIN;
					aInput.clearKeyPressedRecord();
				}
				else if (aInput.isKeyPressed(Input.KEY_RIGHT))
				{
					int[][] worlds = DataManager.getWorlds();
					if (mLevelIndex < worlds[mWorld].length - 1)
					{
						if (mSave.isAvailable(mWorld, mLevelIndex + 1)) mLevelIndex++ ;
					}
					else if (mWorld < worlds.length - 1 && mSave.isAvailable(mWorld + 1))
					{
						mWorld++ ;
						mLevelIndex = 0;
					}
				}
				else if (aInput.isKeyPressed(Input.KEY_LEFT))
				{
					int[][] worlds = DataManager.getWorlds();
					if (mLevelIndex > 0)
					{
						if (mSave.isAvailable(mWorld, mLevelIndex - 1)) mLevelIndex-- ;
					}
					else if (mWorld > 0 && mSave.isAvailable(mWorld - 1))
					{
						mWorld-- ;
						mLevelIndex = worlds[mWorld].length - 1;
					}
				}
				break;
			case LOAD_INPUT :
			case NEW_INPUT :
			default :
				break;
		}
	}
	
	private void newGame(String aName)
	{
		mSave = new Save(aName);
		mWorld = mSave.getLastWorld();
		mLevelIndex = mSave.getLastLevelIndex();
	}
	
	private void loadGame(String aName)
	{
		mSave = DataManager.loadSave(aName);
		mWorld = mSave.getLastWorld();
		mLevelIndex = mSave.getLastLevelIndex();
	}
	
	private void save()
	{
		mSave.setLastWorld(mWorld);
		mSave.setLastLevelIndex(mLevelIndex);
		DataManager.save(mSave);
		mSave = null;
	}
	
	private class Listener implements KeyListener
	{
		private final Input	mInput;
		
		public Listener(Input aInput)
		{
			mInput = aInput;
		}
		
		@Override
		public void inputEnded()
		{}
		
		@Override
		public void inputStarted()
		{}
		
		@Override
		public boolean isAcceptingInput()
		{
			return true;
		}
		
		@Override
		public void setInput(Input aArg0)
		{}
		
		@Override
		public void keyPressed(int aKey, char aChar)
		{
			if (Character.isLetter(aChar) || aChar == ' ') mText += aChar;
			else if (aKey == Input.KEY_BACK && mText.length() > 0) mText = mText.substring(0, mText.length() - 1);
			else if (aKey == Input.KEY_ENTER)
			{
				if (mState == State.NEW_INPUT) newGame(mText);
				else if (mState == State.LOAD_INPUT) loadGame(mText);
				mText = "";
				mState = State.GAME;
				mInput.removeAllKeyListeners();
				mInput.clearKeyPressedRecord();
			}
			else if (aKey == Input.KEY_ESCAPE)
			{
				mText = "";
				mState = State.MAIN;
				mInput.removeAllKeyListeners();
				mInput.clearKeyPressedRecord();
			}
		}
		
		@Override
		public void keyReleased(int aKey, char aChar)
		{}
	}
}
