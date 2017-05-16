package org.marvinproject.image.color.alphaBoundary;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class AlphaBoundary extends MarvinAbstractImagePlugin{

	
	@Override
	public void load(){
		setAttribute("radius", 5);
	}
	
	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
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
		int neighborhood = (Integer)getAttribute("radius");
		for(int y=0; y<imageOut.getHeight(); y++){
			for(int x=0; x<imageOut.getWidth(); x++){
				alphaRadius(imageOut, x, y, neighborhood);
			}
		}
	}
	
	private void alphaRadius(MarvinImage image, int x, int y, int radius){
		
		int oldAlpha = image.getAlphaComponent(x, y);
		int newAlpha;
		int totalAlpha=0;
		int totalPixels=0;
		int hn = radius/2;
		
		for(int j=y-hn; j<y+hn; j++){
			for(int i=x-hn; i<x+hn; i++){
				
				if(i >= 0 && i< image.getWidth() && j >= 0 && j < image.getHeight()){
					totalAlpha += image.getAlphaComponent(i, j);
					totalPixels++;
				}
			}
		}
		
		newAlpha = totalAlpha/totalPixels;
		
		if(newAlpha < oldAlpha)
		image.setAlphaComponent(x, y, newAlpha);
	}
}
