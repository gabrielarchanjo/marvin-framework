/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.color.skinColorDetection;


import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Locates skin color pixels in an image.
 * @author Barry McCullagh
 * @version 08/05/2009
 */
public class SkinColorDetection extends MarvinAbstractImagePlugin
{
	
	MarvinAttributes attributes;

	public void load(){
		attributes = getAttributes();
	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

	/**
	 * Initiate the process of finding skin
	 * @param MarvinImage - the image to be rotated
	 * @param boolean - to display a preview
	 */
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		MarvinImage l_hsvImage = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		ColorSpaceConverter l_colorspaceConverter = new ColorSpaceConverter();
		l_colorspaceConverter.process(imageIn, l_hsvImage, attributesOut, mask, false);
		
		
		/*The first step is to determine regions of the scene which
		 * have appropriate skin tone.
		 */
		findSkinColorPixels(imageIn, l_hsvImage, imageOut);
		
		
		
		
	}
	/**
	 * Using heuristics, identify any pixels which may be skin colored.
	 * @param imageIn
	 * @param a_imageOut
	 */
	private void findSkinColorPixels(MarvinImage imageIn, MarvinImage hsvImage, MarvinImage imageOut)
	{
		int l_imageHeight = imageIn.getHeight();
		int l_imageWidth = imageIn.getWidth();
		
		
		boolean[] l_rules = {false, false, false};
		
		for(int xx = 0; xx < l_imageWidth; xx++)
		{
			for(int yy = 0; yy < l_imageHeight; yy++)
			{
				int l_currentHue = hsvImage.getIntComponent0(xx, yy);
				int l_currentSat = hsvImage.getIntComponent1(xx, yy);
				int l_currentVar = hsvImage.getIntComponent2(xx, yy);
				
				if(l_currentHue > 0 && l_currentHue < 23)
				{
					imageOut.setIntColor(xx, yy, imageIn.getIntColor(xx, yy));
				}
				else
				{
					imageOut.setIntColor(xx, yy, 0);
				}
			/*	checkRule0(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				//checkRule1(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				checkRule2(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				if(l_rules[0] == true && l_rules[2] == true)
				{
					a_imageOut.setRGB(xx, yy, a_imageIn.getRGB(xx, yy));
				}
				else
				{
					a_imageOut.setRGB(xx, yy, 0);
				}
				*/
			}
		}
	}
	
	//rules from http://graphics.cs.msu.ru/en/publications/text/gc2003vsa.pdf
	protected void checkRule2(int currentRed, int currentGreen, int currentBlue, boolean[] a_rules)
	{
		int l_maxCoords = max3nums(currentRed, currentGreen, currentBlue);
		int l_minCoords = min3nums(currentRed, currentGreen, currentBlue);
		if(currentRed > 95 && currentGreen >40 && currentBlue > 20
				&& (l_maxCoords - l_minCoords) > 15
				&& Math.abs(currentRed - currentGreen) > 15
				&& currentRed > currentGreen
				&& currentRed > currentBlue)
		{
			a_rules[2] = true;
		}
		else
		{
			a_rules[2] = false;
		}
	}
	public int max3nums(int a_red, int a_green, int a_blue)
	{
		int l_max = 0;
		if(a_red > a_green)
		{
			l_max = a_red;
		}
		else
		{
			l_max = a_green;
			
		}
		if(l_max < a_blue)
		{
			l_max = a_blue;
		}
		
		return l_max;
	}
	public int min3nums(int a_red, int a_green, int a_blue)
	{
		int l_min = 255;
		if(a_red < a_green)
		{
			l_min = a_red;
		}
		else
		{
			l_min = a_green;
		}
		if(l_min < a_blue)
		{
			return l_min;
		}
		else
		{
			return a_blue;
		}
	}
	
	//rules from http://lrv.fri.uni-lj.si/~peterp/publications/eurocon03.pdf
	protected void checkRule1(int currentRed, int currentGreen, int currentBlue, boolean[] rules)
	{
		if(currentRed > 220 
				&& currentGreen > 210 
				&& currentBlue > 170 
				&& Math.abs(currentRed - currentGreen) <= 15 
				&& currentRed > currentBlue 
				&& currentGreen > currentBlue)
		{
			rules[1] = true;
		}
		else
		{
			rules[1] = false;
		}
	}
	
