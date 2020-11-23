package net.marvinproject.framework.util;

import java.awt.Color;

import net.marvinproject.framework.image.MarvinImage;

public class MarvinImageUtils {

	public static void showCorners(MarvinImage imageIn, MarvinImage imageOut, int[][] cornernessMap, int rectSize){
		MarvinImage.copyColorArray(imageIn, imageOut);
		int rsize=0;
		for(int x=0; x<cornernessMap.length; x++){
			for(int y=0; y<cornernessMap[0].length; y++){
				// Is it a corner?
				if(cornernessMap[x][y] > 0){
					rsize = Math.min(Math.min(Math.min(x, rectSize), Math.min(cornernessMap.length-x, rectSize)), Math.min(Math.min(y, rectSize), Math.min(cornernessMap[0].length-y, rectSize)));
					imageOut.fillRect(x, y, rsize, rsize, Color.red);
				}				
			}
		}
	}
}
