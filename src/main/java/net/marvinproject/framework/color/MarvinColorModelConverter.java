/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.color;

import net.marvinproject.framework.image.MarvinImage;

/**
 * Image color model conversions.
 *
 * @version 02/02/16
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinColorModelConverter {

	/**
	 * Converts an image in RGB mode to BINARY mode
	 * @param img image
	 * @param threshold grays cale threshold
	 * @return new MarvinImage instance in BINARY mode
	 */
	public static MarvinImage rgbToBinary(MarvinImage img, int threshold){		
		MarvinImage resultImage = new MarvinImage(img.getWidth(), img.getHeight(), MarvinImage.COLOR_MODEL_BINARY);

		for(int y=0; y<img.getHeight(); y++){
			for(int x=0; x<img.getWidth(); x++){
				int gray = (int)((img.getIntComponent0(x, y)*0.3)+(img.getIntComponent1(x, y)*0.59)+(img.getIntComponent2(x, y)*0.11));
				
				if(gray <= threshold){
					resultImage.setBinaryColor(x, y, true);
				}
				else{
					resultImage.setBinaryColor(x, y, false);
				}
			}
		}
		return resultImage;
	}
	
	/**
	 * Converts an image in BINARY mode to RGB mode
	 * @param img image
	 * @return new MarvinImage instance in RGB mode
	 */
	public static MarvinImage binaryToRgb(MarvinImage img){
		MarvinImage resultImage = new MarvinImage(img.getWidth(), img.getHeight(), MarvinImage.COLOR_MODEL_RGB);

		for(int y=0; y<img.getHeight(); y++){
			for(int x=0; x<img.getWidth(); x++){
				if(img.getBinaryColor(x, y)){
					resultImage.setIntColor(x, y, 255, 0,0,0);
				}
				else{
					resultImage.setIntColor(x, y, 255, 255,255,255);
				}
			}
		}
		return resultImage;
	}
	
	/**
	 * Converts a boolean array containing the pixel data in BINARY mode to an
	 * integer array with the pixel data in RGB mode.
	 * @param binaryArray pixel binary data
	 * @return pixel integer data in RGB mode.
	 */
	public static int[] binaryToRgb(boolean[] binaryArray){
		int[] rgbArray = new int[binaryArray.length];
		
		for(int i=0; i<binaryArray.length; i++){
			if(binaryArray[i]){
				rgbArray[i] = 0xFF000000;
			}
			else{
				rgbArray[i] = 0xFFFFFFFF;
			}
		}
		return rgbArray;
	}
	
	/**
	 * Converts rgb array [argb, argb2,...] to hsv array [h,s,v,h2,s2,v2...];
	 * @param hsvArray
	 * @return
	 */
	public static double[] rgbToHsv(int[] rgbArray){
		double[] hsvArray = new double[rgbArray.length*3];
		
		double red,green,blue;
		for(int i=0; i<rgbArray.length; i++){
			red = (rgbArray[i] & 0xFF0000) >> 16;
			green = (rgbArray[i] & 0x00FF00) >> 8;
			blue = (rgbArray[i] & 0x0000FF);
			
			red /=255.0;
			green /=255.0;
			blue /=255.0;
			
			double max = Math.max(Math.max(red, green), blue);
			double min = Math.min(Math.min(red, green), blue);
			double c = max-min;
			
			// H 
			double h,s,v;
			if(c !=0 ){
				if(max == red){
					if(green >= blue){
						h = 60 * ((green-blue)/c);
					} else{
						h = 60 * ((green-blue)/c) + 360;
					}
				} else if(max == green){
					h = 60 * ((blue-red)/c) + 120;
				} else{
					h = 60 * ((red-green)/c) + 240;
				}
			} else{
				h = 0;
			}
			
			
			// V
			v = max;
			
			// S
			s = (c!=0? c/v : 0);
			
			hsvArray[(i*3)] = h;
			hsvArray[(i*3)+1] = s;
			hsvArray[(i*3)+2] = v;
			
		}
		return hsvArray;
	}
	
	/**
	 * Converts hsv array [h,s,v,h2,s2,v2...] to rgb array [argb, argb2,...];
	 * @param hsvArray
	 * @return
	 */
	public static int[] hsvToRgb(double[] hsvArray){
		int[] rgbArray = new int[hsvArray.length/3];
		
		for(int i=0, j=0; i<hsvArray.length; i+=3, j++){
			double h = hsvArray[i];
			double s = hsvArray[i+1];
			double v = hsvArray[i+2];
			
			// HSV to RGB
			double hi = (int) (h/60 % 6);
			double f = (h/60) - hi;
			double p = v * (1-s);
			double q = v * (1 - f*s);
			double t = v * (1 - (1 - f) * s);
			
			int iHi = (int)hi;
			
			int r=0,g=0,b=0;
			
			switch(iHi){
				case 0:	r = (int)(v*255);	g = (int)(t*255);	b = (int)(p*255);	break;
				case 1:	r = (int)(q*255);	g = (int)(v*255);	b = (int)(p*255);	break; 
				case 2:	r = (int)(p*255);	g = (int)(v*255);	b = (int)(t*255);	break; 
				case 3:	r = (int)(p*255);	g = (int)(q*255);	b = (int)(v*255);	break; 
				case 4:	r = (int)(t*255);	g = (int)(p*255);	b = (int)(v*255);	break;
				case 5:	r = (int)(v*255);	g = (int)(p*255);	b = (int)(q*255);	break;
			}
			
			rgbArray[j] = 0xFF000000 + (r << 16) + (g << 8) + b; 
			
		}
		
		return rgbArray;
	}
}
