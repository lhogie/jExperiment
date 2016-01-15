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

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.math.MathsUtilities;

import com.carrotsearch.hppc.DoubleArrayList;

public class Point
{
	private final double x;
	private final DoubleArrayList ys = new DoubleArrayList();
	private final RegularFile xFile;
	private final Curve parentCurve;

	public Point(Curve parentCurve, Directory d, double x)
	{
		this.parentCurve = parentCurve;
		this.xFile = new RegularFile(d, String.valueOf(x) + ".dat");
		this.x = x;

		if (xFile.exists())
		{
			try
			{
				for (String line : xFile.getLines())
				{
					try
					{
						ys.add(Double.valueOf(line));
					}
					catch (NumberFormatException e)
					{
						throw new IllegalStateException("file " + xFile.getPath() + " has dirty data in each. Every line should be a number");
					}
				}
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}

	public double getX()
	{
		return x;
	}

	public void addY(double y)
	{
		parentCurve.parentPlot.getParentExperiment().log("plot", parentCurve.parentPlot.getName(), "curve", parentCurve.getName(), "x", x, "y", y,
				"run", getNumberOfMeasures());

		this.ys.add(y);

		updateFile();
	}

	private void updateFile()
	{
		try
		{
			StringBuilder b = new StringBuilder();

			for (double d : ys.toArray())
			{
				b.append(d);
				b.append('\n');
			}

			xFile.setContentAsUTF8(b.toString());
		}
		catch (IOException e)
		{
			throw new IllegalStateException(e);
		}
	}

	public double computeAverageY()
	{
		return MathsUtilities.computeAverage(ys.toArray());
	}

	public double computeStandardDeviationYInf()
	{
		return MathsUtilities.computeStandardDeviationOfInferiorDeviation(ys.toArray());
	}

	public double computeStandardDeviationYSup()
	{
		return MathsUtilities.computeStandardDeviationOfSuperiorDeviation(ys.toArray());
	}

	public String toGNUPlotData(boolean reportError)
	{
		StringBuilder b = new StringBuilder();
		b.append(x);
		b.append('\t');
		b.append(computeAverageY());

		if (reportError && ys.size() > 1)
		{
			b.append('\t');
			b.append(computeStandardDeviationYInf());
			b.append('\t');
			b.append(computeStandardDeviationYSup());
		}

		return b.toString();
	}

	public int getNumberOfMeasures()
	{
		return ys.size();
	}

	public void scale(double n)
	{
		for (int i = 0; i < ys.elementsCount; ++i)
		{
			ys.buffer[i] *= n;
		}
	}

	public void translate(double y)
	{
		for (int i = 0; i < ys.elementsCount; ++i)
		{
			ys.buffer[i] += y;
		}
	}

	public void merge(Point cp)
	{
		if (cp.getX() != x)
			throw new IllegalArgumentException("not same x");

		ys.addAll(cp.ys);
	}
}
