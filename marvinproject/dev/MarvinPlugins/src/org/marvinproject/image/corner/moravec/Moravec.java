/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.corner.moravec;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

public class Moravec extends MarvinAbstractImagePlugin {

	private MarvinImagePlugin gray;
	
	@Override
	public void load() {
		gray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.color.grayScale");
		setAttribute("matrixSize", 3);
		setAttribute("threshold", 0);
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

		int matrixSize = (Integer)getAttribute("matrixSize");
		int threshold = (Integer)getAttribute("threshold");
		
		MarvinImage tempImage = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		gray.process(imageIn, tempImage);
		
		int[][] cornernessMap = new int[tempImage.getWidth()][tempImage.getHeight()];
		int[][] cornernessMapOut = new int[tempImage.getWidth()][tempImage.getHeight()];
		
		for(int y=0; y<tempImage.getHeight(); y++){
			for(int x=0; x<tempImage.getWidth(); x++){
				cornernessMap[x][y] = c(x,y,matrixSize,tempImage);
				
				if(cornernessMap[x][y] < threshold){
					cornernessMap[x][y] = 0;
				}
			}
		}
		
		for(int x=0; x<cornernessMap.length; x++){
			for(int y=0; y<cornernessMap[x].length; y++){
				cornernessMapOut[x][y] = nonmax(x,y,matrixSize,cornernessMap);
				
				if(cornernessMapOut[x][y] > 0){
					cornernessMapOut[x][y] = 1;
				}
			}
		}
		
		if(attrOut != null){
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
	
	private Integer[][] directions = new Integer[][]{{1,0}, {-1, 0}, {0, 1}, {0,-1}, {-1,-1}, {1, -1}, {-1, 1}, {1,1}};
	
	private int c(int x, int y, int matrixSize, MarvinImage image){
		
		int ret = -1;
		int temp;
		int s = matrixSize/2;
		if(x-(s+1) >= 0 && x+(s+1) < image.getWidth() && y-(s+1) >= 0 && y+(s+1) < image.getHeight()){
			
			for(int d=0; d<directions.length; d++){
				temp=0;
				for(int i=-s; i<=s; i++){
					for(int j=-s; j<=s; j++){
						temp += Math.pow(image.getIntComponent0(x+i, y+j)-image.getIntComponent0(x+i+directions[d][0],y+j+directions[d][1]), 2);
					}
				}
				if(ret == -1 || temp < ret){
					ret = temp;
				}
			}
			
		}
		return ret;
	}

}
