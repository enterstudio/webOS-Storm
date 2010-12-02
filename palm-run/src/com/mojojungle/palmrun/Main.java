package com.mojojungle.palmrun;

import com.palm.webos.packager.PackageUtil;
import com.palm.webos.packager.PackagerException;
import com.palm.webos.tools.internal.shell.MessageConsole;
import com.palm.webos.tools.shell.CommandException;
import com.palm.webos.tools.shell.Install;
import com.palm.webos.tools.shell.Launch;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) throws CommandException, PackagerException {
		String targetDevice = args[0];
		String appFolder = args[1];

		String[] arguments = new String[args.length - 2];
		System.arraycopy(args, 2, arguments, 0, arguments.length);
		Properties properties = new Properties();
		for (String argument : arguments) {
			String[] kv = argument.split("=");
			properties.put(kv[0], kv[1]);
		}

		boolean doLog = Boolean.parseBoolean(properties.getProperty("log", "true"));

		File appDir = new File(appFolder);
		File outputDir = appDir.getParentFile();

		Logger logger = Logger.getLogger("com.palm.webos.tools");
		logger.setUseParentHandlers(false);

		com.palm.webos.tools.shell.Package p = new com.palm.webos.tools.shell.Package();
		p.init("palm-package", new String[]{"-o", outputDir.getAbsolutePath(), appDir.getAbsolutePath()},
				new MessageConsole("palm-package", System.out, System.err), logger);
		int exitCode = p.run();
		checkExit(exitCode, "Error packaging the app, exiting.");

		String ipkFileName = PackageUtil.getIpkFilename(appDir);
		File ipkFile = new File(outputDir, ipkFileName);

		Install install = new Install();
		install.init("palm-install", new String[]{"-d", targetDevice, ipkFile.getAbsolutePath()},
				new MessageConsole("palm-install", System.out, System.err), logger);
		exitCode = install.run();
		checkExit(exitCode, "Error installing the app, exiting.");


		Matcher matcher = Pattern.compile("(.*?)_.*?_all.ipk").matcher(ipkFileName);
		matcher.find();
		String appId = matcher.group(1);

		Launch launch = new Launch();
		launch.init("palm-launch", new String[]{"-d", targetDevice, appId},
				new MessageConsole("palm-launch", System.out, System.err), logger);
		exitCode = launch.run();
		checkExit(exitCode, "Error launching the app, exiting.");

		if (doLog) {
			Executor.execute("palm-log", "-d", targetDevice, "-f", appId);
		}
	}

	private static void checkExit(int exitCode, String message) {
		if (exitCode != 0) {
			System.err.println(message);
			System.exit(exitCode);
		}
	}
}