	//rules from http://cs-people.bu.edu/ringb/CS585/PA1/source/ImageFunct.html
	protected void checkRule0(int currentRed, int currentGreen, int currentBlue, boolean[] rules)
	{
		int[] l_currentSkintone = new int[2];
		l_currentSkintone[0] = currentRed;
		l_currentSkintone[1] = currentGreen;
		//check the red and green component of the current pixel
		//If it does not fall in between the required values set as black
		if(currentRed < 40  || currentGreen < 40)
		{
			//a_imageOut.setRGB(xx, yy, 0, 0, 0);
			rules[0] = false;
			//System.out.println(a_rule1);
		}
		else
		{
			int[] l_VectorSkintoneA = new int[2];
			int[] l_VectorSkintoneB = new int[2];
			l_VectorSkintoneA[0] = 225;
			l_VectorSkintoneA[1] = 165;
			l_VectorSkintoneB[0] = 125;
			l_VectorSkintoneB[1] = 50;
			double l_angleCurrentSkinVectorA = calculateVectorAngle(l_VectorSkintoneA, l_currentSkintone);
			double l_angleCurrentSkinVectorB = calculateVectorAngle(l_VectorSkintoneB, l_currentSkintone);
			
			if(l_angleCurrentSkinVectorA < .995 && l_angleCurrentSkinVectorB < .995)
			{
				//a_imageOut.setRGB(xx, yy, 0, 0, 0);
				rules[0] = false;
				
			}
			else
			{
				rules[0] = true;
				//System.out.println(a_rule1);
				//a_imageOut.setRGB(xx, yy, a_imageIn.getRGB(xx,yy));
			}
		}
	}
	/**
	 * Find the minimum and maximum component of all the R, G and B values in an image.
	 * @param imageIn
	 * @param minMax
	 */
	protected void findMinMax(MarvinImage imageIn, int[] minMax)
	{
		for(int l_currentX = 0; l_currentX < imageIn.getWidth(); l_currentX++)
		{
			for(int l_currentY = 0; l_currentY < imageIn.getHeight(); l_currentY++)
			{
				int l_currentRed = imageIn.getIntComponent0(l_currentX, l_currentY);
				int l_currentGreen = imageIn.getIntComponent1(l_currentX, l_currentY);
				int l_currentBlue = imageIn.getIntComponent2(l_currentX, l_currentY);
				//check red
				if(l_currentRed < minMax[0])
				{
					minMax[0] = l_currentRed;
				}
				if(l_currentRed > minMax[1])
				{
					minMax[1] = l_currentRed;
				}
				//check green
				if(l_currentGreen < minMax[0])
				{
					minMax[0] = l_currentGreen;
				}
				if(l_currentGreen > minMax[1])
				{
					minMax[1] = l_currentGreen;
				}
				//check blue
				if(l_currentBlue < minMax[0])
				{
					minMax[0] = l_currentBlue;
				}
				if(l_currentBlue > minMax[1])
				{
					minMax[1] = l_currentBlue;
				}
			}
		}
	}
	/**
	 * The borders may not be continuous so this method examines the pixels
	 * around the current pixel of interest. If there is no border pixel neighbouring
	 * it, it searches within a 16 connected connected neighbourhood to find a bordering pixel
	 * @param l_borderedRegions
	 */
	
/**
 * Get the angle between two, two dimensional vectors
 * @param a_Vector1, a_Vector2 - the vectors who's angle of difference needs to be calculated
 */
	protected double calculateVectorAngle(int[] vector1, int[] vector2)
	{
		if (vector1.length != vector2.length)
		{
			return 0;
		}
		double l_magVector1 = findMagnitude(vector1);
		double l_magVector2 = findMagnitude(vector2);
		double l_dotProduct = dotProduct(vector1, vector2);
		
		return l_dotProduct / (l_magVector1 * l_magVector2);

	}
	/**
	 * Find the magnitude of a 2 element 'vector'
	 * @param vector
	 * @return
	 */
	protected double findMagnitude(int[] vector)  
	{
		if (vector.length != 2)
		{
			return 0;
		}
		else
		{
			return Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1] );
		}
	}
	
	//Static Method that returns the Dot Product of two Vectors
	private double dotProduct(int[] vector1, int[] vector2) 
	{
		if (vector1.length != vector2.length && vector1.length != 2)
		{
			return 0;
		}
	    return vector1[0]*vector2[0] + vector1[1]*vector2[1];
	}
	
	

	
}
