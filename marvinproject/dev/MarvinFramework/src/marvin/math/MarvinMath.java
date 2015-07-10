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
}
