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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import toools.io.file.Directory;
import toools.io.file.RegularFile;

public class Curve extends NamedInDirectoryObject
{
	private final List<Point> points = new ArrayList<Point>();
	private final RegularFile dataFile;
	private Color color;
	private final Object sensor;
	 NumericalPlot2D parentPlot;
	 private boolean useErrorBars = true;



	public Curve(String name, Object sensor, NumericalPlot2D plot)
	{
		super(name, new Directory(plot.getDirectory(), name));
		this.sensor = sensor;
		this.dataFile = new RegularFile(plot.getDirectory(), getName() + ".dat");
		this.parentPlot = plot;
	}
	
	public boolean isUseErrorBars()
	{
		return useErrorBars;
	}

	public void setUseErrorBars(boolean useErrorBars)
	{
		this.useErrorBars = useErrorBars;
	}
	public Object getObject()
	{
		return sensor;
	}

	public List<Point> getPoints()
	{
		return Collections.unmodifiableList(points);
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public Point getPoint(double x)
	{
		for (Point p : points)
		{
			if (p.getX() == x)
			{
				return p;
			}
		}

		Point p = new Point(this, getDirectory(), x);
		points.add(p);
		return p;
	}

	public RegularFile getDataFile()
	{
		return dataFile;
	}

	public int getNumberOfValuesAt(double x)
	{
		return getPoint(x).getNumberOfMeasures();
	}

	public void addPoint(double x, double y)
	{
		getPoint(x).addY(y);
	}

	public String toGNUPlotData()
	{
		StringBuilder b = new StringBuilder();
		b.append("# " + getName() + " (" + points.size() + " points)\n\n");

		for (Point p : points)
		{
			b.append(p.toGNUPlotData(isUseErrorBars()));
			b.append('\n');
		}

		return b.toString();
	}

	public void scale(double n)
	{
		for (Point p : points)
		{
			p.scale(n);
		}
	}

	public void translate(double n)
	{
		for (Point p : points)
		{
			p.translate(n);
		}
	}

	public void merge(Curve c)
	{
		for (Point cp : c.points)
		{
			Point p = getPoint(cp.getX());
			p.merge(cp);
		}
	}
}
