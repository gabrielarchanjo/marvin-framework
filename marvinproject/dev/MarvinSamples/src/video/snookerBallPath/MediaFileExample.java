/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.snookerBallPath;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

public class MediaFileExample extends JFrame implements Runnable{

	private MarvinVideoInterface 	videoAdapter;
	private MarvinImage				imageIn, imageOut, imageBuffer;
	private MarvinImagePanel 		videoPanelLeft, videoPanelRight;
	
	private static FlowLayout		flowLayout = new FlowLayout(FlowLayout.CENTER);
	public MediaFileExample(){
		super("Media File Example");
	
		try{
			// Create the VideoAdapter used to load the video file
			videoAdapter = new MarvinJavaCVAdapter();
			videoAdapter.loadResource("./res/snooker.wmv");
			
			imageOut = new MarvinImage(videoAdapter.getImageWidth(), videoAdapter.getImageHeight());
			imageBuffer = new MarvinImage(videoAdapter.getImageWidth(), videoAdapter.getImageHeight());
			
			// Set up the Graphical User Interface
			loadGUI();
			
			// Start the thread for requesting the video frames 
			new Thread(this).start();
			
			setSize((videoAdapter.getImageWidth()*2)+10,videoAdapter.getImageHeight()+80);
			setVisible(true);
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MediaFileExample m = new MediaFileExample();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		try{
			while(true){
				// Request a video frame
				imageIn = videoAdapter.getFrame();
				
				if(imageIn != null){
					// Post the original frame in the left video panel
					videoPanelLeft.setImage(imageIn);
					// Image processing
					processImage(imageIn, imageOut);
					// Post the processed image in the right video panel
					videoPanelRight.setImage(imageOut);
				} else{
					break;
				}
				try{Thread.sleep(30);}catch(Exception e){}
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Just buffer every pixels that are not green
	 * @param imgIn
	 * @param imgOut
	 */
	int fc=0;
	private void processImage(MarvinImage imgIn, MarvinImage imgOut){
		if(++fc == 8){
			for(int y=0; y<imgIn.getHeight(); y++){
				for(int x=0; x<imgIn.getWidth(); x++){
					
					int red = imgIn.getIntComponent0(x, y);
					int green = imgIn.getIntComponent1(x, y);
					int blue = imgIn.getIntComponent2(x, y);
					
					// Is it not green?
					if
					(!(green > 120 && green > red * 1.4 && blue < 50)){
						imageBuffer.setIntColor(x, y, 255, red, green, blue);
					}
				}
			}
			fc=0;
		}
		
		for(int y=0; y<imgIn.getHeight(); y++){
			for(int x=0; x<imgIn.getWidth(); x++){
				if(imageBuffer.getAlphaComponent(x, y) == 255){
					imgOut.setIntColor(x, y, imageBuffer.getIntColor(x, y));
				}
				else{
					imgOut.setIntColor(x, y, imageIn.getIntColor(x, y));
				}
			}
		}
	}
	
	private void loadGUI(){
		// Create VideoPanel
		videoPanelLeft 	= new MarvinImagePanel();
		videoPanelRight	= new MarvinImagePanel();
		
		setLayout(new BorderLayout());
		add(videoPanelLeft, BorderLayout.WEST);
		add(videoPanelRight, BorderLayout.EAST);
		
		// Panel
		GridLayout grid = new GridLayout(1,2);
		JPanel panelTop = new JPanel(grid);
		JPanel p1 = new JPanel(flowLayout);
		p1.add(new JLabel("ORIGINAL"));
		JPanel p2 = new JPanel(flowLayout);
		p2.add(new JLabel("PROCESSED"));
		panelTop.add(p1);
		panelTop.add(p2);
		add(panelTop, BorderLayout.NORTH);
	}
}
