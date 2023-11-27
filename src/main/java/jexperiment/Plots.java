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
import java.util.function.Consumer;

import toools.io.file.Directory;

public class Plots extends InDirectoryObject {
	
	public Plots(Directory directory) {
		super(directory);
	}

	public void display() throws IOException {
		forEachPlot(p -> p.display());
	}

	public void log(String s) {
		System.out.println("jExperiment - " + s);
	}

	public void log(Object... p) {
		if (p.length % 2 == 1)
			throw new IllegalArgumentException(
					"the format of parameters is: \"name\", value, \"name2\", value2, ..., \"nameN\", valueN");

		StringBuilder b = new StringBuilder();
		b.append("# executing ");

		for (int i = 0; i < p.length; i += 2) {
			b.append("\t" + p[i] + "=" + p[i + 1]);
		}

		log(b.toString());
	}

	public void forEachPlot(Consumer<Plot> c) {
		getDirectory().listDirectories().forEach(d -> c.accept(new Plot(this, d)));
	}

	public void clear() {
		log("clearing results");
		super.clear();
	}

	public Plot createPlot(String title, String xLegend, String yLegend) {
		return new Plot(this, title, xLegend, yLegend);
	}

	public void gnuplot(boolean data, boolean plot, double samplingProbability, boolean printStdDev, AVGMODE avg,
			String defaultStyle) {
		if (!getDirectory().exists())
			throw new IllegalStateException("directory does not exist: " + getDirectory());

		forEachPlot(p -> new GNUPlot().plot(p, data, plot, samplingProbability, printStdDev, avg, defaultStyle));
	}
}
