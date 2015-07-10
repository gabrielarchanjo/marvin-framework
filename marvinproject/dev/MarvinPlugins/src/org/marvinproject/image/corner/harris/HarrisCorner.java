/**
Marvin Project <2007-2015>
http://marvinproject.sourceforge.net

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.corner.harris;

import java.awt.Color;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class HarrisCorner extends MarvinAbstractImagePlugin {

	private MarvinImagePlugin gray;
	@Override
	public void load() {
		gray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");
		setAttribute("matrixSize", 7);
		setAttribute("threshold", 500);
		setAttribute("k", 0.004);
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

		double k = (Double)getAttribute("k");
		int threshold = (Integer)getAttribute("threshold");
		int matrixSize = (Integer)getAttribute("matrixSize");
		
		//convert to grayscale
		MarvinImage tempImage = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		gray.process(imageIn, tempImage);
		
		//temporary variables for faster processing
		int width = tempImage.getWidth();
		int height = tempImage.getHeight();
		
		//array for founded corners
		int[][] cornernessMapOut = new int[width][height];
		
		//1.step
		//calculate diffx, diffy, diffxy
		double diffx[][] = new double[width][height];
		double diffy[][] = new double[width][height];			
		double diffxy[][] = new double[width][height];
		
        // for each line
        for (int y = 1; y < height - 1; y++)
        {
            // for each pixel
            for(int x = 1; x < width - 1; x++)
            {
            	// Convolution with horizontal differentiation kernel mask
                float X = ((tempImage.getIntComponent0(x+1, y-1) + tempImage.getIntComponent0(x+1, y) + tempImage.getIntComponent0(x+1, y+1)) - (tempImage.getIntComponent0(x-1, y-1) + tempImage.getIntComponent0(x-1, y) + tempImage.getIntComponent0(x-1, y-1))) * 0.166666667f;

                // Convolution vertical differentiation kernel mask
                float Y = ((tempImage.getIntComponent0(x+1, y+1) + tempImage.getIntComponent0(x+1, y) + tempImage.getIntComponent0(x, y+1)) - (tempImage.getIntComponent0(x-1, y-1) + tempImage.getIntComponent0(x-1, y-1) + tempImage.getIntComponent0(x-1, y))) * 0.166666667f;

                diffx[x][y]= X*X;
                diffy[x][y]= Y*Y;
                diffxy[x][y]= X*Y;
            }
        }
        
        //2.step
        //gaussian blur
		double diffxGauss[][] = new double[width][height];
		double diffyGauss[][] = new double[width][height];			
		double diffxyGauss[][] = new double[width][height];
        
        // for each line
        for (int y = 1; y < height - 1; y++)
        {
            // for each pixel
            for(int x = 1; x < width - 1; x++)
            {
        
            	diffxGauss[x][y] = (diffx[x-1][y-1] + diffx[x][y-1] * 2 + diffx[x+1][y-1] + diffx[x-1][y] * 2 + diffx[x][y] * 4 + diffx[x+1][y] * 2 + diffx[x-1][y+1] + diffx[x][y+1] * 2 + diffx[x+1][y+1]) / 16;
		
            	diffyGauss[x][y] = (diffy[x-1][y-1] + diffy[x][y-1] * 2 + diffy[x+1][y-1] + diffy[x-1][y] * 2 + diffy[x][y] * 4 + diffy[x+1][y] * 2 + diffy[x-1][y+1] + diffy[x][y+1] * 2 + diffy[x+1][y+1]) / 16;
    		
            	diffxyGauss[x][y] = (diffxy[x-1][y-1] + diffxy[x][y-1] * 2 + diffxy[x+1][y-1] + diffxy[x-1][y] * 2 + diffxy[x][y] * 4 + diffxy[x+1][y] * 2 + diffxy[x-1][y+1] + diffxy[x][y+1] * 2 + diffxy[x+1][y+1]) / 16;
    		
            }
            
        }
        
        //3.step
        double A,B,C,M;
        int cornernessMap[][] = new int[width][height];
        // for each line
        for (int y = 1; y < height - 1; y++)
        {
            // for each pixel
            for(int x = 1; x < width - 1; x++)
            {
            	A = diffxGauss[x][y];
            	B = diffyGauss[x][y];
            	C = diffxyGauss[x][y];
            	
     	
            	   M = (A * B - C * C) - (k * (A + B) * (A + B));
            	   
            	   if(M> threshold){
            		   cornernessMap[x][y] = (int)M;
            	   }
            	   else{
            		   cornernessMap[x][y] = 0;
            	   }
            }
        }
        
        //step 4
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