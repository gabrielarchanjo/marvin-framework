package marvin.image;

public class MarvinBlobSegment {

	private int 		x,
						y;
	private MarvinBlob 	blob;
	
	public MarvinBlobSegment(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setBlob(MarvinBlob blob){
		this.blob = blob;
	}
	
	public MarvinBlob getBlob(){
		return this.blob;
	}
}
