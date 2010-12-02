package com.mojojungle.webosstorm.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.mojojungle.webosstorm.WebOSStorm;

import java.io.File;

public class WebOSRunningState extends CommandLineState {

	public WebOSRunningState(ExecutionEnvironment executionEnvironment) {
		super(executionEnvironment);
		Project project = executionEnvironment.getProject();
		TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
		builder.setViewer(false);
		this.setConsoleBuilder(builder);
	}

	@Override
	protected OSProcessHandler startProcess() throws ExecutionException {
		WebOSRunConfiguration config = (WebOSRunConfiguration) getRunnerSettings().getRunProfile();
		VirtualFile appFolder = config.getAppFolder();
		RunTarget target = config.getTarget();
		boolean loggingEnabled = config.LOGGING;

		if(appFolder == null) {
			throw new ExecutionException("Please select a valid webOS application directory");
		}

		IdeaPluginDescriptor descriptor = PluginManager.getPlugin(PluginId.getId(WebOSStorm.PLUGIN_ID));
		File palmRunJar = new File(descriptor.getPath(), "lib/palm-run.jar");

		GeneralCommandLine command = new GeneralCommandLine();
		command.setExePath("java");
		command.addParameter("-cp");
		command.addParameter(WebOSStorm.getWebOSToolsJarPath() + File.pathSeparatorChar + palmRunJar.getAbsolutePath());
		command.addParameter("com.mojojungle.palmrun.Main");
		command.addParameter(target.getId());
		command.addParameter(appFolder.getPath());
		command.addParameter("log="+loggingEnabled);

		String configName = this.getRunnerSettings().getRunProfile().getName();
		String title = "Run '" + configName + "' on " + target;// + " (" + command.getCommandLineString() + ")";
		return new OSProcessHandler(command.createProcess(), title);
	}

}
