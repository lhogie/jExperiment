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

package jexperiment.cmd;

import j4u.License;

public abstract class Application extends j4u.Application {
	@Override
	public String getAuthor() {
		return "Luc Hogie";
	}

	@Override
	public License getLicence() {
		return License.ApacheV2;
	}

	@Override
	public String getYear() {
		return "2001-2020";
	}

	@Override
	public String getDescription() {
		return "Help plotting results out of Java algorithms";
	}
}
