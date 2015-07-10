/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.combine.combineByMask;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine two images using a mask color.
 * @author Gabriel Ambrosio Archanjo
 */
public class CombineByMask extends MarvinAbstractImagePlugin{
	
	MarvinAttributes 	attributes;
	
	MarvinImage 		combinationImage;
	
	Color 				colorMask;
	
	private int 		xi=0,
						yi=0;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("xi", xi);
		attributes.set("yi", yi);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){ return null; }
	
	public void process(MarvinImage imageIn, MarvinImage imageOut, MarvinAttributes attributesOut, MarvinImageMask mask, boolean previewMode){
		xi = (Integer)attributes.get("xi");
		yi = (Integer)attributes.get("yi");		
		colorMask = (Color)attributes.get("colorMask");
		combinationImage = (MarvinImage)attributes.get("combinationImage");
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();
			
			
		for(int y=0; y<imageIn.getHeight(); y++){
    		for(int x=0; x<imageIn.getWidth(); x++){
    			
    			l_xCI = x-xi;
    			l_yCI = y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    				if(imageIn.getIntColor(x, y) == colorMask.getRGB()){
    					imageOut.setIntColor(x, y, combinationImage.getIntColor(x, y));
    				}
    				else{
    					imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    				}
    			}
    			else{
    				imageOut.setIntColor(x, y, imageIn.getIntColor(x, y));
    			}
    		}
		}
	}
}
