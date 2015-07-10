package marvin.image;

import java.awt.Point;
import java.util.List;

public class MarvinSegment {

	public int 	x1,
	x2,
	y1,
	y2;

	public int width;
	public int height;
	public int mass;

	public MarvinSegment(){
		this.x1 = -1;
		this.x2 = -1;
		this.y1 = -1;
		this.y2 = -1;
		this.width = -1;
		this.height = -1;
		this.mass = 0;
	}

	public String toString(){
		return "{x1:"+x1+", x2:"+x2+", y1:"+y1+", y2:"+y2+", width:"+width+", height:"+height+", mass:"+mass+"}";
	}
	
	public static void segmentMinDistance(List<MarvinSegment> segments, double minDistance){
		MarvinSegment s1,s2;
		for(int i=0; i<segments.size()-1; i++){
			for(int j=i+1; j<segments.size(); j++){
				s1 = segments.get(i);
				s2 = segments.get(j);
				
				if(Point.distance( (s1.x1+s1.x2)/2, (s1.y1+s1.y2)/2, (s2.x1+s2.x2)/2, (s2.y1+s2.y2)/2 ) < minDistance){
					segments.remove(s2);
					j--;
				}
			}
		}
	}
}
