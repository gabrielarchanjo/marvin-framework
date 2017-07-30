/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package image.loadAndSave;

import javax.swing.JFrame;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;

import org.marvinproject.image.color.invert.Invert;

/**
 * Load and save sample.
 * @author Gabriel Ambrosio Archanjo
 */
public class LoadAndSave extends JFrame
{
	public LoadAndSave()
	{
		super("Load and Save Sample");
		process();		
		setSize(800,600);
		setVisible(true);		
	}
	
	private void process(){
		// 1. Load Image
		MarvinImage l_image;
		l_image = MarvinImageIO.loadImage("./res/arara.jpg");
		
		MarvinImagePlugin l_pluginImage = new Invert();
		l_pluginImage.load();
		l_pluginImage.process(l_image, l_image);
		l_image.update();
		
		// 2. Load plug-in
		if(l_image != null){
			MarvinImageIO.saveImage(l_image, "./res/araraNegative.jpg");
		}		
	}
	
	public static void main(String args[]){
		LoadAndSave las = new LoadAndSave();
		las.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}