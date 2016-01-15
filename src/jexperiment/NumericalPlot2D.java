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

import toools.gui.PDFRenderingAWTComponent;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.math.Interval;

public abstract class NumericalPlot2D extends NamedInDirectoryObject
{
	private final String xLegend;
	private final String yLegend;
	private final List<Curve> curves = new ArrayList();
	private final Experiment experiment;
	private final RegularFile pdfOutputFile;
	private boolean logarithmicXAxis = false;
	private boolean logarithmicYAxis = false;
	private Interval xRange, yRange;
	private Object object;
	private Experiment parentExperiment;

	public static final Interval MIN_MAX = new Interval(0, 0);

	public NumericalPlot2D(Experiment experiment, String title, String x, String y, Object o)
	{
		super(title, new Directory(experiment.getDirectory(), title));
		this.xLegend = x;
		this.yLegend = y;
		this.experiment = experiment;
		this.pdfOutputFile = new RegularFile(getDirectory(), getName() + ".pdf");
		this.object = o;
		this.parentExperiment = experiment;
	}

	public Experiment getParentExperiment()
	{
		return parentExperiment;
	}

	public Object getObject()
	{
		return object;
	}

	public Experiment getExperiment()
	{
		return experiment;
	}

	public List<Curve> getCurves()
	{
		return Collections.unmodifiableList(curves);
	}

	public RegularFile getPDFOutputFile()
	{
		return pdfOutputFile;
	}

	public abstract void plot() throws IOException;

	public boolean isLogarithmicXAxis()
	{
		return logarithmicXAxis;
	}

	public void setLogarithmicXAxis(boolean logarithmicXAxis)
	{
		this.logarithmicXAxis = logarithmicXAxis;
	}

	public boolean isLogarithmicYAxis()
	{
		return logarithmicYAxis;
	}

	public void setLogarithmicYAxis(boolean logarithmicYAxis)
	{
		this.logarithmicYAxis = logarithmicYAxis;
	}

	public void display() throws IOException
	{
		plot();

		try
		{
			PDFRenderingAWTComponent c = new PDFRenderingAWTComponent();
			c.setPDFData(getPDFOutputFile().getContent(), 0);
			toools.gui.Utilities.displayInJFrame(c, "2D plot");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Curve findCurve(String title)
	{
		for (Curve c : curves)
		{
			if (c.getName().equals(title))
			{
				return c;
			}
		}

		return null;
	}

	public Curve createCurve(String name)
	{
		return createCurve(name, null);
	}

	public Curve createCurve(String name, Object sensor)
	{
		Curve c = new Curve(name, sensor, this);
		curves.add(c);
		return c;
	}

	public String getXLegend()
	{
		return xLegend;
	}

	public String getYLegend()
	{
		return yLegend;
	}

	public Interval getXRange()
	{
		return xRange;
	}

	public void setXRange(Interval xRange)
	{
		this.xRange = xRange;
	}

	public Interval getYRange()
	{
		return yRange;
	}

	public void setYRange(Interval yRange)
	{
		this.yRange = yRange;
	}

	public void scale(double factor)
	{
		for (Curve p : curves)
		{
			p.scale(factor);
		}
	}

	public void translate(double offset)
	{
		for (Curve p : curves)
		{
			p.translate(offset);
		}
	}
}
