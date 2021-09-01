package jexperiment.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import jexperiment.Experiment;
import jexperiment.Function;
import jexperiment.GNUPlot;
import jexperiment.Plot;
import toools.StopWatch;
import toools.StopWatch.UNIT;
import toools.io.file.Directory;
import toools.reflect.Clazz;

public class ListBenchmark
{
	interface ListInserter
	{
		void insertInList(List l, Random r);

		String getName();
	}

	public static void main(String[] args) throws IOException
	{
		Experiment e = new Experiment(new Directory("Linked list vs. Array list"));
		Random r = new Random();

		ListInserter randomInserter = new ListInserter()
		{
			@Override
			public void insertInList(List l, Random r)
			{
				int index = r.nextInt(l.size());
				l.add(index, l);
			}

			@Override
			public String getName()
			{
				return "insert at random position";
			}
		};

		ListInserter endInserter = new ListInserter()
		{
			@Override
			public void insertInList(List l, Random r)
			{
				l.add(l);
			}

			@Override
			public String getName()
			{
				return "insert at the end (append)";
			}

		};

		ListInserter startInserter = new ListInserter()
		{
			@Override
			public void insertInList(List l, Random r)
			{
				l.add(0, l);
			}

			@Override
			public String getName()
			{
				return "insert at the beginning";
			}

		};

		List<ListInserter> insertionMethods = new ArrayList<>();
		insertionMethods.add(randomInserter);
		insertionMethods.add(startInserter);
		insertionMethods.add(endInserter);

		for (ListInserter i : insertionMethods)
		{
			Plot p = e.createPlot(i.getName(), "size", "time (ns)");

			for (Class clazz : new Class[] { ArrayList.class, Vector.class,
					LinkedList.class })
			{
				Function c = p.createFunction(clazz.getName());

				// try list from 10 to 10^6 elements
				for (int n = 10; n <= 1000000; n *= 2)
				{
					// runs 10 runs for a better statistical confidence
					for (int run = c.configuration("" + n)
							.countMeasures(); run <= 10; ++run)
					{
						// creates the list
						List l = createNSizeList(clazz, n);

						// compute the duration of the operation
						StopWatch sw = new StopWatch(UNIT.ns);
						i.insertInList(l, r);
						c.configuration("" + n).addMeasure(n, sw.getElapsedTime());
					}
				}
			}
		}

		new GNUPlot().plot(e);
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
