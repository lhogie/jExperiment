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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import toools.io.file.Directory;

public class Experiment extends NamedInDirectoryObject
{
	private final List<NumericalPlot2D> plots = new ArrayList<NumericalPlot2D>();

	public Experiment(String name)
	{
		this(name, new Directory(Directory.getHomeDirectory(), "jExperiment"));
	}

	public Experiment(String name, Directory directory)
	{
		super(name, new Directory(directory, name));

		log("Starting experiment " + name);
		log("Results will be available in directory " + getDirectory().getPath());
	}

	public void display() throws IOException
	{
		for (NumericalPlot2D plot : getPlots())
		{
			plot.display();
		}
	}

	public void plot() throws IOException
	{
		for (NumericalPlot2D plot : getPlots())
		{
			plot.plot();
		}

		log("Completed.");
	}

	public void log(String s)
	{
		System.out.println(s);
	}

	public void log(Object... p)
	{
		if (p.length % 2 == 1)
			throw new IllegalArgumentException("the format of parameters is: \"name\", value, \"name2\", value2, ..., \"nameN\", valueN");

		StringBuilder b = new StringBuilder();
		b.append("# executing ");

		for (int i = 0; i < p.length; i += 2)
		{
			b.append("\t" + p[i] + "=" + p[i + 1]);
		}

		log(b.toString());
	}

	public List<NumericalPlot2D> getPlots()
	{
		return Collections.unmodifiableList(plots);
	}

	public void clear()
	{
		log("clearing results");
		super.clear();
	}

	public NumericalPlot2D createPlot(String title, String x, String y, Object object)
	{
		NumericalPlot2D p = new GNUPlotNumericalPlot2D(this, title, x, y, object);
		plots.add(p);
		return p;
	}

	public NumericalPlot2D findPlot(String title)
	{
		for (NumericalPlot2D c : plots)
		{
			if (c.getName().equals(title))
			{
				return c;
			}
		}

		return null;
	}

}
