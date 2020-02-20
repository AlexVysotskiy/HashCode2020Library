package plot.gui;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Chart3D implements ChangeListener{

	private ProgressiveSurfaceModel model;
	private JSurfacePanel surfacePanel;
	private static Chart3D instance;

	private Chart3D(){
		init();
		setModelMapperSample1();
		addMouseListener();
	}

	public static Chart3D getInstance(){
		if(instance == null){
			instance = new Chart3D();
		}

		return instance;
	}

	public void init(){

		model = new ProgressiveSurfaceModel();
		surfacePanel = new JSurfacePanel();
		surfacePanel.setTitleText("Surface plotter Demo");
	}

	public void setModelMapperSample1(){
//		final SolutionMapper mapper = new SolutionMapper();
//
//		model = new ProgressiveSurfaceModel();
//
//		model.setMapper(mapper);
//		model.plot().execute();
//		surfacePanel.setModel(model);
	}

	public void setModelMapperSample2(){
		model = new ProgressiveSurfaceModel();
		model.setMapper(new Mapper() {

			public  float f1( float x, float y)
			{
				float r = x*y+x*y;

				if (r == 0 ) return 1f;
				return (float)( Math.cos(r));
			}

			public  float f2( float x, float y)
			{
				return (float)(Math.tan(x*y));
			}
		});
		model.plot().execute();
		surfacePanel.setModel(model);
	}

	public void setModelMapperSample3(){

		model = new ProgressiveSurfaceModel();
		model.setMapper(new Mapper() {
			public  float f1( float x, float y)
			{
				float r = x*x+y*y;

				if (r == 0 ) return 1f;
				return (float)( Math.sin(r)/(r));
			}

			public  float f2( float x, float y)
			{
				return (float)(Math.sin(x*y));
			}
		});
		model.plot().execute();
		surfacePanel.setModel(model);
	}

	public void setModelMapperSample4(){
		model = new ProgressiveSurfaceModel();
		model.setMapper(new Mapper() {

			public  float f1( float x, float y)
			{
				float r = x*x+y*y;

				if (r == 0 ) return 1f;
				return (float)( Math.cos(r));
			}

			public  float f2( float x, float y)
			{
				return (float)(Math.sin(x*y));
			}
		});
		model.plot().execute();
		surfacePanel.setModel(model);
	}

	public void setModelMapperSample5(){
		model = new ProgressiveSurfaceModel();
		model.setMapper(new Mapper() {

			public  float f1( float x, float y)
			{
				float r = x*x+y*y;

				if (r == 0 ) return 1f;
				return (float)( Math.tan(r) / r);
			}

			public  float f2( float x, float y)
			{
				return (float)(Math.cos(x*y));
			}
		});
		model.plot().execute();
		surfacePanel.setModel(model);
	}

	public void addMouseListener(){



		surfacePanel.getSurface().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {
				BottomPanel.message.setText("<html> &nbsp; Ready </html>");
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}

		});

		surfacePanel.getSurface().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				BottomPanel.message.setText("<html> &nbsp; X: " + e.getXOnScreen() + ", Y: " + e.getYOnScreen() + "</html>");
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				BottomPanel.message.setText("<html> &nbsp; X: " + e.getXOnScreen() + ", Y: " + e.getYOnScreen() + "</html>");

			}
		});

	}

	public JSurfacePanel getPanel(){
		return surfacePanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider)e.getSource();
		if (!slider.getValueIsAdjusting())
			model.setCurrentDefinition(slider.getValue());
	}

}
