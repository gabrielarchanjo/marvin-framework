package org.marvinproject.image.color.thresholdingNeighborhood;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class ThresholdingNeighborhood  extends MarvinAbstractImagePlugin{

	@Override
	public void load() {
		setAttribute("neighborhoodSide", 10);
		setAttribute("samplingPixelDistance", 1);
		setAttribute("thresholdPercentageOfAverage", 1.0D);
	}

	
	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}

	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		int neighborhoodSide = (Integer)getAttribute("neighborhoodSide");
		int samplingPixelDistance = (Integer)getAttribute("samplingPixelDistance");
		double thresholdPercentageOfAverage = (Double)getAttribute("thresholdPercentageOfAverage");
		
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				theshold(imageIn, imageOut, x, y, thresholdPercentageOfAverage, neighborhoodSide, samplingPixelDistance);
			}
		}
	}
	
	private void theshold(MarvinImage image, MarvinImage imageOut, int x, int y, double thresholdPercentageOfAverage, int side, int neighborhoodDistance){
		
		int min=-1;
		int max=-1;
		int pixels=0;
		int average=0;
		
		int inc = neighborhoodDistance;
		
		
		for(int j=y-(side/2); j<y+(inc+side/2); j+=inc){
			for(int i=x-(side/2); i<x+(side/2); i+=inc){
				
				if(i >= 0 && j>= 0 && i < image.getWidth() && j < image.getHeight()){
					
					int color = image.getIntComponent0(i,j);
					
					if(min == -1 || color < min){
						min = color;
					}
					if(max == -1 || color > max){
						max = color;
					}
						
					average+=color;
					pixels++;
				}
			}
		}
		
		average /= pixels;
			
		int color = image.getIntComponent0(x,y);
		
		if(color < average*thresholdPercentageOfAverage || (max-min) <= 30){
			imageOut.setIntColor(x, y, 255, 0, 0, 0);
		} else{
			imageOut.setIntColor(x, y, 255, 255, 255, 255);
		}
		
	}
}
