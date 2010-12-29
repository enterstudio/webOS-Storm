package com.mojojungle.webosstorm.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.mojojungle.webosstorm.WebOSStorm;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class WebOSRunConfiguration extends ModuleBasedConfiguration {

	private RunTarget target = RunTarget.EMULATOR;
	private VirtualFile appFolder = null;
	public boolean LOGGING = true;

	public WebOSRunConfiguration(Project project, WebOSConfigurationFactory factory) {
		super(project.getName(), new RunConfigurationModule(project), factory);
	}

	public RunTarget getTarget() {
		return target;
	}

	public void setTarget(RunTarget target) {
		this.target = target;
	}

	@Nullable
	public VirtualFile getAppFolder() {
		return appFolder;
	}

	public void setAppFolder(VirtualFile appFolder) {
		this.appFolder = appFolder;
	}

	public List<VirtualFile> getValidFolders() {
		return WebOSStorm.getWebOSAppsInProject(getProject());
	}

	@Override
	public Collection<Module> getValidModules() {
		return getAllModules();
	}

	@Override
	protected ModuleBasedConfiguration createInstance() {
		return new WebOSRunConfiguration(getProject(), (WebOSConfigurationFactory) getFactory());
	}

	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new WebOSRunConfigurationEditor();
	}

	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
		return new WebOSRunningState(executor, executionEnvironment);
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException {
		super.readExternal(element);
		DefaultJDOMExternalizer.readExternal(this, element);
		String path = element.getChildText("directory");
		if (path != null)
			appFolder = LocalFileSystem.getInstance().findFileByPath(path);
		String target = element.getChildText("target");
		if (target != null)
			this.target = RunTarget.valueOf(target);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException {
		super.writeExternal(element);
		DefaultJDOMExternalizer.writeExternal(this, element);
		if (appFolder != null)
			element.addContent(new Element("directory").setText(appFolder.getPath()));
		element.addContent(new Element("target").setText(target.name()));
	}

	@Override
	public String suggestedName() {
		if (appFolder != null) {
			return appFolder.getName();
		}
		return super.suggestedName();
	}
}
