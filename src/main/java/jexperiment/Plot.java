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
import java.util.List;

import toools.gui.PDFRenderingAWTComponent;
import toools.io.file.Directory;
import toools.io.file.RegularFile;
import toools.math.Interval;

public class Plot extends InDirectoryObject {
	final Plots experiment;

	public static final Interval MIN_MAX = new Interval(0, 0);

	Plot(Plots experiment, String title, String xLegend, String yLegend) {
		super(new Directory(experiment.getDirectory(), title));
		this.experiment = experiment;
		getFunctionsDirectory().ensureExists();
		setXLegend(xLegend);
		setYLegend(yLegend);
		setLogarithmicXAxis(false);
		setLogarithmicYAxis(false);
		setXRange(null);
		setYRange(null);
	}

	Plot(Plots experiment, Directory d) {
		super(d);
		System.out.println("new plot found at " + d);
		this.experiment = experiment;
	}

	public Directory getFunctionsDirectory() {
		return new Directory(getDirectory(), "functions");
	}

	public void display() {
		RegularFile of = new GNUPlot().plot(this);
		PDFRenderingAWTComponent c = new PDFRenderingAWTComponent();
		c.setPDFData(of.getContent(), 0);
		toools.gui.Utilities.displayInJFrame(c, "2D plot");
	}

	public Function createFunction(String name) {
		return new Function(name, this);
	}

	public String getXLegend() {
		return readString("xlegend");
	}

	public void setXLegend(String l) {
		writeString("xlegend", l);
	}

	public String getYLegend() {
		return readString("ylegend");
	}

	public void setYLegend(String l) {
		writeString("ylegend", l);
	}

	public Interval getXRange() {
		return Interval.valueOf(readString("xrange"));
	}

	public void setXRange(Interval r) {
		writeString("xrange", r == null ? "auto" : r.toString());
	}

	public Interval getYRange() {
		return Interval.valueOf(readString("yrange"));
	}

	public void setYRange(Interval r) {
		writeString("yrange", r == null ? "auto" : r.toString());
	}

	public boolean isLogarithmicXAxis() {
		return readBoolean("xlog");
	}

	public void setLogarithmicXAxis(boolean logarithmicXAxis) {
		writeBoolean("xlog", logarithmicXAxis);
	}

	public boolean isLogarithmicYAxis() {
		return readBoolean("ylog");
	}

	public void setLogarithmicYAxis(boolean logarithmicYAxis) {
		writeBoolean("ylog", logarithmicYAxis);
	}

	public String getName() {
		return getDirectory().getName();
	}

	public List<Function> collectFunctions() {
		List<Function> fctList = new ArrayList<>();
		getFunctionsDirectory().listDirectories().forEach(d -> fctList.add(new Function(this, d)));
		return fctList;
	}
}
