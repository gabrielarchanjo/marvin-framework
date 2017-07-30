/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.videoFilters;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

import org.marvinproject.image.artistic.television.Television;
import org.marvinproject.image.blur.pixelize.Pixelize;
import org.marvinproject.image.color.grayScale.GrayScale;
import org.marvinproject.image.color.invert.Invert;
import org.marvinproject.image.color.sepia.Sepia;
import org.marvinproject.image.color.thresholding.Thresholding;
import org.marvinproject.image.difference.differenceColor.DifferenceColor;
import org.marvinproject.image.edge.edgeDetector.EdgeDetector;
import org.marvinproject.image.halftone.dithering.Dithering;
import org.marvinproject.image.statistical.maximum.Maximum;
import org.marvinproject.image.statistical.minimum.Minimum;
import org.marvinproject.image.transform.flip.Flip;

/**
 * Using filters with video.
 * @author Gabriel Ambrosio Archanjo
 */
public class VideoFilters extends JFrame implements Runnable{
	
	// GUI
	private JPanel 				panelButton,
								panelWest,
								panelLabels,
								panelCenter;
	
	private JButton 			buttonPlayStop,
								buttonNormal,
								buttonPluginGray,
								buttonPluginSepia,
								buttonPluginInvert,
								buttonPluginPixelize,
								buttonThresholding,
								buttonPluginHalftone,						
								buttonPluginMinimum,
								buttonPluginMaximum,	
								buttonPluginFlip,
								buttonPluginTelevision,
								buttonPluginEdgeDetector,					
								buttonPluginDifference,
								buttonRect;
						
	private JLabel				labelCurrentPlugin,
								labelFPS;						
	
	private MarvinImagePanel 	videoPanel;
	
	private MarvinImage 		imageIn;
	private MarvinImage 		imageOut;
	private MarvinImage 		imageLastFrame;
	private MarvinImageMask		imageMask,
								imageMaskRect;
	
	private Thread 				thread;
	
	private MarvinImagePlugin 	pluginImage;
	
	private int					cameraWidth,
								cameraHeight;
	
	private boolean 			playing;
	private boolean				rect;
	
	private MarvinVideoInterface videoInterface;
	
