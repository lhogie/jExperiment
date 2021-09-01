package jexperiment.cmd;

import java.io.IOException;
import java.util.List;

import jexperiment.Configuration;
import jexperiment.Experiment;
import toools.io.file.Directory;

public class print
{
	public static void main(String[] args) throws IOException
	{
		Experiment e = new Experiment(new Directory(args[0]));
		double d = args.length > 1 ? Double.valueOf(args[1]) : 1;

		e.forEachPlot(plot -> {
			System.out.println(plot.getName());
			plot.collectFunctions().forEach(f -> {
				System.out.println("\t" + f.getName());
				List<Configuration> configs = f.collectConfigurations();

				for (Configuration config : configs)
				{
					if (Math.random() < d)
					{
						System.out.println("\t\t" + config);
					}
				}
			});
		});
	}
}
