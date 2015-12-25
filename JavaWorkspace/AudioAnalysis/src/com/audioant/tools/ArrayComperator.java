package com.audioant.tools;

public class ArrayComperator {

	public static double compareArrays(double[] arr1, Double[] arr2, double threshold) {

		int matches = 0;

		for (double arr2Value : arr2) {

			for (double arr1Value : arr1) {

				double min = arr1Value - threshold;
				double max = arr1Value + threshold;

				if (arr2Value < max && arr2Value > min) {
					matches++;
					break;
				}
			}
		}

		int sizeBiggerArray = (arr1.length > arr2.length) ? arr1.length : arr2.length;

		return matches / (double) sizeBiggerArray;
	}
}
