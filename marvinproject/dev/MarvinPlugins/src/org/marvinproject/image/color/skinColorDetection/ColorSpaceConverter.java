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
 * Locates faces in an image.
 * @author Barry McCullagh
 * @version 23/04/2009
 */
public class ColorSpaceConverter extends MarvinAbstractImagePlugin
{
	
	MarvinAttributes attributes;

	public void load(){
		attributes = getAttributes();
	}

	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

	/**
	 * Convert the image from RGB to HSV. Conversion formula from 
	 * http://en.wikipedia.org/wiki/HSL_color_space#Conversion_from_RGB_to_HSL_or_HSV
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
		int l_imageWidth = imageIn.getWidth();
		int l_imageHeight = imageIn.getHeight();
		
		for(int l_currentX = 0; l_currentX < l_imageWidth; l_currentX++)
		{
			for(int l_currentY = 0; l_currentY < l_imageHeight; l_currentY++)
			{
				double l_red = (double)(imageIn.getIntComponent0(l_currentX, l_currentY));
				double l_green = (double)(imageIn.getIntComponent1(l_currentX, l_currentY));
				double l_blue = (double)(imageIn.getIntComponent2(l_currentX, l_currentY));
				
				double l_normalisedRed = ( l_red / 255 );                     //RGB from 0 to 255
				double l_normalisedGreen = ( l_green / 255 );
				double l_normalisedBlue = ( l_blue / 255 );

				double l_currentMin = min3nums( l_normalisedRed, l_normalisedGreen, l_normalisedBlue );    //Min. value of RGB
				double l_currentMax = max3nums( l_normalisedRed, l_normalisedGreen, l_normalisedBlue ) ;   //Max. value of RGB
				

				double l_hue = 0;                                //HSV results from 0 to 1
				double l_sat = 0;
				double l_var = 0;
				//calculate the hue value
				if(l_currentMin == l_currentMax)
				{
					l_hue = 0;
				}
				else if(l_normalisedRed == l_currentMax)
				{
					l_hue = (60 * ((l_normalisedGreen - l_normalisedBlue)/(l_currentMax - l_currentMin)) +360 )%360;
				}
				else if(l_normalisedGreen == l_currentMax)
				{
					l_hue = (60 * ((l_normalisedBlue - l_normalisedRed)/(l_currentMax - l_currentMin)) +120 );
				}
				else if(l_normalisedBlue == l_currentMax)
				{
					l_hue = (60 * ((l_normalisedRed - l_normalisedGreen)/(l_currentMax - l_currentMin)) +240 );
				}
				
				
				//calculate the saturation value
				if(l_currentMax == 0)
				{
					l_sat = 0;
				}
				else
				{
					l_sat = 1 - (l_currentMin/l_currentMax);
				}
				
				l_var = l_currentMax;
				//System.out.println(l_hue);
				//l_hue = l_hue*255;
				//System.out.println((int)((l_hue*255)/360) + "\n___________");
				imageOut.setIntColor(l_currentX, l_currentY, (int)((l_hue*255)/360), (int)(l_sat*255), (int)(l_var*255));
			}
		}
	}
	public void process1
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		int l_imageWidth = a_imageIn.getWidth();
		int l_imageHeight = a_imageIn.getHeight();
		
		for(int l_currentX = 0; l_currentX < l_imageWidth; l_currentX++)
		{
			for(int l_currentY = 0; l_currentY < l_imageHeight; l_currentY++)
			{
				double l_red = (double)(a_imageIn.getIntComponent0(l_currentX, l_currentY));
				double l_green = (double)(a_imageIn.getIntComponent1(l_currentX, l_currentY));
				double l_blue = (double)(a_imageIn.getIntComponent2(l_currentX, l_currentY));
				
				double l_varR = ( l_red / 255 );                     //RGB from 0 to 255
				double l_varG = ( l_green / 255 );
				double l_varB = ( l_blue / 255 );

				double l_varMin = min3nums( l_varR, l_varG, l_varB );    //Min. value of RGB
				double l_varMax = max3nums( l_varR, l_varG, l_varB ) ;   //Max. value of RGB
				double l_delMax = l_varMin - l_varMax;             //Delta RGB value

				double l_var = l_varMax;

				double l_hue = 0;                                //HSV results from 0 to 1
				double l_sat = 0;
				if ( l_delMax != 0 )                     //This is a gray, no chroma...
				{
				
				   l_sat = l_delMax / l_varMax;

				   double l_del_R = ( ( ( l_varMax - l_varR ) / 6 ) + ( l_delMax / 2 ) ) / l_delMax;
				   double l_del_G = ( ( ( l_varMax - l_varG ) / 6 ) + ( l_delMax / 2 ) ) / l_delMax;
				   double l_del_B = ( ( ( l_varMax - l_varB ) / 6 ) + ( l_delMax / 2 ) ) / l_delMax;

				   if ( l_varR == l_varMax )
				   {
					   l_hue = l_del_B - l_del_G;
				   }
				   else if ( l_varG == l_varMax )
				   {
					   l_hue = ( 1 / 3 ) + l_del_R - l_del_B;
				   }
				   else if ( l_varB == l_varMax )
				   {
					   l_hue = ( 2 / 3 ) + l_del_G - l_del_R;
				   }

				   if ( l_hue < 0 )
				   {
					   	l_hue += 1;
				   }
				   if ( l_hue > 1 )
				   {
					   l_hue -= 1;
				   }
				}
				System.out.println(l_hue);
				l_hue = l_hue*255;
				System.out.println(l_hue + "\n___________");
				a_imageOut.setIntColor(l_currentX, l_currentY, (int)l_hue, (int)l_hue, (int)l_hue);
			}
		}
	}
	public double max3nums(double a_red, double a_green, double a_blue)
	{
		double l_max = 0;
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
	public double min3nums(double a_red, double a_green, double a_blue)
	{
		double l_min = 255;
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
}
