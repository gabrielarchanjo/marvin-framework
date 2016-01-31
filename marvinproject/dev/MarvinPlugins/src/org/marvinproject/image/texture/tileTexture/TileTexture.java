/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.texture.tileTexture;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class TileTexture extends MarvinAbstractImagePlugin{

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;
	private MarvinImagePlugin 		flip;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("tile", -1);
		attributes.set("lines", 2);
		attributes.set("columns", 2);
		
		flip = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.transform.flip.jar");
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblLines", "lines");
			attributesPanel.addTextField("txtLines", "lines", attributes);
			attributesPanel.newComponentRow();
			attributesPanel.addLabel("lblColumns", "columns");
			attributesPanel.addTextField("txtColumns", "columns", attributes);
		}
		return attributesPanel;
	}
    
    public void process
    (
    	MarvinImage imgIn, 
    	MarvinImage imgOut, 
    	MarvinAttributes attrOut, 
    	MarvinImageMask mask,
    	boolean previewMode
    )
    {
    	int lines = (Integer)attributes.get("lines");
    	int columns = (Integer)attributes.get("columns");
    	MarvinImage tile = (MarvinImage)attributes.get("tile");
    	int tileWidth = tile.getWidth();
    	int tileHeight = tile.getHeight();
    	
    	boolean[][] arrMask = mask.getMask();
    	if(arrMask != null){
    		MarvinImage.copyColorArray(imgIn, imgOut);
    	}
    	else{
    		imgOut.resize(tile.getWidth()*columns, tile.getHeight()*lines);
    	}
    	
    	MarvinImage tileFlippedH = new MarvinImage(tileWidth, tileHeight);
    	MarvinImage tileFlippedV = new MarvinImage(tileWidth, tileHeight);
    	MarvinImage tileFlippedHV = new MarvinImage(tileWidth, tileHeight);
    	
    	flip.setAttribute("flip", "horizontal");
    	flip.process(tile, tileFlippedH);
    	flip.process(tile, tileFlippedHV);
    	
    	flip.setAttribute("flip", "vertical");
    	flip.process(tile, tileFlippedV);
    	flip.process(tileFlippedHV, tileFlippedHV);
    	    	
    	for(int y=0; y<lines; y++){
    		for(int x=0; x<columns; x++){
    			if(x % 2 == 0 && y % 2 == 0){
    				copyImage(tile, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else if(y % 2 == 0){
    				copyImage(tileFlippedH, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else if(x % 2 == 0){
    				copyImage(tileFlippedV, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    			else{
    				copyImage(tileFlippedHV, imgOut, x*tileWidth, y*tileHeight, arrMask);
    			}
    		}
    		
    		
    	}
    }
    
    private void copyImage(MarvinImage tile, MarvinImage imgOut, int x, int y, boolean mask[][]){
    	for(int j=0; j<tile.getHeight(); j++){
    		for(int i=0; i<tile.getWidth(); i++){
    			if(x+i < imgOut.getWidth() && y+j < imgOut.getHeight() && (mask == null || mask[x+i][y+j])){
    				imgOut.setIntColor(i+x, j+y, tile.getIntColor(i, j));
    			}
    		}
    	}
    }
}
