package marvin.image;

public class MarvinBlob {

	private int			width,
						height,
						area;
	
	private boolean[][] pixels;
	
	public MarvinBlob(int width, int height){
		this.width = width;
		this.height = height;
		this.area = 0;
		pixels = new boolean[width][height];
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public void setValue(int x, int y, boolean value){
		if(!pixels[x][y] && value){
			area++;
		} else if(pixels[x][y] && !value){
			area--;
		}
		
		pixels[x][y] = value;
	}
	
	public int getArea(){
		return this.area;
	}
	
	public boolean getValue(int x, int y){
		return pixels[x][y];
	}
	
	public MarvinContour toContour(){
		
		MarvinContour contour = new MarvinContour();
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				
				if(getValue(x, y)){
					if
					(
						(x-1 < 0 || x+1 == width || y-1 < 0 || y+1 == height) ||
						!getValue(x-1, y-1) ||
						!getValue(x-1, y) ||
						!getValue(x-1, y+1) ||
						!getValue(x, y-1) ||
						!getValue(x, y+1) ||
						!getValue(x+1, y-1) ||
						!getValue(x+1, y) ||
						!getValue(x+1, y+1)
					){
						contour.addPoint(x, y);
					}
				}
			}
		}
		return contour;
	}
}
