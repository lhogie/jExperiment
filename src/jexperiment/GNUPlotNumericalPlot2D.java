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

import java.io.IOException;
import java.util.List;

import toools.extern.Proces;
import toools.io.file.RegularFile;

public class GNUPlotNumericalPlot2D extends NumericalPlot2D
{
	private final RegularFile epsFile;

	public GNUPlotNumericalPlot2D(Experiment experiment, String title, String x, String y, Object o)
	{
		super(experiment, title, x, y, o);
		epsFile = new RegularFile(getPDFOutputFile().getPath().replaceAll("pdf$", "eps"));
	}

	@Override
	public void plot() throws IOException
	{
		getExperiment().log("plotting " + getPDFOutputFile().getPath());

		for (Curve c : getCurves())
		{
			c.getDataFile().setContent(c.toGNUPlotData().getBytes());
		}

		RegularFile cmdFile = new RegularFile(getDirectory(), getName() + ".gnuplot");
		cmdFile.setContent(toGNUPlotCommands().getBytes());
		Proces.exec("gnuplot", cmdFile.getPath());
		getExperiment().log("converting to PDF...");
		Proces.exec("epstopdf", "-outfile=" + getPDFOutputFile().getPath(), epsFile.getPath());

	}

	private String toGNUPlotCommands()
	{
		StringBuilder b = new StringBuilder();
		b.append("set term postscript eps color\n");
		b.append("set title \"" + getName() + "\"\n");
		b.append("set xlabel \"" + getXLegend() + "\"\n");
		b.append("set ylabel \"" + getYLegend() + "\"\n");

		if (getXRange() != null)
		{
			b.append("set xrange " + getXRange().toGNUPlot() + "\n");
		}

		if (getYRange() != null)
		{
			b.append("set yrange " + getYRange().toGNUPlot() + "\n");
		}

		b.append("set output \"" + epsFile.getPath() + "\"\n");
		b.append("set key left top\n");
		b.append((isLogarithmicXAxis() ? "" : "un") + "set logscale x\n");
		b.append((isLogarithmicYAxis() ? "" : "un") + "set logscale y\n");

		b.append("plot ");

		List<Curve> curves = getCurves();

		for (int c = 0; c < curves.size(); ++c)
		{
			if (c > 0)
			{
				b.append(", ");
			}

			Curve curve = curves.get(c);
			b.append('"' + curve.getDataFile().getPath() + "\" with errorlines title \"" + curve.getName() + "\"");
		}

		return b.toString();
	}
}
