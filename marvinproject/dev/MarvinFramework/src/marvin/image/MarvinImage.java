/**
Marvin Project <2007-2016>
http://marvinproject.sourceforge.net/

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import marvin.color.MarvinColorModelConverter;

/**
 * Image object with many operations. This class is the image
 * representation used for the other classes in the framework.
 *
 * @version 1.0 02/13/08
 * @author Danilo Roseto Munoz
 * @author Fabio Andrijaukas
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImage implements Cloneable {
	
	public final static int COLOR_MODEL_RGB 	= 0;
	public final static int COLOR_MODEL_BINARY 	= 1;
	
	// Definitions
	public final static int PROPORTIONAL = 0;	

	// Image
	protected BufferedImage image;
	
	// Array Color
	protected int[] arrIntColor;
	protected boolean[] arrBinaryColor;
	
	// Colors
	protected int rgb, r, b, g;
	protected Color color;

	// Color Model
	protected int colorModel;
	
	// Format
	protected String formatName;
	
	// Components
	protected int numComponents;
	
	// Dimension
	int width;
	int height;
	
	/**
	 * Creates an image with just 1x1 resolution. Useful in cases where the dimension is set dynamically.
	 */
	public MarvinImage(){
		this(1,1);
	}
	/**
	 * Constructor using a image in memory
	 * @param img Image
	 */
	public MarvinImage(BufferedImage img){		
		this.image =  img;
		formatName = "jpg";
		width = img.getWidth();
		height = img.getHeight();
		colorModel = COLOR_MODEL_RGB;
		updateColorArray();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Constructor using a image in memory
	 * @param img Image
	 * @param fmtName Image format name
	 */
	public MarvinImage(BufferedImage img, String fmtName){
		this.image =  img;
		formatName = fmtName;		
		width = img.getWidth();
		height = img.getHeight();
		colorModel = COLOR_MODEL_RGB;
		updateColorArray();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Constructor to blank image, passing the size of image
	 * @param int width
	 * @param int height
	 */
	public MarvinImage(int w, int h){
		colorModel = COLOR_MODEL_RGB;
		formatName = "jpg";
		setDimension(w, h);
	}
	
	public MarvinImage(int w, int h, int cm){
		colorModel = cm;
		formatName = "jpg";
		setDimension(w, h);
	}
	
	
	public int getComponents(){
		return numComponents;
	}
	
	public MarvinImage subimage(int x, int y, int width, int height){
		MarvinImage ret = new MarvinImage(width, height);
		ret.setColorModel(this.getColorModel());
		
		for(int i=y; i<y+height; i++){
			for(int j=x; j<x+width; j++){
				switch(colorModel){
					case COLOR_MODEL_RGB:
						ret.setIntColor(j-x, i-y, this.getIntColor(j,i));
						break;
					case COLOR_MODEL_BINARY:
						ret.setBinaryColor(j-x, i-y, this.getBinaryColor(j,i));
						break;
				}
			}
		}
		return ret;
	}
	
	public void updateColorArray(){
		arrIntColor = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
//		arrIntColor = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	public void update(){
		int w = image.getWidth();
		switch(colorModel){
			case COLOR_MODEL_RGB:
				image.setRGB(0, 0, image.getWidth(), image.getHeight(), arrIntColor,0,w);
				break;
			case COLOR_MODEL_BINARY:
				image.setRGB(0, 0, image.getWidth(), image.getHeight(), MarvinColorModelConverter.binaryToRgb(arrBinaryColor),0,w);
				break;
		}
	}
	
	public void clear(){
		clear(0);
	}
	
	public void clear(int color){
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				setIntColor(x,y,color);
			}
		}
	}
	
	/**
	 * Gets the type
	 */
	public int getType(){		
		return image.getType();
	}
	
	public int getColorModel(){
		return colorModel;
	}
	
	public void setColorModel(int cm){
		colorModel = cm;
		allocColorArray();
	}
	
	//@todo remove ambiguity between Type and FormatName
	/*
	 * @return image format name
	 */
	public String getFormatName(){
		return formatName;
	}
	
	public void setDimension(int w, int h){
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		width = w;
		height = h;
		
		allocColorArray();
				
	}
	
	public void allocColorArray(){
		switch(colorModel){
			case COLOR_MODEL_RGB:
				arrBinaryColor = null;
				arrIntColor = new int[width*height];
				break;
			case COLOR_MODEL_BINARY:
				arrIntColor = null;
				arrBinaryColor = new boolean[width*height];
				break;
		}
	}

	/**
	 * @return integer color array for the entire image.
	 */
	public int[] getIntColorArray(){
		return arrIntColor;
	}
	
	/**
	 *	Set the integer color array for the entire image.
	 **/
	public void setIntColorArray(int[] arr){
		arrIntColor = arr;
	}
	
	public static void copyColorArray(MarvinImage imgSource, MarvinImage imgDestine){
		
		if(imgSource.getColorModel() != imgDestine.getColorModel()){
			throw new RuntimeException("copyColorArray(): Incompatible Images Color Model");
		}
		
		switch(imgSource.getColorModel()){
			case COLOR_MODEL_RGB:
				copyIntColorArray(imgSource, imgDestine);
				break;
			case COLOR_MODEL_BINARY:
				copyBinaryColorArray(imgSource, imgDestine);
				break;
		}
	}
	
	protected static void copyIntColorArray(MarvinImage imgSource, MarvinImage imgDestine){
		System.arraycopy(imgSource.getIntColorArray(), 0, imgDestine.getIntColorArray(), 0, imgSource.getWidth()*imgSource.getHeight());
	}
	
	protected static void copyBinaryColorArray(MarvinImage imgSource, MarvinImage imgDestine){
		System.arraycopy(imgSource.getBinaryColorArray(), 0, imgDestine.getBinaryColorArray(), 0, imgSource.getWidth()*imgSource.getHeight());
	}
	
	public boolean[] getBinaryColorArray(){
		return arrBinaryColor;
	}
	
	public boolean getBinaryColor(int x, int y){
		return arrBinaryColor[y*width+x];
	}
	
	public void setBinaryColor(int x, int y, boolean value){
		arrBinaryColor[y*width+x] = value;
	}
	
	/**
	 * Gets the integer color composition for x, y position
	 * @param int		x 
	 * @param int		y
	 * @return int		color
	 */
	public int getIntColor(int x, int y){
		return arrIntColor[y*width+x];
	}
	
	/**
	 * @param x
	 * @param y
	 * @param alpha
	 */
	public void setAlphaComponent(int x, int y, int alpha){
		int color = arrIntColor[((y*width+x))];
		color = (alpha << 24) + (color & 0x00FFFFFF);
		arrIntColor[((y*width+x))] = color;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return alpha component
	 */
	public int getAlphaComponent(int x, int y){
		return (arrIntColor[((y*width+x))]& 0xFF000000) >>> 24;
	}
	
	/**
	 * Gets the integer color component 0  in the x and y position
	 * @param int		x 
	 * @param int		y
	 * @return int		color component 0
	 */	
	public int getIntComponent0(int x, int y){
		return (arrIntColor[((y*width+x))]& 0x00FF0000) >>> 16;
	}

	/**
	 * Gets the integer color component 1 in the x and y position
	 * @param int	x 
	 * @param int	y
	 * @return int color component 1
	 */	
	public int getIntComponent1(int x, int y){
		return (arrIntColor[((y*width+x))]& 0x0000FF00) >>> 8;
	}

	/**
	 * Gets the integer color component 2 in the x and y position
	 * @param int	x 
	 * @param int	y
	 * @return int blue color
	 */	
	public int getIntComponent2(int x, int y){
		return (arrIntColor[((y*width+x))] & 0x000000FF);
	}

	/**
	 * Returns the width 
	 * @return int	width
	 */
	public int getWidth(){
		return(image.getWidth());  	
	}

	/**
	 * Returns the height 
	 * @return int	height
	 */
	public int getHeight(){
		return(image.getHeight());  
	}
	
	public boolean isValidPosition(int x, int y){
		if(x >= 0 && x < image.getWidth() && y >= 0 && y < getHeight()){
			return true;
		}
		return false;
	}

	public void setIntColor(int x, int y, int alpha, int color){
		arrIntColor[((y*image.getWidth()+x))] = (alpha << 24) + color;
	}
	
	/**
	 * Sets the integer color composition in X an Y position
	 * @param x 		position
	 * @param y 		position
	 * @param color 	color value
	 */
	public void setIntColor(int x, int y, int color){
		arrIntColor[((y*image.getWidth()+x))] = color;
	}

	/**
	 * Sets the integer color in X an Y position
	 * @param x 	position
	 * @param y 	position
	 * @param c0	component 0
	 * @param c1 	component 1
	 * @param c2 	component 2
	 */
	public void setIntColor(int x, int y, int c0, int c1, int c2){
		int alpha = (arrIntColor[((y*width+x))]& 0xFF000000) >>> 24;
		setIntColor(x,y,alpha,c0,c1,c2);
	}
	
	/**
	 * Sets the integer color in X an Y position
	 * @param x 	position
	 * @param y 	position
	 * @param c0	component 0
	 * @param c1 	component 1
	 * @param c2 	component 2
	 */
	public void setIntColor(int x, int y, int alpha, int c0, int c1, int c2){
		arrIntColor[((y*image.getWidth()+x))] = (alpha << 24)+
		(c0 << 16)+
		(c1 << 8)+
		c2;
	}

	/**
	 * Sets a new image 
	 * @param BufferedImage imagem
	 */
	public void setBufferedImage(BufferedImage img){		
		image = img;
		width = img.getWidth();
		height = img.getHeight();
		updateColorArray();		
	}

	/**
	 * @returns a BufferedImage associated with the MarvinImage 
	 */
	public BufferedImage getBufferedImage(){
		return image;
	}
	
	public BufferedImage getBufferedImageNoAlpha(){
		int pixels;
		int[] pixelData;
		BufferedImage image;
		
		// Only for RGB images
		switch(colorModel){
			case COLOR_MODEL_RGB:
				pixels = width*height;
				pixelData = new int[pixels];
				for(int i=0; i<pixels; i++){
					pixelData[i] = arrIntColor[i] & 0x00FFFFFF;
				}
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				image.setRGB(0, 0, width, height, pixelData,0,width);
				return image;
			case COLOR_MODEL_BINARY:
				pixels = width*height;
				pixelData = new int[pixels];
				for(int i=0; i<pixels; i++){
					if(arrBinaryColor[i]){
						pixelData[i] = 0x00000000;
					} else {
						pixelData[i] = 0x00FFFFFF;
					}
				}
				image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				image.setRGB(0, 0, width, height, pixelData,0,width);
				return image;
		}
		return null;
	}
	
	/**
	 * Limits the color value between 0 and 255.
	 * @return int - the color value
	 */
	public int limit8bitsColor(int color){

		if(color > 255){
			color = 255;
			return(color);
		}

		if(color < 0){
			color = 0;
			return(color);
		}
		return color;
	}

	/**
	 * Convolution operator
	 * @return int[]
	 */
	public int[] Multi8p(int x, int y,int masc[][]){
		//a b c
		//d e f
		//g h i
		int aR = getIntComponent0(x-1,y-1);   int bR = getIntComponent0(x-1,y);  int cR = getIntComponent0(x-1,y+1);
		int aG = getIntComponent1(x-1,y-1);   int bG = getIntComponent1(x-1,y);  int cG = getIntComponent1(x-1,y+1);
		int aB = getIntComponent2(x-1,y-1);   int bB = getIntComponent2(x-1,y);  int cB = getIntComponent2(x-1,y+1);


		int dR = getIntComponent0(x,y-1);      int eR = getIntComponent0(x,y);   int fR = getIntComponent0(x,y+1);
		int dG = getIntComponent1(x,y-1);      int eG = getIntComponent1(x,y);   int fG = getIntComponent1(x,y+1);
		int dB = getIntComponent2(x,y-1);      int eB = getIntComponent2(x,y);   int fB = getIntComponent2(x,y+1);


		int gR = getIntComponent0(x+1,y-1);    int hR = getIntComponent0(x+1,y);   int iR = getIntComponent0(x+1,y+1);
		int gG = getIntComponent1(x+1,y-1);    int hG = getIntComponent1(x+1,y);   int iG = getIntComponent1(x+1,y+1);
		int gB = getIntComponent2(x+1,y-1);    int hB = getIntComponent2(x+1,y);   int iB = getIntComponent2(x+1,y+1);

		int rgb[] = new int[3];

		rgb[0] = ( (aR * masc[0][0]) + (bR * masc[0][1]) +(cR * masc[0][2])+
				(dR * masc[1][0]) + (eR * masc[1][1]) +(fR * masc[1][2])+
				(gR * masc[2][0]) + (hR * masc[2][1]) +(iR * masc[2][2]) ); 

		rgb[1] = ( (aG * masc[0][0]) + (bG * masc[0][1]) +(cG * masc[0][2])+
				(dG * masc[1][0]) + (eG * masc[1][1]) +(fG * masc[1][2])+
				(gG * masc[2][0]) + (hG * masc[2][1]) +(iG * masc[2][2]) ); 

		rgb[2] = ( (aB * masc[0][0]) + (bB * masc[0][1]) +(cB * masc[0][2])+
				(dB * masc[1][0]) + (eB * masc[1][1]) +(fB * masc[1][2])+
				(gB * masc[2][0]) + (hB * masc[2][1]) +(iB * masc[2][2]) ); 

		// return the value for all channel
		return(rgb);

	}

	/**
	 * Return a new instance of the BufferedImage
	 * @return BufferedImage
	 */
	public BufferedImage getNewImageInstance(){
		BufferedImage buf = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());
		buf.setData(image.getData());
		return buf;
	}

	/**
	 * Resize and return the image passing the new height and width
	 * @param height
	 * @param width
	 * @return
	 */
	public BufferedImage getBufferedImage(int width, int height)
	{
		// using the new approach of Java 2D API 
		BufferedImage buf = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) buf.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(image,0,0,width,height,null);
		g2d.dispose();
		return(buf);
	}
	
	/**
	 * Resize and return the image passing the new height and width, but maintains width/height factor
	 * @param height
	 * @param width
	 * @return
	 */
	 public BufferedImage getBufferedImage(int width, int height, int type){
		 int	wDif, 
				hDif,				
				fWidth = 0, 
				fHeight = 0;

		 double	imgWidth,
				imgHeight;

		 double factor;
		 imgWidth = image.getWidth();
		 imgHeight = image.getHeight();
		

		 switch(type)
		 {
			 case PROPORTIONAL:
				 wDif = (int)imgWidth - width;
				 hDif = (int)imgHeight - height;
				 if(wDif > hDif){
					 factor = width/imgWidth;
				 }
				 else{
					 factor = height/imgHeight;
				 }
				 fWidth = (int)Math.floor(imgWidth*factor);
				 fHeight = (int)Math.floor(imgHeight*factor);
				 break;
		 }
		 return getBufferedImage(fWidth, fHeight);
	 }
	/**
	 * Resize the image passing the new height and width
	 * @param height
	 * @param width
	 * @return
	 */
	public void resize(int w, int h)
	{

		// using the new approach of Java 2D API 
		BufferedImage buf = new BufferedImage(w,h, image.getType());
		Graphics2D g2d = (Graphics2D) buf.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(image,0,0,w,h,null);
		g2d.dispose();
		image = buf;
		width = w;
		height = h;
		updateColorArray();
	}
	
	/**
	 * Clones the {@link MarvinImage}
	 */
	public MarvinImage clone() {
		MarvinImage newMarvinImg = new MarvinImage(getWidth(), getHeight(), getColorModel());
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		newMarvinImg.setBufferedImage(newImage);
		MarvinImage.copyColorArray(this, newMarvinImg);
		newMarvinImg.update();
		return newMarvinImg;
	}
	/**
	 * Multiple of gradient windwos per masc relation of x y 
	 * @return int[]
	 */
	public double multi8p(int x, int y,double masc){
		int aR = getIntComponent0(x-1,y-1);     int bR = getIntComponent0(x-1,y);    int cR = getIntComponent0(x-1,y+1);
		int aG = getIntComponent1(x-1,y-1);   int bG = getIntComponent1(x-1,y);  int cG = getIntComponent1(x-1,y+1);
		int aB = getIntComponent1(x-1,y-1);   int bB = getIntComponent1(x-1,y);  int cB = getIntComponent1(x-1,y+1);

		
		int dR = getIntComponent0(x,y-1);        int eR = getIntComponent0(x,y);     int fR = getIntComponent0(x,y+1);
		int dG = getIntComponent1(x,y-1);      int eG = getIntComponent1(x,y);   int fG = getIntComponent1(x,y+1);
		int dB = getIntComponent1(x,y-1);      int eB = getIntComponent1(x,y);   int fB = getIntComponent1(x,y+1);

		
		int gR = getIntComponent0(x+1,y-1);      int hR = getIntComponent0(x+1,y);     int iR = getIntComponent0(x+1,y+1);
		int gG = getIntComponent1(x+1,y-1);    int hG = getIntComponent1(x+1,y);   int iG = getIntComponent1(x+1,y+1);
		int gB = getIntComponent1(x+1,y-1);    int hB = getIntComponent1(x+1,y);   int iB = getIntComponent1(x+1,y+1);

		double rgb = 0;

		rgb = ( (aR * masc) + (bR * masc) +(cR * masc)+
				(dR * masc) + (eR * masc) +(fR * masc)+
				(gR * masc) + (hR * masc) +(iR * masc) ); 

		return(rgb);

	}
	
	public int boundRGB(int rgb){

		if(rgb > 255){
			rgb = 255;
			return(rgb);
		}

		if(rgb < 0){
			rgb = 0;
			return(rgb);
		}
		return rgb;
	}
	
	/**
	 * Bresenham큦 Line Drawing implementation
	 */
	public void drawLine(int x0, int y0, int x1, int y1, Color c) {
		int colorRGB = c.getRGB();
		int dy = y1 - y0;
		int dx = x1 - x0;
		int stepx, stepy;
		int fraction;
		
		
		if (dy < 0) { dy = -dy; stepy = -1; 
		} else { stepy = 1; 
		}
	 	if (dx < 0) { dx = -dx; stepx = -1; 
		} else { stepx = 1; 
		}
		dy <<= 1; 							// dy is now 2*dy
		dx <<= 1; 							// dx is now 2*dx
	 
		setIntColor(x0, y0, colorRGB);

		if (dx > dy) {
			fraction = dy - (dx >> 1);	// same as 2*dy - dx
			while (x0 != x1) {
				if (fraction >= 0) {
					y0 += stepy;
					fraction -= dx; 		// same as fraction -= 2*dx
				}
				x0 += stepx;
	   		fraction += dy; 				// same as fraction -= 2*dy
	   		setIntColor(x0, y0, colorRGB);
			}
		} else {
			fraction = dx - (dy >> 1);
			while (y0 != y1) {
				if (fraction >= 0) {
					x0 += stepx;
					fraction -= dy;
				}
			y0 += stepy;
			fraction += dx;
			setIntColor(x0, y0, colorRGB);
			}
		}
	} 

	
	/**
	 * Draws a rectangle in the image. It큦 useful for debugging purposes.
	 * @param x		rect큦 start position in x-axis
	 * @param y		rect큦 start positioj in y-axis
	 * @param w		rect큦 width
	 * @param h		rect큦 height
	 * @param c		rect큦 color
	 */
	public void drawRect(int x, int y, int w, int h, Color c){
		int color = c.getRGB();
		for(int i=x; i<x+w; i++){
			setIntColor(i, y, color);
			setIntColor(i, y+(h-1), color);
		}
		
		for(int i=y; i<y+h; i++){
			setIntColor(x, i, color);
			setIntColor(x+(w-1), i, color);
		}
	}
	
	public void drawRect(int x, int y, int w, int h, int length, Color c){
		for(int i=0; i<length; i++){
			drawRect(x+i, y+i, w-(i*2), h-(i*2), Color.green);
		}
	}
	
	/**
	 * Fills a rectangle in the image.
	 * @param x		rect큦 start position in x-axis
	 * @param y		rect큦 start positioj in y-axis
	 * @param w		rect큦 width
	 * @param h		rect큦 height
	 * @param c		rect큦 color
	 */
	public void fillRect(int x, int y, int w, int h, Color c){
		int color = c.getRGB();
		for(int i=x; i<x+w; i++){
			for(int j=y; j<y+h; j++){
				if(i < width && j < height){
					setIntColor(i,j,color);
				}
			}
		}
	}
	
	/**
	 * Set alpha to 0 for a given color
	 * @param color target color
	 */
	public void setColorToAlpha(int alpha, int color){
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				if((getIntColor(x,y) & 0x00FFFFFF) == (color & 0x00FFFFFF)){
					setAlphaComponent(x,y,alpha);
				}
			}
		}
	}
	
	/**
	 * Set pixels having alpha == 0 to an a given color.
	 * @param color
	 */
	public void setAlphaToColor(int color){
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				if(getAlphaComponent(x, y) == 0){
					setIntColor(x, y, 0xFFFFFFFF);
				}
			}
		}
	}
	
	/**
	 * Compare two MarvinImage objects
	 * @param obj	object to be compared. MarvinImage object is expected.
	 */
	public boolean equals(Object obj){
		MarvinImage img = (MarvinImage) obj;
		int[] l_arrColor = img.getIntColorArray();
		
		if(getWidth() != img.getWidth() || getHeight() != img.getHeight()){
			return false;
		}
		
		for(int l_cont=0; l_cont<getHeight(); l_cont++){
			if(arrIntColor[l_cont] != l_arrColor[l_cont]){
				return false;
			}
		}	
		return true;
	}
}