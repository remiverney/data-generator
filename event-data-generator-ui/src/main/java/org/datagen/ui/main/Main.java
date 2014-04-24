package org.datagen.ui.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

public class Main {

	/**
	 * log4j tracer for this class.
	 */
	private static final Logger TRACER = Logger.getLogger(Main.class);

	private Main() {
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		BasicConfigurator.configure();

		try {
			parseCmdLine(args);
		} catch (ParseException e) {
			System.err.println(e);
		}
	}

	private static void parseCmdLine(String args[]) throws ParseException {
		Options options = new Options();
		CommandLineParser parser = new PosixParser();
		@SuppressWarnings("unused")
		CommandLine cmd = parser.parse(options, args);

		startApp();
	}

	private static void startApp() {
		try {
			DataGeneratorApp window = new DataGeneratorApp();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			TRACER.error("Unexpected exception", e);
			throw e;
		}
	}

}
