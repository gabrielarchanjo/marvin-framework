/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package video.simpleVideoTest;
import javax.swing.JFrame;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

public class SimpleVideoTest extends JFrame implements Runnable{
	
	private MarvinVideoInterface 	videoAdapter;
	private MarvinImage				image;
	private MarvinImagePanel 		videoPanel;
	
	public SimpleVideoTest(){
		super("Simple Video Test");
	
		try{
			// Create the VideoAdapter and connect to the camera
			videoAdapter = new MarvinJavaCVAdapter();
			videoAdapter.connect(1, 640, 480);
			
			// Create VideoPanel
			videoPanel = new MarvinImagePanel();
			add(videoPanel);
			
			// Start the thread for requesting the video frames 
			new Thread(this).start();
			
			setSize(800,600);
			setVisible(true);
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SimpleVideoTest t = new SimpleVideoTest();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		try{
			while(true){
				// Request a video frame and set into the VideoPanel
				image = videoAdapter.getFrame();
				videoPanel.setImage(image);
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
}


