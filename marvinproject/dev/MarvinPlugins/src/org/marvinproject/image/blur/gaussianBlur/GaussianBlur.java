/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package org.marvinproject.image.blur.gaussianBlur;

import marvin.gui.MarvinAttributesPanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Gaussian blur implementation. This algorithm can be optimized in many ways.
 * @author Gabriel Ambrósio Archanjo, Martin Mihályi
 * @version 1.0 02/13/2008
 */
public class GaussianBlur extends MarvinAbstractImagePlugin
{
	private final static int RED = 0;
	private final static int GREEN = 1;
	private final static int BLUE = 2;

	MarvinAttributesPanel	attributesPanel;
	MarvinAttributes 		attributes;
	MarvinPerformanceMeter 	performanceMeter;

	double kernelMatrix[][];
	double resultMatrix[][][];
	double appiledkernelMatrix[][];
	int radius;
	public void load()
	{
		// Attributes
		attributes = getAttributes();
		attributes.set("radius",3);		
		performanceMeter = new MarvinPerformanceMeter();
		radius = 3;
		kernelMatrix = getGaussianKernel();
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		if(attributesPanel == null){
			attributesPanel = new MarvinAttributesPanel();
			attributesPanel.addLabel("lblWidth", "Radius:");
			attributesPanel.addTextField("txtRadius", "radius", attributes);
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
		radius = (Integer)attributes.get("radius");

		int l_imageWidth = imageIn.getWidth();
		int l_imageHeight = imageIn.getHeight();

		performanceMeter.start("Gaussian Blur");
		performanceMeter.enableProgressBar("Gaussian Blur", (l_imageWidth*l_imageHeight)+l_imageWidth);

		int l_pixelColor;
		kernelMatrix = getGaussianKernel();
		resultMatrix = new double[l_imageWidth][l_imageHeight][3];
		appiledkernelMatrix = new double[l_imageWidth][l_imageHeight];
		
		boolean[][] l_arrMask = mask.getMaskArray();
		performanceMeter.startEvent("Apply Kernel");
		for (int x = 0; x < l_imageWidth; x++) {
			for (int y = 0; y < l_imageHeight; y++) {	
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				l_pixelColor = imageIn.getIntColor(x,y);
				applyKernel(x,y,l_pixelColor,imageOut);
			}
			performanceMeter.incProgressBar(l_imageHeight);
		}
		performanceMeter.finishEvent();
		
		
		performanceMeter.startEvent("Apply result");
		for (int x = 0; x < l_imageWidth; x++) {
			for (int y = 0; y < l_imageHeight; y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				resultMatrix[x][y][RED] = (short)((resultMatrix[x][y][0]/appiledkernelMatrix[x][y])%256);
				resultMatrix[x][y][GREEN] = (short)((resultMatrix[x][y][1]/appiledkernelMatrix[x][y])%256);
				resultMatrix[x][y][BLUE] = (short)((resultMatrix[x][y][2]/appiledkernelMatrix[x][y])%256);
				imageOut.setIntColor(x,y,imageIn.getAlphaComponent(x, y), (int)resultMatrix[x][y][0], (int)resultMatrix[x][y][1], (int)resultMatrix[x][y][2]);
			}
			performanceMeter.incProgressBar();
			performanceMeter.stepsFinished(l_imageHeight);			
		}
		performanceMeter.finishEvent();
		
		performanceMeter.finish();
	}

	/*
	 * Calc Gaussian Matrix.
	 */
	private double[][] getGaussianKernel(){
		double l_matrix[][] = new double[(radius*2)+1][(radius*2)+1];
		double l_q=radius/3.0;
		double l_distance;
		double l_x;
		double l_y;
		
		performanceMeter.startEvent("Generate Gaussian Kernel");
		for(int x=1; x<=(radius*2)+1; x++){
			for(int y=1; y<=(radius*2)+1; y++){
				l_x = Math.abs(x-(radius+1));
				l_y = Math.abs(y-(radius+1));
				l_distance = Math.sqrt((l_x*l_x)+(l_y*l_y));
				l_matrix[y-1][x-1] = ( (1.0/(2.0*Math.PI*l_q*l_q))* Math.exp( (-(l_distance*l_distance)) / (2.0*l_q*l_q) ) );
			}
			performanceMeter.stepsFinished(radius*2);
		}
		performanceMeter.finishEvent();
		return l_matrix;
	}

	/*
	 * Apply the blur matrix on a image region. 
	 */
	private void applyKernel(int centerPixel_X, int centerPixel_Y, int pixelColor, MarvinImage image)
	{
		for(int y=centerPixel_Y; y<centerPixel_Y+(radius*2); y++){
			for(int x=centerPixel_X; x<centerPixel_X+(radius*2); x++){
				if(x-radius >= 0 && x-radius < image.getWidth() && y-radius >= 0 && y-radius < image.getHeight()){
					resultMatrix[x-radius][y-radius][RED]+= (((pixelColor & 0x00FF0000) >>> 16)*kernelMatrix[x-centerPixel_X][y-centerPixel_Y]);
					resultMatrix[x-radius][y-radius][GREEN]+= (((pixelColor & 0x0000FF00) >>> 8)*kernelMatrix[x-centerPixel_X][y-centerPixel_Y]);
					resultMatrix[x-radius][y-radius][BLUE]+= ((pixelColor & 0x000000FF)*kernelMatrix[x-centerPixel_X][y-centerPixel_Y]);
					appiledkernelMatrix[x-radius][y-radius] += kernelMatrix[x-centerPixel_X][y-centerPixel_Y];
				}
			}
			performanceMeter.stepsFinished(radius*2);
		}
	}
}