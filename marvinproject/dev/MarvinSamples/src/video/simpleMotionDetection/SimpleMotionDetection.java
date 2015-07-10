/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.simpleMotionDetection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

/**
 * Simple motion detection sample
 * @author Gabriel Ambrosio Archanjo
 */
public class SimpleMotionDetection extends JFrame implements Runnable{

	private JPanel 					panelCenter,
									panelSlider;
	
	
	private JLabel 					labelMotion,
									labelSlider;
	
	private JSlider 				sliderSensibility;
	
	private MarvinVideoInterface	videoInterface;
	private MarvinImagePanel 		videoPanel;
	
	private int						imageWidth,
									imageHeight;
	
	private Thread 					thread;
	
	private MarvinImage 			imageIn, 
									imageOut, 
									imageLastFrame;
	
	private double 					differencePercentage;
	
	private int 					sensibility = 7;
	
	public SimpleMotionDetection(){
		
		try{
			videoPanel = new MarvinImagePanel();
			
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
			
			imageWidth = videoInterface.getImageWidth();
			imageHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(imageWidth, imageHeight);
			
			imageLastFrame = new MarvinImage(imageWidth,imageHeight);
			
			loadGUI();
			
			thread = new Thread(this);
			thread.start();
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){
		setTitle("Simple Motion Detection");
		labelMotion = new JLabel("MOTION: NO");
		labelMotion.setOpaque(true);
		labelMotion.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelMotion.setBackground(Color.red);
		labelMotion.setForeground(Color.white);
		
		labelSlider = new JLabel("Sensibility:");
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 13, 10);
		sliderSensibility.setMinorTickSpacing(1);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(videoPanel, BorderLayout.NORTH);
		panelCenter.add(labelMotion, BorderLayout.SOUTH);
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container l_container = getContentPane();
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(labelMotion, BorderLayout.CENTER);
		l_container.add(panelSlider, BorderLayout.SOUTH);
		
		
		setSize(imageWidth,imageHeight+100);
		setVisible(true);
	}
	
	public void run(){
		try{
			while(true){
				
				imageIn = videoInterface.getFrame();
				MarvinImage.copyColorArray(imageIn, imageOut);
				
				differencePercentage = getDifference(imageLastFrame, imageIn);
				
				MarvinImage.copyColorArray(imageIn, imageOut);			
				MarvinImage.copyColorArray(imageOut, imageLastFrame);
				
				videoPanel.setImage(imageOut);
							
				if(differencePercentage > sensibility){
					labelMotion.setBackground(Color.green);
					labelMotion.setForeground(Color.white);
					labelMotion.setText("MOTION: YES");
				}
				else{
					labelMotion.setBackground(Color.red);
					labelMotion.setForeground(Color.white);
					labelMotion.setText("MOTION: NO");
				}
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}

	private double getDifference(MarvinImage a_imageA, MarvinImage a_imageB){
		
		int l_redA,
			l_redB,
			l_greenA,
			l_greenB,
			l_blueA,
			l_blueB;
		
		double l_pixels=0;
		
		for(int y=0; y<a_imageA.getHeight(); y++){
			for(int x=0; x<a_imageA.getWidth(); x++){
				
				l_redA = a_imageA.getIntComponent0(x, y);
    			l_greenA = a_imageA.getIntComponent1(x, y);
    			l_blueA = a_imageA.getIntComponent2(x, y);
    			
    			l_redB = a_imageB.getIntComponent0(x, y);
    			l_greenB = a_imageB.getIntComponent1(x, y);
    			l_blueB = a_imageB.getIntComponent2(x, y);
    			
    			if
    			(
    				Math.abs(l_redA-l_redB)> 20 ||
    				Math.abs(l_greenA-l_greenB)> 20 ||
    				Math.abs(l_blueA-l_blueB)> 20
    			)
    			{
    				l_pixels++;
    			}	
			}
		}
		return (l_pixels/(a_imageA.getWidth()*a_imageA.getHeight())*100);
	}
	
	public static void main(String args[]){
		SimpleMotionDetection smd = new SimpleMotionDetection();
		smd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (15-sliderSensibility.getValue());
		}
	}
}
