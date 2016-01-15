package jexperiment;

import toools.io.file.Directory;

public class NamedInDirectoryObject extends InDirectoryObject
{
	private final String name;

	public NamedInDirectoryObject(String name, Directory d)
	{
		super(d);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

}
