package org.marvinproject.image.pattern.findTextRegions;

import java.util.ArrayList;
import java.util.List;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.image.MarvinSegment;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.color.thresholding.Thresholding;

public class FindTextRegions extends MarvinAbstractImagePlugin{

	private MarvinImagePlugin threshold;
	
	@Override
	public void load() {
		setAttribute("maxWhiteSpace", 10);
		setAttribute("maxFontLineWidth", 10);
		setAttribute("minTextWidth", 30);
		setAttribute("grayScaleThreshold", 127);
		threshold = new Thresholding();
		threshold.load();
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
		// The image will be affected so it's generated a new instance
		imageIn = imageIn.clone();
		
		int maxWhiteSpace = (Integer)getAttribute("maxWhiteSpace");
		int maxFontLineWidth = (Integer)getAttribute("maxFontLineWidth");
		int minTextWidth = (Integer)getAttribute("minTextWidth");
		int grayScaleThreshold = (Integer)getAttribute("grayScaleThreshold");
		
		threshold.setAttribute("threshold", grayScaleThreshold);
		threshold.process(imageIn, imageIn);
		
		List<List<Integer[]>> segments = new ArrayList<List<Integer[]>>(imageIn.getHeight());
		for(int i=0; i<imageIn.getHeight(); i++){
			segments.add(new ArrayList<Integer[]>());
		}
		
		// map of already processed pixels
		boolean[][] processed = new boolean[imageIn.getWidth()][imageIn.getHeight()];
		
		int color;
		int patternStartX=-1;
		int patternLength=0;
		int whitePixels=0;
		int blackPixels=0;
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				
				if(!processed[x][y]){
					color = imageIn.getIntColor(x, y);
					
					if(color == 0xFFFFFFFF && patternStartX != -1){
						whitePixels++;
						blackPixels=0;
					}
					
					if(color == 0xFF000000){
						
						blackPixels++;
						
						if(patternStartX == -1){
							patternStartX = x;
						}
						
						whitePixels=0;
					}
					
					// check white and black pattern maximum lenghts
					if(whitePixels > maxWhiteSpace || blackPixels > maxFontLineWidth || x == imageIn.getWidth()-1){
						
						if(patternLength >= minTextWidth){
							List<Integer[]> list = segments.get(y);
							list.add(new Integer[]{patternStartX, y, patternStartX+patternLength, y});
						} 
						
						whitePixels=0;
						blackPixels=0;
						patternLength=0;
						patternStartX=-1;
					}
					
					if(patternStartX != -1){
						patternLength++;
					}
					
					processed[x][y] = true;
				}
			}
		}
		
		
		// Group line patterns intersecting in x coordinate and too near in y coordinate.
		for(int y=0; y<imageIn.getHeight()-2; y++){
			
			List<Integer[]> listY = segments.get(y);
			
			for(int w=y+1; w<=y+2; w++){
				
				List<Integer[]> listW = segments.get(w);
				
				for(int i=0; i<listY.size(); i++){
					Integer[] sA = listY.get(i);
					for(int j=0; j<listW.size(); j++){
						
						
						Integer[] sB = listW.get(j);
						
						// horizontal intersection
						if
						(
							(sA[0] <= sB[0] && sA[2] >= sB[2]) ||
							(sA[0] >= sB[0] && sA[0] <= sB[2]) ||
							(sA[2] >= sB[0] && sA[2] <= sB[2])
							
						){
							sA[0] = Math.min(sA[0], sB[0]);
							sA[2] = Math.max(sA[2], sB[2]);
							sA[3] = sB[3];
							
							listY.remove(i);
							i--;
							
							listW.remove(j);
							listW.add(sA);
							
							break;
						}
					}
				}
			}
		}
		
		// Convert the result to a List<> of MarvinSegment objects.
		List<MarvinSegment> marvinSegments = new ArrayList<MarvinSegment>();
		for(int y=0; y<imageIn.getHeight(); y++){
			List<Integer[]> l = segments.get(y);
			for(Integer[] seg:l){
				marvinSegments.add(new MarvinSegment(seg[0], seg[1], seg[2], seg[3]));
			}
		}
		
		attrOut.set("matches", marvinSegments);
	}
}