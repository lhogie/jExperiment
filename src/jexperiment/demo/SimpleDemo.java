package jexperiment.demo;

import java.io.IOException;

import jexperiment.Curve;
import jexperiment.Experiment;
import jexperiment.NumericalPlot2D;
import toools.StopWatch;

public class SimpleDemo
{
	public static void main(String[] args) throws IOException
	{
		Experiment e = new Experiment("une simple experimentation");
		NumericalPlot2D p = e.createPlot("append to string", "string length", "time", String.class);
		Curve c = p.createCurve("string");

		for (int length = 1; length < 10000000; length *= 2)
		{
			while (c.getPoint(length).getNumberOfMeasures() < 4)
			{
				long duration = measureComputationDuration(length);
				c.getPoint(length).addY(duration);
			}
		}

		e.plot();
		e.getDirectory().open();
	}

	private static long measureComputationDuration(int l)
	{
		StopWatch sw = new StopWatch();
		String s = createStringOfLength(l);

		for (int i = 0; i < 1000; ++i)
		{
			s += ' ';
		}

		return sw.getElapsedTime();
	}

	private static String createStringOfLength(int l)
	{
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < l; ++i)
		{
			s.append(' ');
		}

		return s.toString();
	}
}
