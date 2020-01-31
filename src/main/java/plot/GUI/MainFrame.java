package plot.GUI;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainFrame {
	
	public static JFrame frame;
	
	public MainFrame(){
		init();
		addComponents();
	}
	
	public void init(){
		
		frame = new JFrame("Surface Plotter Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(1000, 800);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
	}
	
	public void addComponents(){
		
		frame.add(new BottomPanel().getPanel(), BorderLayout.SOUTH);
		frame.add(new ToolBar().getBar(), BorderLayout.NORTH);
		frame.add(Chart3D.getInstance().getPanel(), BorderLayout.CENTER);
	}
	
	public void show(){
		
		frame.setVisible(true);
	}

}
