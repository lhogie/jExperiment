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
import java.util.List;

import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import jexperiment.Configuration.Point;
import toools.io.file.Directory;

public class Function extends InDirectoryObject
{
	final public Plot parentPlot;

	Function(String name, Plot parentPlot)
	{
		super(new Directory(parentPlot.getFunctionsDirectory(), name));
		this.parentPlot = parentPlot;
		configurationsDirectory().ensureExists();
		setUseErrorBars(true);
	}

	Function(Plot parentPlot, Directory d)
	{
		super(d);
		this.parentPlot = parentPlot;
	}

	public boolean showStandardDeviation()
	{
		return readBoolean("use_error_bars");
	}

	public void setUseErrorBars(boolean useErrorBars)
	{
		writeBoolean("use_error_bars", useErrorBars);
	}

	public List<Configuration> collectConfigurations()
	{
		List<Configuration> configs = new ArrayList<>();
		configurationsDirectory().listDirectories()
				.forEach(d -> configs.add(new Configuration(this, d)));
		return configs;
	}

	public Directory configurationsDirectory()
	{
		return new Directory(getDirectory(), "configs");
	}

	public Double2ObjectMap<DoubleList> toXY()
	{
		Double2ObjectMap<DoubleList> x2y = new Double2ObjectOpenHashMap<>();

		collectConfigurations().forEach(c -> {
			for (Point p : c.collectMeasures())
			{
				DoubleList yList = x2y.get(p.x);

				if (yList == null)
				{
					x2y.put(p.x, yList = new DoubleArrayList());
				}

				yList.add(p.y);
			}
		});

		return x2y;
	}

	public String getGNUPlotStyle()
	{
		return readString("style");
	}

	public void setSyle(String s)
	{
		writeString("style", s);
	}

	public Color getColor()
	{
		return readColor("color");
	}

	public void setColor(Color color)
	{
		writeColor("color", color);
	}

	public Configuration configuration(String id)
	{
		return new Configuration(this, id);
	}

}
