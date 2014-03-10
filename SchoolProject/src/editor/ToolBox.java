package editor;

import game.level.block.Block;
import game.level.block.Texture;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class ToolBox extends JDialog
{
	private static final int	WINDOW_WIDTH	= 10 * 16, WINDOW_HEIGHT = Block.getBlockList().size() / 10 * 16;
	
	private Texture				mActiveTexture	= Texture.NORMAL;
	
	public ToolBox(final Editor aParent)
	{
		super(aParent, false);
		final JMenuBar toolBar = new JMenuBar();
		{
			final JComboBox<Texture> textures = new JComboBox<>();
			for (final Texture texture : Texture.values())
				textures.addItem(texture);
			textures.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent aArg0)
				{
					mActiveTexture = textures.getItemAt(textures.getSelectedIndex());
					repaint();
				}
			});
			toolBar.add(textures);
			setJMenuBar(toolBar);
		}
		
		final Container cp = new Container()
		{
			@Override
			public void paint(final Graphics aG)
			{
				render(aG);
			}
		};
		cp.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		cp.setLocation(0, 0);
		
		setResizable(false);
		
		getContentPane().add(cp, null);
		
		pack();
		
		setAlwaysOnTop(true);
		setTitle("Blöcke");
		setVisible(true);
	}
	
	private void render(final Graphics aG)
	{
		aG.setColor(Color.black);
		aG.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		final ArrayList<Block> blocks = Block.getBlockList();
		for (int i = 0; i < blocks.size(); i++ )
		{
			aG.setColor(new Color(i, i, i));
			Texture texture = Texture.NORMAL;
			if (blocks.get(i).getId(mActiveTexture) != blocks.get(i).getId()) texture = mActiveTexture;
			aG.drawImage(EditorDataManager.getImage(i, texture), (i % 10) * 16, (i / 10) * 16, null);
		}
	}
}
