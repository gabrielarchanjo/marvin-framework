package org.marvinproject.image.color.blackAndWhite;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.color.grayScale.GrayScale;

public class BlackAndWhite extends MarvinAbstractImagePlugin{

	private final static double MAX_RLEVEL = 0.03;
	private MarvinImagePlugin grayScale;
	
	@Override
	public void load() {
		grayScale = new GrayScale();
		grayScale.load();
		setAttribute("level", 10);
	}
	
	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {
		grayScale.process(imageIn, imageOut);
		int level = (Integer)getAttribute("level");
		double rlevel = (level/100.0)*MAX_RLEVEL;
		
		int gray;
		for(int y=0; y<imageOut.getHeight(); y++){
			for(int x=0; x<imageOut.getWidth(); x++){
				gray = imageIn.getIntComponent0(x, y);
				
				
				if(gray <= 127){
					gray = (int)Math.max((gray * (1 - ((127-gray)*rlevel))),0);
				}
				else{
					gray = (int)Math.min(gray* (1+((gray-127)*rlevel)), 255);
				}
				
				imageOut.setIntColor(x, y, gray, gray, gray);
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
