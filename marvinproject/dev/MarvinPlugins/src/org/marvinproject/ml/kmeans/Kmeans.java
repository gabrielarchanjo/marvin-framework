package org.marvinproject.ml.kmeans;

import java.util.ArrayList;
import java.util.List;

import marvin.plugin.MarvinAbstractAlgorithmPlugin;
import marvin.util.MarvinAttributes;

public class Kmeans extends MarvinAbstractAlgorithmPlugin{

	@Override
	public void load() {
		// Parameters with default values
		setAttribute("data", new ArrayList<double[]>());
		setAttribute("k", 1);
		setAttribute("convergenceMargin", 0.0001);
		setAttribute("iterations", 10000);
	}

	@Override
	public void process(MarvinAttributes out) {
		List<double[]> 	data 				= (ArrayList<double[]>)getAttribute("data");
		int 			k 					= (Integer)getAttribute("k");
		double 			convergenceMargin	= (Double)getAttribute("convergenceMargin");
		int 			iterations			= (Integer)getAttribute("iterations");
		
		double[][] clusters = cluster(data, k, convergenceMargin, iterations);
		out.set("clusters", clusters);
	}
	
	private double[][] cluster(List<double[]> data, int k, double convergenceMargin, int iterations){
		double prototypes[][] = new double[k][data.get(0).length];
		double centers[][] = new double[k][data.get(0).length];
		prototypes = gaussianDistributionMarsaglia(distributionMean(data), 0.1, k, data.get(0).length);
		
		int id;
		int dataByPrototype[] = new int[k];
		
		for(int c=0; c<100; c++){
			centers =  new double[k][data.get(0).length];
			dataByPrototype = new int[k];
			
			// For each sample
			for(int i=0; i<data.size(); i++){
				id = getNearestPrototype(data.get(i), prototypes);
				sumToMean(data.get(i), prototypes[id], centers[id]);
				dataByPrototype[id]++;
			}
			
			int convergedPrototypes = 0;
			int convergedDimension=0;
			double step;
			// For each prototype
			for(int i=0; i<k; i++){
				
				// If it is the nearest for a set of sample, move toward its center.
				if(dataByPrototype[i] != 0){
					convergedDimension = 0;
					for(int j=0; j<prototypes[0].length; j++){
						step = centers[i][j]/dataByPrototype[i];
						
						if(Math.abs(step) > convergenceMargin){
							prototypes[i][j] += step;
						}else{
							convergedDimension++;
						}
					}
					if(convergedDimension == prototypes[0].length){
						convergedPrototypes++;
					}
				}
			}
			if(convergedPrototypes == k){
				break;
			}
		}
		meanDensity(data, prototypes);
		return prototypes;
	}
	
	public static int getNearestPrototype(double[] data, double[][] prototypes){
		double minDist=-1;
		double tempDist;
		int nearestId=0;
		
		minDist = euclidianDistance(data, prototypes[0]);
		
		for(int i=1; i<prototypes.length; i++){
			tempDist = euclidianDistance(data, prototypes[i]);
			if(tempDist < minDist){
				minDist = tempDist;
				nearestId = i;
			}
		}
		return nearestId;
	}
	
	private static void sumToMean(double data[], double prototypes[], double center[]){
		for(int i=0; i<center.length; i++){
			center[i] += (data[i]-prototypes[i]);
		}
	}
	
	private static double euclidianDistance(double[] p1, double[] p2){
		double dist=0;
	
		for(int i=0; i<p1.length; i++){
			dist += (p1[i]-p2[i])*(p1[i]-p2[i]);
		}
		
		return Math.sqrt(dist);
	}
	
	private static double[] distributionMean(List<double[]> data){
		double[] mean = new double[data.get(0).length];
		int size = data.size();
		for(int i=0; i<size; i++){
			for(int j=0; j<mean.length; j++){
				mean[j] += data.get(i)[j]/size;
			}
		}
		return mean;
	}
	
	
	public static double[][] gaussianDistributionMarsaglia(double mean[], double variance, int points, int dim){

		double[][] res = new double[points][dim];
		int gen=0;
		double m;
		double sum=0;

		while(gen < points){

			sum=0;
			for(int i=0; i<dim; i++){
				res[gen][i] = (Math.random()*2)-1;
				sum+= (res[gen][i]*res[gen][i]);
			}

			if(sum < 1){
				m = Math.sqrt((-2*Math.log(sum))/sum);

				for(int i=0; i<dim; i++){
					res[gen][i] = mean[i] + res[gen][i]*m*variance;
				}
				gen++;
			}
			
		}
		return res;
	}
	
	private static void meanDensity(List<double[]> data, double[][] prototypess){
		double res=0;
		double tempDist;
		double minDist;
		
		for(int i=0; i<data.size(); i++){
			
			minDist = euclidianDistance(prototypess[0], data.get(i));
			for(int j=1; j<prototypess.length; j++){
				tempDist = euclidianDistance(prototypess[j], data.get(i));
				
				if(tempDist < minDist){
					minDist = tempDist;
				}
			}
			res+=(minDist*minDist);
		}
	}
}
