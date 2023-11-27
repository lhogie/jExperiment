package jexperiment.cmd;

import java.io.IOException;

import jexperiment.Plots;
import toools.io.file.Directory;

public class plot
{
	public static void main(String[] args) throws IOException
	{
		Plots e = new Plots(new Directory(args[0]));
		e.display();
	}
}
