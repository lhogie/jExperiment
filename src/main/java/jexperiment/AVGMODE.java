package jexperiment;

import java.util.Arrays;

public interface AVGMODE
{
	double compute(double[] array);

	public static final AVGMODE IterativeMean = a -> {
		double avg = 0;
		int t = 1;

		for (double x : a)
		{
			avg += (x - avg) / t;
			++t;
		}

		return avg;
	};
	
	public static final AVGMODE Median = a -> {
		Arrays.sort(a);

		if (a.length % 2 == 1)
		{
			return a[a.length / 2];
		}
		else
		{
			return (a[a.length / 2 - 1] + a[a.length / 2]) / 2;
		}
	};
}