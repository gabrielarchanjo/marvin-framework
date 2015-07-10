/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.plugin;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import marvin.gui.MarvinImagePanel;
import marvin.gui.MarvinPluginWindow;

public abstract class MarvinAbstractToolPlugin extends MarvinAbstractPlugin implements MarvinToolPlugin{

	private MarvinImagePanel	imagePanel;
	
	private ImageIcon 			imageIcon;
	private Image				cursorImage;
	
	private Point				cursorHotSpot;
	
	/**
	 * Protected Contructor
	 */
	protected void loadIcon
	(
		String a_pathIcon
	){
		imageIcon = new ImageIcon(loadImage(a_pathIcon));
	}
	
	protected void loadCursor(String pathCursorImage){
		loadCursor(pathCursorImage, new Point(0,0));
	}
	
	protected void loadCursor(String pathCursorImage,Point a_cursorHS){
		cursorImage = loadImage(pathCursorImage);
		cursorHotSpot = a_cursorHS;
	}

	
	/**
	 * Load image
	 * @param a_path
	 * @return
	 */
	protected Image loadImage(String a_path){
		URL url =null;
		Image img=null;
		
		try{
			url = new URL((this.getClass().getResource("").toString()+a_path));
			img = new ImageIcon(url).getImage();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return img;
	}
	
	public ImageIcon getIcon(){
		return imageIcon;
	}
	
	public Image getCursorImage(){
		return cursorImage;
	}
	
	public Point getCursorHotSpot(){
		return cursorHotSpot;
	}
}
