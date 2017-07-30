/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.chromaKey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

import org.marvinproject.image.combine.combineByMask.CombineByMask;
import org.marvinproject.image.subtract.Subtract;
import org.marvinproject.image.transform.scale.Scale;

/**
 * Subtract the background and combine other image.
 * @author Gabriel Ambrosio Archanjo
 */
public class ChromaKey extends JFrame implements Runnable{
	
	private JPanel 					panelBottom,
									panelSlider;	
	private JSlider					sliderColorRange;
	private JButton 				buttonCaptureBackground,
									buttonStart;
	private JLabel					labelColorRange;
				
	private MarvinVideoInterface	 videoInterface;
	private MarvinImagePanel 		videoPanel;
	
	private boolean 				playing;
	private boolean 				removeBackground;
	
	private Thread 					thread;
	
	private MarvinImage 			imageIn,
									imageOut,
									imageBackground;
	
	private MarvinImagePlugin 		pluginChroma, 
									pluginCombine;
	
	private int						imageWidth,
									imageHeight;
	
	private int 					colorRange=30;
	
	public ChromaKey(){
		try{
			videoPanel = new MarvinImagePanel();
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
			
			imageWidth = videoInterface.getImageWidth();
			imageHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(imageWidth, imageHeight);
			
			loadGUI();
			
			pluginChroma = new Subtract();
			pluginChroma.load();
			pluginCombine = new CombineByMask();
			pluginCombine.load();
			
			MarvinImage l_imageParadise = MarvinImageIO.loadImage("./res/paradise.jpg");
			Integer cameraWidth = videoInterface.getImageWidth();
			Integer cameraHeight = videoInterface.getImageHeight();
			 		
			MarvinImagePlugin pluginScale = new Scale();
			pluginScale.load();
			pluginScale.setAttribute("newWidth", cameraWidth);
			pluginScale.setAttribute("newHeight", cameraHeight);
			
			MarvinImage l_imageParadiseResize = new MarvinImage(1,1); 
			pluginScale.process(l_imageParadise, l_imageParadiseResize);
			l_imageParadise = l_imageParadiseResize;
			
			pluginCombine.setAttribute("combinationImage", l_imageParadise);
			pluginCombine.setAttribute("colorMask", new Color(0,0,255));
			
			imageBackground = new MarvinImage(cameraWidth, cameraHeight);
			
			thread = new Thread(this);
			thread.start();
			playing = true;
			removeBackground = false;
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){
		setTitle("Chroma Key Sample");
		
		panelBottom = new JPanel();
		
		ButtonHandler l_buttonHandler = new ButtonHandler();
		buttonCaptureBackground = new JButton("Capture Background");
		buttonStart = new JButton("Start");
		buttonStart.setEnabled(false);
		buttonCaptureBackground.addActionListener(l_buttonHandler);
		buttonStart.addActionListener(l_buttonHandler);
		
		sliderColorRange = new JSlider(JSlider.HORIZONTAL, 0, 50, 30);
		sliderColorRange.setMinorTickSpacing(1);
		sliderColorRange.setPaintTicks(true);
		sliderColorRange.addChangeListener(new SliderHandler());
		
		labelColorRange = new JLabel("Color Range");
		
		panelSlider = new JPanel();
		panelSlider.add(labelColorRange);
		panelSlider.add(sliderColorRange);
		
		panelBottom.add(buttonCaptureBackground);
		panelBottom.add(buttonStart);
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.CENTER);
		l_container.add(panelBottom, BorderLayout.SOUTH);
		
		
		setSize(videoInterface.getImageWidth(),videoInterface.getImageHeight()+100);
		setVisible(true);
	}
	
	public void run(){
		try{
			while(true){			
				if(playing)
				{	
					imageIn = videoInterface.getFrame();
					MarvinImage.copyColorArray(imageIn, imageOut);
					
					if(removeBackground){
						pluginChroma.setAttribute("colorRange", colorRange);
						pluginChroma.process(imageIn, imageOut);
						pluginCombine.process(imageOut, imageOut);					
					}
					else{
						MarvinImage.copyColorArray(imageIn, imageOut);
					}
					videoPanel.setImage(imageOut);
				}
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ChromaKey ck = new ChromaKey();
		ck.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent a_event){
			if(a_event.getSource() == buttonCaptureBackground){
				try{
					MarvinImage.copyColorArray(videoInterface.getFrame(), imageBackground);
					pluginChroma.setAttribute("backgroundImage", imageBackground);
					buttonStart.setEnabled(true);
				}
				catch(MarvinVideoInterfaceException e){
					e.printStackTrace();
				}
			}
			else if(a_event.getSource() == buttonStart){
				removeBackground = true;
			}
		}
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			colorRange = (50-sliderColorRange.getValue());
		}
	}

}
