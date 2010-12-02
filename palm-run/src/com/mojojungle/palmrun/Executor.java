package com.mojojungle.palmrun;

public class Executor {

	public static void execute(String... params) {
		com.palm.webos.tools.shell.CommandShell.main(params);
	}
}
