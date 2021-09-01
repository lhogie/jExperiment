/**
 * Grph
 * Initial Software by Luc HOGIE, Issam TAHIRI, Aurélien LANCIN, Nathann COHEN, David COUDERT.
 * Copyright © INRIA/CNRS/UNS, All Rights Reserved, 2011, v0.9
 *
 * The Grph license grants any use or destribution of both binaries and source code, if
 * a prior notification was made to the Grph development team.
 * Modification of the source code is not permitted. 
 * 
 *
 */

package jexperiment;

import java.util.Arrays;
import java.util.List;

import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import toools.extern.Proces;
import toools.io.Cout;
import toools.io.file.RegularFile;
import toools.math.MathsUtilities;

public class GNUPlot extends Plotter
{
	public static String CMD = "gnuplot";

	@Override
	public RegularFile plot(Plot p)
	{
		return plot(p, true, true, 1, true, AVGMODE.IterativeMean, "linespoints");
	}

	public RegularFile plot(Plot plot, boolean data, boolean executeGNUPlot,
			double samplingProbability, boolean printStdDev, AVGMODE avg,
			String defaultStyle)
	{
		// GNUPlot.CMD = "/opt/local/bin/gnuplot";

		RegularFile pdfFile = new RegularFile(plot.getDirectory(),
				plot.getName() + ".pdf");

		if (data)
		{
			plot.collectFunctions().forEach(f -> {
				plot.experiment.log(
						"creating GNUPlot DAT file for function \"" + f.getName() + '"');
				getDataFile(f).setContent(
						toGNUPlotData(f, printStdDev, samplingProbability, avg)
								.getBytes());
			});
		}

		if (executeGNUPlot)
		{
			RegularFile cmdFile = new RegularFile(plot.getDirectory(),
					plot.getName() + ".gnuplot");
			cmdFile.setContent(toGNUPlotCommands(plot, pdfFile, defaultStyle).getBytes());
			Cout.debugSuperVisible(cmdFile.getContentAsText());
			plot.experiment.log("running GNUPlot");
			Proces.exec(CMD, cmdFile.getPath());
		}

		return pdfFile;
	}

	private RegularFile getDataFile(Function c)
	{
		return new RegularFile(c.getDirectory(), c.getName() + ".dat");
	}

	public String toGNUPlotData(Function f, boolean stdDev, double samplingProbability,
			AVGMODE mode)
	{
		StringBuilder s = new StringBuilder();
		s.append("# " + f.getName() + "\n\n");
		Double2ObjectMap<DoubleList> x2y = f.toXY();

		double[] allX = new DoubleArrayList(x2y.keySet()).toDoubleArray();
		Arrays.sort(allX);

		for (double x : allX)
		{
			if (Math.random() < samplingProbability)
			{
				s.append(x);
				s.append('\t');
				double[] ys = x2y.get(x).toDoubleArray();
				s.append(mode.compute(ys));

				if (stdDev)
				{
					s.append('\t');
					s.append(MathsUtilities.stdDev(ys));
				}

				s.append('\n');
			}
		}

		return s.toString();
	}

	private String toGNUPlotCommands(Plot plot, RegularFile pdfFile, String defaultStyle)
	{
		if (plot == null)
			throw new NullPointerException();

		StringBuilder b = new StringBuilder();
		b.append("set term pdf\n");
		b.append("set output \"" + pdfFile.getPath() + "\"\n");
		b.append("set title \"" + plot.getName().trim() + "\"\n");
		b.append("set xlabel \"" + plot.getXLegend().trim() + "\"\n");
		b.append("set ylabel \"" + plot.getYLegend().trim() + "\"\n");

		if (plot.getXRange() != null)
		{
			b.append("set xrange " + plot.getXRange().toGNUPlot() + "\n");
		}

		if (plot.getYRange() != null)
		{
			b.append("set yrange " + plot.getYRange().toGNUPlot() + "\n");
		}

		b.append("set key left top\n");
		b.append((plot.isLogarithmicXAxis() ? "" : "un") + "set logscale x\n");
		b.append((plot.isLogarithmicYAxis() ? "" : "un") + "set logscale y\n");

		b.append("plot ");

		List<Function> functions = plot.collectFunctions();

		for (int i = 0; i < functions.size(); ++i)
		{
			if (i > 0)
			{
				b.append(", ");
			}

			Function f = functions.get(i);
			String style = f.getGNUPlotStyle();

			if (style == null)
				style = defaultStyle;
			b.append('"' + getDataFile(f).getPath() + "\" with " + style + " title \""
					+ f.getName() + "\"");
		}

		return b.toString();
	}
}
