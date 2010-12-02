package com.mojojungle.webosstorm.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.mojojungle.webosstorm.WebOSStorm;

import javax.swing.*;

public class WebOSConfigurationFactory extends ConfigurationFactory {

	protected WebOSConfigurationFactory(@org.jetbrains.annotations.NotNull ConfigurationType type) {
		super(type);
	}

	@Override
	public Icon getAddIcon() {
		return WebOSStorm.ADD_ICON;
	}

	@Override
	public RunConfiguration createTemplateConfiguration(Project project) {
		return new WebOSRunConfiguration(project, this);
	}
}
