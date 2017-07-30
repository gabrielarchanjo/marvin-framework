/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package image.multithread;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.thread.MarvinThread;
import marvin.thread.MarvinThreadEvent;
import marvin.thread.MarvinThreadListener;
import marvin.util.MarvinPluginLoader;

import org.marvinproject.image.statistical.maximum.Maximum;

/**
 * Processing images single and multi threaded.
 * @author Gabriel Ambrósio Archanjo
 */
public class Multithread extends JFrame implements MarvinThreadListener{
	
	// Interface Components
	private JButton				buttonSingleThread,
								buttonMultiThread,
								buttonResetImage;
	
	private JLabel				labelPerformance;
	
	private MarvinImagePanel 	imagePanel;
	private MarvinImage 		imageIn, imageOut, originalImage;
	
	private int					threadsFinished;
	private long				processStartTime;
		
	public Multithread(){
		super("Multithread Sample");
		
		// Buttons
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonSingleThread = new JButton("Single Thread");
		buttonMultiThread = new JButton("Multi Thread");
		buttonResetImage = new JButton("Reset Image");
		
		buttonSingleThread.addActionListener(buttonHandler);
		buttonMultiThread.addActionListener(buttonHandler);
		buttonResetImage.addActionListener(buttonHandler);
		
		// Label
		labelPerformance = new JLabel("Performance:");
		
		// Panels
		JPanel l_panelIntern = new JPanel();
		l_panelIntern.add(buttonSingleThread);
		l_panelIntern.add(buttonMultiThread);
		l_panelIntern.add(buttonResetImage);
		
		JPanel l_panelBottom = new JPanel();
		l_panelBottom.setLayout(new BorderLayout());
		
		l_panelBottom.add(l_panelIntern, BorderLayout.NORTH);
		l_panelBottom.add(labelPerformance, BorderLayout.SOUTH);
		
		imagePanel = new MarvinImagePanel();
				
		// Container
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(imagePanel, BorderLayout.NORTH);
		l_container.add(l_panelBottom, BorderLayout.SOUTH);
		
		// Load Image
		loadImages();
		
		setSize(originalImage.getWidth()+20,690);
		setVisible(true);
	}
	
	private void loadImages(){
		originalImage = MarvinImageIO.loadImage("./res/senna.jpg");
		imageIn = new MarvinImage(originalImage.getWidth(), originalImage.getHeight());
		imageOut = new MarvinImage(originalImage.getWidth(), originalImage.getHeight());
		imagePanel.setImage(originalImage);
	}
		
	public void threadFinished(MarvinThreadEvent e){
		threadsFinished++;		
		if(threadsFinished == 2){
			imageOut.update();
			imagePanel.setImage(imageOut);
			labelPerformance.setText("Performance: "+ (System.currentTimeMillis()-processStartTime)+ " milliseconds (Multi Thread)");
			repaint();
		}
	}
	
	public static void main(String args[]){
		Multithread t = new Multithread();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void singleThread(){
		processStartTime = System.currentTimeMillis();
		MarvinImagePlugin l_pluginImage = new Maximum();
		l_pluginImage.load();
		l_pluginImage.process(imageIn, imageOut);
		imageOut.update();
		imagePanel.setImage(imageOut);
		labelPerformance.setText("Performance: "+ (System.currentTimeMillis()-processStartTime)+ " milliseconds (Single Thread)");
		repaint();		
	}
	
	private void multiThread(){
		processStartTime = System.currentTimeMillis();
		
		// Load Plug-ins
		MarvinImagePlugin l_pluginImage_1 = new Maximum();
		MarvinImagePlugin l_pluginImage_2 = new Maximum();
		l_pluginImage_1.load();
		l_pluginImage_2.load();
		
		// Create masks
		MarvinImageMask l_mask1 = new MarvinImageMask
		(
			imageIn.getWidth(),			// width
			imageIn.getHeight(),		// height
			0,							// x-start
			0,							// y-start
			imageIn.getWidth(),			// region´s width
			imageIn.getHeight()/2		// region´s height
		);
		
		MarvinImageMask l_mask2 = new MarvinImageMask
		(
			imageIn.getWidth(),			// width
			imageIn.getHeight(),		// height
			0,							// x-start
			imageIn.getHeight()/2,		// y-start
			imageIn.getWidth(),			// region´s width
			imageIn.getHeight()/2		// region´s height
		);
		
		
		MarvinThread l_marvinThread_1 = new MarvinThread(l_pluginImage_1, imageIn, imageOut, l_mask1);
		MarvinThread l_marvinThread_2 = new MarvinThread(l_pluginImage_2, imageIn, imageOut, l_mask2);
		
		l_marvinThread_1.addThreadListener(this);
		l_marvinThread_2.addThreadListener(this);
		
		l_marvinThread_1.start();
		l_marvinThread_2.start();
		
		threadsFinished = 0;
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			imageIn = originalImage.clone();
			if(e.getSource() == buttonSingleThread){
				singleThread();
			}
			else if(e.getSource() == buttonMultiThread){
				multiThread();
			}
			else if(e.getSource() == buttonResetImage){
				imagePanel.setImage(originalImage);
			}
		}
	}
}
