package hw11;

import java.text.DecimalFormat;
import java.util.Random;

public class MonteCarlo {	
	public static void main(String[] args) {
		int numThrows = 10;
		DecimalFormat dFormat = new DecimalFormat("#.0000000000");
		
		while(numThrows < 10000){
			double pi = estimatePi(numThrows);
			System.out.println("------------------------------------");
			System.out.println("Number of estimates: " + numThrows);
			System.out.println("Estimated pi: " + dFormat.format(pi));
			System.out.println("Difference from actual pi: " + dFormat.format(piSimilarity(pi)));
			
			numThrows += 1000;
		}
	}

	private static double estimatePi(int numThrows){		
		double x;
		double y;
		int circleCount = 0;
		
		for(int i=0;i<numThrows;i++){
			Random random =  new Random();
			x = random.nextDouble();
			y = random.nextDouble();
			
			if(Math.pow(x,2) + Math.pow(y,2) <= 1){
				circleCount++;
			}
		}
		
		return (4*(double)circleCount)/(double)numThrows;
	}
	
	private static double averagePi(int numEstimates,int numThrows){
		double sum = 0;
		
		for(int i=0;i<numEstimates;i++){
			sum += estimatePi(numThrows);
		}
		
		return sum/numEstimates;
	}
	
	private static double piSimilarity(double estimate){
		double pi = Math.PI;
		
		return Math.abs((pi-estimate)/pi);
	}
}
