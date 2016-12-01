/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.thresholding;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Thresholding
 * @author Gabriel Ambrosio Archanjo
 */
public class Thresholding extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes		attributes;
	private int 					threshold,
									thresholdRange,
									neighborhood,
									range;
	
	private MarvinImagePlugin pluginGray;
	
	public void load(){
		
		// Attributes
		attributes = getAttributes();
		attributes.set("threshold", 125);
		attributes.set("thresholdRange", -1);
		attributes.set("neighborhood", -1);
		attributes.set("range", -1);
		
		pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale.jar");
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblThreshold", "Threshold");
			attributesPanel.addTextField("txtThreshold", "threshold", attributes);		
			attributesPanel.addLabel("lblNeighborhood", "Neighborhood");
			attributesPanel.addTextField("txtNeighborhood", "neighborhood", attributes);
			attributesPanel.addLabel("lblRange", "Range");
			attributesPanel.addTextField("txtRange", "range", attributes);
		}
		return attributesPanel;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		threshold = (Integer)attributes.get("threshold");
		thresholdRange = (Integer)attributes.get("thresholdRange");
		neighborhood = (Integer)attributes.get("neighborhood");
		range = (Integer)attributes.get("range");
		
		if(thresholdRange == -1){
			thresholdRange = 255-threshold;
		}
		
		pluginGray.process(imageIn, imageOut, attributesOut, mask, previewMode);
		
		boolean[][] bmask = mask.getMask();
		
		if(neighborhood == -1 && range == -1){
			hardThreshold(imageIn, imageOut, bmask);
		}
		else{
			contrastThreshold(imageIn, imageOut);
		}
				
	}
	
	private void hardThreshold(MarvinImage imageIn, MarvinImage imageOut, boolean[][] mask){
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(mask != null && !mask[x][y]){
					continue;
				}
				
				int gray = imageIn.getIntComponent0(x,y); 
				if(gray < threshold || gray > threshold+thresholdRange){
					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x,y), 0,0,0);
				}
				else{
					imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x,y), 255,255,255);
				}				
			}
		}	
	}
	
	private void contrastThreshold
	(
		MarvinImage imageIn,
		MarvinImage imageOut
	){
		range = 1;
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(checkNeighbors(x,y, neighborhood, neighborhood, imageIn)){
					imageOut.setIntColor(x,y,0,0,0);
				}
				else{
					imageOut.setIntColor(x,y,255,255,255);
				}
			}
		}
	}
	
	private boolean checkNeighbors(int x, int y, int neighborhoodX, int neighborhoodY, MarvinImage img){
		
		int color;
		int z=0;
		
		color = img.getIntComponent0(x, y);
		
		for(int i=0-neighborhoodX; i<=neighborhoodX; i++){
			for(int j=0-neighborhoodY; j<=neighborhoodY; j++){
				if(i == 0 && j == 0){
					continue;
				}
				
				if(color < getSafeColor(x+i,y+j, img)-range && getSafeColor(x+i,y+j, img) != -1){
					z++;
				}
			}
		}
		
		if(z > (neighborhoodX*neighborhoodY)*0.5){
			return true;
		}
		
		return false;
	}
	
	private int getSafeColor(int x, int y, MarvinImage img){
		
		if(x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()){
			return img.getIntComponent0(x, y);
		}
		return -1;
	}
}
