/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.convolution;

import javax.imageio.ImageIO;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class Convolution extends MarvinAbstractImagePlugin{
	
	private MarvinAttributesPanel	attributesPanel;
	private MarvinAttributes 		attributes;
	
	public void load(){
		
		attributes = getAttributes();
		attributes.set("matrix", null);
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addMatrixPanel("matrixPanel", "matrix", attributes, 3, 3);
		}
		return attributesPanel;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attributesOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
	{
		double[][] matrix = (double[][])attributes.get("matrix");
		
		if(matrix != null && matrix.length > 0){
			
			for(int y=0; y<imageIn.getHeight(); y++){
				for(int x=0; x<imageIn.getWidth(); x++){
					
					if(y >= matrix.length/2 && y < imageIn.getHeight()-matrix.length/2 && x >= matrix[0].length/2 && x < imageIn.getWidth()-matrix[0].length/2){
						applyMatrix(x, y, matrix, imageIn, imageOut);
					}
					else{
						imageOut.setIntColor(x, y, 0xFF000000);
					}
				}
			}
		}
	}
	
	private void applyMatrix
	(
		int x,
		int y,
		double[][] matrix,
		MarvinImage imageIn,
		MarvinImage imageOut
	){
		
		int nx,ny;
		double resultRed=0;
		double resultGreen=0;
		double resultBlue=0;
		
		int xC=matrix[0].length/2;
		int yC=matrix.length/2;
		
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
					
				if(matrix[i][j] != 0){		
					nx = x + (j-xC);
					ny = y + (i-yC);
					
					if(nx >= 0 && nx < imageOut.getWidth() && ny >= 0 && ny < imageOut.getHeight()){
						
						resultRed	+=	(matrix[i][j]*(imageIn.getIntComponent0(nx, ny)));
						resultGreen += 	(matrix[i][j]*(imageIn.getIntComponent1(nx, ny)));
						resultBlue	+=	(matrix[i][j]*(imageIn.getIntComponent2(nx, ny)));
					}
					
					
				}
				
				
				
			}
		}
		
		resultRed 	= Math.abs(resultRed);
		resultGreen = Math.abs(resultGreen);
		resultBlue = Math.abs(resultBlue);
		
		// allow the combination of multiple applications
		resultRed 	+= imageOut.getIntComponent0(x,y);
		resultGreen += imageOut.getIntComponent1(x,y);
		resultBlue 	+= imageOut.getIntComponent2(x,y);
		
		resultRed 	= Math.min(resultRed, 255);
		resultGreen = Math.min(resultGreen, 255);
		resultBlue 	= Math.min(resultBlue, 255);
		
		resultRed 	= Math.max(resultRed, 0);
		resultGreen = Math.max(resultGreen, 0);
		resultBlue 	= Math.max(resultBlue, 0);
		
		imageOut.setIntColor(x, y, imageIn.getAlphaComponent(x, y), (int)resultRed, (int)resultGreen, (int)resultBlue);
	}
	
}
