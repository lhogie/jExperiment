package jexperiment.cmd;

import j4u.CommandLine;
import j4u.CommandLineSpecification;
import jexperiment.AVGMODE;
import jexperiment.Plots;
import toools.io.file.Directory;

public class gnuplot extends jexperiment {

	public static void main(String[] args) throws Throwable {
		new gnuplot().run(args);
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable {
		Directory d = new Directory(cmdLine.findParameters().get(0));
		Plots e = new Plots(d);
		boolean data = !isOptionSpecified(cmdLine, "--nodata");
		boolean plot = !isOptionSpecified(cmdLine, "--noplot");
		double samplingProbability = Double.valueOf(getOptionValue(cmdLine, "--samplingProbability"));
		boolean stddev = !isOptionSpecified(cmdLine, "--showStandardDeviation");
		AVGMODE avg = getOptionValue(cmdLine, "--avgMode").equals("mean") ? AVGMODE.IterativeMean : AVGMODE.Median;
		String defaultStyle = getOptionValue(cmdLine, "--defaultStyle");

		e.gnuplot(data, plot, samplingProbability, stddev, avg, defaultStyle);
		return 0;
	}

	@Override
	protected void specifyCmdLine(CommandLineSpecification spec) {
		spec.addOption("--nodata", null, null, null, "Do NOT (re)generates DAT files");
		spec.addOption("--noplot", null, null, null, "Do NOT (re-)generates image files");
		spec.addOption("--samplingProbability", "-p", ".*", "1", "samplingProbability");
		spec.addOption("--showStandardDeviation", "-d", ".*", "1", "samplingProbability");
		spec.addOption("--avgMode", "-a", "mean|median", "mean", "averaging method");
		spec.addOption("--defaultStyle", "-s", ".*", "linespoints", "default GNUPlot plotting style");

	}
}
