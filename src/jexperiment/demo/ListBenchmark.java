package jexperiment.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import jexperiment.Curve;
import jexperiment.Experiment;
import jexperiment.NumericalPlot2D;
import toools.Clazz;
import toools.StopWatch;
import toools.StopWatch.UNIT;

public class ListBenchmark
{
	interface ListInserter
	{
		void insertInList(List l, Random r);
	}

	public static void main(String[] args) throws IOException
	{
		Experiment e = new Experiment("Linked list vs. Array list");
		Random r = new Random();

		e.createPlot("Insertion at random positions", "size", "time (ns)", new ListInserter() {
			@Override
			public void insertInList(List l, Random r)
			{
				int index = r.nextInt(l.size());
				l.add(index, l);
			}
		});

		e.createPlot("Insertion at the end", "size", "time (ns)", new ListInserter() {
			@Override
			public void insertInList(List l, Random r)
			{
				l.add(l);
			}
		});

		e.createPlot("Insertion at the begining", "size", "time (ns)", new ListInserter() {
			@Override
			public void insertInList(List l, Random r)
			{
				l.add(0, l);
			}
		});

		// show the directory where the result will be written
		e.getDirectory().open();

		for (NumericalPlot2D p : e.getPlots())
		{
			p.createCurve("linked list", LinkedList.class);
			p.createCurve("array list", ArrayList.class);
			p.createCurve("vector", Vector.class);

			for (Curve c : p.getCurves())
			{
				// try list from 10 to 10^6 elements
				for (int n = 10; n <= 1000000; n *= 2)
				{
					// runs 10 runs for a better statistical confidence
					for (int run = c.getNumberOfValuesAt(n); run <= 10; ++run)
					{
						// creates the list
						List l = createNSizeList((Class) c.getObject(), n);

						// compute the duration of the operation
						StopWatch sw = new StopWatch(UNIT.ns);
						((ListInserter) p.getObject()).insertInList(l, r);
						c.addPoint(n, sw.getElapsedTime());
					}
				}
			}
		}

		e.plot();
	}

	private static List createNSizeList(Class c, int n)
	{
		List l = (List) Clazz.makeInstance(c);

		while (l.size() < n)
		{
			l.add(l);
		}

		return l;
	}

}
