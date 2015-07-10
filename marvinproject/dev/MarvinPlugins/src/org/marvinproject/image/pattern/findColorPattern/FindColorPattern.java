/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.pattern.findColorPattern;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class FindColorPattern extends MarvinAbstractImagePlugin{

	private int[]				arrTargetPattern,
								arrTempPattern;
	
	private int					patternWidth,
								patternHeight;
	
	private int					regionPx,
								regionPy,
								regionWidth,
								regionHeight;
	
	private MarvinImage			image;
	
	private int					imageWidth,
								imageHeight;
	
	private int					colorRange;
	
	private MarvinAttributes	attributes;
	
	private boolean				targetPatternLoaded=false;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("differenceColorRange", 30);
		attributes.set("regionPx", 0);
		attributes.set("regionPy", 0);
		attributes.set("regionWidth", 1);
		attributes.set("regionHeight", 1);
	}

	public MarvinAttributesPanel getAttributesPanel(){return null;}
	
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut, 
		MarvinImageMask a_mask, 
		boolean a_previewMode
	) 
	{
		image = a_imageIn;
		if(!targetPatternLoaded){
			
			regionPx = (Integer)attributes.get("regionPx");
			regionPy = (Integer)attributes.get("regionPy");
			regionWidth = (Integer)attributes.get("regionWidth");
			regionHeight = (Integer)attributes.get("regionHeight");
			
			loadTargetPattern(regionPx, regionPy, regionWidth, regionHeight);			
			targetPatternLoaded = true;
		}
		else{
			colorRange = (Integer)attributes.get("differenceColorRange");
			imageWidth = a_imageIn.getWidth();
			imageHeight = a_imageIn.getHeight();		
			
			
			int l_arrRegion[] = new int[]{regionPx, regionPy, regionWidth, regionHeight};
			
			newRegion(l_arrRegion, regionWidth/10,0,4);
			newRegion(l_arrRegion, regionWidth/20,0,5);
			newRegion(l_arrRegion, 2,0,20);
			newRegionSize(l_arrRegion, 0,8);
			
			regionPx = l_arrRegion[0];
			regionPy = l_arrRegion[1];
			regionWidth = l_arrRegion[2];
			regionHeight = l_arrRegion[3];
		}
		
		// Set output attributes
		a_attributesOut.set("regionPx", regionPx);
		a_attributesOut.set("regionPy", regionPy);
		a_attributesOut.set("regionWidth", regionWidth);
		a_attributesOut.set("regionHeight", regionHeight);
	}
	
	private void loadTargetPattern(int a_x, int a_y, int a_width, int a_height){
		regionPx = a_x;
		regionPy = a_y;
		regionWidth = a_width;
		regionHeight = a_height;
		
		patternWidth = a_width/2;
		patternHeight = a_height/2;
		
		arrTargetPattern = new int[patternWidth*patternHeight];
		arrTempPattern = new int[patternWidth*patternHeight];
		
		captureObject(regionPx, regionPy, regionWidth, regionHeight);
	}
	
	private void captureObject(int a_x, int a_y, int a_width, int a_height){
		double l_xFactor = (double)a_width/patternWidth;
		double l_yFactor = (double)a_height/patternHeight;
		
		double l_dX=a_x,l_dY=a_y;
		int l_iX,l_iY;
		
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			l_dY+=l_yFactor;
			for(int l_w=0; l_w<patternWidth; l_w++){
				l_dX+=l_xFactor;
				
				l_iX = (int)l_dX;
				l_iY = (int)l_dY;
				
				arrTargetPattern[((l_h)*patternWidth)+l_w] = image.getIntColor(l_iX, l_iY);
			}
			l_dX=a_x;
		}
	}
	
	private void newRegion(int[] a_arrRegion, int a_pixelShift, int a_depth, int a_maxDepth){
		double l_tempMatch;
		double l_bestMatch=0;
		
		int 	l_bestShiftX=0,
				l_bestShiftY=0;
			
		
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = -a_pixelShift;
			l_bestShiftY = 0;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = a_pixelShift;
			l_bestShiftY = 0;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = 0;
			l_bestShiftY = -a_pixelShift;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1]+a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = 0;
			l_bestShiftY = +a_pixelShift;
		}
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = -a_pixelShift;
			l_bestShiftY = -a_pixelShift;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1]+a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = -a_pixelShift;
			l_bestShiftY = a_pixelShift;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = a_pixelShift;
			l_bestShiftY = -a_pixelShift;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1]+a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_bestMatch){
			l_bestMatch = l_tempMatch;
			l_bestShiftX = a_pixelShift;
			l_bestShiftY = a_pixelShift;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch >= l_bestMatch){
			return;
		}
		
		a_arrRegion[0]+=l_bestShiftX;
		a_arrRegion[1]+=l_bestShiftY;
		
		if(a_depth < a_maxDepth){
			newRegion(a_arrRegion, a_pixelShift, a_depth+1, a_maxDepth);
		}
	}
	
	private void newRegionSize(int[] a_arrRegion, int a_depth, int a_maxDepth){
		double l_tempMatch;
		double l_betterMatch=0;
		int l_betterIndex=0;
		int l_scaleS = (int)(a_arrRegion[2]*0.05);
		int l_OriginShift = l_scaleS/2;
		
		
		l_tempMatch = matchRegion(a_arrRegion[0]-3, a_arrRegion[1]-l_OriginShift, a_arrRegion[2]+l_scaleS, a_arrRegion[3]+l_scaleS);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 1;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+3, a_arrRegion[1]+l_OriginShift, a_arrRegion[2]-l_scaleS, a_arrRegion[3]-l_scaleS);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 2;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch >= l_betterMatch){
			
			return;
		}
		
		switch(l_betterIndex){
			case 1: 	
				a_arrRegion[0]-=l_OriginShift;	
				a_arrRegion[1]-=l_OriginShift;
				a_arrRegion[2]+=l_scaleS;	
				a_arrRegion[3]+=l_scaleS;
				break;
			case 2: 	
				a_arrRegion[0]+=l_OriginShift;	
				a_arrRegion[1]+=l_OriginShift;
				a_arrRegion[2]-=l_scaleS;	
				a_arrRegion[3]-=l_scaleS;				
				break;
		}
		
		if(a_depth < a_maxDepth){
			newRegionSize(a_arrRegion, a_depth+1, a_maxDepth);
		}		
	}
	
	
	private double matchRegion(int a_x, int a_y, int a_width, int a_height){
		double l_xFactor = (double)a_width/patternWidth;
		double l_yFactor = (double)a_height/patternHeight;
		
		double l_dX=a_x,l_dY=a_y;
		int l_iX,l_iY;
		
		if
		(
			a_x < 0 ||
			a_y < 0 ||
			a_x+a_width+1 > imageWidth || 
			a_y+a_height+1 > imageHeight
		){
			return 0;
		}
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			l_dY+=l_yFactor;
			for(int l_w=0; l_w<patternWidth; l_w++){
				l_dX+=l_xFactor;
				
				l_iX = (int)l_dX;
				l_iY = (int)l_dY;
				
				arrTempPattern[((l_h)*patternWidth)+l_w] = image.getIntColor(l_iX, l_iY);
			}
			l_dX=a_x;
		}
		
		int l_diffPixels=0,
			l_redA,
			l_redB,
			l_greenA,
			l_greenB,
			l_blueA,
			l_blueB;
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			for(int l_w=0; l_w<patternWidth; l_w++){
		
				l_redA 		= (arrTargetPattern[(l_h*patternWidth)+l_w] 	& 0x00FF0000) >>> 16;
				l_redB 		= (arrTempPattern[(l_h*patternWidth)+l_w] 		& 0x00FF0000) >>> 16;
				l_greenA 	= (arrTargetPattern[(l_h*patternWidth)+l_w] 	& 0x0000FF00) >>> 8;
				l_greenB 	= (arrTempPattern[(l_h*patternWidth)+l_w] 		& 0x0000FF00) >>> 8;
				l_blueA		= (arrTargetPattern[(l_h*patternWidth)+l_w] 	& 0x000000FF);
				l_blueB		= (arrTempPattern[(l_h*patternWidth)+l_w] 		& 0x000000FF);
				
				if
				(
					Math.abs(l_redA-l_redB) > colorRange ||
					Math.abs(l_greenA-l_greenB) > colorRange ||
					Math.abs(l_blueA-l_blueB) > colorRange
				){
					l_diffPixels++;
				}
				
			}
		}
		
		return 100-(((double)l_diffPixels/(patternWidth*patternHeight))*100);		
	}

}
