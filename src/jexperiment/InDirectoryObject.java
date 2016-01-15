package jexperiment;

import toools.io.file.Directory;

public class InDirectoryObject
{
	private final Directory directory;

	public InDirectoryObject(Directory d)
	{
		this.directory = d;

		if (!this.directory.exists())
		{
			this.directory.mkdirs();
		}

	}

	public void clear()
	{
		if (getDirectory().exists())
		{
			getDirectory().deleteRecursively();
		}
	}

	public Directory getDirectory()
	{
		return directory;
	}

}
