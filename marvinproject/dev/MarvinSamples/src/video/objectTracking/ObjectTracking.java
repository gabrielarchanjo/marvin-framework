/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.objectTracking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

import org.marvinproject.image.pattern.findColorPattern.FindColorPattern;

/**
 * Object tracking by color pattern
 * @author Gabriel Ambrosio Archanjo
 */

public class ObjectTracking extends JFrame implements Runnable{

	private MarvinVideoInterface	videoInterface;
	private MarvinImagePanel 		videoPanel;
	
	private Thread 					thread;
	
	private MarvinImage 			imageIn, 
									imageOut;
	
	private JPanel					panelSlider;
	
	private JSlider					sliderSensibility;
	
	private JLabel					labelSlider;
	
	private MarvinImagePlugin		pluginImage;
	private MarvinAttributes		attributesOut;
	
	private int						sensibility=30;
	
	private int						imageWidth,
									imageHeight;
	
	private boolean					regionSelected=false;
	private int[]					arrInitialRegion;
	
	public ObjectTracking(){
		
		try{
			videoPanel = new MarvinImagePanel();
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
			
			imageWidth = videoInterface.getImageWidth();
			imageHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(imageWidth, imageHeight);
			
			loadGUI();
			
			pluginImage = new FindColorPattern();
			pluginImage.load();
			
			attributesOut = new MarvinAttributes(null);
	
			thread = new Thread(this);
			thread.start();
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){	
		setTitle("Video Sample - Tracking");
		
		videoPanel.addMouseListener(new MouseHandler());
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sliderSensibility.setMinorTickSpacing(2);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		labelSlider = new JLabel("Sensibility");
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.SOUTH);
		
		setSize(imageWidth+20,imageHeight+100);
		setVisible(true);
	}
		
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		try{
			while(true){
				
				ticks++;
				if(System.currentTimeMillis() - time > 1000){
					System.out.println("FPS: "+ticks+"       ");
					ticks=0;
					time = System.currentTimeMillis();					
				}
				
				imageIn = videoInterface.getFrame();
				MarvinImage.copyColorArray(imageIn, imageOut);
				
				MarvinImage.copyColorArray(imageIn, imageOut);
				
				if(regionSelected){
					pluginImage.setAttribute("differenceColorRange", sensibility);
					pluginImage.process(imageIn, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
					
					imageOut.drawRect
					(
						(Integer)attributesOut.get("regionPx"), 
						(Integer)attributesOut.get("regionPy"),
						(Integer)attributesOut.get("regionWidth"),
						(Integer)attributesOut.get("regionHeight"),
						Color.red
					);
				}
				videoPanel.setImage(imageOut);
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		ObjectTracking l_objectTracking = new ObjectTracking();
		l_objectTracking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}
	
	private class MouseHandler implements MouseListener{
		public void mouseEntered(MouseEvent a_event){}
		public void mouseExited(MouseEvent a_event){}
		public void mousePressed(MouseEvent a_event){}
		public void mouseClicked(MouseEvent a_event){}
		
		public void mouseReleased(MouseEvent a_event){
			if(!regionSelected){
				if(arrInitialRegion == null){
					arrInitialRegion = new int[]{a_event.getX(), a_event.getY(),0,0};
				}
				else{
					arrInitialRegion[2] = a_event.getX()-arrInitialRegion[0];
					arrInitialRegion[3] = a_event.getY()-arrInitialRegion[1];
					
					pluginImage.setAttribute("regionPx", arrInitialRegion[0]);
					pluginImage.setAttribute("regionPy", arrInitialRegion[1]);
					pluginImage.setAttribute("regionWidth", arrInitialRegion[2]);
					pluginImage.setAttribute("regionHeight", arrInitialRegion[3]);
					regionSelected = true;
				}	
			}
		}		
	}
}
