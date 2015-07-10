/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.transform.rotate;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Rotate the image by a user-defined angle.
 * @author Barry McCullagh
 * @version 02/04/2009
 */
public class Rotate extends MarvinAbstractImagePlugin
{
	private final static String CLOCKWISE90 = "clockwise90";
	private final static String ACLOCKWISE90 = "anticlockwise90";
	private final static String OTHER = "Other";

	private int MaximumRotationAngle = 89;

	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;

	public void load(){
		attributes = getAttributes();
		attributes.set("rotate", "angle");
	}

	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("labelRotate", "Options:");
			attributesPanel.addComboBox("combpRotate", "rotate", new Object[]{CLOCKWISE90, ACLOCKWISE90, OTHER}, attributes);
			attributesPanel.newComponentRow();
			
			attributesPanel.addLabel("lblRotateAngle", "Angle of Rotation");
			attributesPanel.addHorizontalSlider("sliderRotateAngle", "RotateAngle", -MaximumRotationAngle, MaximumRotationAngle, 0, attributes);
			attributesPanel.newComponentRow();
		}
		return attributesPanel;
	}
	
	/**
	 * Initiate the rotate process and determine the angle of rotation
	 * @param MarvinImage - the image to be rotated
	 * @param boolean - to display a preview
	 */
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//get the image dimensions
		int l_aImageHeight = a_imageIn.getHeight();
		int l_aImageWidth = a_imageIn.getWidth();
		int l_rotateAngle;
		double l_rotateAngleRadians = 0;
		//a local copy of the image to enable the modification of a_image
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		
		//for speed, check if the user wants a simple rotate by 90 degrees
		String userOption = (String)attributes.get("rotate"); 
		attributes.set("rotateTextField", userOption);
		if(userOption.intern() == CLOCKWISE90.intern())
		{
			//this is an image transpose so swap the width and height
			System.out.println("90clockwise");
			int l_newHeight = l_aImageWidth;
			int l_newWidth = l_aImageHeight;
			a_imageOut.setDimension(l_newWidth, l_newHeight);
			for(int xx = 0; xx < l_aImageWidth; xx ++)
			{
				for(int yy =l_aImageHeight-1; yy >= 0; yy--)
				{
					a_imageOut.setIntColor(l_aImageHeight-1 - yy, xx, a_imageIn.getIntColor(xx, yy));
				}
			}
		}
		else if(userOption.intern() == ACLOCKWISE90.intern())
		{
			//this is an image transpose so swap the width and height
			System.out.println("90clockwise");
			int l_newHeight = l_aImageWidth;
			int l_newWidth = l_aImageHeight;
			a_imageOut.setDimension(l_newWidth, l_newHeight);
			for(int xx = l_aImageWidth-1; xx > 0; xx --)
			{
				for(int yy = 0; yy < l_aImageHeight; yy++)
				{
					a_imageOut.setIntColor(yy, l_aImageWidth -1 - xx, a_imageIn.getIntColor(xx, yy));
				}
			}
		}
		else
		{
			l_rotateAngle = (Integer)attributes.get("RotateAngle");
			
			l_rotateAngleRadians = Math.toRadians(l_rotateAngle);
			rotateImage(a_imageIn, a_imageOut, l_rotateAngleRadians);
			
		}
	}

	/**
	 * Use the information from the original image to interpolate and fill in
	 * gaps in the rotated image
	 * @param MarvinImage a_image - the image to be modified
	 * @param MarvinImage a_originalImage - the image to be used as the source of the interpolation
	 * @param double a_rotateAngle - the angle of rotation
	 */
	private void interpolateImage(MarvinImage a_image, MarvinImage a_originalImage, int[][][] a_LookUpArray, double a_rotateAngle, int a_initialisationValue)
	{
		//get image dimensions
		int l_rotatedImageWidth = a_image.getWidth();
		int l_rotatedImageHeight = a_image.getHeight();
		
		for(int xx = 1; xx < l_rotatedImageWidth-1; xx++)
		{
			for(int yy = 1; yy < l_rotatedImageHeight-1; yy++)
			{
				//if the pixel value has NOT been assigned, in future change this to check for null
				if(a_LookUpArray[xx][yy][0] == a_initialisationValue)
				{
					int l_leftValue = a_LookUpArray[xx-1][yy][2];
					int l_rightValue = a_LookUpArray[xx-1][yy][2];
					int l_difference = l_rightValue - l_leftValue;
					a_image.setIntColor(xx, yy, l_leftValue + (l_difference+2));
				}		
			}
		}
	}

	/**
	 * Create a LookUpArray to see if pixels have been assigned a value
	 * @param a_LookUpArray - the values to be filled in
	 * @param dimensions - the size of the array
	 * @param a_InitialisationValue - what value to assign.
	 */
	public void initialiseLookUpArray(int[][][] a_LookUpArray, int[] dimensions, int a_InitialisationValue)
	{
		for(int xx = 0; xx < dimensions[0]; xx++)
		{
			for(int yy = 0; yy < dimensions[1]; yy++)
			{
				a_LookUpArray[xx][yy][0] = a_InitialisationValue;
				a_LookUpArray[xx][yy][1] = 0;
				a_LookUpArray[xx][yy][2] = 0;
			}				
		}
	}
	
	/**
	 * Rotate an image by a specified angle.
	 * @param a_image - the image to be rotated
	 * @param a_rotateAngle - the angle (in Radians) to rotate
	 */
	private void rotateImage(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_rotateAngleRads)
	{
		//Get the image dimensions
		int l_aimageHeight = a_imageIn.getHeight();
		int l_aimageWidth = a_imageIn.getWidth();
		
		//Calculate the size of the rotated image
		double l_absRotateAngle = Math.abs(a_rotateAngleRads);
		int l_newHeight = (int)(Math.ceil(l_aimageWidth * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageHeight * Math.cos(l_absRotateAngle))));
		int l_newWidth = (int)(Math.ceil(l_aimageHeight * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageWidth * Math.cos(l_absRotateAngle))));

		//The look up array used to interpolate later.
		//Each location in the array will contain
		// the x and y coordinates of the original pixel, and its RGB value
		int[][][] l_LookUpArray = new int [l_newWidth][l_newHeight][3];
		int l_initialisationValue = 654321;
		int[] l_dimensions = {l_newWidth, l_newHeight, 3};
		initialiseLookUpArray(l_LookUpArray, l_dimensions, l_initialisationValue);
	
		//Create a local copy of the image
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		//erase the contents of a_image so it can be filled with new data and resize it
		
		a_imageOut.setDimension(Math.abs(l_newWidth), Math.abs(l_newHeight));

		//Calculate the new coordinate for each pixel and fill in the lookuparray
		for(int xx = 0; xx < l_aimageWidth-0; xx++)
		{
			for(int yy = 0; yy < l_aimageHeight-0; yy++)
			{
				int l_newXCoordinate = (int)( (Math.cos(a_rotateAngleRads)* (xx - (l_aimageWidth/2))) - ((Math.sin(a_rotateAngleRads) * (yy - (l_aimageHeight/2)))) + (l_newWidth/2));
				int l_newYCoordinate = (int)( (Math.sin(a_rotateAngleRads)* (xx - (l_aimageWidth/2))) + ((Math.cos(a_rotateAngleRads) * (yy - (l_aimageHeight/2)))) + (l_newHeight/2));
				try
				{
					a_imageOut.setIntColor(l_newXCoordinate, l_newYCoordinate, a_imageIn.getIntColor(xx, yy));
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][0] = xx;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][1] = yy;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][2] = a_imageIn.getIntColor(xx, yy);
				}
				catch(Exception e)
				{
					System.out.println(l_newXCoordinate + " " + l_newYCoordinate);
				}
			}
		}
		// fill in the holes due to float/integer conversion.
		interpolateImage(a_imageOut, a_imageIn, l_LookUpArray, a_rotateAngleRads, l_initialisationValue);
	}
	
}

