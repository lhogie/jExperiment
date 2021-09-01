package jexperiment.cmd;

import j4u.CommandLine;
import jexperiment.AVGMODE;
import jexperiment.Experiment;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

public class gnuplot extends jexperiment {
	public gnuplot(RegularFile launcher) {
		super(launcher);
		addOption("--nodata", null, null, null, "Do NOT (re)generates DAT files");
		addOption("--noplot", null, null, null, "Do NOT (re-)generates image files");
		addOption("--samplingProbability", "-p", ".*", "1", "samplingProbability");
		addOption("--showStandardDeviation", "-d", ".*", "1", "samplingProbability");
		addOption("--avgMode", "-a", "mean|median", "mean", "averaging method");
		addOption("--defaultStyle", "-s", ".*", "linespoints", "default GNUPlot plotting style");
	}

	public static void main(String[] args) throws Throwable {
		new gnuplot(null).run(args);
	}

	@Override
	public int runScript(CommandLine cmdLine) throws Throwable {
		Directory d = new Directory(cmdLine.findParameters().get(0));
		Experiment e = new Experiment(d);
		boolean data = !isOptionSpecified(cmdLine, "--nodata");
		boolean plot = !isOptionSpecified(cmdLine, "--noplot");
		double samplingProbability = Double.valueOf(getOptionValue(cmdLine, "--samplingProbability"));
		boolean stddev = !isOptionSpecified(cmdLine, "--showStandardDeviation");
		AVGMODE avg = getOptionValue(cmdLine, "--avgMode").equals("mean") ? AVGMODE.IterativeMean : AVGMODE.Median;
		String defaultStyle = getOptionValue(cmdLine, "--defaultStyle");

		e.gnuplot(data, plot, samplingProbability, stddev, avg, defaultStyle);
		return 0;
	}
}
