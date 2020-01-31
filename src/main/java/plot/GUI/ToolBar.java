package plot.GUI;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import plot.Utility.ImageLoader;

public class ToolBar {
	
	public JToolBar bar;
	public JButton export, screenshot, help, exit;
	
	public ToolBar(){
		
		init();
		addComponents();
		addActions();
	}
	
	public void init(){
		
		bar = new JToolBar();
		export = new JButton("Export");
		screenshot = new JButton("Take screenshot");
		help = new JButton("Help");
		exit = new JButton("Exit");
	}
	
	public void addComponents(){
		
		bar.add(export);
		bar.addSeparator();
		bar.add(screenshot);
		bar.addSeparator();
		bar.add(help);
		bar.addSeparator();
		bar.add(exit);
	}

	public void addActions(){
		
		export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ExportDialog().show();
			}
		});
		
		screenshot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				BufferedImage image = null;
				try {
					image = new Robot().createScreenCapture(new Rectangle(Chart3D.getInstance().getPanel().getLocationOnScreen(), Chart3D.getInstance().getPanel().getSize()));
				} catch (HeadlessException | AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Clipboard systemClipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
				systemClipboard.setContents(new ImageTransferable(image), null);
				
				JOptionPane.showMessageDialog(MainFrame.frame, "Successfully copied the plot to the clipboard!");
				
			//	ImageIO.write(image, "png", new File("/screenshot.png"));
			}
		});
		
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.frame, "This is a demo application for 'Enhanced Graph plotting In Prism' GSOC 16 written by Muhammad Omer Saeed. Please see the tooltip of plot panel for controls.", "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public JToolBar getBar(){
		return bar;
	}
	
	 static class ImageTransferable implements Transferable
	    {
	        private Image image;

	        public ImageTransferable (Image image)
	        {
	            this.image = image;
	        }

	        public Object getTransferData(DataFlavor flavor)
	            throws UnsupportedFlavorException
	        {
	            if (isDataFlavorSupported(flavor))
	            {
	                return image;
	            }
	            else
	            {
	                throw new UnsupportedFlavorException(flavor);
	            }
	        }

	        public boolean isDataFlavorSupported (DataFlavor flavor)
	        {
	            return flavor == DataFlavor.imageFlavor;
	        }

	        public DataFlavor[] getTransferDataFlavors ()
	        {
	            return new DataFlavor[] { DataFlavor.imageFlavor };
	        }
	    }
}
