package jexperiment;

import toools.io.file.RegularFile;
import toools.progression.LongProcess;

public abstract class Plotter {
	public abstract RegularFile plot(Plot p);

	public void plot(Plots e) {
		LongProcess lp = new LongProcess("plotting", " plot/s", -1);

		e.forEachPlot(plot -> {
			plot(plot);
			lp.sensor.progressStatus++;
		});

		lp.end();
	}

}
