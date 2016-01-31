/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.gui;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinToolPlugin;

/**
 * Panel to display and edit MarvinImages.
 * @authors Gabriel Ambrosio Archanjo
 */
public class MarvinEditableImagePanel extends MarvinImagePanel implements Runnable{
	
	private MarvinToolPanel		toolPanel;
	private MarvinImageMask		imageMask;
	
	private MarvinToolPlugin	tempTool;
	private Thread				thread;
	
	// Mouse attrbutes
	private MouseEvent			mouseEvent;
	private int					mousePx,
								mousePy;
	private boolean				pressed;
	
	// Event Handlers
	private MouseHandler 		mouseHandler;
	
	private Point				locationOnScreen;
	/**
	 * Constructor
	 */
	public MarvinEditableImagePanel(){
		super();
		
		imageMask = new MarvinImageMask();
		
		mouseHandler = new MouseHandler();
		
		addMouseListener(mouseHandler);
		
		pressed = false;
	}
	
	public void setImage(MarvinImage img){
		super.setImage(img);
		imageMask = new MarvinImageMask(img.getWidth(), img.getHeight());	
	}
	
	/**
	 * Associate a MarvinToolPanel with this MarvinImagePanel.
	 * @param tp MarvinToolPanel Object.
	 */
	public void setToolPanel(MarvinToolPanel tp){
		if(toolPanel != tp){
			toolPanel = tp;
			toolPanel.setImagePanel(this);
			
			if(thread == null){
				thread = new Thread(this);
				thread.start();
			}
		}
	}
	
	/**
	 * Return the associated MarvinToolPanel object.
	 * @return toolPanel reference.
	 */
	public MarvinToolPanel getToolPanel(){
		return toolPanel;
	}
	
	/**
	 * Runnable run() method implementation.
	 */
	
	public void run(){
		while(true){
			if(tempTool != null){
				locationOnScreen = getLocationOnScreen();
				mousePx = (int)MouseInfo.getPointerInfo().getLocation().getX();
				mousePy = (int)MouseInfo.getPointerInfo().getLocation().getY();
				mousePx -= locationOnScreen.x;
				mousePy -= locationOnScreen.y;
				
				
				if(pressed){	
					tempTool.mousePressed(image, imageMask, mousePx, mousePy);
					image.update();
				}
								
				update();				
			}			
		}
	}
	
	public void paintComponent(Graphics a_graphics){
		super.paintComponent(a_graphics);

		if(tempTool != null){
			tempTool.update(a_graphics);
		}
		//if(bufferedImage != null){
		//	getGraphics().drawImage(bufferedImage, 0,0,this);
		//}
	}
	
	private void deleteSelected(){
		boolean[][] mask = imageMask.getMask();
		for(int y=0; y<image.getHeight(); y++){
			for(int x=0; x<image.getWidth(); x++){
				if(!mask[x][y]){
					continue;
				}
				image.setIntColor(x, y, 0xFFFFFFFF);
			}
		}
		
		// Update image and ImagePanel
		image.update();
		update();
		// Clear mask for a new selection
		//imageMask.clear();
		
	}
	
	private class MouseHandler implements MouseListener{
		
		public void mouseClicked(MouseEvent event){
			toolPanel.getCurrentTool().mouseClicked(image, imageMask, event.getX(), event.getY());		
		}

		public void mouseEntered(MouseEvent event) {}

		public void mouseExited(MouseEvent event) {}

		public void mousePressed(MouseEvent event) {
			mouseEvent = event;
			if(toolPanel != null){
				tempTool = toolPanel.getCurrentTool();
			}
			pressed = true;
		}

		public void mouseReleased(MouseEvent event){
			toolPanel.getCurrentTool().mouseReleased(image, imageMask, mousePx, mousePy);
			pressed = false;
		}
	}
}
