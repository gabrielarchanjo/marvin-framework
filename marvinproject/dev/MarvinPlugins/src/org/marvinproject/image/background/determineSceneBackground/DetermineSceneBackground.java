package org.marvinproject.image.background.determineSceneBackground;

import java.util.ArrayList;
import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class DetermineSceneBackground extends MarvinAbstractImagePlugin{

	private int threshold;
	
	@Override
	public void load() {
		setAttribute("threshold", 30);
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
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {}
	
	
	@Override
	public void process
	(
		List<MarvinImage> images,
		MarvinImage imageOut
	){
		threshold = (Integer)getAttribute("threshold");
		MarvinImage image0 = images.get(0);
		for(int y=0; y<image0.getHeight(); y++){
			for(int x=0; x<image0.getWidth(); x++){
				imageOut.setIntColor(x, y, getBackgroundPixel(x,y, images, threshold));
			}
		}	
	}
	
	private int getBackgroundPixel(int x, int y, List<MarvinImage> images, int threshold){
		List<Integer[]> colors = new ArrayList<Integer[]>();
		for(MarvinImage img:images){
			
			Integer[] c = new Integer[4];
			c[0] = img.getIntComponent0(x, y);
			c[1] = img.getIntComponent1(x, y);
			c[2] = img.getIntComponent2(x, y);
			c[3] = 0;
			
			if(colors.isEmpty()){
				colors.add(c);
			}
			else{
				boolean found=false;
				for(Integer[] c2:colors){
					
					if
					(
						Math.abs(c2[0]-c[0]) < threshold*0.3 &&
						Math.abs(c2[1]-c[1]) < threshold*0.3 &&
						Math.abs(c2[2]-c[2]) < threshold*0.3
					){
						c2[0] = (c2[0]+c[0])/2;
						c2[1] = (c2[1]+c[1])/2;
						c2[2] = (c2[2]+c[2])/2;
						c2[3]++;
						found=true;
						break;
					}
				}
				
				if(!found){
					colors.add(c);
				}
			}
		}
		
		int max=-1;
		int maxIndex=0;
		Integer[] c2 = null;
		for(int i=0; i<colors.size(); i++){
			c2 = colors.get(i);
			if(max == -1 || c2[3] > max){
				max = c2[3];
				maxIndex = i; 
			}
		}
		c2 = colors.get(maxIndex);
		return 0xFF + (c2[0] << 16) + (c2[1] << 8) + c2[2]; 
	}
}
