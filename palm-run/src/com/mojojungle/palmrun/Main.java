package com.mojojungle.palmrun;

import com.palm.webos.sdk.SDK;
import com.palm.webos.shell.CommandSet;
import com.palm.webos.shell.CommandShell;
import com.palm.webos.tools.packager.PackageConfig;
import com.palm.webos.tools.packager.PackageContents;
import com.palm.webos.tools.packager.PackageScanner;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

	public static void main(String[] args) {
		int sdkVersion = getSDKVersion();
		String targetDevice = args[0];
		String appFolder = args[1];

		String[] arguments = new String[args.length - 2];
		System.arraycopy(args, 2, arguments, 0, arguments.length);
		Properties settings = new Properties();
		for (String argument : arguments) {
			String[] kv = argument.split("=");
			settings.put(kv[0], kv[1]);
		}

		boolean doLog = Boolean.parseBoolean(settings.getProperty("log", "true"));
		boolean useV1 = Boolean.parseBoolean(settings.getProperty("use-v1", "false"));

		File appDir = new File(appFolder);
		File outputDir = appDir.getParentFile();

		Logger logger = Logger.getLogger("com.palm.webos.tools");
		logger.setUseParentHandlers(false);

		if (sdkVersion == 1) {
			System.err.println(
					"Running Apps is not supported anymore with the webOS SDK 1.4.5\n" +
							"Please update to the most recent webOS SDK 2.1 (http://developer.palm.com)\n" +
							"You will still be able to run webOS 1.4.5 in the emulator and create 1.x compatible ipk's.");
			/*
			com.palm.webos.tools.shell.Main.main(new String[]{"palm-package", "-o", outputDir.getAbsolutePath(), appDir.getAbsolutePath()});

			com.palm.webos.tools.shell.Package p = new com.palm.webos.tools.shell.Package();
			p.init("palm-package", new String[]{"-o", outputDir.getAbsolutePath(), appDir.getAbsolutePath()},
					new MessageConsole("palm-package", System.out, System.err, logger), logger);
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
				com.palm.webos.tools.shell.CommandShell.main(new String[]{"palm-log", "-d", targetDevice, "-f", appId});
				Executor.execute("palm-log", "-d", targetDevice, "-f", appId);
			}
			*/
		} else {
			CommandSet commands = new CommandSet();
			commands.addCommand("package", "palm-package", com.palm.webos.tools.shell.Package.class);
			commands.addCommand("install", "palm-install", com.palm.webos.tools.shell.Install.class);
			commands.addCommand("launch", "palm-launch", com.palm.webos.tools.shell.Launch.class);
			commands.addCommand("log", "palm-log", com.palm.webos.tools.shell.Log.class);

			CommandShell shell = new CommandShell(SDK.getInfo(), commands, logger);
			String[] ppArgs;
			if (useV1) {
				ppArgs = new String[]{"--use-v1-format", "-o", outputDir.getAbsolutePath(), appDir.getAbsolutePath()};
			} else {
				ppArgs = new String[]{"-o", outputDir.getAbsolutePath(), appDir.getAbsolutePath()};
			}
			int exitCode = shell.parseCommand("palm-package", ppArgs);

			PackageConfig config = new PackageConfig();
			config.addSource(appDir);
			PackageScanner scanner = new PackageScanner(config);
			PackageContents contents = null;
			try {
				contents = scanner.readContents();
			} catch (Exception e) {
				exitCode = 2;
			}

			checkExit(exitCode, "Error packaging the app, exiting.");

			String appId = contents.getPackageId();
			String ipkFileName = appId + "_" + contents.getPackageVersion() + "_all.ipk";
			File ipkFile = new File(outputDir, ipkFileName);

			exitCode = shell.parseCommand("palm-install", new String[]{"-d", targetDevice, ipkFile.getAbsolutePath()});
			checkExit(exitCode, "Error installing the app, exiting.");

			exitCode = shell.parseCommand("palm-launch", new String[]{"-d", targetDevice, appId});
			checkExit(exitCode, "Error launching the app, exiting.");

			if (doLog) {
				shell.parseCommand("palm-log", new String[]{"-d", targetDevice, "-f", appId});
			}
		}
	}

	/**
	 * Retrurns 1 or 2 for 1.x and 2.x versions respectively
	 *
	 * @return 1 or 2
	 */
	private static int getSDKVersion() {
		try {
			Class.forName("com.palm.webos.sdk.SDK");
			return 2;
		} catch (ClassNotFoundException e) {
			return 1;
		}
	}

	private static void checkExit(int exitCode, String message) {
		if (exitCode != 0) {
			System.err.println(message);
			System.exit(exitCode);
		}
	}
}
