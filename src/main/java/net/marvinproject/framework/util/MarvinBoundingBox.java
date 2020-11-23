package net.marvinproject.framework.util;

import net.marvinproject.framework.math.MarvinMath;

public class MarvinBoundingBox {

	public int 	x1,
				y1,
				x2,
				y2;
	
	public MarvinBoundingBox(){
		this.x1 = -1;
		this.y1 = -1;
		this.x2 = -1;
		this.y2 = -1;
	}
	
	public MarvinBoundingBox(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void add(int x, int y){
		if(this.x1 == -1 || x < this.x1){		this.x1 = x;	}
		if(this.y1 == -1 || y < this.y1){		this.y1 = y;	}
		if(this.x2 == -1 || x > this.x2){		this.x2 = x;	}
		if(this.y2 == -1 || y > this.y2){		this.y2 = y;	}
	}
	
	public int getWidth(){
		return this.x2-this.x1;
	}
	
	public int getHeight(){
		return this.y2-this.y1;
	}
	
	public boolean contains(int x, int y){
		if
		(
			x >= this.x1 && x <= this.x2 &&
			y >= this.y1 && y <= this.y2
		){
			return true;
		}
		return false;
	}
	
	public void merge(MarvinBoundingBox box2){
		// x1
		if(box2.x1 < this.x1){
			this.x1 = box2.x1;
		}
		// y1
		if(box2.y1 < this.y1){
			this.y1 = box2.y1;
		}
		// x2
		if(box2.x2 > this.x2){
			this.x2 = box2.x2;
		}
		// y2
		if(box2.y2 > this.y2){
			this.y2 = box2.y2;
		}
	}
	
	public boolean near(int x, int y, int distance){
		
		// Upper left
		if			(x < this.x1 && y < this.y1){										
			return MarvinMath.euclideanDistance(x, y, this.x1, this.y1) < distance;
		} 
		// Upper right
		else if	(x > this.x2 && y < this.y1){											
			return MarvinMath.euclideanDistance(x, y, this.x2, this.y1) < distance;
		}
		// Bottom left
		else if	(x < this.x1 && y > this.y2){											
			return MarvinMath.euclideanDistance(x, y, this.x1, this.y2) < distance;
		}
		// Bottom Right
		else if	(x > this.x2 && y > this.y2){											
			return MarvinMath.euclideanDistance(x, y, this.x2, this.y2) < distance;
		}
		// Top or Bottom
		else if	(x >= this.x1 && x <= this.x2){
			
			if(y < this.y1){
				return MarvinMath.euclideanDistance(x, y, x, this.y1) < distance;
			} else{
				return MarvinMath.euclideanDistance(x, y, x, this.y2) < distance;
			}
		}
		// Left or Right
		else if	(y >= this.y1 && y <= this.y2){
			if(x < this.x1){
				return MarvinMath.euclideanDistance(x, y, this.x1, y) < distance;
			} else{
				return MarvinMath.euclideanDistance(x, y, this.x2, y) < distance;
			}
		}
		
		return false;
	}
}
