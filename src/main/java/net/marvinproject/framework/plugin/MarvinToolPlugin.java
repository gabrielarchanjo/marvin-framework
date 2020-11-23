/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.plugin;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import net.marvinproject.framework.gui.MarvinPluginWindow;
import net.marvinproject.framework.image.MarvinImage;
import net.marvinproject.framework.image.MarvinImageMask;


public interface MarvinToolPlugin extends MarvinPlugin{
	
	
	public abstract void load();
	/**
	 * 
	 */
	public abstract ImageIcon getIcon();
	
	public abstract Image getCursorImage();
	
	public abstract Point getCursorHotSpot();
	
	/**
	 * 
	 */
	public abstract MarvinPluginWindow getSettingsWindow();
	
	public abstract void mousePressed(MarvinImage a_image, MarvinImageMask a_imageMask, int a_x, int a_y);
	
	public abstract void mouseClicked(MarvinImage a_image, MarvinImageMask a_imageMask, int a_x, int a_y);
	
	public abstract void mouseReleased(MarvinImage a_image, MarvinImageMask a_imageMask, int a_x, int a_y);
	
	public abstract void update(Graphics a_graphics);
	
	/**
	 * 
	 */
	//public abstract void setImagePanel(MarvinImagePanel a_imagePanel);
	
	/**
	 * 
	 */
	//public abstract MarvinImagePanel getImagePanel();
}
