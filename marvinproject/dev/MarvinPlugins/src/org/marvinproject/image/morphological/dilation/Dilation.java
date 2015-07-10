/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.morphological.dilation;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Dilation extends MarvinAbstractImagePlugin{

	private boolean matrix[][] = null;
	
	@Override
	public void load() {
		setAttribute("matrix", matrix);
		
	}

	@Override
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut,
		MarvinAttributes attrOut, 
		MarvinImageMask mask, 
		boolean previewMode
	)
	{	
		boolean matrix[][] = (boolean[][])getAttribute("matrix");
		
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY && matrix != null){
			
			MarvinImage.copyColorArray(imgIn, imgOut);
			
			for(int y=0; y<imgIn.getHeight(); y++){
				for(int x=0; x<imgIn.getWidth(); x++){
					applyMatrix(x, y, matrix, imgIn, imgOut);
				}
			}
		}
	}
	
	private void applyMatrix
	(
		int x,
		int y,
		boolean[][] matrix,
		MarvinImage imgIn,
		MarvinImage imgOut
	){
		
		int nx,ny;
		int xC=matrix[0].length/2;
		int yC=matrix.length/2;
		
		if(imgIn.getBinaryColor(x, y)){
			for(int i=0; i<matrix.length; i++){
				for(int j=0; j<matrix.length; j++){
					
					if((i != yC || j != xC) && matrix[i][j]){
						
						nx = x + (j-xC);
						ny = y + (i-yC);
						
						if(nx > 0 && nx < imgOut.getWidth() && ny > 0 && ny < imgOut.getHeight()){
							imgOut.setBinaryColor(nx, ny, true);
						}
					}
				}
			}
		}
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}

}
