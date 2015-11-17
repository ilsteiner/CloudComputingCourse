package hw11;

import java.text.DecimalFormat;
import java.util.Random;

public class MonteCarlo {	
	public static void main(String[] args) {
		int numThrows = 10;
		DecimalFormat dFormat = new DecimalFormat("#.0000000000");
		double piSum = 0;
		int estimateCount = 0;
		
		while(numThrows < 100000){
			double pi = estimatePi(numThrows);
			piSum += pi;
			estimateCount += 1;
			System.out.println("Number of estimates: " + numThrows);
			System.out.println("Estimated pi: " + dFormat.format(pi));
			System.out.println("Difference from actual pi: " + dFormat.format(piSimilarity(pi)));
			System.out.println("------------------------------------");
			numThrows += 1000;
		}
		
		System.out.println("Average value for pi: " + dFormat.format(piSum/estimateCount));
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
	
	private static double piSimilarity(double estimate){
		double pi = Math.PI;
		
		return Math.abs((pi-estimate)/pi);
	}
}
