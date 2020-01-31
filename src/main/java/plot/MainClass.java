package plot;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import plot.GUI.MainFrame;

public class MainClass {

	public static void main(String args []){

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

			e.printStackTrace();
		}

		MainFrame gui = new MainFrame();
		gui.show();
	}




}