	public VideoFilters(){
		try{
			videoInterface = new MarvinJavaCVAdapter();
			videoInterface.connect(1);
			
			videoPanel = new MarvinImagePanel();
			cameraWidth = videoInterface.getImageWidth();
			cameraHeight = videoInterface.getImageHeight();
			
			imageOut = new MarvinImage(cameraWidth, cameraHeight);
			
			imageLastFrame = new MarvinImage(cameraWidth,cameraHeight);
			imageMask = MarvinImageMask.NULL_MASK;
			imageMaskRect = new MarvinImageMask(cameraWidth,cameraHeight,cameraWidth/4,cameraHeight/4,cameraWidth/2,cameraHeight/2);
			rect = false;
			
			loadGUI();
			
			thread = new Thread(this);
			thread.start();
			playing = true;
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	private void loadGUI(){
		setTitle("Video Sample - Filters");
		// Labels
		labelCurrentPlugin = new JLabel("Current plug-in: none");
		labelFPS = new JLabel("FPS:   ");
		
		// Buttons
		ButtonHandler l_handler = new ButtonHandler();
		buttonPlayStop = new JButton("Stop");
		buttonNormal = new JButton("Normal");
		buttonPluginGray = new JButton("Gray Scale");
		buttonPluginSepia = new JButton("Sepia");
		buttonPluginInvert = new JButton("Invert Colors");	
		buttonPluginPixelize = new JButton("Pixelize");
		buttonThresholding = new JButton("Thresholding");
		buttonPluginHalftone = new JButton("Halftone");
		buttonPluginMinimum = new JButton("Minimum");
		buttonPluginMaximum = new JButton("Maximum");		
		buttonPluginFlip = new JButton("Flip");
		buttonPluginTelevision = new JButton("Television");
		buttonPluginEdgeDetector = new JButton("Edge Detector");
		buttonPluginDifference = new JButton("Difference");
		buttonRect = new JButton("Create Rect");
		
		buttonPlayStop.addActionListener(l_handler);
		buttonPluginGray.addActionListener(l_handler);
		buttonNormal.addActionListener(l_handler);
		buttonPluginSepia.addActionListener(l_handler);
		buttonPluginInvert.addActionListener(l_handler);
		buttonPluginPixelize.addActionListener(l_handler);
		buttonThresholding.addActionListener(l_handler);
		buttonPluginHalftone.addActionListener(l_handler);		
		buttonPluginMinimum.addActionListener(l_handler);
		buttonPluginMaximum.addActionListener(l_handler);		
		buttonPluginFlip.addActionListener(l_handler);
		buttonPluginTelevision.addActionListener(l_handler);
		buttonPluginEdgeDetector.addActionListener(l_handler);
		buttonPluginDifference.addActionListener(l_handler);
		buttonRect.addActionListener(l_handler);
		
		// Panels
		panelButton = new JPanel();
		panelButton.add(buttonRect);		
		panelButton.add(buttonPlayStop);
		
		panelWest = new JPanel();
		panelWest.setLayout(new GridLayout(15,1));
		panelWest.add(buttonNormal);
		panelWest.add(buttonPluginGray);
		panelWest.add(buttonPluginSepia);
		panelWest.add(buttonPluginInvert);		
		panelWest.add(buttonPluginPixelize);
		panelWest.add(buttonThresholding);
		panelWest.add(buttonPluginHalftone);		
		panelWest.add(buttonPluginMinimum);
		panelWest.add(buttonPluginMaximum);		
		panelWest.add(buttonPluginFlip);
		panelWest.add(buttonPluginTelevision);
		panelWest.add(buttonPluginEdgeDetector);
		panelWest.add(buttonPluginDifference);
		
		panelLabels = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelLabels.add(labelFPS);
		panelLabels.add(labelCurrentPlugin);
		
		panelCenter = new JPanel(new BorderLayout());
		panelCenter.add(videoPanel, BorderLayout.NORTH);
		panelCenter.add(panelLabels, BorderLayout.CENTER);
		panelCenter.add(panelButton, BorderLayout.SOUTH);
		
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(panelCenter, BorderLayout.CENTER);
		container.add(panelWest, BorderLayout.WEST);
		
		setSize(cameraWidth+125,cameraHeight+85);
		setResizable(false);
		setVisible(true);
	}
	
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		try{
			while(true){			
				if(playing)
				{
					ticks++;
					
					if(System.currentTimeMillis() - time > 1000){
						labelFPS.setText("FPS: "+ticks+"       ");
						ticks=0;
						time = System.currentTimeMillis();					
					}
					
					imageIn = videoInterface.getFrame();
					MarvinImage.copyColorArray(imageIn, imageOut);
					
					if(pluginImage == null || rect){
						MarvinImage.copyColorArray(imageIn, imageOut);
					}
				
					if(pluginImage != null){					
						pluginImage.process(imageIn, imageOut, null, imageMask, false);
					}
					imageOut.update();
					videoPanel.setImage(imageOut);
				}
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		VideoFilters l_vs = new VideoFilters();
		l_vs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent a_event){
			if(a_event.getSource() == buttonPlayStop){
				if(playing){
					playing = false;
					buttonPlayStop.setText("Play");					
				}
				else{
					playing = true;
					buttonPlayStop.setText("Stop");
				}
			}
			else if(a_event.getSource() == buttonNormal){
				pluginImage = null;
				labelCurrentPlugin.setText("Current plug-in: none");
			}
			else if(a_event.getSource() == buttonPluginGray){
				pluginImage = new GrayScale();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Gray Scale");
			}
			else if(a_event.getSource() == buttonPluginSepia){
				pluginImage = new Sepia();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Sepia");
			}
			else if(a_event.getSource() == buttonPluginInvert){
				pluginImage = new Invert();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Negative");
			}
			else if(a_event.getSource() == buttonPluginPixelize){
				pluginImage = new Pixelize();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Pixelize");
			}
			else if(a_event.getSource() == buttonThresholding){
				pluginImage = new Thresholding();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Thresholding");
			}
			else if(a_event.getSource() == buttonPluginHalftone){
				pluginImage = new Dithering();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Halftone");
			}
			else if(a_event.getSource() == buttonPluginMinimum){
				pluginImage = new Minimum();
				pluginImage.load();
				pluginImage.setAttribute("size", 2);
				labelCurrentPlugin.setText("Current plug-in: Minimum");
			}
			else if(a_event.getSource() == buttonPluginMaximum){
				pluginImage = new Maximum();
				pluginImage.load();
				pluginImage.setAttribute("size", 2);
				labelCurrentPlugin.setText("Current plug-in: Maximum");
			}
			else if(a_event.getSource() == buttonPluginFlip){
				pluginImage = new Flip();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Flip");
			}
			else if(a_event.getSource() == buttonPluginTelevision){
				pluginImage = new Television();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Television");
			}
			else if(a_event.getSource() == buttonPluginEdgeDetector){
				pluginImage = new EdgeDetector();
				pluginImage.load();
				labelCurrentPlugin.setText("Current plug-in: Edge Detector");
			}	
			else if(a_event.getSource() == buttonPluginDifference){
				pluginImage = new DifferenceColor();
				pluginImage.load();
				pluginImage.setAttribute("comparisonImage", imageLastFrame);
				labelCurrentPlugin.setText("Current plug-in: Difference");
			}
			else if(a_event.getSource() == buttonRect){
				if(rect){
					buttonRect.setText("Create Mask");
					imageMask = MarvinImageMask.NULL_MASK;
				}
				else{
					buttonRect.setText("Remove Mask");
					imageMask = imageMaskRect;
				}
				rect = !rect;
			}
		}
	}
}