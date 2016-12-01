package org.marvinproject.image.pattern.findSubimage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class FindSubimage extends MarvinAbstractImagePlugin{

	@Override
	public void load() {
		setAttribute("similarity", 1.0);
		setAttribute("findAll", true);
		setAttribute("startX", 0);
		setAttribute("startY", 0);
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
	) {
		List<MarvinSegment> segments = new ArrayList<MarvinSegment>();
		Object o = getAttribute("subimage");
		Double similarity = (Double)getAttribute("similarity");
		Boolean findAll = (Boolean)getAttribute("findAll");
		Integer startX = (Integer)getAttribute("startX");
		Integer startY = (Integer)getAttribute("startY");
		if(o != null){
			MarvinImage subimage = (MarvinImage)o;
			int subImagePixels = subimage.getWidth()*subimage.getHeight();
			boolean[][] processed=new boolean[imageIn.getWidth()][imageIn.getHeight()];
			
			int r1,g1,b1,r2,g2,b2;
			// Full image
			mainLoop:for(int y=startY; y<imageIn.getHeight(); y++){
				for(int x=startX; x<imageIn.getWidth(); x++){
					
					if(processed[x][y]){
						continue;
					}
					
					int notMatched=0;
					boolean match=true;
					// subimage
					if(y+subimage.getHeight() < imageIn.getHeight() && x+subimage.getWidth() < imageIn.getWidth()){
					
						
						outerLoop:for(int i=0; i<subimage.getHeight(); i++){
							for(int j=0; j<subimage.getWidth(); j++){
								
								if(processed[x+j][y+i]){
									match=false;
									break outerLoop;
								}
								
								r1 = imageIn.getIntComponent0(x+j, y+i);
								g1 = imageIn.getIntComponent1(x+j, y+i);
								b1 = imageIn.getIntComponent2(x+j, y+i);
								
								r2 = subimage.getIntComponent0(j, i);
								g2 = subimage.getIntComponent1(j, i);
								b2 = subimage.getIntComponent2(j, i);
								
								if
								(
									Math.abs(r1-r2) > 5 ||
									Math.abs(g1-g2) > 5 ||
									Math.abs(b1-b2) > 5
								){
									notMatched++;
									
									if(notMatched > (1-similarity)*subImagePixels){
										match=false;
										break outerLoop;
									}
								}
							}
						}
					} else{
						match=false;
					}
					
					if(match){
						segments.add(new MarvinSegment(x,y,x+subimage.getWidth(), y+subimage.getHeight()));
						
						if(!findAll){
							break mainLoop;
						}
						
						for(int i=0; i<subimage.getHeight(); i++){
							for(int j=0; j<subimage.getWidth(); j++){
								processed[x+j][y+i]=true;
							}
						}
						
					}
				}
			}
		}
		
		if(attrOut != null){
			attrOut.set("matches", segments);
		}
	}
}
