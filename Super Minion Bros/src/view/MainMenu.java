package view;

import game.Game;
import game.Save;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import util.InputKeys;
import util.Key;
import data.DataManager;
import data.LevelManager;

public class MainMenu extends Menu
{
	private enum State
	{
		MAIN, NEW_INPUT, LOAD_INPUT, GAME, OPTIONS;
	}
	
	private int		mWorldId		= 0, mLevelId = 0;
	
	private int		mSaveIndex		= 0;
	
	private Key		mSelectedKey	= null;
	
	private String	mText			= "";
	
	private State	mState;
	
	/**
	 * Creates a new main menu, that is displayed when starting the game or selecting levels.
	 * 
	 * @param aGC
	 *            The containing game container.
	 * @param aGame
	 *            The parent game.
	 */
	public MainMenu(final GameContainer aGC, final Game aGame)
	{
		super(aGame, 0, 0, aGC.getWidth(), aGC.getHeight());
		mState = State.MAIN;
	}
	
	@Override
	public void render(final Graphics aG)
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
			case OPTIONS :
				renderOptions(aG);
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
				if (aInput.isKeyPressed(Input.KEY_ADD))
				{
					Save.instance().volumeUp();
					mGame.showVolume();
				}
				if (aInput.isKeyPressed(Input.KEY_SUBTRACT))
				{
					Save.instance().volumeDown();
					mGame.showVolume();
				}
				if (aInput.isKeyPressed(Input.KEY_A)) Save.instance().previousTexturePack();
				if (aInput.isKeyPressed(Input.KEY_D)) Save.instance().nextTexturePack();
				if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mWorldId, mLevelId);
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE))
				{
					save();
					mState = State.MAIN;
					aInput.clearKeyPressedRecord();
				}
				else if (aInput.isKeyPressed(Input.KEY_RIGHT))
				{
					if (mLevelId < LevelManager.instance().getLevelsCount(mWorldId) - 1)
					{
						if (Save.instance().isAvailable(mWorldId, mLevelId + 1)) mLevelId++ ;
					}
					else if (mWorldId < LevelManager.instance().getWorldsCount() - 1 && Save.instance().isAvailable(mWorldId + 1))
					{
						mWorldId++ ;
						mLevelId = 0;
					}
				}
				else if (aInput.isKeyPressed(Input.KEY_LEFT))
				{
					if (mLevelId > 0)
					{
						if (Save.instance().isAvailable(mWorldId, mLevelId - 1)) mLevelId-- ;
					}
					else if (mWorldId > 0 && Save.instance().isAvailable(mWorldId - 1))
					{
						mWorldId-- ;
						mLevelId = LevelManager.instance().getLevelsCount(mWorldId) - 1;
					}
				}
				else if (aInput.isKeyPressed(Input.KEY_O)) mState = State.OPTIONS;
				break;
			case OPTIONS :
				if (aInput.isKeyPressed(Input.KEY_ESCAPE)) mState = State.GAME;
				if (mSelectedKey == null)
				{
					for (final Key key : Key.values())
						if (aInput.isKeyPressed(key.getDefault()))
						{
							mSelectedKey = key;
							aInput.addKeyListener(new InputKeyListener(aInput));
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
	
	private void renderOptions(final Graphics aG)
	{
		aG.setColor(Color.white);
		aG.drawString("Optionen", mWidth / 2 - 40, 10);
		
		for (int i = 0; i < Key.values().length; i++ )
		{
			final Key key = Key.values()[i];
			if (key == mSelectedKey) aG.setColor(Color.red);
			else aG.setColor(Color.white);
			aG.drawString(key + ": Standard: " + Input.getKeyName(key.getDefault()) + ", Aktiv: " + Input.getKeyName(InputKeys.instance().getKey(key)), mWidth / 2 - 100, 200 + i * 20);
		}
		
		aG.setColor(Color.white);
		aG.drawString(DataManager.getVersion(), 10, mHeight - 20);
	}
	
	private void renderLevelSelection(final Graphics aG)
	{
		aG.drawImage(DataManager.getImage(DataManager.getTexturePack()), 0, 0);
		
		aG.drawString("World: " + mWorldId + " Level: " + mLevelId, mWidth / 2 - 100, 5);
		
		aG.drawString("Spiel: " + Save.instance().getName(), 10, mHeight - 50);
		aG.drawString("Level score: " + Save.instance().getScore(mWorldId, mLevelId), 10, mHeight - 35);
		aG.drawString("World score: " + Save.instance().getScore(mWorldId), 10, mHeight - 20);
		
		aG.drawString("< > - Level auswählen", mWidth / 2 - 100, mHeight - 80);
		aG.drawString("A D - Texturepack: " + DataManager.getTexturePack(), mWidth / 2 - 100, mHeight - 65);
		aG.drawString("Space - Start", mWidth / 2 - 100, mHeight - 50);
		aG.drawString("O - Optionen", mWidth / 2 - 100, mHeight - 35);
		aG.drawString("Escape - Ende", mWidth / 2 - 100, mHeight - 20);
		
		final int levels = LevelManager.instance().getLevelsCount(mWorldId);
		for (int i = 0; i < levels; i++ )
		{
			if (Save.instance().isAvailable(mWorldId, i)) aG.setColor(Color.white);
			else aG.setColor(Color.gray);
			if (i == mLevelId) aG.setColor(Color.red);
			aG.fillRect(mWidth / (levels + 1) * (i + 1) - 25, mHeight / 2 - 15, 50, 30);
			if (i > 0 && Save.instance().isAvailable(mWorldId, i))
			{
				aG.setColor(Color.white);
				aG.drawLine(mWidth / (levels + 1) * i + 25, mHeight / 2, mWidth / (levels + 1) * (i + 1) - 25, mHeight / 2);
			}
		}
		if (mWorldId > 0)
		{
			aG.setColor(Color.white);
			aG.drawLine(0, mHeight / 2, mWidth / (levels + 1) - 25, mHeight / 2);
		}
		if (mWorldId < LevelManager.instance().getWorldsCount() - 1 && Save.instance().isAvailable(mWorldId + 1))
		{
			aG.setColor(Color.white);
			aG.drawLine(mWidth / (levels + 1) * levels + 25, mHeight / 2, mWidth, mHeight / 2);
		}
	}
	
	private void newGame(final String aName)
	{
		Save.newInstance(aName);
		mWorldId = Save.instance().getLastWorldId();
		mLevelId = Save.instance().getLastLevelId();
	}
	
	private void loadGame(final String aName)
	{
		DataManager.loadSave(aName);
		mWorldId = Save.instance().getLastWorldId();
		mLevelId = Save.instance().getLastLevelId();
	}
	
	private void save()
	{
		Save.instance().setLastWorldId(mWorldId);
		Save.instance().setLastLevelId(mLevelId);
		DataManager.save();
	}
	
	private class InputKeyListener implements KeyListener
	{
		private final Input	mInput;
		
		private InputKeyListener(final Input aInput)
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
		public void setInput(final Input aArg0)
		{}
		
		@Override
		public void keyPressed(final int aKey, final char aChar)
		{
			if (aKey != Input.KEY_ESCAPE) InputKeys.instance().setKey(mSelectedKey, aKey);
			mSelectedKey = null;
			mInput.removeAllKeyListeners();
			mInput.clearKeyPressedRecord();
		}
		
		@Override
		public void keyReleased(final int aKey, final char aChar)
		{}
	}
	
	private class Listener implements KeyListener
	{
		private final Input	mInput;
		
		private Listener(final Input aInput)
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
		public void setInput(final Input aArg0)
		{}
		
		@Override
		public void keyPressed(final int aKey, final char aChar)
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
		public void keyReleased(final int aKey, final char aChar)
		{}
	}
}
