/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import marvin.image.MarvinImage;
import marvin.util.MarvinPluginHistory;

/**
 * Panel to display MarvinImages.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImagePanel extends JPanel{
	
	protected MarvinImage 			image;
	protected MarvinPluginHistory 	history;
	private boolean					fitSizeToImage;
	private int						width;
	private int						height;
	
	/**
	 * Constructor
	 */
	public MarvinImagePanel(){
		super();
		fitSizeToImage = true;
	}
	
	/**
	 * Enable history
	 */
	public void enableHistory(){
		history = new MarvinPluginHistory();
	}
	
	/**
	 * Disable history
	 */
	public void disableHistory(){
		history = null;
	}
	
	/**
	 * Returns if the history is enabled.
	 * @return true if the history is enabled, false otherwise
	 */
	public boolean isHistoryEnabled(){
		return (history != null);
	}
	
	/**
	 * Returns the MarvinPluginHistory associated with this panel.
	 * @return MarvinPluginHistory reference
	 */
	public MarvinPluginHistory getHistory(){
		return history;
	}
	
	/**
	 * Instantiates the MarvinImage object and returns its BufferedImage as off-screen 
	 * drawable image to be used for double buffering. 
	 * @param width 	image큦 width
	 * @param height	image큦 width
	 */
	public Image createImage(int width, int height){
		image = new MarvinImage(width, height);		
		setPreferredSize(new Dimension(width, height));		
		return image.getBufferedImage();
	}
	
	/**
	 * Associates a MarvinImage to the image panel.
	 * @param img	image큦 reference to be associated with the image panel.
	 */
	public void setImage(MarvinImage img){
		img.update();
		image = img;
		if(fitSizeToImage && img != null && this.width != image.getWidth() && this.height != image.getHeight()){
			this.width = image.getWidth();
			this.height = image.getHeight();
			Dimension d = new Dimension(this.width, this.height);
			setSize(d);
			setPreferredSize(d);
			validate();
		}
		repaint();
	}
	
	
	/**
	 * Returns the MarvinImage associated with this panel.
	 * @return MarvinImage reference.
	 */
	public MarvinImage getImage(){
		return image;
	}
	
	/**
	 * Overwrite the paint method
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(image != null){
			g.drawImage(image.getBufferedImage(), 0,0,this);
		}
	}
	
	/**
	 * Update component큦 graphical representation
	 */
	public void update(){
		image.update();
		repaint();
	}
}
