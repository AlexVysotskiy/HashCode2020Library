package plot.GUI;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class BottomPanel {
	
	private JPanel panel;
	private JPanel left;
	private JPanel middle;
	private JPanel right;
	public static JSlider slider;
	public static JLabel message;
	public JLabel detail;
	
	public JRadioButton s1, s2, s3, s4, s5;
	
	public BottomPanel(){
		
		init();
		addActions();
	}
	
	public void init(){
		
		panel = new JPanel(new GridLayout(1, 3));
		left = new JPanel(new GridLayout(1, 3));
		middle = new JPanel(new GridLayout(1, 3));
		right = new JPanel(new GridLayout(1, 2));
		
		detail = new JLabel("<html>Plot detail level:</html>");
		
		message = new JLabel("<html> &nbsp;Ready</html>");
		
		slider = new JSlider(0, 6);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.addChangeListener(Chart3D.getInstance());
		slider.setValue(6);
		
		s1 = new JRadioButton("Sample 1");
		s2 = new JRadioButton("Sample 2");
		s3 = new JRadioButton("Sample 3");
		s4 = new JRadioButton("Sample 4");
		s5 = new JRadioButton("Sample 5");
		s1.setSelected(true);
		
		JPanel rr = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		rr.add(detail);
		right.add(rr);
		right.add(slider);

		left.add(message);
		left.add(s1);
		left.add(s2);
		middle.add(s3);
		middle.add(s4);
		middle.add(s5);


		
		panel.add(left);
		panel.add(middle);
		panel.add(right);
	}
	
	public void addActions(){
		
		s1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s1.isSelected()){
					Chart3D.getInstance().setModelMapperSample1();
					
					s2.setSelected(false);
					s3.setSelected(false);
					s4.setSelected(false);
					s5.setSelected(false);
				}
			}
		});
		
		s2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s2.isSelected()){
					Chart3D.getInstance().setModelMapperSample2();
					s1.setSelected(false);
					s3.setSelected(false);
					s4.setSelected(false);
					s5.setSelected(false);
				}
			}
		});
		
		s3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s3.isSelected()){
					Chart3D.getInstance().setModelMapperSample3();
					
					s2.setSelected(false);
					s1.setSelected(false);
					s4.setSelected(false);
					s5.setSelected(false);
				}
			}
		});
		
		s4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s4.isSelected()){
					Chart3D.getInstance().setModelMapperSample4();
					
					s2.setSelected(false);
					s3.setSelected(false);
					s1.setSelected(false);
					s5.setSelected(false);
				}
			}
		});
		
		s5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(s5.isSelected()){
					Chart3D.getInstance().setModelMapperSample5();
					
					s2.setSelected(false);
					s3.setSelected(false);
					s4.setSelected(false);
					s1.setSelected(false);
				}
			}
		});
	}
	
	public JPanel getPanel(){
		
		return panel;
	}

}
