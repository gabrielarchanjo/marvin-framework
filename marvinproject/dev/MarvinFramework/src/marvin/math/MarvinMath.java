package marvin.math;

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
	
	public static double euclideanDistance(double x1, double y1, double z1, double x2, double y2, double z2){
		double dx = (x1-x2);
		double dy = (y1-y2);
		double dz = (z1-z2);
		return Math.sqrt( dx*dx + dy*dy + dz*dz);
	}
}
