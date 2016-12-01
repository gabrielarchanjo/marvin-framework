/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.transform.scale;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Simple and fast scale based on nearest neighbors
 * @author Gabriel Ambrósio Archanjo
 */
public class Scale extends MarvinAbstractImagePlugin{
	
	private MarvinAttributesPanel	attributesPanel;
	private int 					width;
	private int 					height;
	private int 					newWidth;
	private int 					newHeight;
	
	
	private MarvinAttributes 	attributes;
	
	public void load(){
		attributes = getAttributes();
		newWidth = 0;
		newHeight = 0;
		attributes.set("newWidth", newWidth);
		attributes.set("newHeight", newHeight);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblWidth", "Width:");
			attributesPanel.addTextField("txtWidth", "newWidth", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblHeight", "Height:");
			attributesPanel.addTextField("txtHeight", "newHeight", attributes);
		}
		return attributesPanel;
		
	}
			
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean previewMode
	){
		
		if(!previewMode){
			width = a_imageIn.getWidth();
			height = a_imageIn.getHeight();
			newWidth = (Integer)attributes.get("newWidth");
			newHeight = (Integer)attributes.get("newHeight");
			
			if(a_imageOut.getWidth() != newWidth || a_imageOut.getHeight() != newHeight){
				a_imageOut.setDimension(newWidth, newHeight);
			}
			
		    int x_ratio = (int)((width<<16)/newWidth) ;
		    int y_ratio = (int)((height<<16)/newHeight) ;
		    int x2, y2 ;
		    for (int i=0;i<newHeight;i++) {
		        for (int j=0;j<newWidth;j++) {
		            x2 = ((j*x_ratio)>>16) ;
		            y2 = ((i*y_ratio)>>16) ;
		            a_imageOut.setIntColor(j,i, a_imageIn.getAlphaComponent(x2,y2), a_imageIn.getIntColor(x2,y2));
		        }                
		    }	    
		}
	}
}