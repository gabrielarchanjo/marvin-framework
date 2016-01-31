/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.equalization.histogramEqualization;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class HistogramEqualization extends MarvinAbstractImagePlugin{

	@Override
	public void process
	(
		MarvinImage imageIn,
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask,
		boolean preview
	) {
		
		boolean[][] bmask = mask.getMask();
		
		// histogram
		int[] histRed = new int[256];
		int[] histGreen = new int[256];
		int[] histBlue = new int[256];
		int red, green, blue;
		for(int y=0; y<imageIn.getHeight(); y++){
			for(int x=0; x<imageIn.getWidth(); x++){
				if(bmask != null && !bmask[x][y]){
					continue;
				}
				
				red 	= imageIn.getIntComponent0(x, y);
				green 	= imageIn.getIntComponent1(x, y);
				blue 	= imageIn.getIntComponent2(x, y);
				
				histRed[red]++;
				histGreen[green]++;
				histBlue[blue]++;
			}
		}
		
		// Cumulative Distribution Function
		int cdfRed[] = new int[256];
		int cdfGreen[] = new int[256];
		int cdfBlue[] = new int[256];
		cdfRed[0] = histRed[0];
		cdfGreen[0] = histGreen[0];
		cdfBlue[0] = histBlue[0];
		for(int i=1; i<histRed.length; i++){
			cdfRed[i] 	= cdfRed[i-1]+histRed[i];
			cdfGreen[i] = cdfGreen[i-1]+histGreen[i];
			cdfBlue[i] 	= cdfBlue[i-1]+histBlue[i];
		}
		
		// Equalization
		int numberOfPixels = imageIn.getWidth()*imageIn.getHeight();
		int minRed = min(cdfRed);
		int minGreen = min(cdfGreen);
		int minBlue = min(cdfBlue);
		for (int x = 0; x < imageIn.getWidth(); x++) {
			for (int y = 0; y < imageIn.getHeight(); y++) {
				if(bmask != null && !bmask[x][y]){
					continue;
				}
				
				red 	= imageIn.getIntComponent0(x, y);
				green 	= imageIn.getIntComponent1(x, y);
				blue 	= imageIn.getIntComponent2(x, y);
				
				red = (int)((((double)cdfRed[red]-minRed)/(numberOfPixels-minRed)) * 255);
				green = (int)((((double)cdfGreen[green]-minGreen)/(numberOfPixels-minGreen)) * 255);
				blue = (int)((((double)cdfBlue[blue]-minBlue)/(numberOfPixels-minBlue)) * 255);
				imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x,  y), red, green, blue);
			}
		}
	}

	private int min(int[] arr){
		int min=-1;
		for(int i=0; i<arr.length; i++){
			if(min == -1 || arr[i] < min){
				min = arr[i];
			}
		}
		return min;
	}
	
	@Override
	public void load() {}

	@Override
	public MarvinAttributesPanel getAttributesPanel() {
		return null;
	}
}
