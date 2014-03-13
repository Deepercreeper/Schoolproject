package editor;

import game.level.block.Block;
import game.level.block.Item;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class Editor extends JFrame
{
	private boolean					mSaved, mChangesMade, mSizeModelChanged, mMouseLeft, mSelectionMade, mSelecting;
	
	private int						mWidth, mHeight, mWorld, mLevel, mMouseX, mMouseY, mSelectionStartX, mSelectionStartY, mSelectionWidth, mSelectionHeight;
	
	private final JPanel			mCP;
	
	private final JScrollPane		mScrollPane;
	
	private final ToolBox			mToolBox;
	
	private final JComboBox<Item>	mItems;
	
	private final JComboBox<String>	mTools;
	
	private SpinnerModel			mWidthModel, mHeightModel;
	
	private short[][]				mMap, mAlphas;
	private boolean[][] mSelection;
	
	public Editor()
	{
		EditorDataManager.init();
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent aE)
			{
				close();
			}
		});
		
		// Initiate fields
		{
			mItems = new JComboBox<>();
			mTools = new JComboBox<>();
			mCP = new JPanel()
			{
				@Override
				public void paint(final Graphics aG)
				{
					render(aG);
				}
			};
			mCP.addMouseMotionListener(new MouseMotionAdapter()
			{
				@Override
				public void mouseMoved(final MouseEvent aE)
				{
					mMouseX = aE.getX() / Block.SIZE;
					mMouseY = aE.getY() / Block.SIZE;
					showItem();
					repaint();
				}
				
				@Override
				public void mouseDragged(final MouseEvent aE)
				{
					mMouseX = aE.getX() / Block.SIZE;
					mMouseY = aE.getY() / Block.SIZE;
					if (mTools.getSelectedItem().equals("Stift"))
					{
						if (mMouseLeft) setBlock(mMouseX, mMouseY);
						else deleteBlock(mMouseX, mMouseY);
					}
					else setSelection(false);
					repaint();
				}
				
			});
			mCP.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(final MouseEvent aE)
				{
					if (mTools.getSelectedItem().equals("Selektion")) mSelectionMade = false;
				}
				
				@Override
				public void mousePressed(final MouseEvent aE)
				{
					mMouseLeft = aE.getButton() == MouseEvent.BUTTON1;
					if (mTools.getSelectedItem().equals("Stift"))
					{
						if (mMouseLeft) setBlock(mMouseX, mMouseY);
						else deleteBlock(mMouseX, mMouseY);
					}
					else
					{
						mSelectionMade = mSelecting =  true;
						if(!aE.isControlDown()) mSelection = new boolean[mWidth][mHeight];
						mSelectionStartX = mMouseX;
						mSelectionStartY = mMouseY;
						mSelectionWidth = mSelectionHeight = 1;
					}
					repaint();
				}
				
				@Override
				public void mouseReleased(final MouseEvent aE)
				{
					if (mTools.getSelectedItem().equals("Selektion"))
					{
						mSelecting = false;
						setSelection(true);
						if (mSelectionWidth == 1 && mSelectionHeight == 1) mSelectionMade = false;
						repaint();
					}
				}
			});
			mScrollPane = new JScrollPane(mCP);
			getContentPane().add(mScrollPane);
			
			initMenu();
			mChangesMade = false;
			mSaved = false;
			mToolBox = new ToolBox(this);
		}
		
		newMap();
		pack();
		{
			final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(size.width / 2 - (getWidth() + mToolBox.getWidth()) / 2 + mToolBox.getWidth(), size.height / 2 - getHeight() / 2);
			mToolBox.setLocation(getX() - mToolBox.getWidth(), size.height / 2 - mToolBox.getHeight() / 2);
		}
		setVisible(true);
	}
	
	private void newMap()
	{
		if (mChangesMade && !mSaved) saveMap();
		mChangesMade = mSaved = false;
		mWidth = 100;
		mHeight = 30;
		resetMap();
		resizeCP();
		setTitle("Level: " + mWorld + "-" + mLevel);
	}
	
	private void resetMap()
	{
		mMap = new short[mWidth][mHeight];
		mAlphas = new short[mWidth][mHeight];
		mSelection = new boolean[mWidth][mHeight];
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				mAlphas[x][y] = 255;
	}
	
	private void openMap()
	{
		if (mChangesMade && !mSaved) saveMap();
		final int[] worldAndLevel = showOpenDialog();
		if (worldAndLevel == null) return;
		mChangesMade = mSaved = false;
		mWorld = worldAndLevel[0];
		mLevel = worldAndLevel[1];
		
		final BufferedImage data = EditorDataManager.getMapImage(mWorld, mLevel);
		mWidth = data.getWidth();
		mHeight = data.getHeight();
		
		resetMap();
		final int[] rgb = new int[mWidth * mHeight];
		final int[] alphas = new int[mWidth * mHeight];
		data.getRGB(0, 0, mWidth, mHeight, rgb, 0, mWidth);
		data.getAlphaRaster().getPixels(0, 0, mWidth, mHeight, alphas);
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
			{
				final short id = Block.getIdFromCode(0xffffff & rgb[x + y * mWidth]);
				if (id == -1) mMap[x][y] = Block.AIR.getId();
				else
				{
					final Block block = Block.get(id);
					mMap[x][y] = id;
					if (block.isItemBlock()) mAlphas[x][y] = Item.getItem(0xffffff & rgb[x + y * mWidth]).getAlpha();
					else if (block == Block.QUESTION) mAlphas[x][y] = (short) alphas[x + y * mWidth];
					else mAlphas[x][y] = 255;
				}
			}
		
		resizeCP();
		setTitle("Level: " + mWorld + "-" + mLevel);
		repaint();
	}
	
	private void setBlock(final int aX, final int aY)
	{
		if (aX < 0 || aX >= mWidth || aY < 0 || aY >= mHeight) return;
		final Block block = Block.get(mToolBox.getBlockId());
		mMap[aX][aY] = mToolBox.getBlockId();
		if (block.isItemBlock() || block == Block.QUESTION) mAlphas[aX][aY] = mItems.getItemAt(mItems.getSelectedIndex()).getAlpha();
		else mAlphas[aX][aY] = 255;
	}
	
	private void deleteBlock(final int aX, final int aY)
	{
		if (aX < 0 || aX >= mWidth || aY < 0 || aY >= mHeight) return;
		mMap[aX][aY] = 0;
		mAlphas[aX][aY] = 255;
	}
	
	private void showItem()
	{
		if (mMouseX < 0 || mMouseX >= mWidth || mMouseY < 0 || mMouseY >= mHeight) return;
		if (mMap[mMouseX][mMouseY] != Block.ITEM.getId() && Block.get(mMap[mMouseX][mMouseY]) != Block.QUESTION) mCP.setToolTipText("");
		else mCP.setToolTipText(Item.getItem(mAlphas[mMouseX][mMouseY]).toString());
	}
	
	private void saveMap()
	{
		if ( !showSaveDialog()) return;
		EditorDataManager.saveMapImage(mMap, mAlphas, mWorld, mLevel);
		setTitle("Level:" + mWorld + "-" + mLevel);
		/*
		 * TODO
		 * - set mChangesMade
		 */
		mSaved = true;
	}
	
	private void close()
	{
		if (mChangesMade && !mSaved) saveMap();
		setVisible(false);
		System.exit(0);
	}
	
	private void resizeMap()
	{
		if (mSizeModelChanged) return;
		final int oldWidth = mWidth, oldHeight = mHeight;
		mWidth = (int) mWidthModel.getValue();
		mHeight = (int) mHeightModel.getValue();
		final short[][] oldMap = mMap, oldAlphas = mAlphas;
		resetMap();
		for (int x = 0; x < oldWidth && x < mWidth; x++ )
			for (int y = 0; y < oldHeight && y < mHeight; y++ )
			{
				mMap[x][y] = oldMap[x][y];
				mAlphas[x][y] = oldAlphas[x][y];
			}
		resizeCP();
	}
	
	private void render(final Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(0, 0, mWidth * Block.SIZE, mHeight * Block.SIZE);
		
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				Block.render(x, y, mMap[x][y], aG, false);
		
		aG.setColor(Color.red);
		aG.drawRect(mMouseX * Block.SIZE, mMouseY * Block.SIZE, Block.SIZE, Block.SIZE);
		
		if (mSelectionMade)
		{
			aG.setColor(new Color(0f, 0f, 1f, 0.5f));
			for (int x = 0; x < mWidth; x++ )
				for (int y = 0; y < mHeight; y++ )
					if(mSelection[x][y])aG.fillRect(x * Block.SIZE, y * Block.SIZE, Block.SIZE, Block.SIZE);
			if(mSelecting){
				aG.setColor(new Color(1f, 0f, 0f, 0.5f));
				int startX = mSelectionStartX, startY = mSelectionStartY;
				if (mSelectionWidth < 0) startX += mSelectionWidth + 1;
				if (mSelectionHeight < 0) startY += mSelectionHeight + 1;
				aG.fillRect(startX * Block.SIZE,startY * Block.SIZE, Math.abs(mSelectionWidth) * Block.SIZE, Math.abs(mSelectionHeight)*Block.SIZE);
			}
		}
	}
	
	private int[] showOpenDialog()
	{
		final ArrayList<String> levels = EditorDataManager.getLevels();
		final String[] levelArray = levels.toArray(new String[levels.size()]);
		final String level = (String) JOptionPane.showInputDialog(this, "Level öffnen:", "Öffnen...", JOptionPane.PLAIN_MESSAGE, null, levelArray, levels.get(0));
		if (level == null) return null;
		final int[] worldAndLevel = new int[2];
		worldAndLevel[0] = Integer.parseInt(level.split("-")[0]);
		worldAndLevel[1] = Integer.parseInt(level.split("-")[1]);
		return worldAndLevel;
	}
	
	private boolean showSaveDialog()
	{
		boolean done = false;
		int world = -1, level = -1;
		do
		{
			final String worldText = JOptionPane.showInputDialog(this, "Welt auswählen:", "Speichern...", JOptionPane.PLAIN_MESSAGE);
			if (worldText == null) return false;
			try
			{
				world = Integer.parseInt(worldText);
				if (world >= 0) done = true;
			}
			catch (final NumberFormatException aE)
			{}
		}
		while ( !done);
		done = false;
		do
		{
			final String levelText = JOptionPane.showInputDialog(this, "Level auswählen:", "Speichern...", JOptionPane.PLAIN_MESSAGE);
			if (levelText == null) return false;
			try
			{
				level = Integer.parseInt(levelText);
				if (level >= 0) done = true;
			}
			catch (final NumberFormatException aE)
			{}
		}
		while ( !done);
		if (EditorDataManager.getLevels().contains(world + "-" + level)
				&& JOptionPane.showConfirmDialog(this, "Bereits vorhanden. Überschreiben?", "Überschreiben", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return false;
		mWorld = world;
		mLevel = level;
		return true;
	}
	
	private void resizeCP()
	{
		mCP.setPreferredSize(new Dimension(mWidth * Block.SIZE, mHeight * Block.SIZE));
		mScrollPane.revalidate();
		mSizeModelChanged = true;
		mWidthModel.setValue(mWidth);
		mHeightModel.setValue(mHeight);
		mSizeModelChanged = false;
		repaint();
	}
	
	private void setSelection(boolean aAddToSelection)
	{
		mSelectionWidth = -mSelectionStartX + mMouseX;
		if (mSelectionWidth >= 0) mSelectionWidth++ ;
		else mSelectionWidth-- ;
		mSelectionHeight = -mSelectionStartY + mMouseY;
		if (mSelectionHeight >= 0) mSelectionHeight++ ;
		else mSelectionHeight-- ;
		if(!aAddToSelection) return;
		int startX = mSelectionStartX, startY = mSelectionStartY;
		if (mSelectionWidth < 0) startX += mSelectionWidth + 1;
		if (mSelectionHeight < 0) startY += mSelectionHeight + 1;
		for (int x = startX; x < startX + Math.abs(mSelectionWidth); x++ )
			for (int y = startY; y < startY + Math.abs(mSelectionHeight); y++ )
				mSelection[x][y] = true;
				
	}
	
	private void fillSelection()
	{
		if ( !mSelectionMade) return;
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				if(mSelection[x][y])setBlock(x, y);
		mSelectionMade = false;
		repaint();
	}
	
	private void deleteSelection()
	{
		if ( !mSelectionMade) return;
		for (int x = 0; x < mWidth; x++ )
			for (int y = 0; y < mHeight; y++ )
				if(mSelection[x][y])deleteBlock(x, y);
		mSelectionMade = false;
		repaint();
	}
	
	private void initMenu()
	{
		final JMenuBar menu = new JMenuBar();
		{
			final JMenu file = new JMenu("Datei");
			{
				final JMenuItem newFile = new JMenuItem("Neu");
				newFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						newMap();
					}
				});
				newFile.setMnemonic('N');
				newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
				file.add(newFile);
				
				final JMenuItem openFile = new JMenuItem("Öffnen");
				openFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						openMap();
					}
				});
				openFile.setMnemonic('f');
				openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
				file.add(openFile);
				
				file.addSeparator();
				
				final JMenuItem saveFile = new JMenuItem("Speichern");
				saveFile.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						saveMap();
					}
				});
				saveFile.setMnemonic('S');
				saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
				file.add(saveFile);
				
				file.addSeparator();
				
				final JMenuItem close = new JMenuItem("Beenden");
				close.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						close();
					}
				});
				close.setMnemonic('B');
				close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
				file.add(close);
			}
			file.setMnemonic('D');
			menu.add(file);
			
			final JMenuItem fillSelection = new JMenuItem("Selektion füllen");
			fillSelection.setEnabled(false);
			final JMenuItem deleteSelection = new JMenuItem("Selektion löschen");
			deleteSelection.setEnabled(false);
			
			final JMenu edit = new JMenu("Bearbeiten");
			{
				final JMenuItem nextBlock = new JMenuItem("Nächster Block");
				nextBlock.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mToolBox.nextBlock();
					}
				});
				nextBlock.setMnemonic('N');
				nextBlock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, Event.CTRL_MASK));
				edit.add(nextBlock);
				
				final JMenuItem previousBlock = new JMenuItem("Vorheriger Block");
				previousBlock.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mToolBox.previousBlock();
					}
				});
				previousBlock.setMnemonic('V');
				previousBlock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Event.CTRL_MASK));
				edit.add(previousBlock);
				
				edit.addSeparator();
				
				final JMenuItem nextItem = new JMenuItem("Nächstes Item");
				nextItem.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mItems.setSelectedIndex((mItems.getSelectedIndex() + 1) % Item.values().size());
					}
				});
				nextItem.setMnemonic('c');
				nextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
				edit.add(nextItem);
				
				final JMenuItem previousItem = new JMenuItem("Vorheriges Item");
				previousItem.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mItems.setSelectedIndex((mItems.getSelectedIndex() - 1 + Item.values().size()) % Item.values().size());
					}
				});
				previousItem.setMnemonic('r');
				previousItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
				edit.add(previousItem);
				
				edit.addSeparator();
				
				final JMenuItem pencilTool = new JMenuItem("Stift Tool");
				pencilTool.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mTools.setSelectedItem("Stift");
					}
				});
				pencilTool.setMnemonic('S');
				pencilTool.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
				edit.add(pencilTool);
				
				final JMenuItem selectionTool = new JMenuItem("Selektions Tool");
				selectionTool.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						mTools.setSelectedItem("Selektion");
					}
				});
				selectionTool.setMnemonic('l');
				selectionTool.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
				edit.add(selectionTool);
				
				fillSelection.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						fillSelection();
					}
				});
				fillSelection.setMnemonic('F');
				fillSelection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
				edit.add(fillSelection);
				
				deleteSelection.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(final ActionEvent aE)
					{
						deleteSelection();
					}
				});
				deleteSelection.setMnemonic('ö');
				deleteSelection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Event.CTRL_MASK));
				edit.add(deleteSelection);
			}
			edit.setMnemonic('B');
			menu.add(edit);
			
			final JLabel size = new JLabel("Map größe:");
			menu.add(size);
			
			final JSpinner width = new JSpinner(), height = new JSpinner();
			new NumberEditor(width);
			new NumberEditor(height);
			width.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent aE)
				{
					resizeMap();
				}
			});
			height.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(final ChangeEvent aE)
				{
					resizeMap();
				}
			});
			mWidthModel = width.getModel();
			mHeightModel = height.getModel();
			menu.add(width);
			menu.add(height);
			
			mTools.addItem("Stift");
			mTools.addItem("Selektion");
			mTools.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(final ItemEvent aArg0)
				{
					if (mTools.getSelectedItem().equals("Stift"))
					{
						fillSelection.setEnabled(false);
						deleteSelection.setEnabled(false);
						mSelectionMade = false;
						repaint();
					}
					else
					{
						fillSelection.setEnabled(true);
						deleteSelection.setEnabled(true);
					}
				}
			});
			menu.add(mTools);
			
			final JComboBox<String> texturePacks = new JComboBox<>();
			for (final String texturePack : EditorDataManager.getTexturePacks())
				texturePacks.addItem(texturePack);
			texturePacks.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent aE)
				{
					EditorDataManager.setTexturePack(texturePacks.getItemAt(texturePacks.getSelectedIndex()));
					mToolBox.repaint();
				}
			});
			menu.add(texturePacks);
			
			for (final Item item : Item.values())
				mItems.addItem(item);
			menu.add(mItems);
		}
		setJMenuBar(menu);
	}
}
