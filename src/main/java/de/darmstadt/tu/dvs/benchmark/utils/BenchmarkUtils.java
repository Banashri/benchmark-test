package de.darmstadt.tu.dvs.benchmark.utils;

import java.util.Random;

public class BenchmarkUtils {
	
	/**
	 * This method provides an array containing random numbers which will be used as the random index of any collection
	 * Actually the random number of that collection
	 * @param size
	 * @return
	 */
	public static int[] populateRandomNos(int size) {
		int[] randomNos = new int[size];
		Random r = new Random(12345678);
		for (int i = 0; i < size; i++) 
			randomNos[i] = r.nextInt(size);
		
		return randomNos;
	}
}
