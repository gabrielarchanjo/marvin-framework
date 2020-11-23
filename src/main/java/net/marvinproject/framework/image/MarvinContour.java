package net.marvinproject.framework.image;

import java.util.ArrayList;
import java.util.List;

import net.marvinproject.framework.math.Point;

public class MarvinContour {

	private List<Point> points;
	
	public MarvinContour(){
		this.points = new ArrayList<Point>();
	}
	
	public void addPoint(int x, int y){
		this.points.add(new Point(x, y));
	}
	
	public Point getPoint(int index){
		return this.points.get(index);
	}
	
	public List<Point> getPoints(){
		return this.points;
	}
	
	public int length(){
		return this.points.size();
	}
}
