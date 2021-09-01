package jexperiment.demo;

import jexperiment.AVGMODE;
import jexperiment.Configuration;
import jexperiment.Experiment;
import jexperiment.Function;
import jexperiment.GNUPlot;
import jexperiment.Plot;
import toools.io.file.Directory;

public class SimpleDemo {
	public static void main(String[] args) {
		Experiment exp = new Experiment(new Directory("$HOME/simpleDemo"));
		Plot plot = exp.createPlot("append to string", "string length", "time");
		Function fct = plot.createFunction("string");

		for (double length = 1000000; length > 100; length /= 1.2) {
			Configuration config = fct.configuration("len=" + length);

			// we do 5 runs
			while (config.countMeasures() < 5) {
				long startDate = System.nanoTime();
				createStringOfLength((int) length);
				long endDate = System.nanoTime();
				long duration = endDate - startDate;
				config.addMeasure(length, duration);
			}
		}

		new GNUPlot().plot(plot, true, true, 1, true, AVGMODE.IterativeMean,
				"linespoints");
	}

	private static String createStringOfLength(int l) {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < l; ++i) {
			s.append(' ');
		}

		return s.toString();
	}
}
