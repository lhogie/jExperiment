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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.math.MathsUtilities;

public class Configuration extends InDirectoryObject
{
	final Function parentFunction;
	final String id;

	Configuration(Function parentFunction, String id)
	{
		super(new Directory(parentFunction.configurationsDirectory(), id));
		this.parentFunction = parentFunction;
		getDirectory().ensureExists();
		this.id = id;
	}

	Configuration(Function parentFunction, Directory d)
	{
		super(d);
		this.parentFunction = parentFunction;
		this.id = d.getName();
	}

	public void addMeasure(double x, double y)
	{
		parentFunction.parentPlot.experiment.log("plot",
				parentFunction.parentPlot.getName(), "function", parentFunction.getName(),
				"x", x, "y", y, "run", countMeasures());
		String line = x + "\t" + y + "\n";
		getDirectory().getChildRegularFile("points").append(line.getBytes());
	}

	public double computeAverageY(double[] measures)
	{
		return MathsUtilities.avg(measures);
	}

	public static class Entry
	{
		double x, y, stdDev;
	}

	public static class Point
	{
		double x, y;
	}

	public List<Point> collectMeasures()
	{
		RegularFile pointFile = getDirectory().getChildRegularFile("points");

		if (pointFile.exists())
		{
			List<String> lines = pointFile.getLines();
			List<Point> points = new ArrayList<>(lines.size());

			for (String line : lines)
			{
				int tab = line.indexOf('\t');
				Point p = new Point();
				p.x = Double.valueOf(line.substring(0, tab)).doubleValue();
				p.y = Double.valueOf(line.substring(tab + 1)).doubleValue();
				points.add(p);
			}

			return points;
		}
		else
		{
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public String toString()
	{
		return "config " + id;
	}

	public int countMeasures()
	{
		return collectMeasures().size();
	}
}
