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
	
	private int		mWorld		= 0, mLevelIndex = 0;
	
	private int		mSaveIndex	= 0;
	
	private Save	mSave		= null;
	
	private String	mText		= "";
	
	private State	mState;
	
	public MainMenu(GameContainer aGC, Game aGame)
	{
		super(aGame, 0, 0, aGC.getWidth(), aGC.getHeight());
		mState = State.MAIN;
	}
	
	@Override
	public void render(Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(mX, mY, mWidth, mHeight);
		aG.setColor(Color.white);
		switch (mState)
		{
			case MAIN :
				aG.drawString("Space - Neues Spiel", mWidth / 2 - 100, mHeight / 2 - 15);
				aG.drawString("Enter - Spiel Laden", mWidth / 2 - 100, mHeight / 2);
				aG.drawString("Escape - Ende", mWidth / 2 - 100, mHeight / 2 + 15);
				break;
			case LOAD_INPUT :
			case NEW_INPUT :
				for (int i = 1; i <= DataManager.getSaves().size() && mHeight / 2 - i * 15 > 0; i++ )
					aG.drawString(DataManager.getSaves().get(DataManager.getSaves().size() - i), mWidth / 2 - 100, mHeight / 2 - 15 - i * 15);
				if (mState == State.NEW_INPUT) aG.drawString("Name: " + mText, mWidth / 2 - 100, mHeight / 2);
				else
				{
					aG.drawString("< > - Save: " + DataManager.getSaves().get(mSaveIndex), mWidth / 2 - 100, mHeight / 2);
					aG.drawString("Entf - Löschen", mWidth / 2 - 100, mHeight / 2 + 45);
				}
				aG.drawString("Enter - Bestätigen", mWidth / 2 - 100, mHeight / 2 + 15);
				aG.drawString("Escape - Zurück", mWidth / 2 - 100, mHeight / 2 + 30);
				break;
			case GAME :
				renderLevelSelection(aG);
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
				else if (aInput.isKeyPressed(Input.KEY_ENTER) && !DataManager.getSaves().isEmpty()) mState = State.LOAD_INPUT;
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mGame.stop();
				if (mState == State.NEW_INPUT) aInput.addKeyListener(new Listener(aInput));
				break;
			case GAME :
				if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mWorld, mLevelIndex, mSave);
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE))
				{
					save();
					mState = State.MAIN;
					aInput.clearKeyPressedRecord();
				}
				else if (aInput.isKeyPressed(Input.KEY_RIGHT))
				{
					int[] worlds = DataManager.getLevels();
					if (mLevelIndex < worlds[mWorld] - 1)
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
					int[] worlds = DataManager.getLevels();
					if (mLevelIndex > 0)
					{
						if (mSave.isAvailable(mWorld, mLevelIndex - 1)) mLevelIndex-- ;
					}
					else if (mWorld > 0 && mSave.isAvailable(mWorld - 1))
					{
						mWorld-- ;
						mLevelIndex = worlds[mWorld] - 1;
					}
				}
				break;
			case LOAD_INPUT :
				if (aInput.isKeyPressed(Input.KEY_RIGHT)) mSaveIndex = (mSaveIndex + 1) % DataManager.getSaves().size();
				else if (aInput.isKeyPressed(Input.KEY_LEFT)) mSaveIndex = (mSaveIndex - 1 + DataManager.getSaves().size()) % DataManager.getSaves().size();
				else if (aInput.isKeyPressed(Input.KEY_DELETE))
				{
					DataManager.deleteSave(mSaveIndex);
					if (DataManager.getSaves().isEmpty())
					{
						mState = State.MAIN;
						aInput.clearKeyPressedRecord();
					}
					else if (mSaveIndex == DataManager.getSaves().size()) mSaveIndex-- ;
				}
				else if (aInput.isKeyPressed(Input.KEY_ENTER))
				{
					loadGame(DataManager.getSaves().get(mSaveIndex));
					mState = State.GAME;
					aInput.clearKeyPressedRecord();
				}
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE))
				{
					mState = State.MAIN;
					aInput.clearKeyPressedRecord();
				}
				break;
			case NEW_INPUT :
			default :
				break;
		}
	}
	
	private void renderLevelSelection(Graphics aG)
	{
		aG.drawString("World: " + mWorld + " Level: " + mLevelIndex, mWidth / 2 - 100, 5);
		
		aG.drawString("Spiel: " + mSave.getName(), 10, mHeight - 50);
		aG.drawString("Level score: " + mSave.getScore(mWorld, mLevelIndex), 10, mHeight - 35);
		aG.drawString("World score: " + mSave.getScore(mWorld), 10, mHeight - 20);
		
		aG.drawString("< > - Level auswählen", mWidth / 2 - 100, mHeight - 50);
		aG.drawString("Space - Start", mWidth / 2 - 100, mHeight - 35);
		aG.drawString("Escape - Ende", mWidth / 2 - 100, mHeight - 20);
		
		int levels = DataManager.getLevels()[mWorld];
		
		for (int i = 0; i < levels; i++ )
		{
			if (mSave.isAvailable(mWorld, i)) aG.setColor(Color.white);
			else aG.setColor(Color.gray);
			if (i == mLevelIndex) aG.setColor(Color.red);
			aG.fillRect(mWidth / (levels + 1) * (i + 1) - 25, mHeight / 2 - 15, 50, 30);
			if (i > 0 && mSave.isAvailable(mWorld, i))
			{
				aG.setColor(Color.white);
				aG.drawLine(mWidth / (levels + 1) * i + 25, mHeight / 2, mWidth / (levels + 1) * (i + 1) - 25, mHeight / 2);
			}
		}
		if (mWorld > 0)
		{
			aG.setColor(Color.white);
			aG.drawLine(0, mHeight / 2, mWidth / (levels + 1) - 25, mHeight / 2);
		}
		if (mWorld < DataManager.getLevels().length - 1 && mSave.isAvailable(mWorld + 1))
		{
			aG.setColor(Color.white);
			aG.drawLine(mWidth / (levels + 1) * levels + 25, mHeight / 2, mWidth, mHeight / 2);
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
			if (Character.isLetter(aChar) || Character.isDigit(aChar) || aChar == ' ') mText += aChar;
			else if (aKey == Input.KEY_BACK && mText.length() > 0) mText = mText.substring(0, mText.length() - 1);
			else if (aKey == Input.KEY_ENTER)
			{
				newGame(mText);
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
