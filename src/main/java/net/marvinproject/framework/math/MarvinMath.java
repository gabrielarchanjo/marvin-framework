package net.marvinproject.framework.math;

import java.util.List;

public class MarvinMath {

	public static boolean[][] getTrueMatrix(int rows, int cols){
		boolean ret[][] = new boolean[rows][cols];
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				ret[i][j]  = true;
			}
		}
		return ret;
	}
	
	public static double[][] scaleMatrix(double[][] matrix, double scale){
		double[][] ret = new double[matrix.length][matrix.length];
		
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix.length; j++){
				ret[i][j] = matrix[i][j] * scale;
			}
		}
		return ret;
	}
	
	public static double euclideanDistance(double x1, double y1, double x2, double y2){
		double dx = (x1-x2);
		double dy = (y1-y2);
		return Math.sqrt( dx*dx + dy*dy);
	}
	
	public static double euclideanDistance(double x1, double y1, double z1, double x2, double y2, double z2){
		double dx = (x1-x2);
		double dy = (y1-y2);
		double dz = (z1-z2);
		return Math.sqrt( dx*dx + dy*dy + dz*dz);
	}
	
	public static void removeTooNearVertices(List<Point> vertices, double minDistance){
		for(int i=0; i<vertices.size()-1; i++){
			Point p1 = vertices.get(i);
			Point p2 = vertices.get(i+1);
			
			if(MarvinMath.euclideanDistance(p1.x, p1.y, p2.x, p2.y) <= minDistance){
				vertices.remove(i+1);
				i--;
			}
		}
	}
	
	public static void removeCollinearVertices(List<Point> vertices){
		
		for(int i=0; i<vertices.size()-2; i++){
			
			if(isCollinearPoints(vertices.get(i), vertices.get(i+1), vertices.get(i+2))){
				vertices.remove(i+1);
				i--;
			}
			
		}	
	}
	
	public static boolean isCollinearPoints(Point p1, Point p2, Point p3){
		return isCollinearPoints(p1,p2,p3,0.3);
	}
	
	private static boolean isCollinearPoints(Point p1, Point p2, Point p3, double minDistance){
		double p1_p3 = MarvinMath.euclideanDistance(p1.x, p1.y, p3.x, p3.y);
		double p1_p2 = MarvinMath.euclideanDistance(p1.x, p1.y, p2.x, p2.y);
		double p2_p3 = MarvinMath.euclideanDistance(p2.x, p2.y, p3.x, p3.y);
		
		double diff = Math.abs(p1_p3 - (p1_p2+p2_p3));
		return (diff < minDistance);
	}
}
