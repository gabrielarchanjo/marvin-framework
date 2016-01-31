/**
Marvin Project <2007-2016>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package marvin.image;

/**
 * Map with an id/class/weight for earch pixel
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImageMap {
	
	private int 	arrMap[][];
	private int		width,
					height;
	
	/**
	 * Constructor for null map
	 */
	public MarvinImageMap(){
		arrMap = null;
	}
	
	/**
	 * Contructor
	 * @param w		width of the image referenced by the map
	 * @param h		height of the image referenced by the map
	 */
	public MarvinImageMap (int w, int h){
		width = w;
		height = h;
		arrMap = new int[width][height];
	}
	
	public MarvinImageMap(int mask[][]){
		arrMap = mask;
		width = arrMap[0].length;
		height = arrMap.length;
	}
	
	/**
	 * @return width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * @return height;
	 */
	public int getHeight(){
		return height;
	}
		
	/**
	 * Add a point to the mask.
	 * @param x
	 * @param y
	 */
	public void setPixel(int x, int y, int value){
		arrMap[x][y] = value;
	}
	
	/**
	 * Remove point from the mask.
	 * @param x
	 * @param y
	 */
	public void removePixel(int x, int y, int value){
		arrMap[x][y] = 0;
	}
	
	/**
	 * Get value for position x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public int value(int x, int y){
		return arrMap[x][y];
	}
	
	/**
	 * Clear the mask for a new selection
	 */
	public void clear(){
		if(arrMap != null){
			for(int y=0; y<height; y++){
				for(int x=0; x<width; x++){
					arrMap[x][y] = 0;
				}
			}
		}
	}
	
	/**
	 * @return	Mask Array.
	 */
	public int[][] getMask(){
		return arrMap;
	}
	
	/**
	 * @param startX			Start pixel of the region in x axes
	 * @param startY			Start pixel of the region in y axes
	 * @param regionWidth		Width of the region
	 * @param regionHeight		Height of the region
	 */
	public void addRectRegion
	(
		int startX,
		int startY,
		int regionWidth,
		int regionHeight,
		int value
	)
	{
		for(int x=startX; x<startX+regionWidth; x++){
			for(int y=startY; y<startY+regionHeight; y++){
				arrMap[x][y] = value;
			}
		}
	}
}
