package plot.GUI;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import plot.GUI.ToolBar.ImageTransferable;


public class ExportDialog {
	
	private JDialog dialog;
	private ColoredButton png, jpg, pdf;
	
	
	public ExportDialog() {
	
		init();
		addActions();
	}
	
	public void init(){
		
		dialog = new JDialog();
		dialog.setTitle("Export as");
		dialog.setLayout(new GridLayout(3, 1));
		dialog.setModal(true);
		dialog.setSize(new Dimension(450, 500));
		dialog.setLocationRelativeTo(MainFrame.frame);
		
		png = ColoredButton.GetRandomButton("PNG", "export the graph as a png image");
		jpg = ColoredButton.GetRandomButton("JPEG", "export the graph as a jpeg image");
		pdf = ColoredButton.GetRandomButton("PDF", "get the graph as a pdf file");
		
		dialog.add(png);
		dialog.add(jpg);
		dialog.add(pdf);
	}
	
	public void addActions(){
		
		png.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				FileDialog fd = new FileDialog(MainFrame.frame, "Enter the name of the file", FileDialog.SAVE);
				fd.setVisible(true);
				
				String filename;

				try{

					filename = fd.getFiles()[0].getAbsolutePath();

				} catch(Exception ee){
					return;
				}
				
				dialog.dispose();
				
				if(!filename.contains(".")){
					filename = filename + ".png";
				}
				
				File file = new File(filename);
				
				 try {
						BufferedImage image = null;
						try {
							image = new Robot().createScreenCapture(new Rectangle(Chart3D.getInstance().getPanel().getLocationOnScreen(), Chart3D.getInstance().getPanel().getSize()));
						} catch (HeadlessException | AWTException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						Clipboard systemClipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
						systemClipboard.setContents(new ImageTransferable(image), null);
						
						
						ImageIO.write(image, "png", file);
						
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		
		jpg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(MainFrame.frame, "Enter the name of the file", FileDialog.SAVE);
				fd.setVisible(true);
				
				String filename;

				try{

					filename = fd.getFiles()[0].getAbsolutePath();

				} catch(Exception ee){
					return;
				}
				
				if(!filename.contains(".")){
					filename = filename + ".jpg";
				}
				dialog.dispose();
				File file = new File(filename);
				
				 try {
						BufferedImage image = null;
						try {
							image = new Robot().createScreenCapture(new Rectangle(Chart3D.getInstance().getPanel().getLocationOnScreen(), Chart3D.getInstance().getPanel().getSize()));
						} catch (HeadlessException | AWTException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						Clipboard systemClipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
						systemClipboard.setContents(new ImageTransferable(image), null);
						
						ImageIO.write(image, "jpg", file);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		pdf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(MainFrame.frame, "Enter the name of the file", FileDialog.SAVE);
				fd.setVisible(true);
			}
		});
	}
	
	public void show(){
		
		dialog.setVisible(true);
	}

}