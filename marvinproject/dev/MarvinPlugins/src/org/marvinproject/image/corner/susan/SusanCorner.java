/**
Marvin Project <2007-2015>
http://marvinproject.sourceforge.net/

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.corner.susan;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class SusanCorner extends MarvinAbstractImagePlugin {

	private MarvinImagePlugin gray;
	@Override
	public void load() {
		gray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");
		setAttribute("matrixSize", 7);
		setAttribute("threshold", 50);
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
		boolean previewMode
	) {

		int threshold = (Integer)getAttribute("threshold");
		int matrixSize = (Integer)getAttribute("matrixSize");
		
		//convert to grayscale
		MarvinImage tempImage = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		gray.process(imageIn, tempImage);
		
		//temporary variables for faster processing
		int width = tempImage.getWidth();
		int height = tempImage.getHeight();
		
		//SUSAN specific valuables
		int USAN = 0; //number of pixels in circle
		int[][] usan = new int[width][height]; //every pixels' usan value
		
		//array for founded corners
		int[][] cornernessMapOut = new int[width][height];
		int[][] cornernessMap = new int[width][height];
		
		//1. step
		//generate mask
	    boolean[][] circleMask = new boolean[matrixSize][matrixSize];
	    USAN = 0;
	      
	    for (int i = 0; i < matrixSize; i++)
	    {
	       for (int j = 0; j < matrixSize; j++)
	       {
	          if (Math.pow(i - (matrixSize/2) , 2) + Math.pow(j - (matrixSize/2), 2) <= Math.pow(((double)(matrixSize)/2.0), 2))
	          {
	        	circleMask[i][j] = true;
	            USAN++; 
	          }
	          else
	          {
	        	circleMask[i][j] = false;
	          }
	        }
	    }
	     
	    //2. step
	    //calculate usan values for every pixels
	    //in every line
	      for (int i = 0; i < height; i += 1)
	      {
	    	  //in every row
	         for (int j = 0; j < width; j += 1)
	         {
	        	 int blockradius = matrixSize / 2;
	        	 //resize the circle if it is too big
	        	 if (i - blockradius < 0) blockradius = i;
	        	 if (j - blockradius < 0) blockradius = j;
	        	 if (i + blockradius > width - 1)blockradius = width - 1 - i;
	        	 if (j + blockradius > height - 1)blockradius = height - 1 - j;
	        	      
	        	 //define the nucleus
	        	 int nucleusValue = tempImage.getIntComponent0(i, j);
	        	 
	        	 //iterating through the circle
	        	 for (int BRi = i - blockradius; BRi <= i + blockradius; BRi++)
	             {
	                for (int BRj = j - blockradius; BRj <= j + blockradius; BRj++)
	                {
	                   if (BRi == i && BRj == j) continue; //do not consider the nucleus
	                   if (circleMask[BRi - i + blockradius][BRj - j + blockradius] == false) continue; //outside of the mask
	                   
	                   if (Math.abs(tempImage.getIntComponent0(BRi, BRj) - nucleusValue) < threshold)
	                   {
	                      usan[i][j]++;
	                   }
	                }
	             }
	        	 
	         }
	      }
	      
	      //3.step
	      //find the corner points
	      int max = 3 * USAN / 4;
		    //in every line
	      for (int i = 0; i < height; i += 1)
	      {
	    	  //in every row
	         for (int j = 0; j < width; j += 1)
	         {
	        	 if(Math.abs(max - usan[i][j]) < 2)
	        	 {
	        		 cornernessMap[i][j] = 255 - (Math.abs(max - usan[i][j]));
	        	 }
	         }
	      }
	      
	      //4.step
			for(int x=0; x<cornernessMap.length; x++){
				for(int y=0; y<cornernessMap[x].length; y++){
					cornernessMapOut[x][y] = nonmax(x,y,matrixSize,cornernessMap);
					
					if(cornernessMapOut[x][y] > 0){
						cornernessMapOut[x][y] = 1;
					}
				}
			}
	      
		if(attrOut != null){
			//return the founded corners
			attrOut.set("cornernessMap", cornernessMapOut);
		}
	}
	
	private int nonmax(int x, int y, int matrixSize, int[][] matrix){
		int s = matrixSize/2;
		if(x-(s+1) >= 0 && x+(s+1) < matrix.length && y-(s+1) >= 0 && y+(s+1) < matrix[0].length){
			for(int i=-s; i<=s; i++){
				for(int j=-s; j<=s; j++){
					if(i != 0 || j != 0){
						if(matrix[x][y] < matrix[x+i][y+j]){
							return 0;
						}
					}
				}
			}
		}
		return matrix[x][y];
	}
}