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
	
	private int		mWorldId	= 0, mLevelId = 0;
	
	private int		mSaveIndex	= 0;
	
	private Save	mSave		= null;
	
	private String	mText		= "";
	
	private State	mState;
	
	/**
	 * Creates a new main menu, that is displayed when starting the game or selecting levels.
	 * 
	 * @param aGC
	 *            The containing game container.
	 * @param aGame
	 *            The parent game.
	 */
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
					aG.drawString("Entf - L�schen", mWidth / 2 - 100, mHeight / 2 + 45);
				}
				aG.drawString("Enter - Best�tigen", mWidth / 2 - 100, mHeight / 2 + 15);
				aG.drawString("Escape - Zur�ck", mWidth / 2 - 100, mHeight / 2 + 30);
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
				if (aInput.isKeyPressed(Input.KEY_ADD))
				{
					mSave.volumeUp();
					mGame.showVolume();
				}
				if (aInput.isKeyPressed(Input.KEY_SUBTRACT))
				{
					mSave.volumeDown();
					mGame.showVolume();
				}
				if (aInput.isKeyPressed(Input.KEY_SPACE)) mGame.start(mWorldId, mLevelId, mSave);
				else if (aInput.isKeyPressed(Input.KEY_ESCAPE))
				{
					save();
					mState = State.MAIN;
					aInput.clearKeyPressedRecord();
				}
				else if (aInput.isKeyPressed(Input.KEY_RIGHT))
				{
					int[] worlds = DataManager.getLevelsPerWorld();
					if (mLevelId < worlds[mWorldId] - 1)
					{
						if (mSave.isAvailable(mWorldId, mLevelId + 1)) mLevelId++ ;
					}
					else if (mWorldId < worlds.length - 1 && mSave.isAvailable(mWorldId + 1))
					{
						mWorldId++ ;
						mLevelId = 0;
					}
				}
				else if (aInput.isKeyPressed(Input.KEY_LEFT))
				{
					int[] worlds = DataManager.getLevelsPerWorld();
					if (mLevelId > 0)
					{
						if (mSave.isAvailable(mWorldId, mLevelId - 1)) mLevelId-- ;
					}
					else if (mWorldId > 0 && mSave.isAvailable(mWorldId - 1))
					{
						mWorldId-- ;
						mLevelId = worlds[mWorldId] - 1;
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
		aG.drawString("World: " + mWorldId + " Level: " + mLevelId, mWidth / 2 - 100, 5);
		
		aG.drawString("Spiel: " + mSave.getName(), 10, mHeight - 50);
		aG.drawString("Level score: " + mSave.getScore(mWorldId, mLevelId), 10, mHeight - 35);
		aG.drawString("World score: " + mSave.getScore(mWorldId), 10, mHeight - 20);
		
		aG.drawString("< > - Level ausw�hlen", mWidth / 2 - 100, mHeight - 50);
		aG.drawString("Space - Start", mWidth / 2 - 100, mHeight - 35);
		aG.drawString("Escape - Ende", mWidth / 2 - 100, mHeight - 20);
		
		int levels = DataManager.getLevelsPerWorld()[mWorldId];
		
		for (int i = 0; i < levels; i++ )
		{
			if (mSave.isAvailable(mWorldId, i)) aG.setColor(Color.white);
			else aG.setColor(Color.gray);
			if (i == mLevelId) aG.setColor(Color.red);
			aG.fillRect(mWidth / (levels + 1) * (i + 1) - 25, mHeight / 2 - 15, 50, 30);
			if (i > 0 && mSave.isAvailable(mWorldId, i))
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
		if (mWorldId < DataManager.getLevelsPerWorld().length - 1 && mSave.isAvailable(mWorldId + 1))
		{
			aG.setColor(Color.white);
			aG.drawLine(mWidth / (levels + 1) * levels + 25, mHeight / 2, mWidth, mHeight / 2);
		}
	}
	
	private void newGame(String aName)
	{
		mSave = new Save(aName);
		mWorldId = mSave.getLastWorldId();
		mLevelId = mSave.getLastLevelId();
	}
	
	private void loadGame(String aName)
	{
		mSave = DataManager.loadSave(aName);
		mWorldId = mSave.getLastWorldId();
		mLevelId = mSave.getLastLevelId();
	}
	
	private void save()
	{
		mSave.setLastWorldId(mWorldId);
		mSave.setLastLevelId(mLevelId);
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