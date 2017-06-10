/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.morphological.thinning;

import marvin.color.MarvinColorModelConverter;
import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Thinning extends MarvinAbstractImagePlugin{

	private static Boolean[][] B1 = new Boolean[][]{
												{false,false,false},
												{null,true,null},
												{true,true,true}
												};
	
	private static Boolean[][] B2 = new Boolean[][]{
												{null,false,false},
												{true,true,false},
												{true,true,null}
												};
	
	private static Boolean[][] B3 = new Boolean[][]{
												{true,null,false},
												{true,true,false},
												{true,null,false}
												};
	
	private static Boolean[][] B4 = new Boolean[][]{
												{true,true,null},
												{true,true,false},
												{null,false,false}
												};
	
	private static Boolean[][] B5 = new Boolean[][]{
												{true,true,true},
												{null,true,null},
												{false,false,false}
												};
	
	private static Boolean[][] B6 = new Boolean[][]{
												{null,true,true},
												{false,true,true},
												{false,false,null}
												};
	
	private static Boolean[][] B7 = new Boolean[][]{
												{false,null,true},
												{false,true,true},
												{false,null,true}
												};
	
	private static Boolean[][] B8 = new Boolean[][]{
												{false,false,null},
												{false,true,true},
												{null,true,true}
												};
	
	private Boolean[][][] MATRICES = new Boolean[][][]{
		B1, B2, B3, B4, B5, B6, B7, B8	
	};
	
	@Override
	public void load() {}

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
		if(imgIn.getColorModel() == MarvinImage.COLOR_MODEL_BINARY){
			MarvinImage tempImgIn = imgIn.clone();
			int lastChange=-1;
			boolean lastApplyChanged;
			
			int c=0;
			mainLoop:
			while(true){
				for(int mIndex=0; mIndex < MATRICES.length; mIndex++){
					
					lastApplyChanged = false;
					for(int y=0; y<imgIn.getHeight(); y++){
						for(int x=0; x<imgIn.getWidth(); x++){
							
							if(apply(x, y, tempImgIn, imgOut, MATRICES[mIndex])){
								lastChange = mIndex;
								lastApplyChanged = true;
							}
						}
					}
					
					if(lastApplyChanged){
						MarvinImage.copyColorArray(imgOut, tempImgIn);
					}
					
					if(!lastApplyChanged && lastChange == mIndex){
						break mainLoop;
					}
				}
			}
		}
	}
	
	private boolean apply(int x, int y, MarvinImage imgIn, MarvinImage imgOut, Boolean[][] matrix){
		
		int hi = matrix.length/2;
		int hj = matrix[0].length/2;
		
		for(int i=-hi, mi=0; i<=hi; i++, mi++){
			for(int j=-hj, mj=0; j<=hj; j++, mj++){
				
				int nx = x+i;
				int ny = y+j;
				
				if((nx < 0 || nx >= imgIn.getWidth() || ny < 0 || ny >= imgIn.getHeight())){
					if(matrix[mj][mi] != null && matrix[mj][mi]){
						return false;
					}
					continue;
				}
				
				if(matrix[mj][mi] != null && imgIn.getBinaryColor(nx,ny) != matrix[mj][mi]){
					return false;
				}
			}
		}
		
		imgOut.setBinaryColor(x,y,false);
		return true;
	}

	@Override
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
}
