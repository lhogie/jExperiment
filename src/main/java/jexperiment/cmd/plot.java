package jexperiment.cmd;

import java.io.IOException;

import jexperiment.Experiment;
import toools.io.file.Directory;

public class plot
{
	public static void main(String[] args) throws IOException
	{
		Experiment e = new Experiment(new Directory(args[0]));
		e.display();
	}
}
